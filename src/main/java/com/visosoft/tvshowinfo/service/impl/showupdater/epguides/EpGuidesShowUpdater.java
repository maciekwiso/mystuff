package com.visosoft.tvshowinfo.service.impl.showupdater.epguides;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.visosoft.tvshowinfo.dao.EpisodeDao;
import com.visosoft.tvshowinfo.domain.Episode;
import com.visosoft.tvshowinfo.domain.Show;
import com.visosoft.tvshowinfo.service.ShowUpdater;
import com.visosoft.tvshowinfo.util.ShowSearchResult;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component("showUpdaterEpguides")
public class EpGuidesShowUpdater implements ShowUpdater {

    private static final Logger logger = LoggerFactory.getLogger(EpGuidesShowUpdater.class);
//    private static final Pattern EPISODE_DATA_LINE_WITH_DATE = Pattern.compile(" *[0-9]+\\. *([0-9])+- ?([0-9]+).*?([0-9]+ [A-Za-z]{3} [0-9]+).*<a.*>(.*)</a");
    private static final Pattern EPISODE_DATA_LINE_WITH_DATE = Pattern.compile(" *[0-9]+ *([0-9])+-([0-9]+).*?([0-9]+/[A-Za-z]{3}/[0-9]+).*?<a.*?>(.*?)</a");
    private static final DateFormat EPISODE_AIR_DATE_FORMATTER = new SimpleDateFormat("d/MMM/yy", Locale.ENGLISH);

    @Autowired
    private EpisodeDao episodeDao;

    public void setEpisodeDao(EpisodeDao episodeDao) {
        this.episodeDao = episodeDao;
    }

    @Override
    public boolean updateShow(Show show) {
        logger.debug("Starting update of show: {}", show);
        try {
            String episodesData = getEpisodesData(show);
            return updateEps(episodesData, show);
        } catch (Exception e) {
            logger.error("Error on updating show " + show.getTitle(), e);
        }
        return false;
    }

    private boolean updateEps(String episodesData, Show show) throws ParseException {
        int startOfEpsData = episodesData.indexOf("<pre");
        if (startOfEpsData == -1)
            return false;
        int endOfEpsData = episodesData.indexOf("</pre");
        if (endOfEpsData == -1)
            return false;
        String[] epsData = episodesData.substring(startOfEpsData, endOfEpsData).split("\n");
        List<Episode> oldEpisodes = episodeDao.selectAllByShow(show);
        if (oldEpisodes == null) {
            oldEpisodes = Lists.newArrayList();
        }
        for (String line : epsData) {
            Matcher matcher = EPISODE_DATA_LINE_WITH_DATE.matcher(line);
            if (matcher.find()) {
                String airDate = matcher.group(3);
                short season = Short.valueOf(matcher.group(1));
                short epNumber = Short.valueOf(matcher.group(2));
                String epTitle = matcher.group(4);
                Episode newEpisode = new Episode();
                newEpisode.setSeason(season);
                newEpisode.setNumber(epNumber);
                newEpisode.setAirdate(EPISODE_AIR_DATE_FORMATTER.parse(airDate));
                newEpisode.setTitle(epTitle);
                newEpisode.setShow(show);
                processEpisode(newEpisode, oldEpisodes);
            }
        }
        if (!oldEpisodes.isEmpty()) {
            logger.debug("Removing not found episodes {} for show {}", oldEpisodes.size(), show.getTitle());
            oldEpisodes.forEach(episodeDao::delete);
        }
        return true;
    }

    private void processEpisode(Episode newEpisode, List<Episode> oldEpisodes) {
        Optional<Episode> foundOldEp = findOldEp(newEpisode, oldEpisodes);
        if (foundOldEp.isPresent()) {
            Episode foundEp = foundOldEp.get();
            if (!Objects.equals(foundEp.getTitle(), newEpisode.getTitle()) || !Objects.equals(foundEp.getAirdate(), newEpisode.getAirdate())) {
                foundEp.setTitle(newEpisode.getTitle());
                foundEp.setAirdate(newEpisode.getAirdate());
                episodeDao.update(foundEp);
                logger.debug("updated episode {}", newEpisode.toReadableString());
            }
            oldEpisodes.remove(foundEp);
        } else {
            episodeDao.insert(newEpisode);
            logger.debug("added episode {}", newEpisode.toReadableString());
        }
    }

    private Optional<Episode> findOldEp(Episode newEpisode, List<Episode> oldEpisodes) {
        return oldEpisodes.stream().filter(a -> Objects.equals(a.getSeason(), newEpisode.getSeason()) && Objects.equals(a.getNumber(), newEpisode.getNumber()))
                .findFirst();
    }

    protected String getEpisodesData(Show show) throws IOException {
        return Request
                .Get(show.getUrl())
                //.Post(show.getUrl()).bodyForm(Form.form().add("list", "tv.com").build())
                .execute().returnContent().asString();
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
