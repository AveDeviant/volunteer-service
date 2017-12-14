package com.epam.volunteer.service.impl;

import com.epam.volunteer.dao.VolunteerDAO;
import com.epam.volunteer.dao.exception.DAOException;
import com.epam.volunteer.entity.Volunteer;
import com.epam.volunteer.service.VolunteerService;
import com.epam.volunteer.service.exception.ServiceException;
import org.apache.logging.log4j.Level;

import javax.inject.Inject;

public class VolunteerServiceImpl extends AbstractService implements VolunteerService {
    @Inject
    VolunteerDAO volunteerDAO;

    @Override
    public Volunteer getById(long id) throws ServiceException {
        try {
            if (volunteerDAO != null) {
                return volunteerDAO.getById(id);
            }
        } catch (DAOException e) {
            getLogger().log(Level.ERROR, e.getMessage());
            throw new ServiceException(e);
        }
        return null;
    }
}
