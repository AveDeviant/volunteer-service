package test.com.epam.volunteer;


import com.epam.volunteer.config.ApplicationBinder;
import com.epam.volunteer.entity.Medicament;
import com.epam.volunteer.resources.MedicamentResource;
import com.epam.volunteer.service.MedicamentService;
import com.epam.volunteer.service.exception.ServiceException;
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

import javax.inject.Inject;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

public class MedicamentResourceTest extends JerseyTest {

//    MedicamentService medicamentService = Mockito.mock(MedicamentService.class);
//
//
//    @Override
//    protected Application configure() {
//        return new ResourceConfig(MedicamentResource.class);
//    }
//
//    @Test
//    public void test() throws ServiceException {
//        Medicament medicament = new Medicament();
//        medicament.setId(1);
//        medicament.setMedicament("TEST");
//        Mockito.when(medicamentService.getById(1, true)).thenReturn(medicament);
//        Response response = target("/medicament/1").request().get();
//        Assert.assertEquals(response.getStatus(), 200);
//        Assert.assertEquals(response.getEntity(), medicament);
//        response.close();
//    }

}
