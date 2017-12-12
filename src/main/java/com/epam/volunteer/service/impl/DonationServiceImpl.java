package com.epam.volunteer.service.impl;

import com.epam.volunteer.dao.DonationDAO;
import com.epam.volunteer.entity.Donation;
import com.epam.volunteer.service.DonationService;
import com.google.inject.Inject;

public class DonationServiceImpl implements DonationService {
    @Inject
    DonationDAO donationDAO;

    @Override
    public Donation registerDonation(Donation donation) {
        if (donation.getCount() < 1) {
            return null;
        }
        return donationDAO.addDonation(donation);
    }
}
