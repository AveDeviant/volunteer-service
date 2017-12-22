package test.com.epam.volunteer.service;


import com.epam.volunteer.service.MedicamentService;
import com.epam.volunteer.service.exception.ServiceException;
import com.epam.volunteer.service.impl.LinkServiceImpl;
import com.epam.volunteer.service.impl.MedicamentServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class LinkServiceTest {
    private LinkServiceImpl linkService = new LinkServiceImpl();
    private MedicamentService medicamentService = Mockito.mock(MedicamentServiceImpl.class);


    @Test
    public void countPageAmountOddAmount() throws ServiceException {
        linkService.setMedicamentService(medicamentService);
        Mockito.when(medicamentService.countActual()).thenReturn(7L);
        Assert.assertEquals(3, linkService.calculateLastPage(3));
    }

    @Test
    public void countPageAmountOddAmount2() throws ServiceException {
        linkService.setMedicamentService(medicamentService);
        Mockito.when(medicamentService.countActual()).thenReturn(7L);
        Assert.assertEquals(4, linkService.calculateLastPage(2));
    }

    @Test
    public void countPageAmountEvenAmount() throws ServiceException {
        linkService.setMedicamentService(medicamentService);
        Mockito.when(medicamentService.countActual()).thenReturn(4L);
        Assert.assertEquals(2, linkService.calculateLastPage(2));
    }

}
