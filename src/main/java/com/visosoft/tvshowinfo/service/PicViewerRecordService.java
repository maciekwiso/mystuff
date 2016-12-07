package com.visosoft.tvshowinfo.service;

import java.util.List;

import com.visosoft.tvshowinfo.domain.PicViewerRecord;

public interface PicViewerRecordService {
	
	void insert(PicViewerRecord s);
    
    void refresh();

    void refresh(String type);

    List<PicViewerRecord> selectAll(String groupName);
    List<PicViewerRecord> selectUnseen(String groupName);
    List<PicViewerRecord> selectAll(int maxResults, String groupName);
    List<PicViewerRecord> selectUnseen(int maxResults, String groupName);
    PicViewerRecord selectOne(Long id);
    void setAsSeenWithIdLorE(Long id, String groupName);
    List<String> selectUnseenGroups();

    void setGroupDateToDateZero(String groupName);
}
