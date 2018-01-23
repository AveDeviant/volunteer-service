package com.epam.volunteer.resource.mapper;

import com.epam.volunteer.service.exception.ResourceForbiddenException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class IllegalAccessExceptionMapper implements ExceptionMapper<ResourceForbiddenException> {
    @Override
    public Response toResponse(ResourceForbiddenException exception) {
        return Response.status(Response.Status.FORBIDDEN).build();
    }
}
