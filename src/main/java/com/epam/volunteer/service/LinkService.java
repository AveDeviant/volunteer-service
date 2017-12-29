package com.epam.volunteer.service;

import com.epam.volunteer.entity.AbstractEntity;
import com.epam.volunteer.service.exception.ServiceException;
import org.jvnet.hk2.annotations.Contract;

import javax.ws.rs.core.Link;
import javax.ws.rs.core.UriInfo;

@Contract
public interface LinkService {

    Link[] buildLinks(int page, int pageOffset, UriInfo uriInfo, Class<? extends AbstractEntity> cl) throws ServiceException;


}
