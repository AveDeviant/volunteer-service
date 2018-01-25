package com.epam.volunteer.config.factory;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.glassfish.hk2.api.Factory;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;



public class SchedulerFactory implements Factory<Scheduler> {

    @Override
    public Scheduler provide() {
        try {

            return StdSchedulerFactory.getDefaultScheduler();
        } catch (SchedulerException e) {
            LogManager.getLogger().log(Level.INFO, e.getMessage());
        }
        return null;
    }

    @Override
    public void dispose(Scheduler scheduler) {
        try {
            scheduler.shutdown();
        } catch (SchedulerException e) {
            LogManager.getLogger().log(Level.INFO, e.getMessage());
        }
    }
}
