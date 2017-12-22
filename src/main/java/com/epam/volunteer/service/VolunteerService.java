package com.epam.volunteer.service;

import com.epam.volunteer.entity.Volunteer;
import com.epam.volunteer.service.exception.ServiceException;

import java.util.List;

public interface VolunteerService {

    Volunteer getById(long id) throws ServiceException;

    List<Volunteer> getAll() throws ServiceException;

    Volunteer getByEmail(String email) throws ServiceException;
}
