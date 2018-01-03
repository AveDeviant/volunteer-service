package com.epam.volunteer.service.impl;

import com.epam.volunteer.dao.DonationDAO;
import com.epam.volunteer.dao.exception.DAOException;
import com.epam.volunteer.entity.Donation;
import com.epam.volunteer.entity.Medicament;
import com.epam.volunteer.service.DonationService;
import com.epam.volunteer.service.MedicamentService;
import com.epam.volunteer.service.exception.BusinessLogicException;
import com.epam.volunteer.service.exception.ResourceForbiddenException;
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
    public Donation registerDonation(long medicamentId, Donation donation) throws ServiceException,
            BusinessLogicException, ResourceForbiddenException {
        try {
            if (donation.getCount() < 1 || !Optional.ofNullable(donation.getEmployee()).isPresent()) {
                throw new BusinessLogicException();
            }
            try {
                lock.lock();
                Medicament medicament = medicamentService.getById(medicamentId, true);
                if (Optional.ofNullable(medicament).isPresent()) {
                    LocalDateTime dateTime = LocalDateTime.now();
                    donation.setTime(dateTime);
                    donation.setMedicament(medicament);
                    boolean markAsCompleted = false;
                    int count = donation.getMedicament().getCurrentCount() + donation.getCount();
                    if (count >= donation.getMedicament().getRequirement()) {
                        markAsCompleted = true;
                    }
                    LOGGER.log(Level.INFO, "Donation object should be registered: " + donation);
                    return donationDAO.addDonation(donation, markAsCompleted);
                }
                throw new ResourceForbiddenException();
            } finally {
                lock.unlock();
            }
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            throw new ServiceException(e);
        }
    }

}
