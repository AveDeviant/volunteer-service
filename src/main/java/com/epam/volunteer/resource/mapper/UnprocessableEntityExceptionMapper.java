package com.epam.volunteer.resource.mapper;

import com.epam.volunteer.service.exception.BusinessLogicException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class UnprocessableEntityExceptionMapper implements ExceptionMapper<BusinessLogicException> {
    @Override
    public Response toResponse(BusinessLogicException exception) {
        return Response.status(422).build();
    }
}
