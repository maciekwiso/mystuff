package com.visosoft.tvshowinfo.service.impl.picviewer;

import com.visosoft.tvshowinfo.dao.PicViewerDao;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

public class ApartmentsLoaderTest {

    @Test@Ignore
    public void loadPics() {
        PicViewerDao picViewerDao = Mockito.mock(PicViewerDao.class);
        Mockito.when(picViewerDao.withUrlEndingExists(Mockito.anyString())).thenReturn(false);
        ApartmentsLoader apartmentsLoader = new ApartmentsLoader(picViewerDao);
        System.out.println(apartmentsLoader.loadPics());
    }
}