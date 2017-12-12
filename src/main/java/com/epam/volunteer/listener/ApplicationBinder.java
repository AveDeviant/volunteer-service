package com.epam.volunteer.listener;

import com.epam.volunteer.dao.DonationDAO;
import com.epam.volunteer.dao.MedicamentDAO;
import com.epam.volunteer.dao.impl.DonationDAOImpl;
import com.epam.volunteer.dao.impl.MedicamentDAOImpl;
import com.epam.volunteer.manager.EntityManagerWrapper;
import com.epam.volunteer.service.DonationService;
import com.epam.volunteer.service.MedicamentService;
import com.epam.volunteer.service.impl.DonationServiceImpl;
import com.epam.volunteer.service.impl.MedicamentServiceImpl;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

import javax.persistence.EntityManager;

/**
 * @author Mikita Buslauski
 */
public class ApplicationBinder extends GuiceServletContextListener {
    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new ServletModule() {
            @Override
            protected void configureServlets() {
                bind(MedicamentDAO.class).to(MedicamentDAOImpl.class);
                bind(MedicamentService.class).to(MedicamentServiceImpl.class);
                bind(EntityManager.class).to(EntityManagerWrapper.class);
                bind(DonationService.class).to(DonationServiceImpl.class);
                bind(DonationDAO.class).to(DonationDAOImpl.class);
                ResourceConfig rc = new PackagesResourceConfig("com.epam.volunteer.resources");
                for (Class<?> resource : rc.getClasses()) {
                    bind(resource);
                }
                serve("/*").with(GuiceContainer.class);
            }
        });
    }
}
