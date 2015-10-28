package com.visosoft.tvshowinfo.service.impl;

import com.visosoft.tvshowinfo.dao.PicViewerDao;
import com.visosoft.tvshowinfo.domain.PicViewerRecord;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:picviewer-context.xml", "classpath:db-context.xml" })
@TransactionConfiguration(transactionManager="txManager")
@Transactional
public class PicViewerIntegrationTest {

    @Autowired
    private PicViewerDao picViewerDao;

    @Test @Ignore
    public void test() {
        PicViewerRecord p = new PicViewerRecord();
        p.setUrl("url1");
        p.setGroupName("group1");
        p.setTitle("title1");
        p.setSeen(false);
        picViewerDao.insert(p);
        p = new PicViewerRecord();
        p.setUrl("url2");
        p.setGroupName("group1");
        p.setTitle("title2");
        p.setSeen(false);
        picViewerDao.insert(p);
        p = new PicViewerRecord();
        p.setUrl("url3");
        p.setGroupName("group3");
        p.setTitle("title3");
        p.setSeen(false);
        picViewerDao.insert(p);

        List<String> result = picViewerDao.selectUnseenGroups();
        System.out.println(result);
    }
}
