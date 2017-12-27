package test.com.epam.volunteer.service;

import com.epam.volunteer.dao.DonationDAO;
import com.epam.volunteer.dao.exception.DAOException;
import com.epam.volunteer.dao.impl.DonationDAOImpl;
import com.epam.volunteer.entity.Donation;
import com.epam.volunteer.entity.Employee;
import com.epam.volunteer.entity.Medicament;
import com.epam.volunteer.service.MedicamentService;
import com.epam.volunteer.service.exception.ServiceException;
import com.epam.volunteer.service.impl.DonationServiceImpl;
import com.epam.volunteer.service.impl.MedicamentServiceImpl;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;
import org.mockito.Mockito;

import javax.ws.rs.core.Application;

public class DonationServiceTest extends JerseyTest {
    private static DonationServiceImpl donationService = new DonationServiceImpl();
    private DonationDAO donationDAO = Mockito.mock(DonationDAOImpl.class);
    private MedicamentService medicamentService = Mockito.mock(MedicamentServiceImpl.class);


    @Override
    protected Application configure() {
        ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.register(new TestBinder());
        return resourceConfig;
    }


    @Test
    public void registerDonationIncorrectDonationSize() throws ServiceException, DAOException {
        donationService.setDonationDAO(donationDAO);
        donationService.setMedicamentService(medicamentService);
        Donation donation = new Donation();
        donation.setEmployee(new Employee());
        Medicament medicament = new Medicament();
        medicament.setId(1);
        medicament.setStatus(true);
        medicament.setRequirement(20);
        medicament.setCurrentCount(15);
        donation.setMedicament(medicament);
        Mockito.when(medicamentService.getById(1, true)).thenReturn(medicament);
        donationService.registerDonation(donation);
        Mockito.verify(donationDAO, Mockito.never()).addDonation(donation, true);
        Mockito.verify(donationDAO, Mockito.never()).addDonation(donation, false);
    }

    @Test
    public void registerDonationMedicamentIsntInitialized() throws ServiceException, DAOException {
        donationService.setDonationDAO(donationDAO);
        donationService.setMedicamentService(medicamentService);
        Donation donation = new Donation();
        donation.setEmployee(new Employee());
        donation.setCount(2);
        Mockito.when(medicamentService.getById(0, true)).thenReturn(new Medicament());
        donationService.registerDonation(donation);
        Mockito.verify(donationDAO, Mockito.never()).addDonation(donation, true);
        Mockito.verify(donationDAO, Mockito.never()).addDonation(donation, false);
    }

    @Test
    public void registerDonationEmployeeIsntInitialized() throws ServiceException, DAOException {
        donationService.setDonationDAO(donationDAO);
        donationService.setMedicamentService(medicamentService);
        Donation donation = new Donation();
        Medicament medicament = new Medicament();
        medicament.setId(1);
        medicament.setStatus(true);
        medicament.setRequirement(20);
        medicament.setCurrentCount(15);
        donation.setCount(2);
        donation.setEmployee(new Employee());
        Mockito.when(medicamentService.getById(1, true)).thenReturn(medicament);
        donationService.registerDonation(donation);
        Mockito.verify(donationDAO, Mockito.never()).addDonation(donation, true);
        Mockito.verify(donationDAO, Mockito.never()).addDonation(donation, false);
    }

    @Test
    public void registerDonationTest() throws ServiceException, DAOException {
        donationService.setDonationDAO(donationDAO);
        donationService.setMedicamentService(medicamentService);
        Donation donation = new Donation();
        donation.setEmployee(new Employee());
        Medicament medicament = new Medicament();
        medicament.setId(1);
        medicament.setStatus(true);
        medicament.setRequirement(20);
        medicament.setCurrentCount(15);
        donation.setMedicament(medicament);
        donation.setCount(1);
        Mockito.when(medicamentService.getById(1, true)).thenReturn(medicament);
        donationService.registerDonation(donation);
        Mockito.verify(donationDAO, Mockito.never()).addDonation(donation, true);
        Mockito.verify(donationDAO, Mockito.times(1)).addDonation(donation, false);
    }

    @Test
    public void registerDonationMedicamentNotAvailable() throws DAOException, ServiceException {
        donationService.setDonationDAO(donationDAO);
        donationService.setMedicamentService(medicamentService);
        Donation donation = new Donation();
        donation.setEmployee(new Employee());
        Medicament medicament = new Medicament();
        medicament.setId(1);
        medicament.setStatus(true);
        medicament.setRequirement(20);
        medicament.setCurrentCount(15);
        donation.setMedicament(medicament);
        donation.setCount(1);
        Mockito.when(medicamentService.getById(1, true)).thenReturn(null);
        donationService.registerDonation(donation);
        Mockito.verify(donationDAO, Mockito.never()).addDonation(donation, true);
        Mockito.verify(donationDAO, Mockito.never()).addDonation(donation, false);
    }

    @Test
    public void registerLastDonationTest() throws ServiceException, DAOException {
        donationService.setDonationDAO(donationDAO);
        donationService.setMedicamentService(medicamentService);
        Donation donation = new Donation();
        donation.setEmployee(new Employee());
        Medicament medicament = new Medicament();
        medicament.setId(1);
        medicament.setStatus(true);
        medicament.setRequirement(20);
        medicament.setCurrentCount(15);
        donation.setMedicament(medicament);
        donation.setCount(6);
        Mockito.when(medicamentService.getById(1, true)).thenReturn(medicament);
        donationService.registerDonation(donation);
        Mockito.verify(donationDAO, Mockito.never()).addDonation(donation, false);
        Mockito.verify(donationDAO, Mockito.times(1)).addDonation(donation, true);
    }


    private class TestBinder extends AbstractBinder {
        @Override
        protected void configure() {
            bind(medicamentService).to(MedicamentService.class);
            bind(donationDAO).to(DonationDAO.class);
        }
    }
}
