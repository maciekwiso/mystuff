package com.visosoft.tvshowinfo.dao.hibernate;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.visosoft.tvshowinfo.dao.UserDao;
import com.visosoft.tvshowinfo.domain.User;
import com.visosoft.tvshowinfo.domain.UserRole.Role;
@Repository
public class UserDaoHibernate implements UserDao {
	@PersistenceContext
	private EntityManager em;
	
	
	public void insert(User s) {
        em.persist(s);
    }
    
    public User update(User s) {
        s = em.merge(s);
        return s;
    }
    
    public void delete(User s) {
        s = em.merge(s);
        em.remove(s);
    }
    
    public List<User> selectAll() {
        String query = "select e from User e order by e.id";
        List<User> recs = em.createQuery(query).getResultList();
        return recs;
    }
    
    public User selectOne(Long id) {
    	String query = "select e from User e where e.id=:un";
		try {
			return (User)em.createQuery(query).setParameter("un", id).getSingleResult();
		} catch (NoResultException e) {
		}
		return null;
    }

	@Override
	public User selectOne(String username) {
		String query = "select e from User e where e.username=:un";
		try {
			return (User)em.createQuery(query).setParameter("un", username).getSingleResult();
		} catch (NoResultException e) {
		}
		return null;
	}

	@Override
	public List<User> selectAll(Role role) {
		 String query = "select e from User e, UserRole r where e.id=r.user.id and r.role=:val order by e.id";
	     List<User> recs = em.createQuery(query).setParameter("val", role).getResultList();
	     return recs;
	}

}
