package com.epam.volunteer.resources;


import com.epam.volunteer.entity.Medicament;
import com.epam.volunteer.service.MedicamentService;
import com.google.inject.Inject;


import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@Path("/medicament")
public class MedicamentResource extends AbstractResource {
    @Inject
    private MedicamentService medicamentService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMedicament(@QueryParam("page") @DefaultValue("1") int page,
                                  @QueryParam("size") @DefaultValue("2") int size,
                                  @Context UriInfo uriInfo) {
        if (page <= 0 || size <= 0) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        List<Medicament> medicament = medicamentService.getAllActual(page, size);
        return Response.ok(medicament).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") long id) {
        Medicament medicament = medicamentService.getById(id);
        if (medicament != null) {
            return Response.ok(medicament).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
