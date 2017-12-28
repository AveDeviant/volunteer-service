package com.epam.volunteer.service;

import com.epam.volunteer.entity.Donation;
import com.epam.volunteer.service.exception.ServiceException;
import org.jvnet.hk2.annotations.Contract;

@Contract
public interface DonationService {

    Donation registerDonation(Donation donation) throws ServiceException;
}
