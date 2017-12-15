package com.epam.volunteer.dao.impl;

import com.epam.volunteer.dao.DonationDAO;
import com.epam.volunteer.dao.exception.DAOException;
import com.epam.volunteer.entity.Donation;
import org.apache.logging.log4j.Level;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.time.LocalDateTime;

@Service
public class DonationDAOImpl extends AbstractDAO implements DonationDAO {
    private EntityManager entityManager;

    @Inject
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Donation addDonation(Donation donation, boolean markAsCompleted) throws DAOException {
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            donation.setTime(LocalDateTime.now());
            entityManager.persist(donation);
            donation.getMedicament().setCurrentCount(donation.getMedicament().getCurrentCount() + donation.getCount());
            if (markAsCompleted) {
                donation.getMedicament().setStatus(false);
            }
            entityManager.merge(donation.getMedicament());
            entityManager.flush();
            transaction.commit();
            return donation;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            getLogger().log(Level.ERROR, e.getMessage());
            throw new DAOException(e);
        }
    }
}