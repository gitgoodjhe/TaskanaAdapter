package pro.taskana.adapter.camunda.outbox.rest.controller;

import java.util.List;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import pro.taskana.adapter.camunda.outbox.rest.model.CamundaTaskEvent;
import pro.taskana.adapter.camunda.outbox.rest.model.CamundaTaskEventList;
import pro.taskana.adapter.camunda.outbox.rest.resource.CamundaTaskEventListResource;
import pro.taskana.adapter.camunda.outbox.rest.resource.CamundaTaskEventListResourceAssembler;
import pro.taskana.adapter.camunda.outbox.rest.service.CamundaTaskEventsService;

/** Controller for the Outbox REST service. */
@Path(Mapping.URL_EVENTS)
public class CamundaTaskEventsController {

  CamundaTaskEventsService camundaTaskEventService = new CamundaTaskEventsService();
  CamundaTaskEventListResourceAssembler camundaTaskEventListResourceAssembler =
      new CamundaTaskEventListResourceAssembler();

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getEvents(
      @QueryParam("type") final List<String> requestedEventTypes,
      @QueryParam("retries") final String retries) {

    CamundaTaskEventList camundaTaskEventList = new CamundaTaskEventList();

    if (!requestedEventTypes.isEmpty()) {

      List<CamundaTaskEvent> camundaTaskEvents =
          camundaTaskEventService.getEvents(requestedEventTypes);

      camundaTaskEventList.setCamundaTaskEvents(camundaTaskEvents);

      CamundaTaskEventListResource camundaTaskEventListResource =
          camundaTaskEventListResourceAssembler.toResource(camundaTaskEventList);

      return Response.status(200).entity(camundaTaskEventListResource).build();
    }

    if (retries != null) {

      int remainingRetries = Integer.valueOf(retries);
      List<CamundaTaskEvent> failedCamundaTaskEvents =
          camundaTaskEventService.getFailedEvents(remainingRetries);

      camundaTaskEventList.setCamundaTaskEvents(failedCamundaTaskEvents);

      CamundaTaskEventListResource failedCamundaTaskEventListResource =
          camundaTaskEventListResourceAssembler.toResource(camundaTaskEventList);

      return Response.status(200).entity(failedCamundaTaskEventListResource).build();
    }

    List<CamundaTaskEvent> camundaTaskEvents =
        camundaTaskEventService.getAllEvents();

    camundaTaskEventList.setCamundaTaskEvents(camundaTaskEvents);

    CamundaTaskEventListResource camundaTaskEventListResource =
        camundaTaskEventListResourceAssembler.toResource(camundaTaskEventList);

    return Response.status(200).entity(camundaTaskEventListResource).build();
  }

  @Path(Mapping.URL_EVENT)
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getEvent(@PathParam("eventId") final int eventId) {

    CamundaTaskEvent camundaTaskEvent = camundaTaskEventService.getEvent(eventId);

    return Response.status(200).entity(camundaTaskEvent).build();
  }

  @Path(Mapping.URL_DELETE_EVENTS)
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Response deleteEvents(String idsAsString) {

    camundaTaskEventService.deleteEvents(idsAsString);

    return Response.status(204).build();
  }

  @Path(Mapping.URL_DECREASE_REMAINING_RETRIES)
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Response decreaseRemainingRetries(String eventIdsOfTasksFailedToStart) {

    camundaTaskEventService.decreaseRemainingRetries(eventIdsOfTasksFailedToStart);

    return Response.status(204).build();
  }

  @GET
  @Path(Mapping.URL_COUNT_FAILED_EVENTS)
  @Produces(MediaType.APPLICATION_JSON)
  public Response getFailedEventsCount(@QueryParam("retries") int remainingRetries) {

    String failedEventsCount = camundaTaskEventService.getEventsCount(remainingRetries);

    return Response.status(200).entity(failedEventsCount).build();
  }

  @Path(Mapping.URL_EVENT)
  @PATCH
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response setRemainingRetries(
      @PathParam("eventId") int eventId, Map<String, Integer> newRemainingRetries) {

    CamundaTaskEvent event = camundaTaskEventService.setRemainingRetries(eventId, newRemainingRetries);

    return Response.status(200).entity(event).build();
  }

  @PATCH
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response setRemainingRetriesForAllFailedEvents(
      @QueryParam("retries") final String retries, Map<String, Integer> newRemainingRetries) {

    if(retries == null || retries.isEmpty()){
      WebApplicationException ex = new WebApplicationException("failed");
      return Response.status(400).entity(ex.getMessage()).build();
    }

    int remainingRetries = Integer.valueOf(retries);
    List<CamundaTaskEvent> camundaTaskEvents =
        camundaTaskEventService.setRemainingRetriesForAllBlacklisted(remainingRetries, newRemainingRetries);

    CamundaTaskEventList camundaTaskEventList = new CamundaTaskEventList();
    camundaTaskEventList.setCamundaTaskEvents(camundaTaskEvents);

    CamundaTaskEventListResource camundaTaskEventListResource =
        camundaTaskEventListResourceAssembler.toResource(camundaTaskEventList);

    return Response.status(200).entity(camundaTaskEventListResource).build();
  }

  @Path(Mapping.URL_EVENT)
  @DELETE
  public Response deleteBlacklistedEvent(@PathParam("eventId") int eventId) {

    camundaTaskEventService.deleteBacklistedEvent(eventId);

    return Response.status(204).build();
  }

  @Path(Mapping.DELETE_FAILED_EVENTS)
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Response deleteAllFailedEvents() {

    camundaTaskEventService.deleteAllFailedEvents();

    return Response.status(204).build();
  }
}
