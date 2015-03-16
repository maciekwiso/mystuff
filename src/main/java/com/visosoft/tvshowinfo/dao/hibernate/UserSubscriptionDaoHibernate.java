package com.visosoft.tvshowinfo.dao.hibernate;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.visosoft.tvshowinfo.dao.UserSubscriptionDao;
import com.visosoft.tvshowinfo.domain.Show;
import com.visosoft.tvshowinfo.domain.User;
import com.visosoft.tvshowinfo.domain.UserSubscription;
@Repository
public class UserSubscriptionDaoHibernate implements UserSubscriptionDao {
	private static final Logger LOG = LoggerFactory.getLogger(UserSubscriptionDaoHibernate.class);
	
	@PersistenceContext
	private EntityManager em;
	
	
	public void insert(UserSubscription s) {
        em.persist(s);
    }
    
    public UserSubscription update(UserSubscription s) {
        s = em.merge(s);
        return s;
    }
    
    public void delete(UserSubscription s) {
        s = em.merge(s);
        em.remove(s);
    }
    
    public UserSubscription selectOne(Long id) {
        UserSubscription recs = em.find(UserSubscription.class, id);
        return recs;
    }

	@Override
	public List<UserSubscription> selectAll(User user) {
		String query = "select e from UserSubscription e JOIN FETCH e.user JOIN FETCH e.show where e.user.id=:un order by e.show.title";
		return (List<UserSubscription>)em.createQuery(query).setParameter("un", user.getId()).getResultList();
	}

	@Override
	public List<UserSubscription> selectAll(Show show) {
		String query = "select e from UserSubscription e JOIN FETCH e.user JOIN FETCH e.show where e.show.id=:un order by e.show.title";
		return (List<UserSubscription>)em.createQuery(query).setParameter("un", show.getId()).getResultList();
	}

	@Override
	public List<UserSubscription> selectActiveSubscriptions() {
		String query = "select e from UserSubscription e JOIN FETCH e.user JOIN FETCH e.show "
				+ "where e.enabled=true and e.user.enabled=true order by e.show.title";
		return (List<UserSubscription>)em.createQuery(query).getResultList();
	}

	@Override
	public List<UserSubscription> selectActiveEmailSubscriptions() {
		String query = "select e from UserSubscription e JOIN FETCH e.user JOIN FETCH e.show "
				+ "where e.enabled=true and e.emailEnabled=true order by e.show.title";
		return (List<UserSubscription>)em.createQuery(query).getResultList();
	}

	@Override
	public void delete(Show s) {
		String query = "delete from UserSubscription u where u.show = :show";
		int rows = em.createQuery(query).setParameter("show", s).executeUpdate();
		LOG.info("deleted {} rows for show: {}", rows, s);
	}


}
