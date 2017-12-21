package com.epam.volunteer.resources;


import com.epam.volunteer.dto.AbstractDTO;
import com.epam.volunteer.dto.DTOType;
import com.epam.volunteer.dto.base.BaseDonationDTO;
import com.epam.volunteer.dto.extended.MedicamentDTO;
import com.epam.volunteer.dto.marshaller.DTOMarshaller;
import com.epam.volunteer.dto.marshaller.DTOUnmarshaller;
import com.epam.volunteer.entity.Donation;
import com.epam.volunteer.entity.Medicament;
import com.epam.volunteer.response.ServerMessage;
import com.epam.volunteer.service.DonationService;
import com.epam.volunteer.service.LinkService;
import com.epam.volunteer.service.MedicamentService;
import com.epam.volunteer.service.exception.ServiceException;
import org.apache.logging.log4j.Level;


import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.Optional;

@Path("/medicament")
public class MedicamentResource extends AbstractResource {
    private MedicamentService medicamentService;
    private DonationService donationService;
    private LinkService linkService;

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

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMedicament(@QueryParam("page") @DefaultValue("1") int page,
                                  @QueryParam("size") @DefaultValue("2") int size,
                                  @Context UriInfo uriInfo) {
        try {
            List<Medicament> medicament = medicamentService.getAllActual(page, size);
            if (medicament.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).build();  //логично?
            }
            List<AbstractDTO> dto = DTOMarshaller.marshalDTOList(medicament, DTOType.BASIC);
            Link[] links = linkService.buildLinks(page, size, uriInfo);
            return Response.ok(dto)
                    .links(links)
                    .build();
        } catch (ServiceException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            e.printStackTrace();
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
    public Response addNew(MedicamentDTO medicament, @Context UriInfo uriInfo) {
        try {
            if (Optional.ofNullable(medicament).isPresent() &&
                    Optional.ofNullable(medicament.getMedicament()).isPresent()) {
                if (!Optional.ofNullable(medicament.getVolunteerDTO()).isPresent()) {
                    return Response.status(Response.Status.UNAUTHORIZED).build();
                }
                Medicament input = (Medicament) DTOUnmarshaller.unmarshalDTO(medicament);
                Medicament newMed = medicamentService.addNew(input);
                AbstractDTO dto = DTOMarshaller.marshalDTO(newMed, DTOType.EXTENDED);
                if (newMed != null) {
                    UriBuilder builder = uriInfo.getAbsolutePathBuilder();
                    return Response.created(builder.path(String.valueOf(dto.getId())).build())
                            .entity(dto)
                            .build();
                }
            }
            return Response.status(Response.Status.BAD_REQUEST).entity(ServerMessage.INVALID_INPUT).build();
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
    public Response createDonation(@PathParam("id") long id, BaseDonationDTO donation, @Context UriInfo uriInfo) {
        try {
            Medicament medicament = medicamentService.getById(id, true);
            if (Optional.ofNullable(medicament).isPresent()) {
                if (!Optional.ofNullable(donation.getEmployeeDTO()).isPresent()) {
                    return Response.status(Response.Status.UNAUTHORIZED).build();
                }
                if (Optional.ofNullable(donation.getMedicamentDTO()).isPresent()) {
                    Donation donation1 = (Donation) DTOUnmarshaller.unmarshalDTO(donation);
                    donation1.setMedicament(medicament);
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

    @DELETE
    @Path("/{id: [0-9]+ }")
    public Response deleteMedicament(@PathParam("id") long id) {

        return null;
    }
}
