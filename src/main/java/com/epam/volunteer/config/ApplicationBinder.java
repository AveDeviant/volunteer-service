package com.epam.volunteer.config;

import com.epam.volunteer.dao.DonationDAO;
import com.epam.volunteer.dao.MedicamentDAO;
import com.epam.volunteer.dao.VolunteerDAO;
import com.epam.volunteer.dao.impl.DonationDAOImpl;
import com.epam.volunteer.dao.impl.MedicamentDAOImpl;
import com.epam.volunteer.dao.impl.VolunteerDAOImpl;
import com.epam.volunteer.entity.Volunteer;
import com.epam.volunteer.manager.EntityManagerWrapper;
import com.epam.volunteer.service.DonationService;
import com.epam.volunteer.service.MedicamentService;
import com.epam.volunteer.service.VolunteerService;
import com.epam.volunteer.service.impl.DonationServiceImpl;
import com.epam.volunteer.service.impl.MedicamentServiceImpl;
import com.epam.volunteer.service.impl.VolunteerServiceImpl;
import org.glassfish.hk2.api.DynamicConfiguration;
import org.glassfish.hk2.api.DynamicConfigurationService;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.persistence.EntityManager;

/**
 * @author Mikita Buslauski
 */
public class ApplicationBinder extends AbstractBinder {

    @Override
    protected void configure() {
        bind(MedicamentDAOImpl.class).to(MedicamentDAO.class);
        bind(MedicamentServiceImpl.class).to(MedicamentService.class);
        bind(EntityManagerWrapper.class).to(EntityManager.class);
        bind(DonationServiceImpl.class).to(DonationService.class);
        bind(DonationDAOImpl.class).to(DonationDAO.class);
        bind(VolunteerServiceImpl.class).to(VolunteerService.class);
        bind(VolunteerDAOImpl.class).to(VolunteerDAO.class);
    }
}
