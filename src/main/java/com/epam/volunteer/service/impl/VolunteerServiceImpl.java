package com.epam.volunteer.service.impl;

import com.epam.volunteer.dao.VolunteerDAO;
import com.epam.volunteer.dao.exception.DAOException;
import com.epam.volunteer.entity.Medicament;
import com.epam.volunteer.entity.Volunteer;
import com.epam.volunteer.service.MedicamentService;
import com.epam.volunteer.service.VolunteerService;
import com.epam.volunteer.service.exception.ServiceException;
import com.epam.volunteer.util.Validator;
import org.apache.logging.log4j.Level;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VolunteerServiceImpl extends AbstractService implements VolunteerService {
    private MedicamentService medicamentService;
    private VolunteerDAO volunteerDAO;

    @Inject
    public void setMedicamentService(MedicamentService medicamentService) {
        this.medicamentService = medicamentService;
    }

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

    @Override
    public Volunteer getByEmail(String email) throws ServiceException {
        if (Validator.checkEmail(email)) {
            try {
                return volunteerDAO.getByEmail(email);
            } catch (DAOException e) {
                getLogger().log(Level.ERROR, e.getMessage());
                throw new ServiceException(e);
            }
        }
        return null;
    }

    @Override
    public boolean authorizationPassed(String email, long medicamentId) throws ServiceException {
        Medicament medicament = medicamentService.getById(medicamentId);
        if (Optional.ofNullable(medicament).isPresent()) {
            return medicament.getVolunteer().getEmail().equals(email);
        }
        return false;
    }
}
