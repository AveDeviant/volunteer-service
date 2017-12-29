package com.epam.volunteer.resource;


import com.epam.volunteer.dto.AbstractDTO;
import com.epam.volunteer.dto.DTOType;
import com.epam.volunteer.dto.base.BaseDonationDTO;
import com.epam.volunteer.dto.base.BaseMedicamentDTO;
import com.epam.volunteer.dto.extended.MedicamentDTO;
import com.epam.volunteer.dto.marshaller.DTOMarshaller;
import com.epam.volunteer.dto.marshaller.DTOUnmarshaller;
import com.epam.volunteer.entity.Donation;
import com.epam.volunteer.entity.Employee;
import com.epam.volunteer.entity.Medicament;
import com.epam.volunteer.entity.Volunteer;
import com.epam.volunteer.response.ServerMessage;
import com.epam.volunteer.service.*;
import com.epam.volunteer.service.exception.ServiceException;
import com.epam.volunteer.service.impl.*;
import io.swagger.annotations.*;
import org.apache.logging.log4j.Level;


import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.Optional;

@Path("/medicament")
@Api(value = "medicament")
@Produces("application/json")
public class MedicamentResource extends AbstractResource {
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
                    "Link to the current page of medicament list ", response = Link.class),
                    @ResponseHeader(name = "Link", description = "Link to the first page of the medicament list",
                            response = Link.class),
                    @ResponseHeader(name = "Link", description = "Link to the last page of the medicament list",
                            response = Link.class),
                    @ResponseHeader(name = "Link", description = "Link to the previous page of the medicament list",
                            response = Link.class),
                    @ResponseHeader(name = "Link", description = "Link to the next page of the medicament list",
                            response = Link.class)}),
            @ApiResponse(code = 500, message = "Internal error.")
    })
    public Response getMedicament(@ApiParam(value = "page") @QueryParam("page") @DefaultValue("1") int page,
                                  @ApiParam(value = "offset") @QueryParam("size") @DefaultValue("2") int size) {
        try {
            provideInitialization();
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
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            LOGGER.log(Level.INFO, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("/{id : [0-9]+ }")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get medicament with specified ID.", notes = "Authorization header should be provided in case" +
            " user attempts to access the medicament that is already unavailable. In this case only a volunteer who" +
            " exposed this medicament can access this resource. It is assumed that an email address will be sent via the" +
            " authorization header (mock authorization)).",
            authorizations = {@Authorization(value = "authorization_header")}, response = MedicamentDTO.class)
    public Response getById(@ApiParam(value = "Medicament ID", required = true) @PathParam("id") long id,
                            @ApiParam(value = "Authorization") @HeaderParam(HttpHeaders.AUTHORIZATION) String email) {
        try {
            provideInitialization();
            Medicament medicament = medicamentService.getById(id);
            if (medicament != null) {    //workaround below
                if (medicament.isStatus() || (!medicament.isStatus() && volunteerService.authorizationPassed(email, id))) {
                    AbstractDTO dto = DTOMarshaller.marshalDTO(medicament, DTOType.EXTENDED);
                    return Response.ok(dto).build();
                }
                return Response.status(Response.Status.FORBIDDEN).build();
            }
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (ServiceException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addNew(BaseMedicamentDTO medicament, @HeaderParam(HttpHeaders.AUTHORIZATION) String email) {
        try {
            if (Optional.ofNullable(medicament.getMedicament()).isPresent()) {
                provideInitialization();
                Volunteer volunteer = volunteerService.getByEmail(email);
                if (!Optional.ofNullable(volunteer).isPresent()) {
                    return Response.status(Response.Status.UNAUTHORIZED).build();
                }
                Medicament input = (Medicament) DTOUnmarshaller.unmarshalDTO(medicament);
                input.setVolunteer(volunteer);
                Medicament newMed = medicamentService.addNew(input);
                if (Optional.ofNullable(newMed).isPresent()) {
                    AbstractDTO dto = DTOMarshaller.marshalDTO(newMed, DTOType.EXTENDED);
                    UriBuilder builder = uriInfo.getAbsolutePathBuilder();
                    return Response.created(builder.path(String.valueOf(dto.getId())).build())
                            .entity(dto)
                            .build();
                }
            }
            return Response.status(422).entity(ServerMessage.INVALID_INPUT).build();
        } catch (ServiceException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(ServerMessage.INVALID_INPUT).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id: [0-9]+ }/donation")
    public Response createDonation(@PathParam("id") long id, BaseDonationDTO donation,
                                   @HeaderParam(HttpHeaders.AUTHORIZATION) String employeeEmail) {
        try {
            provideInitialization();
            Medicament medicament = medicamentService.getById(id, true);
            if (Optional.ofNullable(medicament).isPresent()) {
                Employee employee = employeeService.getByEmail(employeeEmail);
                if (!Optional.ofNullable(employee).isPresent()) {    // "authorization"
                    return Response.status(Response.Status.UNAUTHORIZED).build();
                }
                Donation donation1 = (Donation) DTOUnmarshaller.unmarshalDTO(donation);
                donation1.setMedicament(medicament);
                donation1.setEmployee(employee);
                donation1 = donationService.registerDonation(donation1);
                if (donation1 == null) {
                    return Response.status(Response.Status.FORBIDDEN).build();
                }
                UriBuilder builder = uriInfo.getAbsolutePathBuilder();
                AbstractDTO dto = DTOMarshaller.marshalDTO(donation1, DTOType.EXTENDED);
                return Response.created(builder.path(String.valueOf(dto.getId())).build())
                        .entity(dto)
                        .build();
            }
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (ServiceException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(ServerMessage.INVALID_INPUT).build();
        }
    }

    @POST
    @Path("/{id: [0-9]+ }")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") long id, BaseMedicamentDTO medicamentDTO,
                           @HeaderParam(HttpHeaders.AUTHORIZATION) String volunteerEmail) {
        try {
            if (Optional.ofNullable(medicamentDTO).isPresent()) {
                provideInitialization();
                if (!volunteerService.authorizationPassed(volunteerEmail, id)) {
                    return Response.status(Response.Status.UNAUTHORIZED).build();
                }
                Medicament input = (Medicament) DTOUnmarshaller.unmarshalDTO(medicamentDTO);
                Medicament result = medicamentService.update(id, input);
                if (Optional.ofNullable(result).isPresent()) {
                    AbstractDTO dto = DTOMarshaller.marshalDTO(result, DTOType.EXTENDED);
                    return Response.ok()
                            .entity(dto)
                            .build();
                }
                return Response.status(Response.Status.FORBIDDEN).build();
            }
            return Response.status(Response.Status.BAD_REQUEST).entity(ServerMessage.INVALID_INPUT).build();
        } catch (ServiceException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @DELETE
    @Path("/{id: [0-9]+ }")
    public Response delete(@PathParam("id") long id, @HeaderParam(HttpHeaders.AUTHORIZATION) String volunteerEmail) {
        try {
            provideInitialization();
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


    private void provideInitialization() {
        employeeService = Optional.ofNullable(employeeService).orElse(new EmployeeServiceImpl());
        donationService = Optional.ofNullable(donationService).orElse(new DonationServiceImpl());
        medicamentService = Optional.ofNullable(medicamentService).orElse(new MedicamentServiceImpl());
        volunteerService = Optional.ofNullable(volunteerService).orElse(new VolunteerServiceImpl());
        linkService = Optional.ofNullable(linkService).orElse(new LinkServiceImpl());
    }
}
