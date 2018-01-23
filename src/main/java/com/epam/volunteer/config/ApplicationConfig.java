package com.epam.volunteer.config;


import com.epam.volunteer.filter.CORSFilter;
import com.epam.volunteer.scheduler.job.CountJob;
import com.epam.volunteer.serializer.LocalDateTimeDeserializer;
import com.epam.volunteer.serializer.LocalDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.swagger.jaxrs.config.BeanConfig;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ws.rs.ApplicationPath;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

@ApplicationPath("rest")
public class ApplicationConfig extends ResourceConfig {

    public ApplicationConfig() {
        LogManager.getLogger().log(Level.INFO, "APP CONFIG");
        packages("com.epam.volunteer.resource");
        register(new ApplicationBinder());
        register(CORSFilter.class);
        configureJSONProvider();
        configureSwagger();
    }

    private void configureJSONProvider() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        SimpleModule module = new SimpleModule();
        module.addSerializer(new LocalDateTimeSerializer(LocalDateTime.class));
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(LocalDateTime.class));
        mapper.registerModule(module);
//        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);          //Instead of custom DTO?
        JacksonJaxbJsonProvider provider = new JacksonJaxbJsonProvider();
        provider.setMapper(mapper);
        register(provider);
    }

    private void configureSwagger() {
        register(io.swagger.jaxrs.listing.ApiListingResource.class);
        register(io.swagger.jaxrs.listing.SwaggerSerializers.class);
        BeanConfig config = new BeanConfig();
        config.setTitle("Volunteer service");
        config.setVersion("v1");
        config.setSchemes(new String[]{"http"});
        config.setHost("localhost:8000");
        config.setBasePath("/pet-volunteer/rest");
        config.setResourcePackage("com.epam.volunteer.resource");
        config.setPrettyPrint(true);
        config.setScan(true);
    }


}
