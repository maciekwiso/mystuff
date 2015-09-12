package com.visosoft.tvshowinfo.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.annotations.VisibleForTesting;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.visosoft.tvshowinfo.dao.PicViewerDao;
import com.visosoft.tvshowinfo.domain.PicViewerRecord;
import com.visosoft.tvshowinfo.service.PicViewerRecordService;

@Service
@Transactional
public class PicViewerRecordServiceImpl implements PicViewerRecordService {

    private static final Logger logger = LoggerFactory.getLogger(PicViewerRecordServiceImpl.class);

    private final Pattern artPattern = Pattern.compile("<article.*?</article>", Pattern.DOTALL);
    private final Pattern titlePattern = Pattern.compile("<a .*?>(.*?)</a>", Pattern.DOTALL);
    private final Pattern srcPattern = Pattern.compile("src=\"(.*?)\"");
    private final Pattern gifPattern = Pattern.compile("\"([^\"]*\\.gif)\"");
    private final Pattern mp4Pattern = Pattern.compile("\"([^\"]*\\.mp4)\"");

    @Autowired
    private PicViewerDao picViewerDao;

    @Override
    public void insert(PicViewerRecord s) {
        picViewerDao.insert(s);
    }

    @Override
    public void refresh() {
        logger.info("Starting Pic Viewer refresh");
        doRefresh();
        logger.info("Pic Viewer refresh done, deleting old pics");
        picViewerDao.deleteOld();
    }

    @Override
    public List<PicViewerRecord> selectAll() {
        return picViewerDao.selectAll();
    }

    @Override
    public List<PicViewerRecord> selectUnseen() {
        return picViewerDao.selectUnseen();
    }

    @Override
    public PicViewerRecord selectOne(Long id) {
        return picViewerDao.selectOne(id);
    }

    @Override
    public void setAsSeenWithIdLorE(Long id) {
        picViewerDao.setAsSeenWithIdLorE(id);
    }

    private void doRefresh() {
        try {
            String contents = get9gagContents();
            addPics(contents);
        } catch (Exception e) {
            logger.error("Exception in doRefresh", e);
        }
    }

    private static String get9gagContents() throws IOException {
        return Request.Get("http://9gag.com").execute().returnContent().asString();
    }

    @VisibleForTesting protected void addPics(String contents) {
        Matcher m = artPattern.matcher(contents);
        int added = 0;
        while (m.find()) {
            String currentContent = m.group();
            Matcher titleMatcher = titlePattern.matcher(currentContent);
            titleMatcher.find();
            String title = titleMatcher.group(1).trim();
            Optional<String> url = Optional.empty();
            Matcher mp4Matcher = mp4Pattern.matcher(currentContent);
            Matcher gifMatcher = gifPattern.matcher(currentContent);
            if (mp4Matcher.find()) {
                url = Optional.of(mp4Matcher.group(1));
            } else if (gifMatcher.find()) {
                url = Optional.of(gifMatcher.group(1));
            } else {
                Matcher m2 = srcPattern.matcher(currentContent);
                if (m2.find()) {
                    String foundUrl = m2.group(1);
                    if (!foundUrl.endsWith("_v1.jpg") && !foundUrl.contains("long-post-cover")) {
                        url = Optional.of(foundUrl);
                    }
                } else {
                    logger.warn("Didn't find src pattern in [{}]", currentContent);
                }
            }
            if (url.isPresent() && addNewPic(url.get(), title)) added++;
        }
        logger.info("Added new pics {}", added);
    }

    private boolean addNewPic(String url, String title) {
        int lastSlash = url.lastIndexOf("/");
        if (lastSlash == -1) {
            return false;
        }
        if (picViewerDao.withUrlEndingExists(url.substring(lastSlash))) {
            return false;
        }
        PicViewerRecord p = new PicViewerRecord();
        p.setUrl(url);
        p.setTitle(title);
        picViewerDao.insert(p);
        return true;
    }

    @Override
    public List<PicViewerRecord> selectAll(int maxResults) {
        return picViewerDao.selectAll(maxResults);
    }

    @Override
    public List<PicViewerRecord> selectUnseen(int maxResults) {
        return picViewerDao.selectUnseen(maxResults);
    }
}
