package com.epam.volunteer.config.factory;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.hk2.api.Factory;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * @author Mikita Buslauski
 */
public class ManagerFactory implements Factory<EntityManager> {
    private static final Logger LOGGER = LogManager.getLogger();
    @Inject
    EntityManagerFactory entityManagerFactory;

    @Override
    public EntityManager provide() {
        LOGGER.log(Level.DEBUG, entityManagerFactory);
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        LOGGER.log(Level.DEBUG, entityManager);
        return entityManager;
    }

    @Override
    public void dispose(EntityManager entityManager) {
        entityManager.close();
        LOGGER.log(Level.INFO, "EntityManager closed.");
    }
}
