package com.visosoft.tvshowinfo.dao.hibernate;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.visosoft.tvshowinfo.dao.PicViewerDao;
import com.visosoft.tvshowinfo.domain.PicViewerRecord;

@Repository
public class PicViewerDaoHibernate implements PicViewerDao {

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public void insert(PicViewerRecord s) {
		em.persist(s);
	}

	@Override
	public void deleteOld() {
		String query = "select e from PicViewerRecord e order by e.id desc";
		PicViewerRecord pvr = null;
		try {
			pvr = (PicViewerRecord)em.createQuery(query).setFirstResult(1000).setMaxResults(1).getSingleResult();
		} catch (NoResultException e) {
			return;
		}
		query = "delete from PicViewerRecord e where e.id < :id";
		em.createQuery(query).setParameter("id", pvr.getId()).executeUpdate();
	}

	@Override
	public List<PicViewerRecord> selectAll() {
		String query = "select e from PicViewerRecord e order by e.id";
		return em.createQuery(query).getResultList();
	}

	@Override
	public List<PicViewerRecord> selectUnseen() {
		String query = "select e from PicViewerRecord e where e.seen=false order by e.id";
		return em.createQuery(query).getResultList();
	}

	@Override
	public PicViewerRecord selectOne(Long id) {
		return em.find(PicViewerRecord.class, id);
	}

	@Override
	public void setAsSeenWithIdLorE(Long id) {
		String query = "update PicViewerRecord e set e.seen=true where e.id <= :id";
		em.createQuery(query).setParameter("id", id).executeUpdate();
	}

	@Override
	public PicViewerRecord selectOne(String url) {
		String query = "select e from PicViewerRecord e where e.url=:url";
		try {
			return (PicViewerRecord)em.createQuery(query).setParameter("url", url).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public boolean withUrlEndingExists(String urlEnding) {
		String query = "select e from PicViewerRecord e where e.url like :url";
		try {
			return em.createQuery(query).setParameter("url", "%" + urlEnding).getSingleResult() != null;
		} catch (NoResultException e) {
			return false;
		}
	}

	@Override
	public List<PicViewerRecord> selectAll(int maxResults) {
		String query = "select e from PicViewerRecord e order by e.id";
		return em.createQuery(query).setMaxResults(maxResults).getResultList();
	}

	@Override
	public List<PicViewerRecord> selectUnseen(int maxResults) {
		String query = "select e from PicViewerRecord e where e.seen=false order by e.id";
		return em.createQuery(query).setMaxResults(maxResults).getResultList();
	}

}
