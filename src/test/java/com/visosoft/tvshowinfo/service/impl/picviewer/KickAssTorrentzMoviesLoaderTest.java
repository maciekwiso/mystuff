package com.visosoft.tvshowinfo.service.impl.picviewer;

import com.visosoft.tvshowinfo.dao.PicViewerDao;
import com.visosoft.tvshowinfo.service.impl.XMLUnmarshallerImpl;
import com.visosoft.tvshowinfo.service.impl.picviewer.kat.ObjectFactory;
import com.visosoft.tvshowinfo.service.impl.picviewer.kat.Rss;
import org.apache.http.client.fluent.Request;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.cert.Certificate;

import static org.junit.Assert.*;

/**
 * Created by maciek on 2015-12-16.
 */
public class KickAssTorrentzMoviesLoaderTest {

    @Test
    public void testLoadTorrent() throws Exception {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(Rss.class);
        PicViewerDao picViewerDao = Mockito.mock(PicViewerDao.class);
        KickAssTorrentzMoviesLoader loader = new KickAssTorrentzMoviesLoader(new XMLUnmarshallerImpl(marshaller), picViewerDao);

        loader.loadPics();
    }
}