package test.com.epam.volunteer.resources;

import com.epam.volunteer.dto.AbstractDTO;
import com.epam.volunteer.dto.DTOType;
import com.epam.volunteer.dto.extended.VolunteerDTO;
import com.epam.volunteer.dto.marshaller.DTOMarshaller;
import com.epam.volunteer.entity.Volunteer;
import com.epam.volunteer.resources.VolunteerResource;
import com.epam.volunteer.service.VolunteerService;
import com.epam.volunteer.service.exception.ServiceException;
import com.epam.volunteer.service.impl.VolunteerServiceImpl;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

public class VolunteerResourceTest extends JerseyTest {
    private VolunteerService volunteerService = Mockito.mock(VolunteerServiceImpl.class);


    @Override
    protected Application configure() {
        ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.register(VolunteerResource.class);
        resourceConfig.register(new TestBinder());
        return resourceConfig;
    }


    @Test
    public void getByIdExists() throws ServiceException {
        Volunteer volunteer = new Volunteer();
        volunteer.setName("test1");
        volunteer.setId(1);
        Mockito.when(volunteerService.getById(1)).thenReturn(volunteer);
        Response response = target("/volunteer/1").request().get();
        Assert.assertEquals(response.getStatus(), 200);
        AbstractDTO expected = DTOMarshaller.marshalDTO(volunteer, DTOType.EXTENDED);
        VolunteerDTO result = target("/volunteer/1").request().get(VolunteerDTO.class);
        Assert.assertEquals(expected, result);
        response.close();
    }

    @Test
    public void getByIdNotFound() throws ServiceException {
        Mockito.when(volunteerService.getById(1)).thenReturn(null);
        Response response = target("/volunteer/1").request().get();
        Assert.assertEquals(response.getStatus(), 404);
    }

    @Test
    public void getAll() throws ServiceException {
        Volunteer volunteer = new Volunteer();
        volunteer.setName("test1");
        volunteer.setId(1);
        List<Volunteer> volunteers = new ArrayList<>();
        volunteers.add(volunteer);
        Mockito.when(volunteerService.getAll()).thenReturn(volunteers);
        List<AbstractDTO> expected = DTOMarshaller.marshalDTOList(volunteers, DTOType.BASIC);
        List<VolunteerDTO> result = target("/volunteer").request().get(new GenericType<List<VolunteerDTO>>() {
        });
        Assert.assertEquals(expected, result);
    }

    private class TestBinder extends AbstractBinder {

        @Override
        protected void configure() {
            bind(volunteerService).to(VolunteerService.class);
        }
    }
}
