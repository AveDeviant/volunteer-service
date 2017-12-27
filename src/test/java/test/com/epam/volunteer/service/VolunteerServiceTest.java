package test.com.epam.volunteer.service;

import com.epam.volunteer.dao.VolunteerDAO;
import com.epam.volunteer.dao.exception.DAOException;
import com.epam.volunteer.dao.impl.VolunteerDAOImpl;
import com.epam.volunteer.entity.Medicament;
import com.epam.volunteer.entity.Volunteer;
import com.epam.volunteer.service.MedicamentService;
import com.epam.volunteer.service.exception.ServiceException;
import com.epam.volunteer.service.impl.MedicamentServiceImpl;
import com.epam.volunteer.service.impl.VolunteerServiceImpl;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import javax.ws.rs.core.Application;

public class VolunteerServiceTest extends JerseyTest {
    private static VolunteerServiceImpl volunteerService = new VolunteerServiceImpl();
    private MedicamentService medicamentService = Mockito.mock(MedicamentServiceImpl.class);
    private VolunteerDAO volunteerDAO = Mockito.mock(VolunteerDAOImpl.class);


    @Override
    protected Application configure() {
        ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.register(new TestBinder());
        return resourceConfig;
    }

    @Test
    public void getByIdTest() throws DAOException, ServiceException {
        volunteerService.setVolunteerDAO(volunteerDAO);
        Volunteer data = new Volunteer();
        data.setName("test");
        Mockito.when(volunteerDAO.getById(1)).thenReturn(data);
        assert data.equals(volunteerService.getById(1));
    }

    @Test(expected = ServiceException.class)
    public void addNewUniquenessConflict() throws DAOException, ServiceException {
        Volunteer test = new Volunteer();
        test.setName("test");
        test.setEmail("test@mail.com");
        volunteerService.setVolunteerDAO(volunteerDAO);
        Mockito.when(volunteerDAO.addNew(test)).thenThrow(new DAOException());
        volunteerService.addNew(test);
    }

    @Test
    public void addNewNullEntity() throws ServiceException, DAOException {
        Volunteer test = null;
        volunteerService.setVolunteerDAO(volunteerDAO);
        volunteerService.addNew(test);
        Mockito.verify(volunteerDAO, Mockito.never()).addNew(test);
    }

    @Test
    public void authorizationPassedTestFailure() throws ServiceException {
        Volunteer volunteer = new Volunteer();
        volunteer.setEmail("fail");
        Medicament medicament = new Medicament();
        medicament.setVolunteer(volunteer);
        volunteerService.setMedicamentService(medicamentService);
        Mockito.when(medicamentService.getById(1)).thenReturn(medicament);
        assert !volunteerService.authorizationPassed("test", 1);
    }

    @Test
    public void authorizationPassedTestSuccess() throws ServiceException {
        Volunteer volunteer = new Volunteer();
        volunteer.setEmail("test");
        Medicament medicament = new Medicament();
        medicament.setVolunteer(volunteer);
        volunteerService.setMedicamentService(medicamentService);
        Mockito.when(medicamentService.getById(1)).thenReturn(medicament);
        assert volunteerService.authorizationPassed("test", 1);
    }


    private class TestBinder extends AbstractBinder {
        @Override
        protected void configure() {
            bind(medicamentService).to(MedicamentService.class);
            bind(volunteerDAO).to(VolunteerDAO.class);
        }
    }
}
