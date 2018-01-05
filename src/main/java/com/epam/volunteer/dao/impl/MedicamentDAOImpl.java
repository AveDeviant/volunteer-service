package com.epam.volunteer.dao.impl;


import com.epam.volunteer.dao.MedicamentDAO;
import com.epam.volunteer.dao.exception.DAOException;
import com.epam.volunteer.entity.Medicament;
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
import java.util.Optional;


@Service
public class MedicamentDAOImpl implements MedicamentDAO {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String SELECT_ALL = "SELECT m FROM Medicament m";
    private static final String QUERY_COUNT_ACTUAL = "Medicament.countActual";
    private EntityManager entityManager;

    @Inject
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Medicament> getAll() throws DAOException {
        try {
            return entityManager.createQuery(SELECT_ALL).getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            throw new DAOException(e);
        }
    }

    @Override
    public Medicament getById(long id) throws DAOException {
        try {
            return entityManager.find(Medicament.class, id);
        } catch (Exception e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            throw new DAOException(e);
        }
    }

    @Override
    public List<Medicament> getFormatted(int page, int size) throws DAOException {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Medicament> criteriaQuery = criteriaBuilder.createQuery(Medicament.class);
            Root<Medicament> sm = criteriaQuery.from(Medicament.class);
            criteriaQuery.where(criteriaBuilder.equal(sm.get("isActual"), true));
            TypedQuery<Medicament> typedQuery = entityManager.createQuery(criteriaQuery);
            typedQuery.setFirstResult((page - 1) * size);
            typedQuery.setMaxResults(size);
            return typedQuery.getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            throw new DAOException(e);
        }
    }

    @Override
    public Medicament addNew(Medicament medicament) throws DAOException {
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.persist(medicament);
            entityManager.flush();
            transaction.commit();
            return medicament;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            LOGGER.log(Level.ERROR, e.getMessage());
            throw new DAOException(e);
        }
    }

    @Override
    public Medicament update(long id, Medicament medicament) throws DAOException {
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            Medicament entity = entityManager.find(Medicament.class, id);
            entity.setActual(medicament.isActual());
            entity.setRequirement(medicament.getRequirement());
            entity.setMedicament(medicament.getMedicament());
            entityManager.merge(entity);
            transaction.commit();
            return entity;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            LOGGER.log(Level.ERROR, e.getMessage());
            throw new DAOException(e);
        }
    }

    @Override
    public void delete(long id) throws DAOException {
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            Medicament medicament = entityManager.find(Medicament.class, id);
            if (medicament != null) {
                entityManager.remove(medicament);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            LOGGER.log(Level.ERROR, e.getMessage());
            throw new DAOException(e);
        }
    }

    @Override
    public long countActual() throws DAOException {
        try {
            return entityManager.createNamedQuery(QUERY_COUNT_ACTUAL, Long.class).getSingleResult();
        } catch (Exception e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            throw new DAOException(e.getMessage());
        }
    }

}
