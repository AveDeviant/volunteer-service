package com.epam.volunteer.service.impl;

import com.epam.volunteer.dao.VolunteerDAO;
import com.epam.volunteer.dao.exception.DAOException;
import com.epam.volunteer.entity.Volunteer;
import com.epam.volunteer.service.VolunteerService;
import com.epam.volunteer.service.exception.ServiceException;
import org.apache.logging.log4j.Level;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class VolunteerServiceImpl extends AbstractService implements VolunteerService {
    private VolunteerDAO volunteerDAO;

    @Inject
    public void setVolunteerDAO(VolunteerDAO volunteerDAO) {
        this.volunteerDAO = volunteerDAO;
    }

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

    @Override
    public List<Volunteer> getAll() throws ServiceException {
        try {
            if (volunteerDAO != null) {
                return volunteerDAO.getAll();
            }
        } catch (DAOException e) {
            getLogger().log(Level.ERROR, e.getMessage());
            throw new ServiceException(e);
        }
        return new ArrayList<>();
    }
}
