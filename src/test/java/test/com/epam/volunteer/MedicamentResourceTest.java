package test.com.epam.volunteer;


import com.epam.volunteer.config.ApplicationBinder;
import com.epam.volunteer.dto.MedicamentDTO;
import com.epam.volunteer.dto.marshaller.DTOMarshaller;
import com.epam.volunteer.entity.Medicament;
import com.epam.volunteer.resources.MedicamentResource;
import com.epam.volunteer.service.DonationService;
import com.epam.volunteer.service.MedicamentService;
import com.epam.volunteer.service.exception.ServiceException;
import com.epam.volunteer.service.impl.DonationServiceImpl;
import com.epam.volunteer.service.impl.MedicamentServiceImpl;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.Binder;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.MockInjection;

import javax.inject.Inject;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

public class MedicamentResourceTest extends JerseyTest {

    private MedicamentService medicamentService = Mockito.mock(MedicamentServiceImpl.class);
    private DonationService donationService = Mockito.mock(DonationServiceImpl.class);


    @Override
    protected Application configure() {
        ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.register(MedicamentResource.class);
        resourceConfig.register(new TestBinder());
        return resourceConfig;
    }

    @Test
    public void getEntityExists() throws ServiceException {
        Medicament medicament = new Medicament();
        medicament.setId(1);
        medicament.setMedicament("TEST");
        Mockito.when(medicamentService.getById(1, true)).thenReturn(medicament);
        Response response = target("/medicament/1").request().get();
        Assert.assertEquals(response.getStatus(), 200);
        MedicamentDTO expected = (MedicamentDTO) DTOMarshaller.marshalDTO(medicament, true);
        MedicamentDTO result = target("/medicament/1").request().get(MedicamentDTO.class);
        Assert.assertEquals(expected, result);
        response.close();
    }

    @Test
    public void notFoundTest() throws ServiceException {
        Mockito.when(medicamentService.getById(1, true)).thenReturn(null);
        Response response = target("/medicament/1").request().get();
        Assert.assertEquals(404, response.getStatus());
    }


    private class TestBinder extends AbstractBinder {

        @Override
        protected void configure() {
            bind(medicamentService).to(MedicamentService.class);
            bind(donationService).to(DonationService.class);
        }
    }
}
