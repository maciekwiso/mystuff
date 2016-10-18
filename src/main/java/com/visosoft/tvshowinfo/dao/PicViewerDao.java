package com.visosoft.tvshowinfo.dao;

import java.util.List;

import com.visosoft.tvshowinfo.domain.PicViewerRecord;

public interface PicViewerDao {
	void insert(PicViewerRecord s);
    
    public void deleteOld();
    public List<PicViewerRecord> selectAll(String groupName);
    public List<PicViewerRecord> selectUnseen(String groupName);
    public PicViewerRecord selectOne(Long id);
    public PicViewerRecord selectOne(String url);
    void setAsSeenWithIdLorE(Long id,String groupName);
    boolean withUrlEndingExists(String urlEnding);

	List<PicViewerRecord> selectAll(int maxResults,String groupName);

	List<PicViewerRecord> selectUnseen(int maxResults, String groupName);

    List<String> selectUnseenGroups();

    void setGroupDateToDateZero(String groupName);
}
