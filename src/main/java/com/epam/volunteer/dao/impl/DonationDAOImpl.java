package com.epam.volunteer.dao.impl;

import com.epam.volunteer.dao.DonationDAO;
import com.epam.volunteer.entity.Donation;
import com.epam.volunteer.entity.Medicament;
import com.google.inject.Inject;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.time.LocalDateTime;

public class DonationDAOImpl implements DonationDAO {

    @Inject
    private EntityManager manager;

    @Override
    public Donation addDonation(Donation donation) {
        EntityTransaction transaction = manager.getTransaction();
        transaction.begin();
        donation.setTime(LocalDateTime.now());
        manager.persist(donation);
        Medicament medicament = donation.getMedicament();
        medicament.setCurrentCount(donation.getCount());
        manager.refresh(medicament);
        manager.flush();
        transaction.commit();
        return donation;
    }
}
