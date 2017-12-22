package test.com.epam.volunteer.service;

import com.epam.volunteer.dao.MedicamentDAO;
import com.epam.volunteer.dao.exception.DAOException;
import com.epam.volunteer.dao.impl.MedicamentDAOImpl;
import com.epam.volunteer.dto.AbstractDTO;
import com.epam.volunteer.dto.DTOType;
import com.epam.volunteer.dto.marshaller.DTOMarshaller;
import com.epam.volunteer.entity.Medicament;
import com.epam.volunteer.resources.MedicamentResource;
import com.epam.volunteer.service.exception.ServiceException;
import com.epam.volunteer.service.impl.MedicamentServiceImpl;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
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
        resourceConfig.register(MedicamentResource.class);
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


    private class TestBinder extends AbstractBinder {
        @Override
        protected void configure() {
            bind(medicamentDAO).to(MedicamentDAO.class);
        }
    }
}
