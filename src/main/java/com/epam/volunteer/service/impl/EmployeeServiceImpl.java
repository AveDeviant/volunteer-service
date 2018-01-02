package com.epam.volunteer.service.impl;

import com.epam.volunteer.dao.EmployeeDAO;
import com.epam.volunteer.dao.exception.DAOException;
import com.epam.volunteer.dao.impl.EmployeeDAOImpl;
import com.epam.volunteer.entity.Employee;
import com.epam.volunteer.service.EmployeeService;
import com.epam.volunteer.service.exception.ServiceException;
import com.epam.volunteer.util.Validator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private static final Logger LOGGER = LogManager.getLogger();
    private EmployeeDAO employeeDAO;

    @Inject
    public void setEmployeeDAO(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    @Override
    public Employee getByEmail(String email) throws ServiceException {
        if (Validator.checkEmail(email)) {
            try {
                return employeeDAO.getByEmail(email);
            } catch (DAOException e) {
                LOGGER.log(Level.ERROR, e.getMessage());
                throw new ServiceException(e);
            }
        }
        return null;
    }

}
