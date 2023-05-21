package me.rikmentink.studybuddy.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/version")
public class Version {
    
    @GET
    @Produces("text/plain")
    public String getProjectVersion() {
        String version = "0.1.0";
        return version;
    }
}
