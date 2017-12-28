package com.epam.volunteer.resources;

import com.epam.volunteer.dto.AbstractDTO;
import com.epam.volunteer.dto.DTOType;
import com.epam.volunteer.dto.base.BaseVolunteerDTO;
import com.epam.volunteer.dto.marshaller.DTOMarshaller;
import com.epam.volunteer.dto.marshaller.DTOUnmarshaller;
import com.epam.volunteer.entity.Volunteer;
import com.epam.volunteer.manager.EntityManagerWrapper;
import com.epam.volunteer.response.ServerMessage;
import com.epam.volunteer.service.VolunteerService;
import com.epam.volunteer.service.exception.ServiceException;
import com.epam.volunteer.service.impl.VolunteerServiceImpl;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.Optional;

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
            provideInitialization();
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
            provideInitialization();
            List<Volunteer> volunteerList = volunteerService.getAll();
            List<AbstractDTO> abstractDTOs = DTOMarshaller.marshalDTOList(volunteerList, DTOType.BASIC);
            return Response.ok(abstractDTOs).build();
        } catch (ServiceException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addNew(BaseVolunteerDTO volunteerDTO, @Context UriInfo uriInfo) {
        try {
            if (Optional.ofNullable(volunteerDTO).isPresent()) {
                provideInitialization();
                Volunteer volunteer = (Volunteer) DTOUnmarshaller.unmarshalDTO(volunteerDTO);
                Volunteer result = volunteerService.addNew(volunteer);
                if (Optional.ofNullable(result).isPresent()) {
                    AbstractDTO dto = DTOMarshaller.marshalDTO(result, DTOType.EXTENDED);
                    UriBuilder builder = uriInfo.getAbsolutePathBuilder();
                    return Response.created(builder.path(String.valueOf(dto.getId())).build())
                            .entity(dto)
                            .build();
                }
                return Response.status(422).build();
            }
            return Response.status(Response.Status.BAD_REQUEST).entity(ServerMessage.INVALID_INPUT).build();
        } catch (ServiceException e) {
            return Response.status(Response.Status.CONFLICT).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    private void provideInitialization() {
        volunteerService = Optional.ofNullable(volunteerService).orElse(new VolunteerServiceImpl());
    }
}
