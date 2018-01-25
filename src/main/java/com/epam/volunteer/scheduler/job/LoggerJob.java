package com.epam.volunteer.scheduler.job;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class LoggerJob implements Job {
    @Inject
    private EntityManagerFactory managerFactory;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            Logger logger = LogManager.getLogger();
            EntityManager manager = managerFactory.createEntityManager();
            logger.log(Level.DEBUG, managerFactory);
            logger.log(Level.DEBUG, manager);
            long count = manager.createNamedQuery("Volunteer.countAll", Long.class).getSingleResult();
            manager.close();
            logger.log(Level.INFO,
                    "Volunteers amount: " + count + ". Next fire time: " + jobExecutionContext.getNextFireTime() + "\n" +
                            "----------------------------------------------");
        } catch (Exception e) {
            LogManager.getLogger().log(Level.ERROR, e.getMessage());
            e.printStackTrace();
        }
    }
}
