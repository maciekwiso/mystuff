package com.visosoft.tvshowinfo.dao.hibernate;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import com.google.common.collect.Collections2;
import org.springframework.stereotype.Repository;

import com.visosoft.tvshowinfo.dao.PicViewerDao;
import com.visosoft.tvshowinfo.domain.PicViewerRecord;
import org.springframework.util.CollectionUtils;

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
			pvr = (PicViewerRecord)em.createQuery(query).setFirstResult(10000).setMaxResults(1).getSingleResult();
		} catch (NoResultException e) {
			return;
		}
		query = "delete from PicViewerRecord e where e.id < :id";
		em.createQuery(query).setParameter("id", pvr.getId()).executeUpdate();
	}

	@Override
	public List<PicViewerRecord> selectAll(String groupName) {
		String query = "select e from PicViewerRecord e where e.groupName = :groupName order by e.id";
		return em.createQuery(query).setParameter("groupName", groupName).getResultList();
	}

	@Override
	public List<PicViewerRecord> selectUnseen(String groupName) {
		String query = "select e from PicViewerRecord e where e.seen=false and e.groupName=:groupName order by e.id";
		return em.createQuery(query).setParameter("groupName", groupName).getResultList();
	}

	@Override
	public PicViewerRecord selectOne(Long id) {
		return em.find(PicViewerRecord.class, id);
	}

	@Override
	public void setAsSeenWithIdLorE(Long id, String groupName) {
		String query = "update PicViewerRecord e set e.seen=true where e.id <= :id and e.groupName=:groupName";
		em.createQuery(query).setParameter("id", id).setParameter("groupName", groupName).executeUpdate();
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
		return !CollectionUtils.isEmpty(em.createQuery(query).setParameter("url", "%" + urlEnding).getResultList());
	}

	@Override
	public List<PicViewerRecord> selectAll(int maxResults, String groupName) {
		String query = "select e from PicViewerRecord e where e.groupName=:groupName order by e.id";
		return em.createQuery(query).setMaxResults(maxResults).setParameter("groupName", groupName).getResultList();
	}

	@Override
	public List<PicViewerRecord> selectUnseen(int maxResults, String groupName) {
		String query = "select e from PicViewerRecord e where e.seen=false and e.groupName=:groupName order by e.id";
		return em.createQuery(query).setMaxResults(maxResults).setParameter("groupName", groupName).getResultList();
	}

	@Override
	public List<String> selectUnseenGroups() {
		String query = "select distinct e.groupName from PicViewerRecord e where e.seen=false order by e.groupName";
		return em.createQuery(query).getResultList();
	}

}
