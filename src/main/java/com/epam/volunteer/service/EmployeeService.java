package com.epam.volunteer.service;

import com.epam.volunteer.entity.Employee;
import com.epam.volunteer.service.exception.ServiceException;
import org.jvnet.hk2.annotations.Contract;

@Contract
public interface EmployeeService {

    Employee getByEmail(String email) throws ServiceException;
}
