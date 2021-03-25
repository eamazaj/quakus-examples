package com.eamazaj;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

@Path("/hello")
public class HelloResource {

    @Inject
    HelloService helloService;

    @ConfigProperty(name = "db.username")
    String username;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return helloService.sayHello();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/secrets")
    public Map<String, String> getSecrets() {
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        return map;
    }
}