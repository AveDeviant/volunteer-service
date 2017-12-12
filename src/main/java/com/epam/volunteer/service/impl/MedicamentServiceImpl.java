package com.epam.volunteer.service.impl;

import com.epam.volunteer.dao.MedicamentDAO;
import com.epam.volunteer.entity.Medicament;
import com.epam.volunteer.service.MedicamentService;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.List;
import java.util.stream.Collectors;


@Singleton
public class MedicamentServiceImpl implements MedicamentService {
    @Inject
    private MedicamentDAO medicamentDAO;

    @Override
    public Medicament getById(long id) {
        return medicamentDAO.getById(id);
    }

    @Override
    public List<Medicament> getAll() {
        return medicamentDAO.getAll();
    }

    @Override
    public List<Medicament> getAllActual() {
        return getAll().stream().filter(m -> m.isStatus()).collect(Collectors.toList());
    }

    @Override
    public List<Medicament> getAllActual(int page, int size) {
        return medicamentDAO.getFormatted(page,size).stream().collect(Collectors.toList());
    }
}
