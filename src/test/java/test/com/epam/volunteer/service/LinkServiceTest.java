package test.com.epam.volunteer.service;

import com.epam.volunteer.dao.MedicamentDAO;
import com.epam.volunteer.dao.impl.MedicamentDAOImpl;
import com.epam.volunteer.service.LinkService;
import com.epam.volunteer.service.exception.ServiceException;
import com.epam.volunteer.service.impl.LinkServiceImpl;
import org.junit.Test;
import org.mockito.Mockito;

import javax.ws.rs.core.Link;
import javax.ws.rs.core.UriInfo;

public class LinkServiceTest {

    private MedicamentDAO medicamentDAO = Mockito.mock(MedicamentDAOImpl.class);
    private UriInfo uriInfo = Mockito.mock(UriInfo.class);

    @Test
    public void testIncorrectSizeAndPage() throws ServiceException {
        LinkService linkService = new LinkServiceImpl();
        Link[] output = linkService.buildLinks(-1, 2, null);
        assert output.length == 0;
    }
}
