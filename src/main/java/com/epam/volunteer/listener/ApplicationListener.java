package com.epam.volunteer.listener;

import com.epam.volunteer.scheduler.job.LoggerJob;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.monitoring.ApplicationEvent;
import org.glassfish.jersey.server.monitoring.ApplicationEventListener;
import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;
import org.quartz.*;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;

import javax.inject.Inject;
import java.util.ResourceBundle;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

public class ApplicationListener implements ApplicationEventListener {
    private static final ResourceBundle TRIGGER_CONFIG = ResourceBundle.getBundle("trigger");
    @Inject
    private Scheduler scheduler;
    @Inject
    private  ServiceLocator serviceLocator;


    @Override
    public void onEvent(ApplicationEvent applicationEvent) {
        switch (applicationEvent.getType()) {
            case INITIALIZATION_FINISHED:
                setupScheduler();
                break;
            case DESTROY_FINISHED:
                shutdownScheduler();
                break;
        }
    }

    @Override
    public RequestEventListener onRequest(RequestEvent requestEvent) {
        return null;
    }


    private void setupScheduler() {
        try {
            scheduler.setJobFactory(new JobFactory() {
                private final Logger LOGGER = LogManager.getLogger();
                @Override
                public Job newJob(TriggerFiredBundle triggerFiredBundle, Scheduler scheduler) throws SchedulerException {
                    {
                        JobDetail jobDetail = triggerFiredBundle.getJobDetail();
                        Class<? extends Job> jobClass = jobDetail.getJobClass();
                        try {
                            LOGGER.log(Level.INFO, serviceLocator);
                            LOGGER.debug("Producing instance of job {} (class {})",
                                    jobDetail.getKey(), jobClass.getName());
                            Job sh = serviceLocator.createAndInitialize(jobClass);
                            LOGGER.log(Level.INFO, sh);
                            return sh;
                        } catch (Exception e) {
                            throw new SchedulerException();
                        }

                    }
                }
            });
            JobDetail jobDetail = newJob(LoggerJob.class).build();
            Trigger trigger = newTrigger().forJob(jobDetail)
                    .startNow().withSchedule(cronSchedule(TRIGGER_CONFIG.getString("logger.job.time")))
                    .build();
            scheduler.scheduleJob(jobDetail, trigger);
            scheduler.start();
        } catch (SchedulerException e) {
            LogManager.getLogger().log(Level.ERROR, e.getMessage());
        }
    }

    private void shutdownScheduler() {
        try {
            scheduler.shutdown();
        } catch (SchedulerException e) {
            LogManager.getLogger().log(Level.ERROR, e.getMessage());
        }
    }
}
