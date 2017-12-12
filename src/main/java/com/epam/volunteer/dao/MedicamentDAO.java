package com.epam.volunteer.dao;

import com.epam.volunteer.entity.Medicament;

import java.util.List;

/**
 * @author Mikita Buslauski
 */
public interface MedicamentDAO {

    List<Medicament> getAll();

    Medicament getById(long id);

    List<Medicament> getFormatted(int startedFrom, int size);
}
