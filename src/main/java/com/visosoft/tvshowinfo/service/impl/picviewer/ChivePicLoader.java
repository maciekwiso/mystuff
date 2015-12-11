package com.visosoft.tvshowinfo.service.impl.picviewer;

import com.google.common.collect.Lists;
import com.visosoft.tvshowinfo.dao.PicViewerDao;
import com.visosoft.tvshowinfo.domain.PicViewerRecord;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChivePicLoader implements PicLoader {

    private static final Logger logger = LoggerFactory.getLogger(ChivePicLoader.class);

    private static final Pattern ARTICLE_LINK_PATTERN = Pattern.compile("<h1 class=\"post-title entry-title\" itemprop=\"headline\"><a .*?href=\"(.*?)\".*?>(.*?)</a></h1>");
    private static final Pattern ARTICLE_TITLE_PATTERN = Pattern.compile("<title>(.*?)</");
    private static final Pattern ARTICLE_FIGURE_PATTERN = Pattern.compile("<figure(.*)</figure>", Pattern.DOTALL);
    private static final Pattern ARTICLE_IMG_TAG_PATTERN = Pattern.compile("<img(.*?)/>", Pattern.DOTALL);
    private static final Pattern ARTICLE_IMG_PIC_PATTERN = Pattern.compile("src=\"(.*?)\".*?alt=\"(.*?)\"", Pattern.DOTALL);
    private static final Pattern ARTICLE_IMG_GIF_PATTERN = Pattern.compile("alt=\"(.*?)\".*?data-gifsrc=\"(.*?)\"", Pattern.DOTALL);

    private final PicViewerDao picViewerDao;

    public ChivePicLoader(PicViewerDao picViewerDao) {
        this.picViewerDao = picViewerDao;
    }

    @Override
    public void loadPics() {
        try {
            String contents = Request.Get("http://thechive.com/").connectTimeout(5000).socketTimeout(10000).execute().returnContent().asString();
            List<String> articlesUrls = getUrlsForArticles(contents);
            articlesUrls.forEach(this::parseArticleUrl);
        } catch (Exception e) {
            logger.error("Exception in loadPics", e);
            e.printStackTrace();
        }
    }

    protected void parseArticleUrl(String url) {
        String contents = null;
        try {
            contents = Request.Get(url).connectTimeout(5000).socketTimeout(10000).execute().returnContent().asString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Matcher matcher = ARTICLE_TITLE_PATTERN.matcher(contents);
        matcher.find();
        String groupName = matcher.group(1);
        if (groupName.lastIndexOf('(') != -1)
            groupName = groupName.substring(0, groupName.lastIndexOf("(")).trim();
        groupName = groupName.replaceAll("&#?[0-9A-Za-z]+;", " ").replaceAll("[^a-zA-Z0-9 ]", "").replaceAll("theCHIVE", "").trim();
        matcher = ARTICLE_FIGURE_PATTERN.matcher(contents);
        if (!matcher.find()) {
            logger.warn("Didn't find figure pattern for group {}", groupName);
        } else {
            String allPicsStr = matcher.group(1);
            matcher = ARTICLE_IMG_TAG_PATTERN.matcher(allPicsStr);
            int added = 0;
            while (matcher.find()) {
                String imgTag = matcher.group(1);
                Matcher matcherGif = ARTICLE_IMG_GIF_PATTERN.matcher(imgTag);
                String picUrl = null;
                String title = null;
                if (matcherGif.find()) {
                    picUrl = matcherGif.group(2);
                    title = matcherGif.group(1);
                } else {
                    Matcher matcherPic = ARTICLE_IMG_PIC_PATTERN.matcher(imgTag);
                    if (matcherPic.find()) {
                        picUrl = matcherPic.group(1);
                        title = matcherPic.group(2);
                    }
                }
                if (picUrl != null && addPic(picUrl, title, groupName))
                    added++;
            }
            logger.info("Added pics for the chive for article {}: {}", groupName, added);
        }
    }

    private boolean addPic(String picUrl, String title, String groupName) {
        picUrl = picUrl.substring(0, picUrl.indexOf('?'));
        if (!picViewerDao.withUrlEndingExists(picUrl.substring(10))) {
            PicViewerRecord picViewerRecord = new PicViewerRecord();
            picViewerRecord.setGroupName(groupName);
            picViewerRecord.setUrl(picUrl);
            title = title.replaceAll("\n", " ").trim();
            picViewerRecord.setTitle(title);
            picViewerDao.insert(picViewerRecord);
            return true;
        }
        return false;
    }

    protected static List<String> getUrlsForArticles(String contents) {
        Matcher matcher = ARTICLE_LINK_PATTERN.matcher(contents);
        List<String> urls = Lists.newArrayList();
        while (matcher.find()) {
            String title = matcher.group(2);
            String url = matcher.group(1);
            if (url.contains("http://thechive") && titleIsPhotos(title)) {
                urls.add(url);
            }
        }
        return urls;
    }

    private static boolean titleIsPhotos(String title) {
        return title.endsWith("Photos)");
    }
}
