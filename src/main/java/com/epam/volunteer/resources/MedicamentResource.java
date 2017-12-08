package com.epam.volunteer.resources;


import com.epam.volunteer.entity.Medicament;
import com.epam.volunteer.service.MedicamentService;
import com.epam.volunteer.service.impl.MedicamentServiceImpl;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/medicament")
public class MedicamentResource {
    //IoC
        private static final MedicamentService medicamentService = new MedicamentServiceImpl();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMedicament() {
        return Response.ok().build();
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
