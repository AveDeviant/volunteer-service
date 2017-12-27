package com.epam.volunteer.dao;

import com.epam.volunteer.dao.exception.DAOException;
import com.epam.volunteer.entity.Volunteer;

import java.util.List;

public interface VolunteerDAO {

    Volunteer getById(long id) throws DAOException;

    List<Volunteer> getAll() throws DAOException;

    Volunteer getByEmail(String email) throws DAOException;

    Volunteer addNew(Volunteer volunteer) throws DAOException;
}
