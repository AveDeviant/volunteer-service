package com.epam.volunteer.service;

import com.epam.volunteer.entity.Donation;
import com.epam.volunteer.service.exception.BusinessLogicException;
import com.epam.volunteer.service.exception.ResourceForbiddenException;
import com.epam.volunteer.service.exception.ServiceException;
import org.jvnet.hk2.annotations.Contract;

@Contract
public interface DonationService {

    Donation registerDonation(long medicamentId, Donation donation) throws ServiceException, BusinessLogicException, ResourceForbiddenException;
}
