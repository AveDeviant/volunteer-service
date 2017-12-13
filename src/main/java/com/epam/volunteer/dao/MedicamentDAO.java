package com.epam.volunteer.dao;

import com.epam.volunteer.dao.exception.DAOException;
import com.epam.volunteer.entity.Medicament;
import org.jvnet.hk2.annotations.Contract;

import java.util.List;

/**
 * @author Mikita Buslauski
 */
@Contract
public interface MedicamentDAO {

    List<Medicament> getAll() throws DAOException;

    Medicament getById(long id) throws DAOException;

    List<Medicament> getFormatted(int page, int size) throws DAOException;

    Medicament addNew(Medicament medicament) throws DAOException;
}
