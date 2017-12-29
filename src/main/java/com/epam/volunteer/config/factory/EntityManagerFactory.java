package com.epam.volunteer.config.factory;

import com.epam.volunteer.manager.EntityManagerWrapper;
import org.glassfish.hk2.api.Factory;

import javax.persistence.EntityManager;

public class EntityManagerFactory implements Factory<EntityManager> {
    @Override
    public EntityManager provide() {
        return new EntityManagerWrapper();
    }

    @Override
    public void dispose(EntityManager entityManager) {

    }
}
