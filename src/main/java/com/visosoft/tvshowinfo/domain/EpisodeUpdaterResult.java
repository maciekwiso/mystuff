package com.visosoft.tvshowinfo.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maciek on 19.03.2017.
 */
public class EpisodeUpdaterResult {

    public final List<Episode> toUpdate = new ArrayList<>();
    public final List<Episode> toInsert = new ArrayList<>();
    public final List<Episode> toDelete = new ArrayList<>();
}
