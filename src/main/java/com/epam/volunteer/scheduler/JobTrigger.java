package com.epam.volunteer.scheduler;

import com.epam.volunteer.scheduler.job.CountJob;
import com.epam.volunteer.service.VolunteerService;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class JobTrigger {
    @Inject
    private VolunteerService volunteerService;

    @Inject
    private Scheduler scheduler;

    @PostConstruct
    public void triggerJob() throws SchedulerException {
        scheduler.getContext().put("volunteerService", volunteerService);
        JobDetail jobDetail = newJob(CountJob.class).build();
        Trigger trigger = newTrigger().forJob(jobDetail)
                .startNow().withSchedule(simpleSchedule()
                        .withIntervalInSeconds(5)
                        .repeatForever())
                .build();
        scheduler.scheduleJob(jobDetail, trigger);
        scheduler.start();
    }
}
