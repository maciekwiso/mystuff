package com.visosoft.tvshowinfo.service.impl.picviewer;

import com.google.common.annotations.VisibleForTesting;
import com.visosoft.tvshowinfo.dao.PicViewerDao;
import com.visosoft.tvshowinfo.domain.PicViewerRecord;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NineGagPicLoader implements PicLoader {

    private static final Logger logger = LoggerFactory.getLogger(NineGagPicLoader.class);

    private final PicViewerDao picViewerDao;
    private final Pattern artPattern = Pattern.compile("<article.*?</article>", Pattern.DOTALL);
    private final Pattern titlePattern = Pattern.compile("<a .*?>(.*?)</a>", Pattern.DOTALL);
    private final Pattern srcPattern = Pattern.compile("src=\"(.*?)\"");
    private final Pattern gifPattern = Pattern.compile("\"([^\"]*\\.gif)\"");
    private final Pattern mp4Pattern = Pattern.compile("\"([^\"]*\\.mp4)\"");

    public NineGagPicLoader(PicViewerDao picViewerDao) {
        this.picViewerDao = picViewerDao;
    }

    @Override
    public void loadPics() {
        try {
            String contents = get9gagContents();
            addPics(contents);
        } catch (Exception e) {
            logger.error("Exception in refresh9gag", e);
        }
    }

    private static String get9gagContents() throws IOException {
        return Request.Get("http://9gag.com").execute().returnContent().asString();
    }

    @VisibleForTesting
    protected void addPics(String contents) {
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
        p.setGroupName("9gag");
        picViewerDao.insert(p);
        return true;
    }
}
