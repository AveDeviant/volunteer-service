package com.epam.volunteer.resources;


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
import org.apache.logging.log4j.Level;


import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.Optional;

@Path("/medicament")
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
    public Response getMedicament(@QueryParam("page") @DefaultValue("1") int page,
                                  @QueryParam("size") @DefaultValue("2") int size) {
        try {
            List<Medicament> medicament = medicamentService.getAllActual(page, size);
            List<AbstractDTO> dto = DTOMarshaller.marshalDTOList(medicament, DTOType.BASIC);
            Link[] links = linkService.buildLinks(page, size, uriInfo);
            return Response.ok(dto)
                    .links(links)
                    .build();
        } catch (ServiceException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            LOGGER.log(Level.INFO, e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @GET
    @Path("/{id : [0-9]+ }")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") long id) {
        try {
            Medicament medicament = medicamentService.getById(id, true);
            if (medicament != null) {
                AbstractDTO dto = DTOMarshaller.marshalDTO(medicament, DTOType.EXTENDED);
                return Response.ok(dto).build();
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
            return Response.status(422).entity(ServerMessage.INVALID_INPUT).build();
        } catch (ServiceException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            e.printStackTrace();
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
                    return Response.status(422).entity(ServerMessage.INVALID_DONATION).build();
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
        System.out.println("medicamentDTO:" + medicamentDTO);
        try {
            if (Optional.ofNullable(medicamentDTO).isPresent()) {
                if (!volunteerService.authorizationPassed(volunteerEmail, id)) {
                    return Response.status(Response.Status.UNAUTHORIZED).build();
                }
                Medicament input = (Medicament) DTOUnmarshaller.unmarshalDTO(medicamentDTO);
                System.out.println("input: "+input);
                Medicament result = medicamentService.update(id, input);
                System.out.println(result);
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
