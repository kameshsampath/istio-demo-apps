package dev.kameshs.demos.preference.rest;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/")
public class PreferenceResource {

  private final Logger logger = Logger.getLogger(PreferenceResource.class.getName());

  @Inject
  @RestClient
  RecommendationService recommendationService;

  @ConfigProperty(name = "response.message")
  Instance<String> responseMessageFormat;

  @ConfigProperty(name = "response.service-not-available.message")
  Instance<String> serviceNotAvailableMessage;

  @ConfigProperty(name = "response.service-not-found.message")
  Instance<String> serviceNotFoundMessage;

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public RestResponse<String> getPreference() {
    String response = recommendationService.getRecommendation();
    return RestResponse
      .ok(String.format(responseMessageFormat.get(), response));
  }

  @ServerExceptionMapper
  public RestResponse<String> handleProcessingException(ProcessingException ps) {
    logger.log(Level.WARNING, "Error calling recommendation ", ps);

    String message = String.format(responseMessageFormat.get(), ps.getCause().getMessage());
    Response.Status responseStatus = Response.Status.SERVICE_UNAVAILABLE;

    if (ps.getCause() instanceof ConnectException) {
      logger.warning("ConnectException while connecting to recommendation service");
      message = serviceNotAvailableMessage.get();
    } else if (ps.getCause() instanceof UnknownHostException) {
      logger.warning("Host 'recommendation' not known");
      message = serviceNotFoundMessage.get();
    }

    return RestResponse
      .status(responseStatus, message);
  }

}