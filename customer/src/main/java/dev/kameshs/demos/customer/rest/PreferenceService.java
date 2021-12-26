package dev.kameshs.demos.customer.rest;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@RegisterRestClient(configKey = "preferences-api")
public interface PreferenceService {

  @Path("/")
  @GET
  @Produces("text/plain")
  String getPreference();

}
