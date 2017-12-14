package com.epam.volunteer.service;

import com.epam.volunteer.entity.Volunteer;
import com.epam.volunteer.service.exception.ServiceException;

public interface VolunteerService {

    Volunteer getById(long id) throws ServiceException;
}
