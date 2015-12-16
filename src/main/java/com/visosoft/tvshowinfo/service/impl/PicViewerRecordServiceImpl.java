package com.visosoft.tvshowinfo.service.impl;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.visosoft.tvshowinfo.service.XMLUnmarshaller;
import com.visosoft.tvshowinfo.service.impl.picviewer.ChivePicLoader;
import com.visosoft.tvshowinfo.service.impl.picviewer.KickAssTorrentzMoviesLoader;
import com.visosoft.tvshowinfo.service.impl.picviewer.NineGagPicLoader;
import com.visosoft.tvshowinfo.service.impl.picviewer.PicLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.visosoft.tvshowinfo.dao.PicViewerDao;
import com.visosoft.tvshowinfo.domain.PicViewerRecord;
import com.visosoft.tvshowinfo.service.PicViewerRecordService;

import javax.annotation.PostConstruct;

@Service
@Transactional
public class PicViewerRecordServiceImpl implements PicViewerRecordService {

    private static final Logger logger = LoggerFactory.getLogger(PicViewerRecordServiceImpl.class);

    @Autowired
    private PicViewerDao picViewerDao;

    @Autowired
    private XMLUnmarshaller xmlUnmarshaller;

    private List<PicLoader> picLoaders;

    @PostConstruct
    public void init() {
        picLoaders = ImmutableList.of(new NineGagPicLoader(picViewerDao), new ChivePicLoader(picViewerDao), new KickAssTorrentzMoviesLoader(xmlUnmarshaller, picViewerDao));
    }

    @Override
    public void insert(PicViewerRecord s) {
        picViewerDao.insert(s);
    }

    @Override
    public void refresh() {
        logger.info("Starting Pic Viewer loadPics");
        picLoaders.forEach(PicLoader::loadPics);
        logger.info("Pic Viewer loadPics done, deleting old pics");
        picViewerDao.deleteOld();
    }

    @Override
    public List<PicViewerRecord> selectAll(String groupName) {
        return picViewerDao.selectAll(groupName);
    }

    @Override
    public List<PicViewerRecord> selectUnseen(String groupName) {
        return picViewerDao.selectUnseen(groupName);
    }

    @Override
    public PicViewerRecord selectOne(Long id) {
        return picViewerDao.selectOne(id);
    }

    @Override
    public void setAsSeenWithIdLorE(Long id, String groupName) {
        picViewerDao.setAsSeenWithIdLorE(id, groupName);
    }

    @Override
    public List<String> selectUnseenGroups() {
        return picViewerDao.selectUnseenGroups();
    }

    @Override
    public List<PicViewerRecord> selectAll(int maxResults, String groupName) {
        return picViewerDao.selectAll(maxResults, groupName);
    }

    @Override
    public List<PicViewerRecord> selectUnseen(int maxResults, String groupName) {
        return picViewerDao.selectUnseen(maxResults, groupName);
    }
}
