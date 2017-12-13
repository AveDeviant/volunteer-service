package com.epam.volunteer.service.impl;

import com.epam.volunteer.dao.DonationDAO;
import com.epam.volunteer.dao.exception.DAOException;
import com.epam.volunteer.entity.Donation;
import com.epam.volunteer.service.DonationService;
import com.epam.volunteer.service.exception.ServiceException;
import org.apache.logging.log4j.Level;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;

@Service
public class DonationServiceImpl extends AbstractService implements DonationService {
    @Inject
    DonationDAO donationDAO;

    @Override
    public Donation registerDonation(Donation donation) throws ServiceException {
        try {
            if (donation.getCount() < 1) {
                return null;
            }
            return donationDAO.addDonation(donation);
        } catch (DAOException e) {
            getLogger().log(Level.ERROR, e.getMessage());
            throw new ServiceException(e);
        }
    }
}
