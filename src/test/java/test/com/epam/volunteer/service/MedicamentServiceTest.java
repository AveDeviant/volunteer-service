package test.com.epam.volunteer.service;

import com.epam.volunteer.dao.MedicamentDAO;
import com.epam.volunteer.dao.exception.DAOException;
import com.epam.volunteer.dao.impl.MedicamentDAOImpl;
import com.epam.volunteer.entity.Medicament;
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
        medicament1.setStatus(false);
        Medicament medicament2 = new Medicament();
        medicament2.setStatus(true);
        medicament2.setId(2);
        Medicament medicament3 = new Medicament();
        medicament3.setId(3);
        medicament3.setStatus(true);
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

    @Test
    public void updateUnableToUpdate() throws ServiceException, DAOException {
        service.setMedicamentDAO(medicamentDAO);
        Medicament medicament = new Medicament();
        medicament.setMedicament("test");
        Medicament data = new Medicament();
        data.setStatus(false);
        Mockito.when(medicamentDAO.getById(1)).thenReturn(data);
        Medicament result = service.update(1, medicament);
        Mockito.verify(medicamentDAO, Mockito.never()).addNew(medicament);
        Assert.assertTrue(result == null);
    }

    @Test
    public void updateEnteredNullEntity() throws ServiceException, DAOException {
        service.setMedicamentDAO(medicamentDAO);
        Medicament medicament = null;
        service.update(1, medicament);
        Mockito.verify(medicamentDAO, Mockito.never()).update(1, medicament);
    }

    @Test
    public void updateAcceptOperation() throws DAOException, ServiceException {
        service.setMedicamentDAO(medicamentDAO);
        Medicament data = new Medicament();
        data.setId(1);
        data.setStatus(true);
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
        data.setStatus(false);
        Mockito.when(medicamentDAO.getById(1)).thenReturn(data);
        assert null == service.getById(1, true);
    }

    @Test
    public void getByIdSuccess() throws DAOException, ServiceException {
        service.setMedicamentDAO(medicamentDAO);
        Medicament data = new Medicament();
        data.setMedicament("Test");
        data.setStatus(true);
        Mockito.when(medicamentDAO.getById(1)).thenReturn(data);
        assert data.equals(service.getById(1, true));
    }


    @Test
    public void addNewNull() throws ServiceException, DAOException {
        service.setMedicamentDAO(medicamentDAO);
        service.addNew(null);
        Mockito.verify(medicamentDAO, Mockito.never()).addNew(null);
    }

    @Test
    public void addNewNotNull() throws ServiceException, DAOException {
        service.setMedicamentDAO(medicamentDAO);
        Medicament medicament = new Medicament();
        service.addNew(medicament);
        Mockito.verify(medicamentDAO, Mockito.times(1)).addNew(medicament);
    }

    private class TestBinder extends AbstractBinder {
        @Override
        protected void configure() {
            bind(medicamentDAO).to(MedicamentDAO.class);
        }
    }
}
