package com.epam.volunteer.dao.impl;

import com.epam.volunteer.dao.VolunteerDAO;
import com.epam.volunteer.dao.exception.DAOException;
import com.epam.volunteer.entity.Volunteer;
import org.apache.logging.log4j.Level;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

@Service
public class VolunteerDAOImpl extends AbstractDAO implements VolunteerDAO {
    private static final String QUERY_GET_ALL = "Volunteer.getAll";

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
            getLogger().log(Level.ERROR, e.getMessage());
            throw new DAOException(e);
        }
    }

    @Override
    public List<Volunteer> getAll() throws DAOException {
        try {
            return entityManager.createNamedQuery(QUERY_GET_ALL, Volunteer.class).getResultList();
        } catch (Exception e) {
            getLogger().log(Level.ERROR, e.getMessage());
            throw new DAOException(e);
        }
    }
}
