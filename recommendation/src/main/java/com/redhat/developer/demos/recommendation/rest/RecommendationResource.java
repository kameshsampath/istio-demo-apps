package com.redhat.developer.demos.recommendation.rest;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.resteasy.reactive.RestResponse;

import javax.enterprise.inject.Instance;
import javax.json.Json;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/")
public class RecommendationResource {


  private final Logger logger = Logger.getLogger(getClass().getName());

  @ConfigProperty(name = "response.message")
  Instance<String> responseMessageFormat;

  @ConfigProperty(name = "response.misbehave.message")
  Instance<String> responseMisbehaveMessage;

  @ConfigProperty(name = "response.misbehave-503.message")
  Instance<String> responseMisbehave503Message;

  @ConfigProperty(name = "response.misbehave-200.message")
  Instance<String> responseMisbehave200Message;

  /**
   * Counter to help us see the lifecycle
   */
  AtomicInteger count = new AtomicInteger(0);

  /**
   * Flag for throwing a 503 when enabled
   */
  private boolean misbehave = false;

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public RestResponse<String> getRecommendations() {

    String strMessageFormat = responseMessageFormat.get();

    int requestCounter = count.incrementAndGet();

    logger.info(String.format(strMessageFormat, requestCounter));

    // timeout();
    logger.log(Level.FINE, "recommendation service ready to return");
    if (misbehave) {
      return doMisbehavior();
    }
    return RestResponse
      .status(Response.Status.OK, String.format(strMessageFormat, requestCounter));
  }

  private RestResponse<String> doMisbehavior() {
    logger.log(Level.FINE, String.format("Misbehaving %d", count.incrementAndGet()));
    return RestResponse.
      status(Response.Status.SERVICE_UNAVAILABLE, responseMisbehaveMessage.get());
  }

  @GET
  @Path("/misbehave")
  @Produces(MediaType.TEXT_PLAIN)
  public RestResponse<String> flagMisbehave() {
    this.misbehave = true;
    logger.log(Level.FINE, "'misbehave' has been set to 'true'");
    return RestResponse.
      status(Response.Status.OK, responseMisbehave503Message.get());
  }

  @GET
  @Path("/behave")
  @Produces(MediaType.TEXT_PLAIN)
  public RestResponse<String> flagBehave() {
    this.misbehave = false;
    logger.log(Level.FINE, "'misbehave' has been set to 'false'");
    return RestResponse.
      status(Response.Status.OK, responseMisbehave200Message.get());
  }

  private String getNow() {
    final Client client = ClientBuilder.newClient();
    final Response res = client.target("http://worldclockapi.com/api/json/cet/now").request().get();
    final String jsonObject = res.readEntity(String.class);
    return Json.createReader(new ByteArrayInputStream(jsonObject.getBytes())).readObject().getString("currentDateTime");
  }

  private void timeout() {
    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      logger.info("Thread interrupted");
    }
  }

}