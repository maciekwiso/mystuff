package com.visosoft.tvshowinfo.service.impl;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.visosoft.tvshowinfo.dao.UserSubscriptionDao;
import com.visosoft.tvshowinfo.domain.EmailTask;
import com.visosoft.tvshowinfo.domain.EmailTaskStatus;
import com.visosoft.tvshowinfo.domain.Show;
import com.visosoft.tvshowinfo.domain.User;
import com.visosoft.tvshowinfo.domain.UserSubscription;
import com.visosoft.tvshowinfo.service.EmailQueueService;
import com.visosoft.tvshowinfo.service.ShowUpdater;
import com.visosoft.tvshowinfo.service.ShowUpdaterResolverService;
import com.visosoft.tvshowinfo.service.UserSubscriptionEmailListGeneratorService;
import com.visosoft.tvshowinfo.service.UserSubscriptionService;
import com.visosoft.tvshowinfo.service.UsersShowsInfoGeneratorService;
import com.visosoft.tvshowinfo.util.ShowSearchResult;
import com.visosoft.tvshowinfo.util.ShowsInfoGeneratedData;
import com.visosoft.tvshowinfo.util.Utils;

@Service
public class UserSubscriptionServiceImpl implements UserSubscriptionService, UserSubscriptionEmailListGeneratorService {

	private static final Integer EMAIL_PRIORITY = 1;
	
	private static final Logger logger = LoggerFactory.getLogger(UserSubscriptionServiceImpl.class);

	@Autowired
	private UserSubscriptionDao userSubscriptionDao;
	
	@Autowired
	private ShowUpdaterResolverService showUpdaterResolverService;
	
	@Value("#{settings['mailqueue.subject']}")
	private String emailSubjectTemplate;
	
	@Value("#{settings['mailqueue.content']}")
	private String emailContentTemplate;
	
	private final String EMAILVAR_USERNAME = "\\$username";
	private final String EMAILVAR_SHOWS = "\\$shows";
	private final String EMAILVAR_YESTERDAY = "\\$yesterday";
	private final String EMAILVAR_TODAY = "\\$today";
	private final String EMAILVAR_INTWOWEEKS = "\\$twoweeks";
	private final String EMAILVAR_LATERTHANTWOWEEKS = "\\$latertwoweeks";
	
	@Autowired
	private UsersShowsInfoGeneratorService usersShowsInfoGeneratorService;
	
	@Autowired
	private EmailQueueService emailQueueService;
	
	@Override
	@Transactional
	public void insert(UserSubscription s) {
		userSubscriptionDao.insert(s);
	}

	@Override
	@Transactional
	public UserSubscription update(UserSubscription s) {
		return userSubscriptionDao.update(s);
	}

	@Override
	@Transactional
	public void delete(UserSubscription s) {
		userSubscriptionDao.delete(s);
	}

	@Override
	@Transactional
	public List<UserSubscription> selectAll(User user) {
		return userSubscriptionDao.selectAll(user);
	}

	@Override
	@Transactional
	public List<UserSubscription> selectAll(Show show) {
		return userSubscriptionDao.selectAll(show);
	}
	
	@Override
	@Transactional
	public UserSubscription select(long id) {
		return userSubscriptionDao.selectOne(id);
	}

	@Override
	public void generateEmailList() {
		List<UserSubscription> subs = userSubscriptionDao.selectActiveEmailSubscriptions();
		ShowsInfoGeneratedData data = usersShowsInfoGeneratorService.generateData(subs);
		generateEmailList(data);
	}
	
	private void generateEmailList(ShowsInfoGeneratedData data) {
		for (Map.Entry<User, SortedSet<Show>> rec : data.getUserSubs().entrySet()) {
			generateEmailList(rec, data);
		}
	}
	
	private void generateEmailList(Map.Entry<User, SortedSet<Show>> rec, ShowsInfoGeneratedData data) {
		StringBuilder yesterday = Utils.generateEpisodesList(rec.getValue(), data.getYesterday(), false, true);
		StringBuilder today = Utils.generateEpisodesList(rec.getValue(), data.getToday(), false, true);
		StringBuilder inTwoWeeks = Utils.generateEpisodesList(rec.getValue(), data.getInTwoWeeks(), true, true);
		StringBuilder moreThanTwoWeeks = Utils.generateEpisodesList(rec.getValue(), data.getMoreThanTwoWeeks(), true, true);
		StringBuilder shows = Utils.generateSubscribedShowsContent(rec.getValue());
		String subject = emailSubjectTemplate;
		String content = emailContentTemplate.replaceAll(EMAILVAR_SHOWS, shows.toString())
				.replaceAll(EMAILVAR_USERNAME, rec.getKey().getUsername())
				.replaceAll(EMAILVAR_TODAY, today.toString())
				.replaceAll(EMAILVAR_INTWOWEEKS, inTwoWeeks.toString())
				.replaceAll(EMAILVAR_LATERTHANTWOWEEKS, moreThanTwoWeeks.toString())
				.replaceAll(EMAILVAR_YESTERDAY, yesterday.toString());
		addEmailDataToEmailQueue(rec.getKey(), subject, content);
	}

	private void addEmailDataToEmailQueue(User user, String subject, String content) {
		Date now = new Date();
		EmailTask et = new EmailTask();
		et.setAdded(now);
		et.setAddress(user.getEmail());
		et.setAttempts(0);
		et.setContent(content);
		et.setExecCount(0);
		et.setExecId("");
		et.setName(user.getUsername());
		et.setPriority(EMAIL_PRIORITY);
		et.setStartNotSoonerThan(now);
		et.setStatus(EmailTaskStatus.READY);
		et.setSubject(subject);
		et.setLastError("");
		emailQueueService.add(et);
		logger.debug("Added email task: {}", et);
	}

	@Override
	public List<ShowSearchResult> searchShow(String showName) {
		Collection<ShowUpdater> showUpdaters = showUpdaterResolverService.getAllUpdaters();
		// TODO change it to handle multiple updaters
		ShowUpdater updater = showUpdaters.iterator().next();
		return updater.searchShow(showName);
	}

	@Override
	public String generateSubscriptionUrl(ShowSearchResult ssr) {
		ShowUpdater updater = showUpdaterResolverService.resolve(ssr.getShowUpdaterId());
		return updater.generateSubscriptionLink(ssr);
	}
	
	
}
