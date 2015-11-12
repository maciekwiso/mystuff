package com.visosoft.tvshowinfo.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.visosoft.tvshowinfo.service.PicViewerRecordService;

@Component
public class PicViewerTask {

	@Autowired
	private PicViewerRecordService picViewerRecordService;
	
	@Scheduled(cron = "0 0/5 * * * ?")
	public void run() {
		picViewerRecordService.refresh();
	}
}
