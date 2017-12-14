package com.epam.volunteer.dao;

import com.epam.volunteer.dao.exception.DAOException;
import com.epam.volunteer.entity.Volunteer;

public interface VolunteerDAO {

    Volunteer getById(long id) throws DAOException;
}
