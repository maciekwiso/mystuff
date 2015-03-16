package com.visosoft.tvshowinfo.dao;

import java.util.List;

import com.visosoft.tvshowinfo.domain.PicViewerRecord;

public interface PicViewerDao {
	void insert(PicViewerRecord s);
    
    public void deleteOld();
    public List<PicViewerRecord> selectAll();
    public List<PicViewerRecord> selectUnseen();
    public PicViewerRecord selectOne(Long id);
    public PicViewerRecord selectOne(String url);
    void setAsSeenWithIdLorE(Long id);
    boolean withUrlEndingExists(String urlEnding);

	List<PicViewerRecord> selectAll(int maxResults);

	List<PicViewerRecord> selectUnseen(int maxResults);
}
