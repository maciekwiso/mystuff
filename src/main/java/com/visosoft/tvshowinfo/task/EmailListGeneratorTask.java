package com.visosoft.tvshowinfo.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.visosoft.tvshowinfo.service.UserSubscriptionEmailListGeneratorService;

@Component
public class EmailListGeneratorTask {

	private static final Logger logger = LoggerFactory.getLogger(EmailListGeneratorTask.class);
	
	@Autowired
	private UserSubscriptionEmailListGeneratorService userSubscriptionEmailListGeneratorService;
	
	@Scheduled(cron = "0 0 1 * * ?")
	public void generateEmailList() {
		logger.info("Starting generating the email list");
		userSubscriptionEmailListGeneratorService.generateEmailList();
		logger.info("Finished generating the email list");
	}
}
