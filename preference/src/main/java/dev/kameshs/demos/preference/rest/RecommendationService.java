package dev.kameshs.demos.preference.rest;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@RegisterRestClient(configKey = "recommendation-api")
public interface RecommendationService {

  @Path("/")
  @GET
  @Produces("text/plain")
  String getRecommendation();

}
