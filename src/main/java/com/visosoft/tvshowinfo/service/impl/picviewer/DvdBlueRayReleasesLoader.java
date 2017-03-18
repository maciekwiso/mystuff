package com.visosoft.tvshowinfo.service.impl.picviewer;

import com.visosoft.tvshowinfo.dao.PicViewerDao;
import com.visosoft.tvshowinfo.domain.PicViewerRecord;
import com.visosoft.tvshowinfo.service.XMLUnmarshaller;
import com.visosoft.tvshowinfo.service.impl.picviewer.moviesreleases.Channel;
import com.visosoft.tvshowinfo.service.impl.picviewer.moviesreleases.Item;
import com.visosoft.tvshowinfo.service.impl.picviewer.moviesreleases.Rss;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by maciek on 2015-12-16.
 */
public class DvdBlueRayReleasesLoader implements PicLoader {

    private static final Logger logger = LoggerFactory.getLogger(DvdBlueRayReleasesLoader.class);
    private final XMLUnmarshaller xmlUnmarshaller;
    private final String rssUrl = "http://feeds.filmjabber.com/UpcomingDVD";
    private final PicViewerDao picViewerDao;
    private final Pattern DATE_PATTERN = Pattern.compile("date: ([A-Za-z]+) ([0-9]+), ([0-9]{4})");

    public DvdBlueRayReleasesLoader(XMLUnmarshaller xmlUnmarshaller, PicViewerDao picViewerDao) {
        this.xmlUnmarshaller = xmlUnmarshaller;
        this.picViewerDao = picViewerDao;
    }

    @Override
    public List<PicViewerRecord> loadPics() {
        Rss rss = null;
        try {
            rss = xmlUnmarshaller.unmarshall(Request.Get(rssUrl).connectTimeout(5000).socketTimeout(10000).execute().returnContent().asString(), Rss.class);
        } catch (IOException e) {
            logger.error("Error on reading movie releases rss file", e);
        }
        if (rss != null) {
            loadMovies(rss.getChannel());
        }
        return new ArrayList<>();
    }

    protected void loadMovies(Channel channel) {
        logger.info("Added movie releases: {}", channel.getItem().stream().map(this::loadRelease).filter(Predicate.isEqual(true)).count());
    }

    protected boolean loadRelease(Item item) {
        if (!picViewerDao.withUrlEndingExists(item.getLink())) {
            PicViewerRecord picViewerRecord = new PicViewerRecord();
            picViewerRecord.setGroupName("DvdBlueRayReleases");
            picViewerRecord.setUrl(item.getLink());
            Matcher matcher = DATE_PATTERN.matcher(item.getDescription());
            matcher.find();
            String title = item.getTitle() + " - " + matcher.group(1) + " " + matcher.group(2) + " " + matcher.group(3);
            picViewerRecord.setTitle(title);
            picViewerDao.insert(picViewerRecord);
            return true;
        }
        return false;
    }
}
