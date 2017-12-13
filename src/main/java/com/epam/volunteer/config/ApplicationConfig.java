package com.epam.volunteer.config;


import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("rest")
public class ApplicationConfig extends ResourceConfig {
    public ApplicationConfig() {
        packages("com.epam.volunteer.resources");
        register(new ApplicationBinder());
        register(JacksonJsonProvider.class);
    }

}
