package com.epam.volunteer.config;

import com.epam.volunteer.config.factory.ManagerFactory;
import com.epam.volunteer.config.factory.EntityManagerFactoryHK2Implementation;
import com.epam.volunteer.config.factory.SchedulerFactory;
import com.epam.volunteer.dao.DonationDAO;
import com.epam.volunteer.dao.EmployeeDAO;
import com.epam.volunteer.dao.MedicamentDAO;
import com.epam.volunteer.dao.VolunteerDAO;
import com.epam.volunteer.dao.impl.DonationDAOImpl;
import com.epam.volunteer.dao.impl.EmployeeDAOImpl;
import com.epam.volunteer.dao.impl.MedicamentDAOImpl;
import com.epam.volunteer.dao.impl.VolunteerDAOImpl;
import com.epam.volunteer.service.*;
import com.epam.volunteer.service.impl.*;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.quartz.Scheduler;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;


/**
 * @author Mikita Buslauski
 */
public class ApplicationBinder extends AbstractBinder {

    @Override
    protected void configure() {
        bind(MedicamentDAOImpl.class).to(MedicamentDAO.class).in(Singleton.class);
        bind(MedicamentServiceImpl.class).to(MedicamentService.class).in(Singleton.class);
        bindFactory(EntityManagerFactoryHK2Implementation.class).to(EntityManagerFactory.class).in(Singleton.class);
        bindFactory(ManagerFactory.class).to(EntityManager.class)
                .proxy(true)
                .proxyForSameScope(false)
                .in(RequestScoped.class);
        bindFactory(SchedulerFactory.class).to(Scheduler.class).in(Singleton.class);
        bind(DonationServiceImpl.class).to(DonationService.class).in(Singleton.class);
        bind(DonationDAOImpl.class).to(DonationDAO.class).in(Singleton.class);
        bind(VolunteerServiceImpl.class).to(VolunteerService.class).in(Singleton.class);
        bind(VolunteerDAOImpl.class).to(VolunteerDAO.class).in(Singleton.class);
        bind(LinkServiceImpl.class).to(LinkService.class).in(Singleton.class);
        bind(EmployeeServiceImpl.class).to(EmployeeService.class).in(Singleton.class);
        bind(EmployeeDAOImpl.class).to(EmployeeDAO.class).in(Singleton.class);
    }
}
