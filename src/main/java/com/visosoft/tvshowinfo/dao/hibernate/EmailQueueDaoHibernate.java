package com.visosoft.tvshowinfo.dao.hibernate;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.visosoft.tvshowinfo.dao.EmailQueueDao;
import com.visosoft.tvshowinfo.domain.EmailTask;
import com.visosoft.tvshowinfo.domain.EmailTaskStatus;

@Repository
public class EmailQueueDaoHibernate implements EmailQueueDao {

	@PersistenceContext
	private EntityManager em;
	private static final int MAX_EXEC_COUNT = 3;
	private static final int MAX_ATTEMPTS = 100;
	private static final Logger logger = LoggerFactory.getLogger(EmailQueueDaoHibernate.class);
	
	@Override
	public void insert(EmailTask s) {
		em.persist(s);
	}

	@Override
	public EmailTask update(EmailTask s) {
		s = em.merge(s);
        return s;
	}

	@Override
	public void delete(EmailTask s) {
		s = em.merge(s);
        em.remove(s);
	}

	@Override
	public List<EmailTask> selectAll() {
		return selectAll(null);
	}

	@Override
	public List<EmailTask> selectAll(EmailTaskStatus status) {
		String query = "select e from EmailTask e";
		if (status != null) {
			query += " where e.status = :st";
		}
        query += " order by e.added desc";
        Query q = em.createQuery(query);
        if (status != null) {
        	q.setParameter("st", status);
        }
        List<EmailTask> recs = q.getResultList();
        return recs;
	}

	@Override
	public EmailTask selectOne(Long id) {
		EmailTask recs = em.find(EmailTask.class, id);
        return recs;
	}

	@Override
	public void refreshStuckPendingTasks() {
		String q = "update EmailTask SET status= :str WHERE lastAttempt < :yest and status = :stp";
		Calendar c = new GregorianCalendar();
		c.add(Calendar.HOUR_OF_DAY, -24);
		int u = em.createQuery(q).setParameter("str", EmailTaskStatus.READY)
			.setParameter("stp", EmailTaskStatus.PENDING)
			.setParameter("yest", c.getTime()).executeUpdate();
		if (u > 0) {
			em.flush();
			em.clear();
		}
		logger.debug("Updated: {}", u);
	}

	@Override
	public EmailTask selectOne(EmailTask lastTask, String execId, int priority,
			int priorityHigherThan) {
		long lastId = 0;
		if (lastTask != null) {
			lastId = lastTask.getId();
		}
		String query = "select e from EmailTask e where e.startNotSoonerThan <= CURRENT_TIMESTAMP"
				+ " and e.id > :lastid and e.status = :st and e.attempts < :maxatt and "
				+ "(e.execId != :execid OR (e.execId = :execid and e.execCount < :maxcount))";
		if (priorityHigherThan >= 0) {
			query += " and e.priority > :prioh";
		} else {
			query += " and e.priority <= :prio";
		}
        query += " order by e.priority, e.id";
        Query q = em.createQuery(query);
        q.setParameter("lastid", lastId).setParameter("st", EmailTaskStatus.READY)
        	.setParameter("maxcount", MAX_EXEC_COUNT)
        	.setParameter("execid", execId)
        	.setParameter("maxatt", MAX_ATTEMPTS)
        	.setMaxResults(1);
        if (priorityHigherThan >= 0) {
			q.setParameter("prioh", priorityHigherThan);
		} else {
			q.setParameter("prio", priority);
		}
        try {
        	return (EmailTask)q.getSingleResult();
        } catch (NoResultException e) {
        	return null;
        }
	}

}
