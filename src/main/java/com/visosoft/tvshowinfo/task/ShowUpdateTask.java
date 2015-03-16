package com.visosoft.tvshowinfo.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.visosoft.tvshowinfo.service.ShowsDataUpdaterService;

@Component
public class ShowUpdateTask {
	
	@Autowired
	private ShowsDataUpdaterService showService;
	
	private static final Logger logger = LoggerFactory.getLogger(ShowUpdateTask.class);
	
	@Scheduled(cron = "0 30 0/2 * * ?")
	public void showUpdateTask() {
		logger.info("starting show update task");
		showService.updateShowsData();
		logger.info("finishing show update task");
	}
}
