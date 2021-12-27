package dev.kameshs.demos.customer.rest;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/")
public class CustomerResource {

  private final Logger logger = Logger.getLogger(getClass().getName());

  @Inject
  @RestClient
  PreferenceService preferenceService;

  @ConfigProperty(name = "response.message")
  Instance<String> responseMessageFormat;

  @ConfigProperty(name = "response.service-not-available.message")
  Instance<String> serviceNotAvailableMessage;

  @ConfigProperty(name = "response.service-not-found.message")
  Instance<String> serviceNotFoundMessage;

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public RestResponse<String> getCustomer() {
    String response = preferenceService.getPreference().trim();
    return RestResponse
      .ok(String.format(responseMessageFormat.get(), response));
  }

  @ServerExceptionMapper
  public RestResponse<String> handleProcessingException(ProcessingException ps) {
    logger.log(Level.WARNING, "Error calling preference ", ps);

    String message = String.format(responseMessageFormat.get(), ps.getCause().getMessage());

    Response.Status responseStatus = Response.Status.SERVICE_UNAVAILABLE;

    if (ps.getCause() instanceof ConnectException) {
      logger.warning("ConnectException while connecting to preference service");
      message = serviceNotAvailableMessage.get();
    } else if (ps.getCause() instanceof UnknownHostException) {
      logger.warning("Host 'preference' not known");
      message = serviceNotFoundMessage.get();
    }

    return RestResponse
      .status(responseStatus, message);
  }

  @ServerExceptionMapper
  public RestResponse<String> handleWebAppException(WebApplicationException webAppEx) {
    Response response = webAppEx.getResponse();

    String messageFormat = responseMessageFormat.get();
    String message = String.format(messageFormat, "Unknown Error while calling preference");

    if (response.hasEntity()) {
      message = String.format(messageFormat, response.readEntity(String.class));
    }
    return RestResponse
      .status(Response.Status.fromStatusCode(response.getStatus()), message);
  }
}