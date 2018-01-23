package com.epam.volunteer.resource.mapper;

import com.epam.volunteer.response.ServerMessage;
import com.epam.volunteer.service.exception.EntityValidationException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InvalidEntityExceptionMapper implements ExceptionMapper<EntityValidationException> {
    @Override
    public Response toResponse(EntityValidationException exception) {
        return Response.status(422).entity(ServerMessage.INVALID_INPUT).build();
    }
}
