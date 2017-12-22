package com.epam.volunteer.service.impl;

import com.epam.volunteer.entity.AbstractEntity;
import com.epam.volunteer.entity.Medicament;
import com.epam.volunteer.service.MainService;
import com.epam.volunteer.service.MedicamentService;
import com.epam.volunteer.service.exception.ServiceException;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LinkServiceImpl extends AbstractService implements com.epam.volunteer.service.LinkService {
    private static final String SELF_REF = "self";
    private static final String LAST_REF = "last";
    private static final String FIRST_REF = "first";
    private static final String PREVIOUS_REF = "prev";
    private static final String NEXT_REF = "next";
    private static final String QUERY_PARAM_SIZE = "size";
    private static final String QUERY_PARAM_PAGE = "page";
    private static final String CONTENT_TYPE = "application/json";
    private MedicamentService medicamentService;

    @Inject
    public void setMedicamentService(MedicamentService medicamentService) {
        this.medicamentService = medicamentService;
    }

    //Pagination for all business objects?
    @Override
    public Link[] buildLinks(int page, int pageOffset, UriInfo uriInfo) throws ServiceException {
        List<Link> links = new ArrayList<>();
        if (Optional.ofNullable(uriInfo).isPresent()) {
            Link self = Link.fromUri(uriInfo.getRequestUri()).rel(SELF_REF).build();
            links.add(self);
            links.add(buildFirstLink(pageOffset, uriInfo));
            links.add(buildLastLink(pageOffset, uriInfo));
            links.add(buildPreviousLink(page, pageOffset, uriInfo));
            links.add(buildNextLink(page, pageOffset, uriInfo));
        }
        Link[] array = new Link[links.size()];
        array = links.toArray(array);
        return array;
    }


    private Link buildFirstLink(int size, UriInfo uriInfo) {
        UriBuilder builderFirst = uriInfo.getAbsolutePathBuilder();
        builderFirst.queryParam(QUERY_PARAM_PAGE, 1);
        builderFirst.queryParam(QUERY_PARAM_SIZE, size);
        return Link.fromUri(builderFirst.build()).
                rel(FIRST_REF)
                .type(CONTENT_TYPE)
                .build();
    }

    private Link buildLastLink(int pageOffset, UriInfo uriInfo) throws ServiceException {
        long lastPage = calculateLastPage(pageOffset);
        UriBuilder builderLast = uriInfo.getAbsolutePathBuilder();
        builderLast.queryParam(QUERY_PARAM_PAGE, lastPage);
        builderLast.queryParam(QUERY_PARAM_SIZE, pageOffset);
        return Link.fromUri(builderLast.build())
                .rel(LAST_REF)
                .type(CONTENT_TYPE)
                .build();
    }

    private Link buildPreviousLink(int page, int pageOffset, UriInfo uriInfo) {
        if (page > 1 && pageOffset > 0) {
            UriBuilder builderPrev = uriInfo.getAbsolutePathBuilder();
            builderPrev.queryParam(QUERY_PARAM_PAGE, page - 1);
            builderPrev.queryParam(QUERY_PARAM_SIZE, pageOffset);
            return Link.fromUri(builderPrev.build())
                    .rel(PREVIOUS_REF)
                    .type(CONTENT_TYPE)
                    .build();
        }
        return Link.fromUri("").rel(PREVIOUS_REF).build();
    }

    private Link buildNextLink(int page, int pageOffset, UriInfo uriInfo) throws ServiceException {
        long lastPage = calculateLastPage(pageOffset);
        if (page < lastPage && pageOffset > 0) {
            UriBuilder builderPrev = uriInfo.getAbsolutePathBuilder();
            builderPrev.queryParam(QUERY_PARAM_PAGE, page + 1);
            builderPrev.queryParam(QUERY_PARAM_SIZE, pageOffset);
            return Link.fromUri(builderPrev.build())
                    .rel(NEXT_REF)
                    .type(CONTENT_TYPE)
                    .build();
        }
        return Link.fromUri("").rel(NEXT_REF).build();
    }


    public long calculateLastPage(int pageOffset) throws ServiceException {
        long count = medicamentService.countActual();
        long lastPage = count;
        if (pageOffset > 0) {
            lastPage = count / pageOffset;
            if (count % pageOffset > 0) {
                lastPage++;
            }
        }
        return lastPage;
    }

}
