package com.epam.volunteer.service.impl;

import com.epam.volunteer.dao.MedicamentDAO;
import com.epam.volunteer.dao.exception.DAOException;
import com.epam.volunteer.entity.Medicament;
import com.epam.volunteer.service.MedicamentService;
import com.epam.volunteer.service.exception.ServiceException;
import org.apache.logging.log4j.Level;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class MedicamentServiceImpl extends AbstractService implements MedicamentService {
    private MedicamentDAO medicamentDAO;

    @Inject
    public void setMedicamentDAO(MedicamentDAO medicamentDAO) {
        this.medicamentDAO = medicamentDAO;
    }

    @Override
    public Medicament getById(long id, boolean status) throws ServiceException {
        Medicament medicament = null;
        try {
            medicament = medicamentDAO.getById(id);
            if (medicament != null) {
                if (medicament.isStatus() != status) {
                    return null;
                }
            }
            return medicament;
        } catch (DAOException e) {
            getLogger().log(Level.ERROR, e.getMessage());
            throw new ServiceException(e);
        }
    }

    @Override
    public Medicament getById(long id) throws ServiceException {
        try {
            return medicamentDAO.getById(id);
        } catch (DAOException e) {
            getLogger().log(Level.ERROR, e.getMessage());
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Medicament> getAll() throws ServiceException {
        List<Medicament> medicament = new ArrayList<>();
        try {
            medicament.addAll(medicamentDAO.getAll());
            return medicament;
        } catch (DAOException e) {
            getLogger().log(Level.ERROR, e.getMessage());
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Medicament> getAllActual() throws ServiceException {
        try {
            return getAll().stream().filter(m -> m != null && m.isStatus()).collect(Collectors.toList());
        } catch (ServiceException e) {
            getLogger().log(Level.ERROR, e.getMessage());
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Medicament> getAllActual(int page, int size) throws ServiceException {
        List<Medicament> medicament = new ArrayList<>();
        try {
            if (!(page <= 0) && !(size <= 0)) {
                medicament.addAll(medicamentDAO.getFormatted(page, size));
            }
            return medicament;
        } catch (DAOException e) {
            getLogger().log(Level.ERROR, e.getMessage());
            throw new ServiceException(e);
        }
    }

    @Override
    public Medicament addNew(Medicament medicament) throws ServiceException {
        if (Optional.ofNullable(medicament).isPresent()) {
            try {
                return medicamentDAO.addNew(medicament);
            } catch (DAOException e) {
                getLogger().log(Level.ERROR, e.getMessage());
                throw new ServiceException(e);
            }
        }
        return null;
    }

    @Override
    public void delete(long id) throws ServiceException {
        try {
            medicamentDAO.delete(id);
        } catch (DAOException e) {
            getLogger().log(Level.ERROR, e.getMessage());
            throw new ServiceException(e);
        }
    }

    @Override
    public Medicament update(long id, Medicament medicament) throws ServiceException {
        try {
            Medicament entity = getById(id, true);
            //   entity doesn't exist or unable for update\ uninitialized entered entity.
            if (entity == null || !Optional.ofNullable(medicament).isPresent()) {
                return null;
            }
            return medicamentDAO.update(id, medicament);
        } catch (DAOException e) {
            getLogger().log(Level.ERROR, e.getMessage());
            throw new ServiceException(e);
        }
    }

    @Override
    public long countActual() throws ServiceException {
        try {
            return medicamentDAO.countActual();
        } catch (DAOException e) {
            getLogger().log(Level.ERROR, e.getMessage());
            throw new ServiceException(e);
        }
    }
}
