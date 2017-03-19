package com.visosoft.tvshowinfo.service.impl;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Lists;
import com.google.api.services.youtube.YouTube;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Resources;
import com.visosoft.tvshowinfo.dao.PicViewerDao;
import com.visosoft.tvshowinfo.domain.PicViewerRecord;
import org.apache.http.client.fluent.Request;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class PicViewerRecordServiceImplTest {

    @Test@Ignore
    public void testRefresh() throws Exception {
        URL resource = PicViewerRecordServiceImplTest.class.getResource("/pic_viewer/pic_viewer_data.txt");
        String contents = new String(Files.readAllBytes(Paths.get(resource.toURI())));
        PicViewerRecordServiceImpl picViewerRecordService = new PicViewerRecordServiceImpl();
        PicViewerDao picViewerDao = Mockito.mock(PicViewerDao.class);
        ReflectionTestUtils.setField(picViewerRecordService, "picViewerDao", picViewerDao);
        Mockito.doAnswer(invocation -> {System.out.println(Arrays.toString(invocation.getArguments()));return null; }).when(picViewerDao).insert(Mockito.any(PicViewerRecord.class));
        //picViewerRecordService.addPics(contents);
    }

    @Test@Ignore
    public void parallelYoutube() {
        final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
        final JsonFactory JSON_FACTORY = new JacksonFactory();
        List<String> channels = ImmutableList.<String>builder()
                .add("UUa6vGFO9ty8v5KZJXQxdhaw")//kimmel
                .add("UUi7GJNg51C3jgmYTUwqoUXA")//conan
                .add("UU8-Th83bH_thdKZDJCrn88g")//fallon
                .add("UUVTyTA7-g9nopHeHbeuvpRA")//meyers
                .add("UUJ0uqCI0Vqr2Rrt1HseGirg")//corden
                .add("UUMtFAi84ehTSYSE9XoHefig")//colbert
                .build();
        YouTube youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, request -> {}).setApplicationName("my-videos-finder").build();
        channels.parallelStream().forEach(a -> {
            try {
                System.out.println(Thread.currentThread().getName() + " " + youtube.playlistItems().list("contentDetails").setKey("AIzaSyCNo7XDGEbVDwkGO_ry8NfV_ptHgw0wqSw")
                        .setPlaylistId(a).setMaxResults(15L).execute().getItems().size());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Ignore
    @Test public void parallelRequests() {
        List<String> theList = com.google.common.collect.Lists.newArrayList("first", "second", "third", "forth");
        theList.parallelStream().forEach(this::dorequest);
    }

    private void dorequest(String s) {
        try {
            System.out.println(s);
            System.out.println(s + Request.Get("http://9gag.com").connectTimeout(5000).socketTimeout(10000).execute().returnContent().asString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test@Ignore
    public void parallelStreamTest() {
        List<String> theList = com.google.common.collect.Lists.newArrayList("first", "second", "third", "forth");
        theList.parallelStream().map(this::nextObj).forEachOrdered(this::after);
    }

    private void after(String s) {
        System.out.println(Thread.currentThread().getName() + " " + s);
        try {
            TimeUnit.SECONDS.sleep(new Random().nextInt(4));
            System.out.println(Thread.currentThread().getName() + " " + s + " after");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    AtomicInteger i = new AtomicInteger(0);

    private String nextObj(String val) {
        int ci = i.incrementAndGet();
        try {
            System.out.println(Thread.currentThread().getName() + " " + val);
            TimeUnit.SECONDS.sleep(new Random().nextInt(4));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return val + ci;
    }
}