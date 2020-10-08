package pro.taskana.adapter.camunda.outbox.rest.controller;

import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
  public Response getEvents(@QueryParam("type") final List<String> requestedEventTypes) {

    List<CamundaTaskEvent> camundaTaskEvents =
        camundaTaskEventService.getEvents(requestedEventTypes);
    CamundaTaskEventList camundaTaskEventList = new CamundaTaskEventList();
    camundaTaskEventList.setCamundaTaskEvents(camundaTaskEvents);

    CamundaTaskEventListResource camundaTaskEventListResource =
        camundaTaskEventListResourceAssembler.toResource(camundaTaskEventList);

    return Response.status(200).entity(camundaTaskEventListResource).build();
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
  @Path(Mapping.URL_BLACKLISTED_EVENTS)
  @Produces(MediaType.APPLICATION_JSON)
  public Response getBlacklistedEvents() {

    List<CamundaTaskEvent> blacklistedCamundaTaskEvents = camundaTaskEventService.getBlacklistedEvents();

    CamundaTaskEventList blacklistedCamundaTaskEventList = new CamundaTaskEventList();
    blacklistedCamundaTaskEventList.setCamundaTaskEvents(blacklistedCamundaTaskEvents);

    CamundaTaskEventListResource blacklistedCamundaTaskEventListResource =
        camundaTaskEventListResourceAssembler.toResource(blacklistedCamundaTaskEventList);

    return Response.status(200).entity(blacklistedCamundaTaskEventListResource).build();
  }

  @GET
  @Path(Mapping.URL_COUNT_BLACKLISTED_EVENTS)
  @Produces(MediaType.APPLICATION_JSON)
  public Response getBlacklistedEventsCount() {

    int blacklistedEventsCount = camundaTaskEventService.getBlacklistedEventsCount();

    return Response.status(200).entity(blacklistedEventsCount).build();
  }

  @Path(Mapping.URL_EVENT)
  @PATCH
  @Consumes(MediaType.APPLICATION_JSON)
  public Response setRemainingRetries(
      @PathParam("eventId") int eventId, String remainingRetries) {

    camundaTaskEventService.setRemainingRetries(eventId, remainingRetries);

    return Response.status(204).build();
  }

  @Path(Mapping.URL_BLACKLISTED_EVENTS)
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Response setRemainingRetriesForAllBlacklisted(int retries) {

    camundaTaskEventService.setRemainingRetriesForAllBlacklisted(retries);

    return Response.status(204).build();
  }

  @Path(Mapping.URL_DELETE_BLACKLISTED_EVENT)
  @DELETE
  @Consumes(MediaType.APPLICATION_JSON)
  public Response deleteBlacklistedEvent(@PathParam("eventId") int eventId) {

    camundaTaskEventService.deleteBacklistedEvent(eventId);

    return Response.status(204).build();
  }

  @Path(Mapping.URL_BLACKLISTED_EVENTS)
  @DELETE
  @Consumes(MediaType.APPLICATION_JSON)
  public Response deleteAllBlacklistedEvents() {

    camundaTaskEventService.deleteAllBacklistedEvents();

    return Response.status(204).build();
  }
}
