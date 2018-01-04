package test.com.epam.volunteer.service;

import com.epam.volunteer.dao.MedicamentDAO;
import com.epam.volunteer.dao.exception.DAOException;
import com.epam.volunteer.dao.impl.MedicamentDAOImpl;
import com.epam.volunteer.entity.Medicament;
import com.epam.volunteer.service.exception.EntityValidationException;
import com.epam.volunteer.service.exception.ResourceForbiddenException;
import com.epam.volunteer.service.exception.ServiceException;
import com.epam.volunteer.service.impl.MedicamentServiceImpl;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import javax.ws.rs.core.Application;
import java.util.ArrayList;
import java.util.List;

public class MedicamentServiceTest extends JerseyTest {
    private MedicamentServiceImpl service = new MedicamentServiceImpl();
    private MedicamentDAO medicamentDAO = Mockito.mock(MedicamentDAOImpl.class);


    @Override
    protected Application configure() {
        ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.register(new TestBinder());
        return resourceConfig;
    }

    @Test
    public void getAllActual() throws DAOException, ServiceException {
        service.setMedicamentDAO(medicamentDAO);
        Medicament medicament1 = new Medicament();
        medicament1.setId(1);
        medicament1.setActual(false);
        Medicament medicament2 = new Medicament();
        medicament2.setActual(true);
        medicament2.setId(2);
        Medicament medicament3 = new Medicament();
        medicament3.setId(3);
        medicament3.setActual(true);
        List<Medicament> data = new ArrayList<>();
        data.add(medicament1);
        data.add(medicament2);
        data.add(medicament3);
        Mockito.when(medicamentDAO.getAll()).thenReturn(data);
        List<Medicament> result = service.getAllActual();
        assert result.size() == 2;
    }

    @Test
    public void getFormattedListIncorrectPage() throws ServiceException {
        List<Medicament> formatted = service.getAllActual(0, 3);
        Assert.assertTrue(formatted.isEmpty());
        formatted = service.getAllActual(1, 0);
        Assert.assertTrue(formatted.isEmpty());
        formatted = service.getAllActual(-1, 3);
        Assert.assertTrue(formatted.isEmpty());
        formatted = service.getAllActual(2, -1);
        Assert.assertTrue(formatted.isEmpty());
    }

    @Test(expected = ResourceForbiddenException.class)
    public void updateUnableToUpdate() throws ServiceException, DAOException {
        service.setMedicamentDAO(medicamentDAO);
        Medicament medicament = new Medicament();
        medicament.setMedicament("test");
        Medicament data = new Medicament();
        data.setActual(false);
        Mockito.when(medicamentDAO.getById(1)).thenReturn(data);
        Medicament result = service.update(1, medicament);
    }

    @Test(expected = ResourceForbiddenException.class)
    public void updateEnteredNullEntity() throws ServiceException, DAOException {
        service.setMedicamentDAO(medicamentDAO);
        Medicament medicament = null;
        service.update(1, medicament);
    }

    @Test
    public void updateAcceptOperation() throws DAOException, ServiceException {
        service.setMedicamentDAO(medicamentDAO);
        Medicament data = new Medicament();
        data.setId(1);
        data.setActual(true);
        Medicament medicament = new Medicament();
        medicament.setMedicament("test");
        Mockito.when(medicamentDAO.getById(1)).thenReturn(data);
        service.update(1, medicament);
        Mockito.verify(medicamentDAO, Mockito.times(1)).update(1, medicament);
    }

    @Test
    public void getByIdUnable() throws DAOException, ServiceException {
        service.setMedicamentDAO(medicamentDAO);
        Medicament data = new Medicament();
        data.setActual(false);
        Mockito.when(medicamentDAO.getById(1)).thenReturn(data);
        assert null == service.getById(1, true);
    }

    @Test
    public void getByIdSuccess() throws DAOException, ServiceException {
        service.setMedicamentDAO(medicamentDAO);
        Medicament data = new Medicament();
        data.setMedicament("Test");
        data.setActual(true);
        Mockito.when(medicamentDAO.getById(1)).thenReturn(data);
        assert data.equals(service.getById(1, true));
    }


    @Test(expected = EntityValidationException.class)
    public void addNewNull() throws ServiceException, DAOException {
        service.setMedicamentDAO(medicamentDAO);
        service.addNew(null);
    }

    @Test
    public void addNewNotNull() throws ServiceException, DAOException {
        service.setMedicamentDAO(medicamentDAO);
        Medicament medicament = new Medicament();
        medicament.setRequirement(1);
        service.addNew(medicament);
        Mockito.verify(medicamentDAO, Mockito.times(1)).addNew(medicament);
    }

    @Test(expected = EntityValidationException.class)
    public void addNewIncorrectRequirementSize() throws ServiceException, DAOException {
        Medicament medicament = new Medicament();
        service.addNew(medicament);
    }

    private class TestBinder extends AbstractBinder {
        @Override
        protected void configure() {
            bind(medicamentDAO).to(MedicamentDAO.class);
        }
    }
}
