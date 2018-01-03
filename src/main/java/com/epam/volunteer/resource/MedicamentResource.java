package com.epam.volunteer.resource;


import com.epam.volunteer.dto.AbstractDTO;
import com.epam.volunteer.dto.DTOType;
import com.epam.volunteer.dto.base.BaseDonationDTO;
import com.epam.volunteer.dto.base.BaseMedicamentDTO;
import com.epam.volunteer.dto.extended.DonationDTO;
import com.epam.volunteer.dto.extended.MedicamentDTO;
import com.epam.volunteer.dto.marshaller.DTOMarshaller;
import com.epam.volunteer.dto.marshaller.DTOUnmarshaller;
import com.epam.volunteer.entity.Donation;
import com.epam.volunteer.entity.Employee;
import com.epam.volunteer.entity.Medicament;
import com.epam.volunteer.entity.Volunteer;
import com.epam.volunteer.response.ServerMessage;
import com.epam.volunteer.service.*;
import com.epam.volunteer.service.exception.BusinessLogicException;
import com.epam.volunteer.service.exception.EntityValidationException;
import com.epam.volunteer.service.exception.ResourceForbiddenException;
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

@Path("/medicament")
@Api(value = "medicament")
@Produces("application/json")
public class MedicamentResource {
    private static final Logger LOGGER = LogManager.getLogger();
    private MedicamentService medicamentService;
    private DonationService donationService;
    private LinkService linkService;
    private EmployeeService employeeService;
    private VolunteerService volunteerService;
    @Context
    private UriInfo uriInfo;

    @Inject
    public void setMedicamentService(MedicamentService medicamentService) {
        this.medicamentService = medicamentService;
    }

    @Inject
    public void setDonationService(DonationService donationService) {
        this.donationService = donationService;
    }

    @Inject
    public void setLinkService(LinkService linkService) {
        this.linkService = linkService;
    }

    @Inject
    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Inject
    public void setVolunteerService(VolunteerService volunteerService) {
        this.volunteerService = volunteerService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get medicament list in page format.",
            response = BaseMedicamentDTO.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", responseHeaders = {@ResponseHeader(name = "Link", description =
                    "Links to the current, previous, next, first and last pages of medicament list ", response = String.class)}),
            @ApiResponse(code = 500, message = "Internal error."),
            @ApiResponse(code = 404, message = "Medicament not found.")
    })
    public Response getMedicament(@ApiParam(value = "page") @QueryParam("page") @DefaultValue("1") int page,
                                  @ApiParam(value = "offset") @QueryParam("size") @DefaultValue("2") int size) {
        try {
            List<Medicament> medicament = medicamentService.getAllActual(page, size);
            if (!medicament.isEmpty()) {
                List<AbstractDTO> dto = DTOMarshaller.marshalDTOList(medicament, DTOType.BASIC);
                Link[] links = linkService.buildLinks(page, size, uriInfo, Medicament.class);
                return Response.ok(dto)
                        .links(links)
                        .build();
            }
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (ServiceException e) {
            LOGGER.log(Level.INFO, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("/{id: [0-9]+ }")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get medicament with specified ID.", notes = "Authorization header should be provided in case" +
            " user attempts to access the medicament that is already unavailable. In this case only a volunteer who" +
            " exposed this medicament can access this resource. It is assumed that an email address will be sent via the" +
            " authorization header (MOCK AUTHORIZATION!).",
            authorizations = {@Authorization(value = "authorization_header")}, response = MedicamentDTO.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Resource not found"),
            @ApiResponse(code = 403, message = "Access forbidden"),
            @ApiResponse(code = 500, message = "Internal error.")})
    public Response getById(@ApiParam(value = "Medicament ID", required = true) @PathParam("id") long id,
                            @ApiParam(value = "Authorization") @HeaderParam(HttpHeaders.AUTHORIZATION) String email) {
        try {
            Medicament medicament = medicamentService.getById(id);
            if (medicament != null) {    //workaround below
                if (medicament.isActual() || (!medicament.isActual() && volunteerService.authorizationPassed(email, id))) {
                    AbstractDTO dto = DTOMarshaller.marshalDTO(medicament, DTOType.EXTENDED);
                    return Response.ok(dto).build();
                }
                return Response.status(Response.Status.FORBIDDEN).build();
            }
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (ServiceException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Add new medicament to the list.", notes = "Note: new medicament becomes available even if" +
            " medicament status equals to false. Authorization header must be provided. It is" +
            " assumed that volunteer email will be sent via the authorization header (MOCK AUTHORIZATION!).",
            response = MedicamentDTO.class,
            authorizations = {@Authorization(value = "authorization_header")})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created", response = MedicamentDTO.class, responseHeaders =
            @ResponseHeader(name = "Location", description = "URL of created resource", response = String.class)),
            @ApiResponse(code = 401, message = "Unauthorized."),
            @ApiResponse(code = 422, message = "Unprocessable entity."),
            @ApiResponse(code = 500, message = "Internal error.")})
    public Response addNew(@ApiParam(value = "New medicament that should be added.", required = true)
                                   BaseMedicamentDTO medicament, @ApiParam(value = "Authorization", required = true)
                           @HeaderParam(HttpHeaders.AUTHORIZATION) String email) {
        try {
            if (Optional.ofNullable(medicament.getMedicament()).isPresent()) {
                Volunteer volunteer = volunteerService.getByEmail(email);
                if (!Optional.ofNullable(volunteer).isPresent()) {
                    return Response.status(Response.Status.UNAUTHORIZED).build();
                }
                Medicament input = (Medicament) DTOUnmarshaller.unmarshalDTO(medicament);
                input.setVolunteer(volunteer);
                Medicament newMed = medicamentService.addNew(input);
                AbstractDTO dto = DTOMarshaller.marshalDTO(newMed, DTOType.EXTENDED);
                UriBuilder builder = uriInfo.getAbsolutePathBuilder();
                return Response.created(builder.path(String.valueOf(dto.getId())).build())
                        .entity(dto)
                        .build();
            }
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (ServiceException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (EntityValidationException e) {
            return Response.status(422).entity(ServerMessage.INVALID_INPUT).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id: [0-9]+ }/donations")
    @ApiOperation(value = "Make a donation for specified medicament.", notes = "Company employee sends the amount of donation" +
            " he wants to make. Authorization header must be provided. It is assumed that employee email will be sent via" +
            " authorization header (MOCK AUTHORIZATION!).",
            authorizations = {@Authorization(value = "authorization_header")},
            response = DonationDTO.class)
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Created",
            responseHeaders = @ResponseHeader(name = "Location", description = "URL of created resource",
                    response = String.class), response = DonationDTO.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Operation forbidden."),
            @ApiResponse(code = 404, message = "Resource not found"),
            @ApiResponse(code = 500, message = "Internal error.")})
    public Response createDonation(@ApiParam(value = "ID of desired medicament", required = true) @PathParam("id") long id,
                                   @ApiParam(value = "Employee donation", required = true) BaseDonationDTO donation,
                                   @ApiParam(value = "Authorization", required = true)
                                   @HeaderParam(HttpHeaders.AUTHORIZATION) String employeeEmail) {
        try {
            Employee employee = employeeService.getByEmail(employeeEmail);
            if (!Optional.ofNullable(employee).isPresent()) {    // "authorization"
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            Donation donation1 = (Donation) DTOUnmarshaller.unmarshalDTO(donation);
            donation1.setEmployee(employee);
            donation1 = donationService.registerDonation(id, donation1);
            UriBuilder builder = uriInfo.getAbsolutePathBuilder();
            AbstractDTO dto = DTOMarshaller.marshalDTO(donation1, DTOType.EXTENDED);
            return Response.created(builder.path(String.valueOf(dto.getId())).build())
                    .entity(dto)
                    .build();
        } catch (ServiceException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (BusinessLogicException | EntityValidationException e) {
            return Response.status(422).build();
        } catch (ResourceForbiddenException e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @POST
    @Path("/{id: [0-9]+ }")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Update medicament with specified ID.", notes = "Medicament title, requirement and status can be updated." +
            " Authorization header must be provided in order to protect a resource against an illegal access. It is assumed " +
            "that volunteer email will be sent via authorization header (MOCK AUTHORIZATION!).",
            response = MedicamentDTO.class,
            authorizations = {@Authorization(value = "authorization_header")})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = MedicamentDTO.class),
            @ApiResponse(code = 403, message = "Access forbidden."),
            @ApiResponse(code = 400, message = "Invalid input."),
            @ApiResponse(code = 401, message = "Unauthorized person."),
            @ApiResponse(code = 500, message = "Internal error.")})
    public Response update(@ApiParam(value = "Id of desired medicament", required = true) @PathParam("id") long id,
                           @ApiParam(value = "Updated medicament object", required = true) BaseMedicamentDTO medicamentDTO,
                           @ApiParam(value = "Authorization", required = true)
                           @HeaderParam(HttpHeaders.AUTHORIZATION) String volunteerEmail) {
        try {
            if (Optional.ofNullable(medicamentDTO).isPresent()) {
                if (!volunteerService.authorizationPassed(volunteerEmail, id)) {
                    return Response.status(Response.Status.UNAUTHORIZED).build();
                }
                Medicament input = (Medicament) DTOUnmarshaller.unmarshalDTO(medicamentDTO);
                Medicament result = medicamentService.update(id, input);
                AbstractDTO dto = DTOMarshaller.marshalDTO(result, DTOType.EXTENDED);
                return Response.ok()
                        .entity(dto)
                        .build();
            }
            return Response.status(Response.Status.BAD_REQUEST).entity(ServerMessage.INVALID_INPUT).build();
        } catch (ServiceException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (ResourceForbiddenException e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @DELETE
    @Path("/{id: [0-9]+ }")
    @ApiOperation(value = "Delete medicament using medicament ID.", notes = " Authorization header must be provided" +
            " in order to protect a resource against an illegal access. It is assumed that volunteer email" +
            " will be sent via authorization header (MOCK AUTHORIZATION!).",
            authorizations = {@Authorization(value = "authorization_header")})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "No content."),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal error.")})
    public Response delete(@ApiParam(value = "ID of desired medicament", required = true) @PathParam("id") long id,
                           @ApiParam(value = "Authorization", required = true)
                           @HeaderParam(HttpHeaders.AUTHORIZATION) String volunteerEmail) {
        try {
            if (volunteerService.authorizationPassed(volunteerEmail, id)) {  // "authorization"
                medicamentService.delete(id);
                return Response.noContent().build();
            }
            return Response.status(Response.Status.UNAUTHORIZED).build();
        } catch (ServiceException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

}
