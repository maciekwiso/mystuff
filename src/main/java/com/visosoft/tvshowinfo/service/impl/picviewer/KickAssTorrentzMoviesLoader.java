package com.visosoft.tvshowinfo.service.impl.picviewer;

import com.visosoft.tvshowinfo.dao.PicViewerDao;
import com.visosoft.tvshowinfo.domain.PicViewerRecord;
import com.visosoft.tvshowinfo.service.XMLUnmarshaller;
import com.visosoft.tvshowinfo.service.impl.picviewer.kat.Channel;
import com.visosoft.tvshowinfo.service.impl.picviewer.kat.Item;
import com.visosoft.tvshowinfo.service.impl.picviewer.kat.Rss;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Predicate;

/**
 * Created by maciek on 2015-12-16.
 */
public class KickAssTorrentzMoviesLoader implements PicLoader {

    private static final Logger logger = LoggerFactory.getLogger(KickAssTorrentzMoviesLoader.class);
    private final XMLUnmarshaller xmlUnmarshaller;
    private final String rssUrl = "https://kat.cr/movies/?rss=1";
    private final PicViewerDao picViewerDao;

    public KickAssTorrentzMoviesLoader(XMLUnmarshaller xmlUnmarshaller, PicViewerDao picViewerDao) throws MalformedURLException {
        this.xmlUnmarshaller = xmlUnmarshaller;
        this.picViewerDao = picViewerDao;
    }

    @Override
    public void loadPics() {
        Rss rss = null;
        try {
            rss = xmlUnmarshaller.unmarshall(Request.Get(rssUrl).connectTimeout(5000).socketTimeout(10000).execute().returnContent().asString(), Rss.class);
        } catch (IOException e) {
            logger.error("Error on reading kickass torrents rss file", e);
        }
        if (rss != null) {
            loadTorrentz(rss.getChannel());
        }
    }

    protected void loadTorrentz(Channel channel) {
        logger.info("Added kickass torrentz movies: {}", channel.getItem().stream().map(this::loadTorrent).filter(Predicate.isEqual(true)).count());
    }

    protected boolean loadTorrent(Item item) {
        if (!picViewerDao.withUrlEndingExists(item.getLink())) {
            PicViewerRecord picViewerRecord = new PicViewerRecord();
            picViewerRecord.setGroupName("KickAssTorrentzMovies");
            picViewerRecord.setUrl(item.getLink());
            picViewerRecord.setTitle(item.getTitle());
            picViewerDao.insert(picViewerRecord);
            return true;
        }
        return false;
    }
}
