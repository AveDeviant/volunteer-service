package com.epam.volunteer.config.factory;

import org.glassfish.hk2.api.Factory;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ResourceBundle;

public class LOLFactory implements Factory<EntityManagerFactory> {
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("persistenceConfig");

    @Override
    public EntityManagerFactory provide() {
        String schema = RESOURCE_BUNDLE.getString("persistence.unit");
        return Persistence.createEntityManagerFactory(schema);
    }

    @Override
    public void dispose(EntityManagerFactory entityManagerFactory) {

    }
}
