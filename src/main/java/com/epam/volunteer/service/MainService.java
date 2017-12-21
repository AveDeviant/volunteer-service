package com.epam.volunteer.service;

import com.epam.volunteer.service.exception.ServiceException;

public interface MainService {

    long countActual() throws ServiceException;

}
