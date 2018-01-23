package com.epam.volunteer.config.factory;

import com.epam.volunteer.service.VolunteerService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.glassfish.hk2.api.Factory;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import javax.inject.Inject;

public class SchedulerFactory implements Factory<Scheduler> {
    @Inject
    private VolunteerService volunteerService;

    @Override
    public Scheduler provide() {
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            LogManager.getLogger().log(Level.INFO, "VOLUNTEER SERVICE IN SCHEDULE FACTORY: " + volunteerService);
            scheduler.getContext().put("volunteerService", volunteerService);
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
