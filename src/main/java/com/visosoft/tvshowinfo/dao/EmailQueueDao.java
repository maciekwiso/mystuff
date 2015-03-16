package com.visosoft.tvshowinfo.dao;

import java.util.List;

import com.visosoft.tvshowinfo.domain.EmailTask;
import com.visosoft.tvshowinfo.domain.EmailTaskStatus;

public interface EmailQueueDao {
	
	void insert(EmailTask s);
	
    EmailTask update(EmailTask s);
    
    void delete(EmailTask s);
    
    List<EmailTask> selectAll();
    
    List<EmailTask> selectAll(EmailTaskStatus status);
    
    EmailTask selectOne(Long id);
    
    void refreshStuckPendingTasks();
    
    EmailTask selectOne(EmailTask lastTask, String execId, int priority, int priorityHigherThan);
}
