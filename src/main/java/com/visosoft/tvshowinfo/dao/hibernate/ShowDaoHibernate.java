package com.visosoft.tvshowinfo.dao.hibernate;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.visosoft.tvshowinfo.dao.ShowDao;
import com.visosoft.tvshowinfo.domain.Show;
@Repository
public class ShowDaoHibernate implements ShowDao {
	@PersistenceContext
	private EntityManager em;
	
	public void insert(Show s) {
        em.persist(s);
    }
    
    public Show update(Show s) {
        s = em.merge(s);
        return s;
    }
    
    public void delete(Show s) {
        s = em.merge(s);
        em.remove(s);
    }
    
    public List<Show> selectAll() {
        String query = "select e from Show e order by e.title";
        List<Show> recs = em.createQuery(query).getResultList();
        return recs;
    }
    
    public Show selectOne(Long id) {
        Show recs = em.find(Show.class, id);
        return recs;
    }

	@Override
	public Show selectOne(String showName) {
		if (showName == null) {
			return null;
		}
		String query = "select e from Show e where e.title=:title";
		Show recs = null;
		try {
			recs = (Show)em.createQuery(query).setParameter("title", showName).getSingleResult();
		} catch (NoResultException e) {
		}
        return recs;
	}

}
