package com.visosoft.tvshowinfo.service;

import java.util.List;

import com.visosoft.tvshowinfo.domain.EmailTask;

public interface EmailQueueService {
	
	void cancelPending(EmailTask emailTask);
	List<EmailTask> listPending();
	List<EmailTask> listReady();
	List<EmailTask> listDone();
	void add(EmailTask emailTask);
}
