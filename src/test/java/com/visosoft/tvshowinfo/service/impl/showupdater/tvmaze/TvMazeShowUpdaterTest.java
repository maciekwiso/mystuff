package com.visosoft.tvshowinfo.service.impl.showupdater.tvmaze;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.visosoft.tvshowinfo.dao.EpisodeDao;
import com.visosoft.tvshowinfo.domain.Show;
import junit.framework.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by maciek on 2015-09-15.
 */
public class TvMazeShowUpdaterTest {

    @Test public void testUpdater() throws IOException {
        TvMazeShowUpdater tvMazeShowUpdater = new TvMazeShowUpdater();
        Show show = new Show();
        show.setTitle("Some show");
        show.setUrl("http://api.tvmaze.com/shows/1/episodes");
        EpisodeDao episodeDao = Mockito.mock(EpisodeDao.class);
        Mockito.when(episodeDao.selectAllByShow(show)).thenReturn(ImmutableList.of());
        tvMazeShowUpdater.setEpisodeDao(episodeDao);

        Assert.assertNotNull(tvMazeShowUpdater.updateShow(show));
    }
}
