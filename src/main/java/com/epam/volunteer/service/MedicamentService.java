package com.epam.volunteer.service;

import com.epam.volunteer.entity.Medicament;
import com.epam.volunteer.service.exception.ServiceException;

import java.util.List;

@org.jvnet.hk2.annotations.Contract
public interface MedicamentService {

    Medicament getById(long id, boolean status) throws ServiceException;

    Medicament getById(long id) throws ServiceException;

    List<Medicament> getAll() throws ServiceException;

    List<Medicament> getAllActual() throws ServiceException;

    List<Medicament> getAllActual(int page, int size) throws ServiceException;

    Medicament addNew(Medicament medicament) throws ServiceException;

}