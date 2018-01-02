package com.epam.volunteer.service.impl;

import com.epam.volunteer.dao.DonationDAO;
import com.epam.volunteer.dao.exception.DAOException;
import com.epam.volunteer.dao.impl.DonationDAOImpl;
import com.epam.volunteer.entity.Donation;
import com.epam.volunteer.entity.Medicament;
import com.epam.volunteer.service.DonationService;
import com.epam.volunteer.service.MedicamentService;
import com.epam.volunteer.service.exception.ServiceException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class DonationServiceImpl implements DonationService {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final ReentrantLock lock = new ReentrantLock();
    private DonationDAO donationDAO;
    private MedicamentService medicamentService;

    @Inject
    public void setDonationDAO(DonationDAO donationDAO) {
        this.donationDAO = donationDAO;
    }

    @Inject
    public void setMedicamentService(MedicamentService medicamentService) {
        this.medicamentService = medicamentService;
    }

    @Override
    public Donation registerDonation(Donation donation) throws ServiceException {
        try {
            if (donation.getCount() < 1 || !Optional.ofNullable(donation.getEmployee()).isPresent() ||
                    !Optional.ofNullable(donation.getMedicament()).isPresent()) {
                return null;
            }
            try {
                lock.lock();
                Medicament medicament = medicamentService.getById(donation.getMedicament().getId(), true);
                if (Optional.ofNullable(medicament).isPresent()) {
                    LocalDateTime dateTime = LocalDateTime.now();
                    donation.setTime(dateTime);
                    boolean markAsCompleted = false;
                    int count = donation.getMedicament().getCurrentCount() + donation.getCount();
                    if (count >= donation.getMedicament().getRequirement()) {
                        markAsCompleted = true;
                    }
                    return donationDAO.addDonation(donation, markAsCompleted);
                }
                return null;
            } finally {
                lock.unlock();
            }
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            throw new ServiceException(e);
        }
    }

}
