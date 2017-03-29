package com.turbointernational.metadata.dao;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.turbointernational.metadata.entity.AuthProvider_;
import com.turbointernational.metadata.entity.User;
import com.turbointernational.metadata.entity.User_;
import com.turbointernational.metadata.web.dto.Page;

/**
 * @author jrodriguez
 */
@Repository
public class UserDao extends AbstractDao<User> {

    public UserDao() {
        super(User.class);
    }

    public List<User> findActiveUsers() {
        return em.createQuery("SELECT o FROM User o WHERE o.enabled = true", User.class).getResultList();
    }

    public User findUserByUsername(String username) {
        if (isNotBlank(username)) {
            try {
                return em.createNamedQuery("findUserByUsername", User.class).setParameter("username", username)
                        .getSingleResult();
            } catch (NoResultException | NonUniqueResultException e) {
                // Ignore, return null.
            }
        }
        return null;
    }

    public User findUserByEmail(String email) {
        if (isNotBlank(email)) {
            try {
                return em.createNamedQuery("findUserByEmail", User.class).setParameter("email", email)
                        .getSingleResult();
            } catch (NoResultException | NonUniqueResultException e) {
                // Ignore, return null.
            }
        }
        return null;
    }

    public User findByPasswordResetToken(String token) {
        Query q = em.createQuery("SELECT u FROM User u WHERE u.passwordResetToken = :token", User.class);
        q.setParameter("token", token);
        return (User) q.getSingleResult();
    }

    public Page<User> filterUsers(String displayName, String userName, String email, Long authProviderId,
            Boolean enabled, String sortProperty, String sortOrder, Integer offset, Integer limit) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> ecq = cb.createQuery(User.class);
        Root<User> root = ecq.from(User.class);
        ecq.select(root);
        int numPredicates = 0;
        List<Predicate> lstPredicates = new ArrayList<>(4);
        if (displayName != null) {
            lstPredicates.add(cb.like(cb.lower(root.get(User_.name)), "%" + displayName.toLowerCase() + "%"));
            numPredicates++;
        }
        if (userName != null) {
            lstPredicates.add(cb.like(cb.lower(root.get(User_.username)), "%" + userName.toLowerCase() + "%"));
            numPredicates++;
        }
        if (email != null) {
            lstPredicates.add(cb.like(cb.lower(root.get(User_.email)), "%" + email.toLowerCase() + "%"));
            numPredicates++;
        }
        if (authProviderId == null) {
            lstPredicates.add(cb.isNull(root.get(User_.authProvider).get(AuthProvider_.id)));
            numPredicates++;
        } else if (authProviderId > 0) {
            lstPredicates.add(cb.equal(root.get(User_.authProvider).get(AuthProvider_.id), authProviderId));
            numPredicates++;
        }
        if (enabled != null) {
            lstPredicates.add(cb.equal(root.get(User_.enabled), enabled));
        }
        Predicate[] arrPredicates = lstPredicates.toArray(new Predicate[numPredicates]);
        ecq.where(arrPredicates);
        if (sortOrder != null) {
            if (sortProperty == null) {
                throw new NullPointerException("Parameter 'sortOrder' can't be null.");
            }
            From<?, ?> f = root;
            if (sortOrder.equalsIgnoreCase("asc")) {
                ecq.orderBy(cb.asc(f.get(sortProperty)));
            } else if (sortOrder.equalsIgnoreCase("desc")) {
                ecq.orderBy(cb.desc(f.get(sortProperty)));
            } else {
                throw new AssertionError("Unknown sort order: " + sortOrder);
            }
        }
        TypedQuery<User> q = em.createQuery(ecq);
        if (offset != null) {
            q.setFirstResult(offset);
        }
        if (limit != null) {
            q.setMaxResults(limit);
        }
        List<User> recs = q.getResultList();
        CriteriaQuery<Long> ucq = cb.createQuery(Long.class);
        Root<User> userCountRoot = ucq.from(User.class);
        ucq.select(cb.count(userCountRoot));
        ucq.where(arrPredicates);
        long total = em.createQuery(ucq).getSingleResult();
        return new Page<>(total, recs);
    }

}
