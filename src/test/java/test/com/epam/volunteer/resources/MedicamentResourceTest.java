package test.com.epam.volunteer.resources;


import com.epam.volunteer.dto.AbstractDTO;
import com.epam.volunteer.dto.DTOType;
import com.epam.volunteer.dto.extended.MedicamentDTO;
import com.epam.volunteer.dto.marshaller.DTOMarshaller;
import com.epam.volunteer.dto.marshaller.DTOUnmarshaller;
import com.epam.volunteer.entity.AbstractEntity;
import com.epam.volunteer.entity.Medicament;
import com.epam.volunteer.entity.Volunteer;
import com.epam.volunteer.resources.MedicamentResource;
import com.epam.volunteer.service.*;
import com.epam.volunteer.service.impl.*;
import com.epam.volunteer.service.exception.ServiceException;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;


import javax.servlet.MultipartConfigElement;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class MedicamentResourceTest extends JerseyTest {

    private MedicamentService medicamentService = Mockito.mock(MedicamentServiceImpl.class);
    private DonationService donationService = Mockito.mock(DonationServiceImpl.class);
    private LinkService linkService = Mockito.mock(LinkServiceImpl.class);
    private EmployeeService employeeService = Mockito.mock(EmployeeServiceImpl.class);
    private VolunteerService volunteerService = Mockito.mock(VolunteerService.class);
    private HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    private UriInfo uriInfo = Mockito.mock(UriInfo.class);
    private UriBuilder uriBuilder = Mockito.mock(UriBuilder.class);

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


    @Test
    public void testAddNewUnauthorized() throws ServiceException {
        Medicament medicament = new Medicament();
        medicament.setId(1);
        medicament.setMedicament("test");
        medicament.setStatus(true);
        AbstractDTO dto = DTOMarshaller.marshalDTO(medicament, DTOType.BASIC);
        Entity<AbstractDTO> med = Entity.entity(dto, MediaType.APPLICATION_JSON);
        Mockito.when(volunteerService.getByEmail("test")).thenReturn(null);
        Response response = target("/medicament")
                .request()
                .header("Authorization", "test")
                .buildPost(med).invoke();
        Assert.assertEquals(401, response.getStatus());
    }

    @Test
    public void testAddNew() throws ServiceException {
        Medicament medicament = new Medicament();
        medicament.setId(1);
        medicament.setMedicament("test");
        medicament.setStatus(true);
        medicament.setRequirement(2);
        medicament.setCurrentCount(0);
        Volunteer volunteer = new Volunteer();
        volunteer.setName("volunteer");
        volunteer.setId(1);
        volunteer.setEmail("test");
        AbstractDTO dto = DTOMarshaller.marshalDTO(medicament, DTOType.BASIC);
        Entity<AbstractDTO> dtoEntity = Entity.entity(dto, MediaType.APPLICATION_JSON);
        Mockito.when(volunteerService.getByEmail("test")).thenReturn(volunteer);
        medicament.setVolunteer(volunteer);
        Mockito.when(medicamentService.addNew(medicament)).thenReturn(medicament);
        Response response = target("/medicament").request().header("Authorization", "test")
                .buildPost(dtoEntity).invoke();
        Mockito.verify(medicamentService, times(1)).addNew(medicament);
        Assert.assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
    }


    @Test
    public void deleteUnauthorized() throws ServiceException {
        Mockito.when(volunteerService.authorizationPassed("test", 1)).thenReturn(false);
        Response response = target("/medicament/1").request().header("Authorization", "test")
                .buildDelete().invoke();
        Mockito.verify(medicamentService, never()).delete(1);
        Assert.assertEquals(401, response.getStatus());
    }

    @Test
    public void deleteAccept() throws ServiceException {
        Volunteer volunteer = new Volunteer();
        volunteer.setName("Privet");
        Medicament medicament = new Medicament();
        medicament.setVolunteer(volunteer);
        Mockito.when(volunteerService.authorizationPassed("test", 1)).thenReturn(true);
        Mockito.when(medicamentService.getById(1)).thenReturn(medicament);
        Response response = target("/medicament/1").request().header("Authorization", "test")
                .buildDelete().invoke();
        Mockito.verify(medicamentService, times(1)).delete(1);
        Assert.assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }

    @Test
    public void updateUnauthorized() throws ServiceException {
        Medicament medicament = new Medicament();
        medicament.setRequirement(20);
        medicament.setId(1);
        AbstractDTO abstractDTO = DTOMarshaller.marshalDTO(medicament, DTOType.BASIC);
        Entity<AbstractDTO> dtoEntity = Entity.entity(abstractDTO, MediaType.APPLICATION_JSON);
        Mockito.when(volunteerService.authorizationPassed("test", 1)).thenReturn(false);
        Mockito.when(medicamentService.getById(1, true)).thenReturn(new Medicament());
        Response response = target("/medicament/1").request()
                .buildPost(dtoEntity).invoke();
        Assert.assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
        verify(medicamentService, never()).update(1, (Medicament) DTOUnmarshaller.unmarshalDTO(abstractDTO));
    }

    @Test
    public void updateAuthorizationAccept() throws ServiceException {
        Medicament medicament = new Medicament();
        medicament.setRequirement(20);
        medicament.setId(1);
        AbstractDTO abstractDTO = DTOMarshaller.marshalDTO(medicament, DTOType.BASIC);
        Entity<AbstractDTO> dtoEntity = Entity.entity(abstractDTO, MediaType.APPLICATION_JSON);
        Mockito.when(volunteerService.authorizationPassed("test", 1)).thenReturn(true);
        Mockito.when(medicamentService.getById(1, true)).thenReturn(new Medicament());
        Response response = target("/medicament/1").request().header("Authorization", "test")
                .buildPost(dtoEntity).invoke();
        verify(medicamentService, times(1)).update(1, (Medicament) DTOUnmarshaller.unmarshalDTO(abstractDTO));
    }

    @Test
    public void updateResourceUnable() throws ServiceException {
        Medicament medicament = new Medicament();
        medicament.setRequirement(20);
        medicament.setId(1);
        AbstractDTO abstractDTO = DTOMarshaller.marshalDTO(medicament, DTOType.BASIC);
        Entity<AbstractDTO> dtoEntity = Entity.entity(abstractDTO, MediaType.APPLICATION_JSON);
        Mockito.when(volunteerService.authorizationPassed("test", 1)).thenReturn(true);
        Response response = target("/medicament/1").request().header("Authorization", "test")
                .buildPost(dtoEntity).invoke();
        Mockito.when(medicamentService.update(1, medicament)).thenReturn(null);
        Assert.assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }


    @Test
    public void updateSuccess() throws ServiceException {
        Medicament medicament = new Medicament();
        medicament.setRequirement(20);
        medicament.setId(1);
        AbstractDTO abstractDTO = DTOMarshaller.marshalDTO(medicament, DTOType.BASIC);
        Entity<AbstractDTO> dtoEntity = Entity.entity(abstractDTO, MediaType.APPLICATION_JSON);
        Mockito.when(volunteerService.authorizationPassed("test", 1)).thenReturn(true);
        Mockito.when(medicamentService.update(1, medicament)).thenReturn(medicament);
        Response response = target("/medicament/1").request().header("Authorization", "test")
                .buildPost(dtoEntity).invoke();
        Mockito.verify(medicamentService, times(1)).update(1, medicament);
        Mockito.when(medicamentService.getById(1,true)).thenReturn(medicament);
        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }


    private class TestBinder extends AbstractBinder {

        @Override
        protected void configure() {
            bind(medicamentService).to(MedicamentService.class);
            bind(donationService).to(DonationService.class);
            bind(linkService).to(LinkService.class);
            bind(employeeService).to(EmployeeService.class);
            bind(volunteerService).to(VolunteerService.class);
            bind(request).to(HttpServletRequest.class);
        }
    }
}
