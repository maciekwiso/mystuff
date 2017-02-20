package com.visosoft.tvshowinfo.service.impl.showupdater.tvmaze;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.visosoft.tvshowinfo.dao.EpisodeDao;
import com.visosoft.tvshowinfo.domain.Episode;
import com.visosoft.tvshowinfo.domain.Show;
import com.visosoft.tvshowinfo.service.ShowUpdater;
import com.visosoft.tvshowinfo.util.ShowSearchResult;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;

@Component("showUpdaterTvMaze")
public class TvMazeShowUpdater implements ShowUpdater {

    private static final Logger logger = LoggerFactory.getLogger(TvMazeShowUpdater.class);
    private static final DateFormat EPISODE_AIR_DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    @Autowired
    private EpisodeDao episodeDao;

    public void setEpisodeDao(EpisodeDao episodeDao) {
        this.episodeDao = episodeDao;
    }

    @Override
    public boolean updateShow(Show show) {
        logger.debug("Starting update of show: {}", show);
        try {
            List<TvMazeEpisode> episodesData = getEpisodesData(show);
            return updateEps(episodesData, show);
        } catch (Exception e) {
            logger.error("Error on updating show " + show.getTitle(), e);
        }
        return false;
    }

    private boolean updateEps(List<TvMazeEpisode> episodesData, Show show) {
        List<Episode> oldEpisodes = episodeDao.selectAllByShow(show);
        if (oldEpisodes == null) {
            oldEpisodes = Lists.newArrayList();
        }
        for (TvMazeEpisode episode : episodesData) {
            processEpisode(episode, oldEpisodes, show);
        }
        if (!oldEpisodes.isEmpty()) {
            logger.debug("Removing not found episodes {} for show {}", oldEpisodes.size(), show.getTitle());
            oldEpisodes.forEach(episodeDao::delete);
        }
        return true;
    }

    private void processEpisode(TvMazeEpisode newEpisode, List<Episode> oldEpisodes, Show show) {
        try {
            Optional<Episode> foundOldEp = findOldEp(newEpisode, oldEpisodes);
            Date newEpAirDate = EPISODE_AIR_DATE_FORMATTER.parse(newEpisode.getAirdate());
            if (foundOldEp.isPresent()) {
                Episode foundEp = foundOldEp.get();
                if (!Objects.equals(foundEp.getTitle(), newEpisode.getName()) || !Objects.equals(foundEp.getAirdate(), newEpAirDate)) {
                    foundEp.setTitle(newEpisode.getName());
                    foundEp.setAirdate(newEpAirDate);
                    episodeDao.update(foundEp);
                    logger.debug("updated episode {}", foundEp.toReadableString());
                }
                oldEpisodes.remove(foundEp);
            } else {
                Episode newEpisodeDomain = createEpisode(newEpAirDate, newEpisode, show);
                episodeDao.insert(newEpisodeDomain);
                logger.debug("added episode {}", newEpisodeDomain.toReadableString());
            }
        } catch (ParseException e) {
            logger.error("Couldn't parse date for episode: " + newEpisode.getName(), e);
        }
    }

    private Episode createEpisode(Date airDate, TvMazeEpisode tvMazeEpisode, Show show) {
        Episode e = new Episode();
        e.setAirdate(airDate);
        e.setSeason(tvMazeEpisode.getSeason().shortValue());
        e.setNumber(tvMazeEpisode.getNumber().shortValue());
        e.setTitle(tvMazeEpisode.getName());
        e.setShow(show);
        return e;
    }

    private Optional<Episode> findOldEp(TvMazeEpisode newEpisode, List<Episode> oldEpisodes) {
        return oldEpisodes.stream().filter(a -> Objects.equals(a.getSeason().intValue(), newEpisode.getSeason().intValue()) && Objects.equals(a.getNumber().intValue(), newEpisode.getNumber().intValue()))
                .findFirst();
    }

    private List<TvMazeEpisode> getEpisodesData(Show show) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(Request.Get(show.getUrl()).connectTimeout(5000).socketTimeout(10000).execute().returnContent().asString(), objectMapper.getTypeFactory().constructCollectionType(List.class, TvMazeEpisode.class));
    }

    @Override
    public List<ShowSearchResult> searchShow(String showName) {
        return null;
    }

    @Override
    public String generateSubscriptionLink(ShowSearchResult show) {
        return null;
    }
}
