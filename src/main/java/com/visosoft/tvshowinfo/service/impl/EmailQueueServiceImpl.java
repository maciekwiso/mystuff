package com.visosoft.tvshowinfo.service.impl;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.visosoft.tvshowinfo.dao.EmailQueueDao;
import com.visosoft.tvshowinfo.domain.EmailTask;
import com.visosoft.tvshowinfo.domain.EmailTaskStatus;
import com.visosoft.tvshowinfo.service.EmailQueueSenderService;
import com.visosoft.tvshowinfo.service.EmailQueueService;
import com.visosoft.tvshowinfo.service.ShowsDataUpdaterService;
import com.visosoft.tvshowinfo.service.UserSubscriptionEmailListGeneratorService;

@Service
public class EmailQueueServiceImpl implements ApplicationListener<ContextRefreshedEvent>, EmailQueueService, EmailQueueSenderService {
	
	@Autowired
	private EmailQueueDao emailQueueDao;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private UserSubscriptionEmailListGeneratorService userSubscriptionEmailListGeneratorService;
	
	@Resource
	private TransactionTemplate tt;
	
	private static final Logger logger = LoggerFactory.getLogger(EmailQueueServiceImpl.class);
	private static final int IGNORE_HIGHER_PRIORITY = -1;
	
	@Value("#{emailSender['testmail.run']}")
	private boolean sendTestEmail;
	@Value("#{emailSender['testmail.subject']}")
	private String testSubject;
	@Value("#{emailSender['testmail.content']}")
	private String testContent;
	@Value("#{emailSender['testmail.toname']}")
	private String testToName;
	@Value("#{emailSender['testmail.tomail']}")
	private String testToMail;
	
	private boolean contextRefreshedFired;
	@Value("#{settings['emailgenerator.runatstartup']}")
	private boolean startEmailGenerator;
	
	@Autowired
	private ShowsDataUpdaterService showService;
	
	@Override
	@Transactional
	public void cancelPending(EmailTask emailTask) {
		emailTask.setStatus(EmailTaskStatus.CANCELLED);
		emailQueueDao.update(emailTask);
	}

	@Override
	@Transactional(readOnly = true)
	public List<EmailTask> listPending() {
		return emailQueueDao.selectAll(EmailTaskStatus.PENDING);
	}

	@Override
	@Transactional(readOnly = true)
	public List<EmailTask> listDone() {
		return emailQueueDao.selectAll(EmailTaskStatus.DONE);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<EmailTask> listReady() {
		return emailQueueDao.selectAll(EmailTaskStatus.READY);
	}

	@Override
	@Transactional
	public void add(EmailTask emailTask) {
		emailQueueDao.insert(emailTask);
	}

	@Override
	public void start() {
		refreshStuckPending();
		startQueue();
	}

	private void refreshStuckPending() {
		tt.execute(new TransactionCallbackWithoutResult() {
			
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				try {
					emailQueueDao.refreshStuckPendingTasks();
				}  catch (RuntimeException e) {
					status.setRollbackOnly();
					logger.error("Rolledback.", e);
				}
			}
		});
	}
	
	private void startQueue() {
		EmailTask lastTask = null;
		final String execId = createExecId();
		int priority = 0;
		EmailTask currentTask = null;
		long processed = 0;
		while ((currentTask = findNextReadyTask(lastTask, execId, priority)) != null ) {
			setCurrentAsPending(currentTask, execId);
			boolean success = true;
			try {
				sendEmail(currentTask);
			} catch (Exception e) {
				success = false;
				setCurrentAsFailed(currentTask, e);
			}
			if (success) {
				setCurrentAsDone(currentTask);
			}
			lastTask = currentTask;
			priority = lastTask.getPriority();
			processed++;
		}
		logger.debug("Emails processed: " + processed);
	}

	private void setCurrentAsFailed(final EmailTask currentTask, final Exception e) {
		tt.execute(new TransactionCallbackWithoutResult() {
			
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
					currentTask.setStatus(EmailTaskStatus.READY);
					currentTask.setLastError(e.getMessage() == null ? e.getClass().getCanonicalName() : e.getMessage());
					logger.debug("Email wasn't sent: " + currentTask);
					try {
						emailQueueDao.update(currentTask);
					}  catch (RuntimeException e) {
						status.setRollbackOnly();
						logger.error("Rolledback.", e);
					}
			}});
	}
	
	private void setCurrentAsDone(final EmailTask currentTask) {
		tt.execute(new TransactionCallbackWithoutResult() {
			
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
					currentTask.setStatus(EmailTaskStatus.DONE);
					currentTask.setWhenDone(new Date());
					try {
						emailQueueDao.update(currentTask);
					}  catch (RuntimeException e) {
						status.setRollbackOnly();
						logger.error("Rolledback.", e);
					}
			}});
	}

	private void sendEmail(EmailTask currentTask) throws MessagingException {
		logger.debug("Sending mail: " + currentTask);
		MimeMessage msg = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(msg);
		helper.setTo(currentTask.getName() + " <" + currentTask.getAddress() + ">");
		helper.setText(currentTask.getContent());
		helper.setSubject(currentTask.getSubject());
		mailSender.send(msg);
		logger.debug("Sent mail: " + currentTask);
	}

	private void setCurrentAsPending(final EmailTask currentTask, final String execId) {
		tt.execute(new TransactionCallbackWithoutResult() {
			
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				currentTask.setStatus(EmailTaskStatus.PENDING);
				currentTask.setAttempts(currentTask.getAttempts() + 1);
				currentTask.setLastAttempt(new Date());
				if (!currentTask.getExecId().equals(execId)) {
					currentTask.setExecId(execId);
					currentTask.setExecCount(0);
				}
				currentTask.setExecCount(currentTask.getExecCount() + 1);
				try {
					emailQueueDao.update(currentTask);
				}  catch (RuntimeException e) {
					status.setRollbackOnly();
					logger.error("Rolledback.", e);
				}
			}
		});
	}

	private EmailTask findNextReadyTask(final EmailTask lastTask, final String execId,
			final int priority) {
		return tt.execute(new TransactionCallback<EmailTask>() {
			
			@Override
			public EmailTask doInTransaction(TransactionStatus status) {
				EmailTask newTask = findNextReadyTaskQuery(null, execId, priority - 1, IGNORE_HIGHER_PRIORITY);
				if (newTask != null) return newTask;
				newTask = findNextReadyTaskQuery(lastTask, execId, priority, IGNORE_HIGHER_PRIORITY);
				if (newTask != null) return newTask;
				newTask = findNextReadyTaskQuery(null, execId, 0, priority);
				if (newTask != null) return newTask;
				return findNextReadyTaskQuery(null, execId, priority, IGNORE_HIGHER_PRIORITY);
			}
		});
	}
	
	private EmailTask findNextReadyTaskQuery(EmailTask lastTask, String execId,
			int priority, int priorityHigherThan) {
		return emailQueueDao.selectOne(lastTask, execId, priority, priorityHigherThan);
	}

	private String createExecId() {
		return String.valueOf(System.nanoTime());
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (contextRefreshedFired) return;
		contextRefreshedFired = true;
		addTestMail();
		startEmailGenerator();
		updateShowsInfo();
        checkJava8();
	}

    private void checkJava8() {
        List<String> list = new LinkedList<>();
        list.add("first element");
        list.add("second");
        logger.debug("using lambda expression");
        list.stream().forEach(p -> logger.debug(p));
    }

    private void updateShowsInfo() {
		showService.updateShowsData();
	}

	private void startEmailGenerator() {
		if (!startEmailGenerator) {
			return;
		}
		logger.info("Starting generating the email list");
		userSubscriptionEmailListGeneratorService.generateEmailList();
		logger.info("Finished generating the email list");
	}

	private void addTestMail() {
		if (!sendTestEmail) {
			return;
		}
		tt.execute(new TransactionCallbackWithoutResult() {
			
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				try {
					logger.debug("entered addTestEmailToQueue");
					Date now = new Date();
					EmailTask et = new EmailTask();
					et.setAdded(now);
					et.setAddress(testToMail);
					et.setContent(testContent);
					et.setAttempts(0);
					et.setContent(testContent);
					et.setExecCount(0);
					et.setExecId("");
					et.setName(testToName);
					et.setPriority(0);
					et.setStartNotSoonerThan(now);
					et.setStatus(EmailTaskStatus.READY);
					et.setSubject(testSubject);
					et.setLastError("");
					add(et);
					logger.info("added test mail to the queue: " + et);
				}  catch (RuntimeException e) {
					status.setRollbackOnly();
					logger.error("Rolledback.", e);
				}
			}
		});
	}
}
