package com.visosoft.tvshowinfo.dao.hibernate;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.visosoft.tvshowinfo.dao.RoleDao;
import com.visosoft.tvshowinfo.domain.UserRole;
import com.visosoft.tvshowinfo.domain.User;
@Repository
public class RoleDaoHibernate implements RoleDao {
	@PersistenceContext
	private EntityManager em;
	
	
	public void insert(UserRole s) {
        em.persist(s);
    }
    
    public UserRole update(UserRole s) {
        s = em.merge(s);
        return s;
    }
    
    public void delete(UserRole s) {
        s = em.merge(s);
        em.remove(s);
    }
    
    public UserRole selectOne(Long id) {
        UserRole recs = em.find(UserRole.class, id);
        return recs;
    }

	@Override
	public List<UserRole> selectAll(User user) {
		String query = "select e from UserRole e JOIN FETCH e.user where e.user.id=:un";
		return (List<UserRole>)em.createQuery(query).setParameter("un", user.getId()).getResultList();
	}


}
