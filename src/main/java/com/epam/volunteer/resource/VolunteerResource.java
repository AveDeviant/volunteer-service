package com.epam.volunteer.resource;

import com.epam.volunteer.dto.AbstractDTO;
import com.epam.volunteer.dto.DTOType;
import com.epam.volunteer.dto.base.BaseMedicamentDTO;
import com.epam.volunteer.dto.base.BaseVolunteerDTO;
import com.epam.volunteer.dto.extended.VolunteerDTO;
import com.epam.volunteer.dto.marshaller.DTOMarshaller;
import com.epam.volunteer.dto.marshaller.DTOUnmarshaller;
import com.epam.volunteer.entity.Medicament;
import com.epam.volunteer.entity.Volunteer;
import com.epam.volunteer.service.VolunteerService;
import com.epam.volunteer.service.exception.EntityValidationException;
import com.epam.volunteer.service.exception.ResourceNotFoundException;
import com.epam.volunteer.service.exception.ServiceException;
import io.swagger.annotations.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.Optional;

@Path("/volunteers")
@Api(value = "volunteers")
@Produces("application/json")
public class VolunteerResource {
    private static final Logger LOGGER = LogManager.getLogger();
    private VolunteerService volunteerService;

    @Inject
    public void setVolunteerService(VolunteerService volunteerService) {
        this.volunteerService = volunteerService;
    }

    @GET
    @Path("/{id}")
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
            Volunteer volunteer = volunteerService.getById(id);
            if (volunteer != null) {
                AbstractDTO dto = DTOMarshaller.marshalDTO(volunteer, DTOType.BASIC);
                return Response.ok(dto).build();
            }
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (ServiceException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("/{id}/medicament")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get all actual medicament of specified volunteer.", response = BaseMedicamentDTO.class,
            responseContainer = "List")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Volunteer not found"),
            @ApiResponse(code = 500, message = "Internal error.")})
    public Response getVolunteerMedicament(@ApiParam(value = "ID of desired volunteer.", required = true)
                                           @PathParam("id") long id) {
        try {
            List<Medicament> volunteerMedicament = volunteerService.getVolunteerMedicament(id);
            List<AbstractDTO> dto = DTOMarshaller.marshalDTOList(volunteerMedicament, DTOType.BASIC);
            return Response.ok(dto).build();
        } catch (ServiceException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (ResourceNotFoundException e) {  //or can I return a null object?
            return Response.status(Response.Status.NOT_FOUND).build();
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
                Volunteer volunteer = (Volunteer) DTOUnmarshaller.unmarshalDTO(volunteerDTO);
                Volunteer result = volunteerService.addNew(volunteer);
                AbstractDTO dto = DTOMarshaller.marshalDTO(result, DTOType.EXTENDED);
                UriBuilder builder = uriInfo.getAbsolutePathBuilder();
                return Response.created(builder.path(String.valueOf(dto.getId())).build())
                        .entity(dto)
                        .build();
            }
            return Response.status(422).build();
        } catch (ServiceException e) {
            return Response.status(Response.Status.CONFLICT).build();
        }  catch (Exception e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

}
