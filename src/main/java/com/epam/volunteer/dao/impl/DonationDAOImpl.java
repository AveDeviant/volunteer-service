package com.epam.volunteer.dao.impl;

import com.epam.volunteer.dao.DonationDAO;
import com.epam.volunteer.dao.exception.DAOException;
import com.epam.volunteer.entity.Donation;
import com.epam.volunteer.entity.Medicament;
import org.apache.logging.log4j.Level;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.time.LocalDateTime;

@Service
public class DonationDAOImpl extends AbstractDAO implements DonationDAO {

    @Inject
    private EntityManager manager;

    @Override
    public Donation addDonation(Donation donation) throws DAOException {
        EntityTransaction transaction = manager.getTransaction();
        try {
            transaction.begin();
            donation.setTime(LocalDateTime.now());
            manager.persist(donation);
            Medicament medicament = donation.getMedicament();
            medicament.setCurrentCount(donation.getCount());
            manager.refresh(medicament);
            manager.flush();
            transaction.commit();
            return donation;
        } catch (Exception e) {
            transaction.rollback();
            getLogger().log(Level.ERROR, e.getMessage());
            throw new DAOException(e);
        }
    }
}
