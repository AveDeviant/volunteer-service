package com.epam.volunteer.service.impl;

import com.epam.volunteer.dao.EmployeeDAO;
import com.epam.volunteer.dao.exception.DAOException;
import com.epam.volunteer.entity.Employee;
import com.epam.volunteer.service.EmployeeService;
import com.epam.volunteer.service.exception.BusinessLogicException;
import com.epam.volunteer.service.exception.ServiceException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private static final Logger LOGGER = LogManager.getLogger();
    private EmployeeDAO employeeDAO;

    @Inject
    public void setEmployeeDAO(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    @Override
    public Employee getByEmail(String email) throws ServiceException, BusinessLogicException {
            try {
                return employeeDAO.getByEmail(email);
            } catch (DAOException e) {
                LOGGER.log(Level.ERROR, e.getMessage());
                throw new ServiceException(e);
            }
    }

}
