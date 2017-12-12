package com.epam.volunteer.dao.impl;


import com.epam.volunteer.dao.MedicamentDAO;
import com.epam.volunteer.entity.Medicament;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

@Singleton
public class MedicamentDAOImpl implements MedicamentDAO {
    private static final String SELECT_ALL = "SELECT m FROM Medicament m";

    @Inject
    private EntityManager manager;

    @Override
    public List<Medicament> getAll() {
        List<Medicament> medicament;
        medicament = manager.createQuery(SELECT_ALL).getResultList();
        return medicament;
    }

    @Override
    public Medicament getById(long id) {
        return manager.find(Medicament.class, id);
    }

    @Override
    public List<Medicament> getFormatted(int startedFrom, int size) {
        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<Medicament> criteriaQuery = criteriaBuilder.createQuery(Medicament.class);
        TypedQuery<Medicament> typedQuery = manager.createQuery(criteriaQuery);
        typedQuery.setFirstResult((startedFrom-1) * size);
        typedQuery.setMaxResults(size);
        return typedQuery.getResultList();
    }
}
