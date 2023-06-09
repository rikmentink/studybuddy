package me.rikmentink.studybuddy;

import javax.inject.Inject;
import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

@ApplicationPath("/api/")
public class JerseyConfig extends ResourceConfig {

    @Inject
    public JerseyConfig() {
        packages("me.rikmentink.studybuddy.controller");
        register(RolesAllowedDynamicFeature.class);
    }
}