package com.visosoft.tvshowinfo.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.visosoft.tvshowinfo.service.EmailQueueSenderService;


@Component
public class EmailQueueTask {
	private static final Logger logger = LoggerFactory.getLogger(EmailQueueTask.class);

	@Autowired
	private EmailQueueSenderService emailQueueService;
	
	@Scheduled(fixedDelay=30000)
	public void startQueue() {
		logger.info("Email queue start");
		emailQueueService.start();
		logger.info("Email queue finish");
	}
}
