package com.visosoft.tvshowinfo.service.impl.picviewer;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.common.collect.ImmutableList;
import com.visosoft.tvshowinfo.dao.PicViewerDao;
import com.visosoft.tvshowinfo.domain.PicViewerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;
import java.util.List;
public class YoutubeLoader implements PicLoader {

    public static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    public static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private final PicViewerDao picViewerDao;
    private static final Logger logger = LoggerFactory.getLogger(ChivePicLoader.class);
    private long count = 0;
    private final List<String> channels = ImmutableList.<String>builder()
            .add("UUa6vGFO9ty8v5KZJXQxdhaw")//kimmel
            .add("UUi7GJNg51C3jgmYTUwqoUXA")//conan
            .add("UU8-Th83bH_thdKZDJCrn88g")//fallon
            .add("UUVTyTA7-g9nopHeHbeuvpRA")//meyers
            .add("UUJ0uqCI0Vqr2Rrt1HseGirg")//corden
            .add("UUMtFAi84ehTSYSE9XoHefig")//colbert
            .build();
    private final Date theDate = new Date(116, 1, 1);
    private YouTube youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, request -> {}).setApplicationName("my-videos-finder").build();

    public YoutubeLoader(PicViewerDao picViewerDao) {
        this.picViewerDao = picViewerDao;
    }

    @Override
    public void loadPics() {
        if (count++ % 10 == 0) {
            doLoadPics();
        }
    }

    private void doLoadPics() {
        logger.info("Starting youtube loader");
        channels.forEach(this::loadChannelVideos);
    }

    private void loadChannelVideos(String playlistId) {
        try {
            PlaylistItemListResponse playlistItems = youtube.playlistItems().list("contentDetails").setKey("AIzaSyCNo7XDGEbVDwkGO_ry8NfV_ptHgw0wqSw")
                    .setPlaylistId(playlistId).setMaxResults(15L).execute();
            int added = 0;
            for (PlaylistItem playlistItem : playlistItems.getItems()) {
                String videoId = playlistItem.getContentDetails().getVideoId();
                if (!picViewerDao.withUrlEndingExists(videoId)) {
                    added++;
                    PicViewerRecord picViewerRecord = new PicViewerRecord();
                    picViewerRecord.setGroupName("youtube");
                    picViewerRecord.setUrl(videoId);
                    picViewerRecord.setTitle(videoId);
                    picViewerRecord.setAdded(theDate);
                    picViewerDao.insert(picViewerRecord);
                }
            }
            logger.info("Added {} videos from playlist {}", added, playlistId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
