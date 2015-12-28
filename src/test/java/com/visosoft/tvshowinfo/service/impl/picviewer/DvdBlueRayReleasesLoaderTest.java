package com.visosoft.tvshowinfo.service.impl.picviewer;

import com.visosoft.tvshowinfo.dao.PicViewerDao;
import com.visosoft.tvshowinfo.service.impl.XMLUnmarshallerImpl;
import com.visosoft.tvshowinfo.service.impl.picviewer.moviesreleases.Rss;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

/**
 * Created by maciek on 2015-12-16.
 */
public class DvdBlueRayReleasesLoaderTest {

    @Test @Ignore
    public void testLoadReleases() throws Exception {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(Rss.class);
        PicViewerDao picViewerDao = Mockito.mock(PicViewerDao.class);
        DvdBlueRayReleasesLoader loader = new DvdBlueRayReleasesLoader(new XMLUnmarshallerImpl(marshaller), picViewerDao);

        loader.loadPics();
    }
}