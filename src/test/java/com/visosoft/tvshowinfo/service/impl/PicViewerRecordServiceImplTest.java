package com.visosoft.tvshowinfo.service.impl;

import com.google.api.client.util.Lists;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.visosoft.tvshowinfo.dao.PicViewerDao;
import com.visosoft.tvshowinfo.domain.PicViewerRecord;
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

    @Test
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