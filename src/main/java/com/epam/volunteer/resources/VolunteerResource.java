package com.epam.volunteer.resources;

import com.epam.volunteer.dto.AbstractDTO;
import com.epam.volunteer.dto.DTOType;
import com.epam.volunteer.dto.marshaller.DTOMarshaller;
import com.epam.volunteer.entity.Volunteer;
import com.epam.volunteer.service.VolunteerService;
import com.epam.volunteer.service.exception.ServiceException;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/volunteer")
public class VolunteerResource extends AbstractResource {
    private VolunteerService volunteerService;

    @Inject
    public void setVolunteerService(VolunteerService volunteerService) {
        this.volunteerService = volunteerService;
    }

    @GET
    @Path("/{id: [0-9]+ }")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") long id) {
        try {
            Volunteer volunteer = volunteerService.getById(id);
            if (volunteer != null) {
                AbstractDTO dto = DTOMarshaller.marshalDTO(volunteer, DTOType.EXTENDED);
                return Response.ok(dto).build();
            }
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (ServiceException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        try {
            List<Volunteer> volunteerList = volunteerService.getAll();
            List<AbstractDTO> abstractDTOs = DTOMarshaller.marshalDTOList(volunteerList, DTOType.BASIC);
            return Response.ok(abstractDTOs).build();
        } catch (ServiceException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
