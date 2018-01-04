package com.epam.volunteer.service;

import com.epam.volunteer.entity.Medicament;
import com.epam.volunteer.entity.Volunteer;
import com.epam.volunteer.service.exception.ServiceException;

import java.util.List;

public interface VolunteerService {

    Volunteer getById(long id) throws ServiceException;

    List<Volunteer> getAll() throws ServiceException;

    List<Volunteer> getAll(int page, int size) throws ServiceException;

    Volunteer getByEmail(String email) throws ServiceException;

    Volunteer addNew(Volunteer volunteer) throws ServiceException;

    List<Medicament> getVolunteerMedicament(long volunteerId) throws ServiceException;

    boolean authorizationPassed(String email, long medicamentId) throws ServiceException;

    long countAll() throws ServiceException;
}
