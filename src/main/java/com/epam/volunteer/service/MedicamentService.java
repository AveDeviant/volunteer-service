package com.epam.volunteer.service;

import com.epam.volunteer.entity.Medicament;

import java.util.List;

public interface MedicamentService {

    Medicament getById(long id);

    List<Medicament> getAll();

    List<Medicament> getAllActual();

    List<Medicament> getAllActual(int page, int size);

}
