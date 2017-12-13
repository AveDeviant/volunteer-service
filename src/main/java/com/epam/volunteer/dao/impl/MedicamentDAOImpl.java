package com.epam.volunteer.dao.impl;


import com.epam.volunteer.dao.MedicamentDAO;
import com.epam.volunteer.dao.exception.DAOException;
import com.epam.volunteer.entity.Medicament;
import org.apache.logging.log4j.Level;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import java.util.List;


@Service
public class MedicamentDAOImpl extends AbstractDAO implements MedicamentDAO {
    private static final String SELECT_ALL = "SELECT m FROM Medicament m";

    @Inject
    private EntityManager manager;

    @Override
    public List<Medicament> getAll() throws DAOException {
        try {
            List<Medicament> medicament;
            medicament = manager.createQuery(SELECT_ALL).getResultList();
            return medicament;
        } catch (Exception e) {
            getLogger().log(Level.ERROR, e.getMessage());
            throw new DAOException(e);
        }
    }

    @Override
    public Medicament getById(long id) throws DAOException {
        try {
            return manager.find(Medicament.class, id);
        } catch (Exception e) {
            getLogger().log(Level.ERROR, e.getMessage());
            throw new DAOException(e);
        }
    }

    @Override
    public List<Medicament> getFormatted(int page, int size) throws DAOException {
        try {
            CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
            CriteriaQuery<Medicament> criteriaQuery = criteriaBuilder.createQuery(Medicament.class);
            Root<Medicament> sm = criteriaQuery.from(Medicament.class);
            criteriaQuery.where(criteriaBuilder.equal(sm.get("status"), true));
            TypedQuery<Medicament> typedQuery = manager.createQuery(criteriaQuery);
            typedQuery.setFirstResult((page - 1) * size);
            typedQuery.setMaxResults(size);
            return typedQuery.getResultList();
        } catch (Exception e) {
            getLogger().log(Level.ERROR, e.getMessage());
            throw new DAOException(e);
        }
    }

    @Override
    public Medicament addNew(Medicament medicament) throws DAOException {
        EntityTransaction transaction = manager.getTransaction();
        transaction.begin();
        try {
            manager.persist(medicament);
            manager.flush();
            transaction.commit();
            return medicament;
        } catch (Exception e) {
            transaction.rollback();
            getLogger().log(Level.ERROR, e.getMessage());
            throw new DAOException(e);
        }
    }
}
