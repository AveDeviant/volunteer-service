package com.epam.volunteer.dao;

import com.epam.volunteer.dao.exception.DAOException;
import com.epam.volunteer.entity.Donation;
import org.jvnet.hk2.annotations.Contract;

@Contract
public interface DonationDAO {

    Donation addDonation(Donation donation, boolean markAsCompleted) throws DAOException;
}
