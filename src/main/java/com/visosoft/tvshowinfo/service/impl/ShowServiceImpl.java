package com.visosoft.tvshowinfo.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.visosoft.tvshowinfo.domain.EpisodeUpdaterResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.visosoft.tvshowinfo.dao.EpisodeDao;
import com.visosoft.tvshowinfo.dao.ShowDao;
import com.visosoft.tvshowinfo.dao.UserSubscriptionDao;
import com.visosoft.tvshowinfo.domain.Show;
import com.visosoft.tvshowinfo.service.ShowService;
import com.visosoft.tvshowinfo.service.ShowUpdater;
import com.visosoft.tvshowinfo.service.ShowUpdaterResolverService;
import com.visosoft.tvshowinfo.service.ShowsDataUpdaterService;

@Service
public class ShowServiceImpl implements ShowService, ShowsDataUpdaterService {

    @Autowired
    private ShowDao showDao;
    @Autowired
    private EpisodeDao episodeDao;
    @Autowired
    private UserSubscriptionDao userSubscriptionDao;
    @Autowired
    private ShowUpdaterResolverService showUpdaterResolverService;

    @Resource
    private TransactionTemplate tt;

    private static final Logger logger = LoggerFactory.getLogger(ShowServiceImpl.class);

    @Override
    @Transactional
    public void insert(Show s) {
        logger.debug("inserting: {}", s);
        showDao.insert(s);
    }

    @Override
    @Transactional
    public Show update(Show s) {
        logger.debug("update: {}", s);
        return showDao.update(s);
    }

    @Override
    @Transactional
    public void delete(Show s) {
        logger.debug("delete: " + s);
        userSubscriptionDao.delete(s);
        episodeDao.deleteByShow(s);
        showDao.delete(s);
    }

    @Override
    @Transactional
    public List<Show> selectAll() {
        logger.debug("selectAll");
        return showDao.selectAll();
    }

    @Override
    @Transactional
    public Show selectOne(Long id) {
        logger.debug("select One {}", id);
        return showDao.selectOne(id);
    }

    @Override
    public void updateShowsData() {
        List<Show> list = showDao.selectAll();
        list.parallelStream().map(this::dataForUpdate).filter(Objects::nonNull).collect(Collectors.toList()).forEach(this::updateShow);
    }

    @Override
    public void updateShow(Show s) {
        EpisodeUpdaterResult updateResult = dataForUpdate(s);
        if (updateResult == null) {
            logger.error("Error on updating show's data: " + s.getTitle());
        } else {
            updateShow(updateResult);
        }
    }

    private EpisodeUpdaterResult dataForUpdate(Show s) {
        logger.debug("Resolving updater for show: " + s);
        final ShowUpdater updater = showUpdaterResolverService.resolve(s);
        if (updater == null) {
            logger.error("Couldn't resolve updater");
            return null;
        }
        return updater.updateShow(s);
    }

    private void updateShow(EpisodeUpdaterResult result) {
        tt.execute(new TransactionCallbackWithoutResult() {

            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    result.toUpdate.forEach(episodeDao::update);
                    result.toInsert.forEach(episodeDao::insert);
                    result.toDelete.forEach(episodeDao::delete);
                } catch (RuntimeException e) {
                    status.setRollbackOnly();
                    logger.error("Rolledback. Exception while updating show", e);
                }
            }
        });
    }

    @Override
    @Transactional
    public Show selectOne(String showName) {
        return showDao.selectOne(showName);
    }
}
