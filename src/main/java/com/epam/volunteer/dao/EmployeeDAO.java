package com.epam.volunteer.dao;

import com.epam.volunteer.dao.exception.DAOException;
import com.epam.volunteer.entity.Employee;

public interface EmployeeDAO {

    Employee getByEmail(String email) throws DAOException;
}
