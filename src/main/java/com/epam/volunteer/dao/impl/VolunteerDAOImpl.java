package com.epam.volunteer.dao.impl;

import com.epam.volunteer.dao.VolunteerDAO;
import com.epam.volunteer.dao.exception.DAOException;
import com.epam.volunteer.entity.Volunteer;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Service
public class VolunteerDAOImpl implements VolunteerDAO {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String QUERY_GET_ALL = "Volunteer.getAll";
    private static final String QUERY_COUNT_ALL = "Volunteer.countAll";
    private EntityManager entityManager;

    @Inject
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Volunteer getById(long id) throws DAOException {
        try {
            return entityManager.find(Volunteer.class, id);
        } catch (Exception e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            throw new DAOException(e);
        }
    }

    @Override
    public List<Volunteer> getAll() throws DAOException {
        try {
            return entityManager.createNamedQuery(QUERY_GET_ALL, Volunteer.class).getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            throw new DAOException(e);
        }
    }

    @Override
    public List<Volunteer> getAll(int page, int size) throws DAOException {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Volunteer> criteriaQuery = criteriaBuilder.createQuery(Volunteer.class);
            TypedQuery<Volunteer> typedQuery = entityManager.createQuery(criteriaQuery);
            typedQuery.setFirstResult((page - 1) * size);
            typedQuery.setMaxResults(size);
            return typedQuery.getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            throw new DAOException(e);
        }
    }

    @Override
    public Volunteer getByEmail(String email) throws DAOException {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Volunteer> criteriaQuery = criteriaBuilder.createQuery(Volunteer.class);
            Root<Volunteer> sm = criteriaQuery.from(Volunteer.class);
            criteriaQuery.where(criteriaBuilder.equal(sm.get("email"), email));
            TypedQuery<Volunteer> typedQuery = entityManager.createQuery(criteriaQuery);
            List<Volunteer> volunteers = typedQuery.getResultList();
            if (!volunteers.isEmpty()) {
                return volunteers.get(0);
            }
            return null;
        } catch (Exception e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            throw new DAOException(e);
        }
    }

    @Override
    public Volunteer addNew(Volunteer volunteer) throws DAOException {
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.persist(volunteer);
            entityManager.flush();
            transaction.commit();
            return volunteer;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            LOGGER.log(Level.ERROR, e.getMessage());
            throw new DAOException(e);
        }
    }

    @Override
    public long countAll() throws DAOException {
        try {
            return entityManager.createNamedQuery(QUERY_COUNT_ALL, Long.class).getSingleResult();
        } catch (Exception e) {
            throw new DAOException(e.getMessage());
        }
    }

}
