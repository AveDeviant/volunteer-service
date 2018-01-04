package com.epam.volunteer.service.impl;

import com.epam.volunteer.dao.VolunteerDAO;
import com.epam.volunteer.dao.exception.DAOException;
import com.epam.volunteer.entity.Medicament;
import com.epam.volunteer.entity.Volunteer;
import com.epam.volunteer.service.MedicamentService;
import com.epam.volunteer.service.VolunteerService;
import com.epam.volunteer.service.exception.EntityValidationException;
import com.epam.volunteer.service.exception.ResourceNotFoundException;
import com.epam.volunteer.service.exception.ServiceException;
import com.epam.volunteer.util.Validator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class VolunteerServiceImpl implements VolunteerService {
    private static final Logger LOGGER = LogManager.getLogger();
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
            return volunteerDAO.getById(id);
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Volunteer> getAll() throws ServiceException {
        try {
            return volunteerDAO.getAll();
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Volunteer> getAll(int page, int size) throws ServiceException {
        List<Volunteer> volunteers = new ArrayList<>();
        try {
            if (!(page <= 0) && !(size <= 0)) {
                volunteers.addAll(volunteerDAO.getAll(page, size));
            }
            return volunteers;
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            throw new ServiceException(e);
        }
    }

    @Override
    public Volunteer getByEmail(String email) throws ServiceException {
        try {
            return volunteerDAO.getByEmail(email);
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            throw new ServiceException(e);
        }
    }

    @Override
    public Volunteer addNew(Volunteer volunteer) throws ServiceException {
        if (Optional.ofNullable(volunteer).isPresent() && Validator.checkEmail(volunteer.getEmail())) {
            try {
                return volunteerDAO.addNew(volunteer);
            } catch (DAOException e) {
                LOGGER.log(Level.ERROR, e.getMessage());
                throw new ServiceException(e);
            }
        }
        throw new EntityValidationException();
    }

    @Override
    public List<Medicament> getVolunteerMedicament(long volunteerId) throws ServiceException {
        Volunteer volunteer = getById(volunteerId);
        if (Optional.ofNullable(volunteer).isPresent()) {
            return volunteer.getMedicament().stream().filter(Medicament::isActual).collect(Collectors.toList());
        }
        throw new ResourceNotFoundException();
    }

    @Override
    public boolean authorizationPassed(String email, long medicamentId) throws ServiceException {
        Medicament medicament = medicamentService.getById(medicamentId);
        if (Optional.ofNullable(medicament).isPresent()) {
            return medicament.getVolunteer().getEmail().equals(email);
        }
        return false;
    }

    @Override
    public long countAll() throws ServiceException {
        try {
            return volunteerDAO.countAll();
        } catch (DAOException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            throw new ServiceException(e);
        }
    }

}
