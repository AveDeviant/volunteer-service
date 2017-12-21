package test.com.epam.volunteer.resources;


import com.epam.volunteer.dto.AbstractDTO;
import com.epam.volunteer.dto.DTOType;
import com.epam.volunteer.dto.extended.MedicamentDTO;
import com.epam.volunteer.dto.marshaller.DTOMarshaller;
import com.epam.volunteer.entity.Medicament;
import com.epam.volunteer.entity.Volunteer;
import com.epam.volunteer.resources.MedicamentResource;
import com.epam.volunteer.service.DonationService;
import com.epam.volunteer.service.impl.LinkServiceImpl;
import com.epam.volunteer.service.MedicamentService;
import com.epam.volunteer.service.exception.ServiceException;
import com.epam.volunteer.service.impl.DonationServiceImpl;
import com.epam.volunteer.service.impl.MedicamentServiceImpl;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import javax.ws.rs.core.*;
import java.util.ArrayList;
import java.util.List;

public class MedicamentResourceTest extends JerseyTest {

    private MedicamentService medicamentService = Mockito.mock(MedicamentServiceImpl.class);
    private DonationService donationService = Mockito.mock(DonationServiceImpl.class);
    private LinkServiceImpl linkService = Mockito.mock(LinkServiceImpl.class);

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
        medicament.setVolunteer(new Volunteer());
        Mockito.when(medicamentService.getById(1, true)).thenReturn(medicament);
        Response response = target("/medicament/1").request().get();
        Assert.assertEquals(response.getStatus(), 200);
        AbstractDTO expected = DTOMarshaller.marshalDTO(medicament, DTOType.EXTENDED);
        MedicamentDTO result = target("/medicament/1").request().get(MedicamentDTO.class);
        Assert.assertEquals(expected, result);
        response.close();
    }


    @Test
    public void getAllAvailableTest() throws ServiceException {
        Medicament medicament = new Medicament();
        medicament.setId(1);
        medicament.setMedicament("TEST");
        medicament.setStatus(true);
        medicament.setVolunteer(new Volunteer());
        Medicament medicament2 = new Medicament();
        medicament2.setId(2);
        medicament2.setStatus(true);
        medicament2.setMedicament("TEST2");
        medicament2.setVolunteer(new Volunteer());
        List<Medicament> available = new ArrayList<>();
        available.add(medicament);
        available.add(medicament2);
        Mockito.when(medicamentService.getAllActual(1, 2)).thenReturn(available);
        Mockito.when(linkService.buildLinks(1, 2, Mockito.mock(UriInfo.class))).thenReturn(new Link[]{Link.fromUri("").build()});
        Response response = target("/medicament").request().get();
        Assert.assertEquals(response.getStatus(), 200);
        List<AbstractDTO> expected = DTOMarshaller.marshalDTOList(available, DTOType.BASIC);
        List<MedicamentDTO> result = target("/medicament").request().get(new GenericType<List<MedicamentDTO>>() {
        });
        Assert.assertEquals(expected, result);
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
            bind(linkService).to(LinkServiceImpl.class);
        }
    }
}
