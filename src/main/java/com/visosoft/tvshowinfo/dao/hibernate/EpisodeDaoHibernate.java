package com.visosoft.tvshowinfo.dao.hibernate;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.visosoft.tvshowinfo.dao.EpisodeDao;
import com.visosoft.tvshowinfo.domain.Episode;
import com.visosoft.tvshowinfo.domain.Show;
@Repository
public class EpisodeDaoHibernate implements EpisodeDao {
	@PersistenceContext
	private EntityManager em;
	private static final Logger logger = LoggerFactory.getLogger(EpisodeDaoHibernate.class);
	public void insert(Episode s) {
        em.persist(s);
    }
    
    public Episode update(Episode s) {
        s = em.merge(s);
        return s;
    }
    
    public void delete(Episode s) {
        s = em.merge(s);
        em.remove(s);
    }
    
    public void deleteByShow(Show show) {
        String query = "delete Episode e where e.show.id=" + show.getId();
        em.createQuery(query).executeUpdate();
    }
    
    public List<Episode> selectAllByShow(Show show) {
        return this.selectAllByShow(show, false);
    }
    
    public List<Episode> selectAllByShow(Show show, boolean onlyNew) {
        String query = "select e from Episode e where e.show.id=" + show.getId();
        if (onlyNew) {
            query += " and e.airdate >= current_date()";
        }
        query += " order by e.season, e.number";
        List<Episode> recs = em.createQuery(query).getResultList();
        return recs;
    }
    
    public List<Episode> selectEpisodesFromDays(int daysIntoPast, int daysIntoFuture, Collection<Show> shows) {
        String query = "select e from Episode e JOIN FETCH e.show where ";
        query += " e.airdate >= ";
        Calendar c1 = Calendar.getInstance();
        c1.setTime(new Date());
        c1.add(Calendar.DAY_OF_MONTH, -Math.abs(daysIntoPast));
        Calendar c2 = Calendar.getInstance();
        c2.setTime(new Date());
        c2.add(Calendar.DAY_OF_MONTH, daysIntoFuture);        
        query += "'" + String.format("%s-%02d-%02d", c1.get(Calendar.YEAR),
            c1.get(Calendar.MONTH)+1, c1.get(Calendar.DAY_OF_MONTH)) + "'";
        query += " and e.airdate <= '" + String.format("%s-%02d-%02d", c2.get(Calendar.YEAR),
            c2.get(Calendar.MONTH)+1, c2.get(Calendar.DAY_OF_MONTH)) + "'";
        if (shows != null && !shows.isEmpty()) {
        	query += " and e.show in (:shows)";
        }
        query += " order by e.airdate, e.show.title, e.number";
        Query q = em.createQuery(query);
        if (shows != null && !shows.isEmpty()) {
        	q.setParameter("shows", shows);
        }
        List<Episode> recs = q.getResultList();
        return recs;
    }
    
    public List<Episode> selectNewEpisodes() {
        String query = "select e from Episode e where ";
        query += " e.airdate >= current_date()";
        query += " order by e.airdate, e.show.title, e.number";
        List<Episode> recs = em.createQuery(query).getResultList();
        return recs;
    }
    
    public Episode selectOne(Long id) {
        Episode recs = em.find(Episode.class, id);
        return recs;
    }
}