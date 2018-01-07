package com.epam.volunteer.config.factory;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.hk2.api.Factory;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ResourceBundle;

public class EntityManagerFactoryHK2Implementation implements Factory<EntityManagerFactory> {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("persistenceConfig");

    @Override
    public EntityManagerFactory provide() {
        String schema = RESOURCE_BUNDLE.getString("persistence.unit");
        return Persistence.createEntityManagerFactory(schema);
    }

    @Override
    public void dispose(EntityManagerFactory entityManagerFactory) {
        entityManagerFactory.close();
        LOGGER.log(Level.INFO, "EntityManagerFactory closed.");
    }
}
