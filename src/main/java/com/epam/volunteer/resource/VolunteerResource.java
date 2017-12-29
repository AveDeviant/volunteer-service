package com.epam.volunteer.resource;

import com.epam.volunteer.dto.AbstractDTO;
import com.epam.volunteer.dto.DTOType;
import com.epam.volunteer.dto.base.BaseDonationDTO;
import com.epam.volunteer.dto.base.BaseMedicamentDTO;
import com.epam.volunteer.dto.base.BaseVolunteerDTO;
import com.epam.volunteer.dto.extended.VolunteerDTO;
import com.epam.volunteer.dto.marshaller.DTOMarshaller;
import com.epam.volunteer.dto.marshaller.DTOUnmarshaller;
import com.epam.volunteer.entity.Volunteer;
import com.epam.volunteer.response.ServerMessage;
import com.epam.volunteer.service.VolunteerService;
import com.epam.volunteer.service.exception.ServiceException;
import com.epam.volunteer.service.impl.VolunteerServiceImpl;
import io.swagger.annotations.*;
import jdk.net.SocketFlow;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.Optional;

@Path("/volunteer")
@Api(value = "volunteer")
@Produces("application/json")
public class VolunteerResource extends AbstractResource {
    private VolunteerService volunteerService;

    @Inject
    public void setVolunteerService(VolunteerService volunteerService) {
        this.volunteerService = volunteerService;
    }

    @GET
    @Path("/{id: [0-9]+ }")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get volunteer with specified ID.",
            response = VolunteerDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Volunteer with specified ID nor found."),
            @ApiResponse(code = 500, message = "Internal error.")
    })
    public Response getById(@ApiParam(value = "Volunteer ID", required = true) @PathParam("id") long id) {
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
    @ApiOperation(value = "Get all volunteers.",
            response = BaseVolunteerDTO.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "Internal error.")
    })
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
    @ApiOperation(value = "Register a new volunteer.",
            response = VolunteerDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "OK", responseHeaders = {
                    @ResponseHeader(name = "Location", description = "URL of created resource", response = String.class)}),
            @ApiResponse(code = 500, message = "Internal error."),
            @ApiResponse(code = 422, message = "Unprocessable entity/invalid email."),
            @ApiResponse(code = 409, message = "Non-unique volunteer name/email"),
            @ApiResponse(code = 400, message = "Bad request.")
    })
    public Response addNew(@ApiParam(value = "New volunteer object.", required = true)
                                   BaseVolunteerDTO volunteerDTO, @Context UriInfo uriInfo) {
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
