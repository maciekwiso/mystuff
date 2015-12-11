package com.visosoft.tvshowinfo.service.impl.picviewer;

import com.visosoft.tvshowinfo.dao.PicViewerDao;
import com.visosoft.tvshowinfo.domain.PicViewerRecord;
import org.apache.http.client.fluent.Request;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by maciek on 2015-10-15.
 */
public class ChivePicLoaderTest {

    @Test @Ignore
    public void testLoadPics() throws Exception {
        PicViewerDao picViewerDaoMock = Mockito.mock(PicViewerDao.class);
        Mockito.doAnswer(a -> {
            PicViewerRecord picViewerRecord = (PicViewerRecord)a.getArguments()[0];
            System.out.println("inserting url [" + picViewerRecord.getUrl() + "] - title [" + picViewerRecord.getTitle() + "] group: [" + picViewerRecord.getGroupName() + "]");
            return null;
        }).when(picViewerDaoMock).insert(Mockito.any());
        Mockito.when(picViewerDaoMock.withUrlEndingExists(Mockito.anyString())).thenReturn(false);
        ChivePicLoader chivePicLoader = new ChivePicLoader(picViewerDaoMock);

        chivePicLoader.parseArticleUrl("http://thechive.com/2015/10/15/couples-that-chive-together-stay-together-48-photos/");
    }

    @Test @Ignore
    public void testLoadGalleries() throws Exception {
        PicViewerDao picViewerDaoMock = Mockito.mock(PicViewerDao.class);
        ChivePicLoader chivePicLoader = new ChivePicLoader(picViewerDaoMock);

        String contents = Request.Get("http://thechive.com/").connectTimeout(5000).socketTimeout(10000).execute().returnContent().asString();
        List<String> articlesUrls = chivePicLoader.getUrlsForArticles(contents);
        System.out.println(articlesUrls.size());
        System.out.println(articlesUrls);
    }
}