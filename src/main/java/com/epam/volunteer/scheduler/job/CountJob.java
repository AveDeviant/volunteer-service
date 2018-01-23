package com.epam.volunteer.scheduler.job;

import com.epam.volunteer.service.VolunteerService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;

import javax.inject.Inject;

public class CountJob implements Job, InterruptableJob {
    @Inject
    private VolunteerService volunteerService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            Logger logger = LogManager.getLogger();
            logger.log(Level.INFO, "----------------------------------------------");
            long count = ((VolunteerService) jobExecutionContext.getScheduler().getContext().get("service")).countAll();
            logger.log(Level.INFO, "Current count of registered volunteers: " + count + "\n" +
                    " Next fire time: " + jobExecutionContext.getNextFireTime());
            logger.log(Level.INFO, "----------------------------------------------");
        } catch (Exception e) {
            LogManager.getLogger().log(Level.ERROR, "Some problems have been detected.");
        }
    }

    @Override
    public void interrupt() throws UnableToInterruptJobException {
        Thread.currentThread().interrupt();
    }
}
