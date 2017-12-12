package com.epam.volunteer.resources;


import com.epam.volunteer.entity.Donation;
import com.epam.volunteer.entity.Medicament;
import com.epam.volunteer.service.DonationService;
import com.epam.volunteer.service.MedicamentService;
import com.google.inject.Inject;


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
        if (page <= 0 || size <= 0) {    //TODO: вынести в сервис и там вернуть пустой лист для 404
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        List<Medicament> medicament = medicamentService.getAllActual(page, size);
        return Response.ok(medicament).build();
    }

    @GET
    @Path("/{id : [0-9]+ }")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") long id) {
        Medicament medicament = medicamentService.getById(id, true);
        if (medicament != null) {
            return Response.ok(medicament).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addNew(Medicament medicament, @Context UriInfo uriInfo) {
        Medicament newMed = medicamentService.addNew(medicament);
        if (newMed != null) {
            UriBuilder builder = uriInfo.getAbsolutePathBuilder();
            return Response.created(builder.path(String.valueOf(newMed.getId())).build())
                    .entity(newMed)
                    .build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id: [0-9]+ }/donation")
    public Response createDonation(@PathParam("id") long id, Donation donation, @Context UriInfo uriInfo) {
        Medicament medicament = medicamentService.getById(id, true);
        if (medicament != null) {
            Donation donation1 = donationService.registerDonation(donation);
            if (donation1 == null) {
                return Response.status(422).build();
            }
            UriBuilder builder = uriInfo.getAbsolutePathBuilder();
            return Response.created(builder.path(String.valueOf(donation1.getId())).build())
                    .entity(donation1)
                    .build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
