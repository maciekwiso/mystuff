package com.visosoft.tvshowinfo.service.impl.showupdater.epguides;

import com.google.common.collect.ImmutableList;
import com.visosoft.tvshowinfo.dao.EpisodeDao;
import com.visosoft.tvshowinfo.domain.Show;
import junit.framework.Assert;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static org.junit.Assert.*;

/**
 * Created by maciek on 2015-09-12.
 */
public class EpGuidesShowUpdaterTest {

    @Test
    public void testUpdateShow() throws Exception {
//        URL resource = EpGuidesShowUpdaterTest.class.getResource("/epguides/dataset1.txt");
//        String epData = new String(Files.readAllBytes(Paths.get(resource.toURI())));
//
//        EpGuidesShowUpdater epGuidesShowUpdater = new EpGuidesShowUpdater() {
//            @Override
//            protected String getEpisodesData(Show show) throws IOException {
//                return epData;
//            }
//        };
//
//        Show show = new Show();
//        show.setTitle("Some show");
//        EpisodeDao episodeDao = Mockito.mock(EpisodeDao.class);
//        Mockito.when(episodeDao.selectAllByShow(show)).thenReturn(ImmutableList.of());
//        epGuidesShowUpdater.setEpisodeDao(episodeDao);
//
//        Assert.assertTrue(epGuidesShowUpdater.updateShow(show));

    }
}