package com.visosoft.tvshowinfo.service;

import java.util.List;

import com.visosoft.tvshowinfo.domain.PicViewerRecord;

public interface PicViewerRecordService {
	
	void insert(PicViewerRecord s);
    
    void refresh();
    
    List<PicViewerRecord> selectAll();
    List<PicViewerRecord> selectUnseen();
    List<PicViewerRecord> selectAll(int maxResults);
    List<PicViewerRecord> selectUnseen(int maxResults);
    PicViewerRecord selectOne(Long id);
    void setAsSeenWithIdLorE(Long id);
}
