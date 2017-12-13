package com.epam.volunteer.resources;


import com.epam.volunteer.dto.AbstractDTO;
import com.epam.volunteer.dto.DTOBuilder;
import com.epam.volunteer.dto.MedicamentDTO;
import com.epam.volunteer.entity.Donation;
import com.epam.volunteer.entity.Medicament;
import com.epam.volunteer.service.DonationService;
import com.epam.volunteer.service.MedicamentService;
import com.epam.volunteer.service.exception.ServiceException;


import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Path("/medicament")
public class MedicamentResource extends AbstractResource {
    @Inject
    private MedicamentService medicamentService;

    @Inject
    private DonationService donationService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMedicament(@QueryParam("page") @DefaultValue("1") int page,
                                  @QueryParam("size") @DefaultValue("2") int size) {
        try {
            List<Medicament> medicament = medicamentService.getAllActual(page, size);
            if (medicament.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).build();  //логично?
            }
            List<AbstractDTO> dto = DTOBuilder.buildDTOList(medicament);
            return Response.ok(dto).build();
        } catch (ServiceException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("/{id : [0-9]+ }")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") long id) {
        try {
            Medicament medicament = medicamentService.getById(id, true);
            if (medicament != null) {
                AbstractDTO dto = DTOBuilder.buildDTO(medicament);
                return Response.ok(dto).build();
            }
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (ServiceException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addNew(Medicament medicament, @Context UriInfo uriInfo) {   //dto?
        try {
            Medicament newMed = medicamentService.addNew(medicament);
            AbstractDTO dto = DTOBuilder.buildDTO(newMed);
            if (newMed != null) {
                UriBuilder builder = uriInfo.getAbsolutePathBuilder();
                return Response.created(builder.path(String.valueOf(dto.getId())).build())
                        .entity(dto)
                        .build();
            }
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (ServiceException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id: [0-9]+ }/donation")
    public Response createDonation(@PathParam("id") long id, Donation donation, @Context UriInfo uriInfo) {
        try {
            Medicament medicament = medicamentService.getById(id, true);
            if (medicament != null) {
                Donation donation1 = donationService.registerDonation(donation);
                if (donation1 == null) {
                    return Response.status(422).build();
                }
                UriBuilder builder = uriInfo.getAbsolutePathBuilder();
                AbstractDTO dto = DTOBuilder.buildDTO(donation1);
                return Response.created(builder.path(String.valueOf(dto.getId())).build())
                        .entity(dto)
                        .build();
            }
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (ServiceException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
