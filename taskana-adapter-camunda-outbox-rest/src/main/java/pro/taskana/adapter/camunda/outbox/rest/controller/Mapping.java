package pro.taskana.adapter.camunda.outbox.rest.controller;

/** Collection of Url to Controller mappings. */
public class Mapping {

  public static final String URL_EVENTS = "/events";
  public static final String URL_EVENT = "/{eventId}";
  public static final String URL_DELETE_EVENTS = "/delete";
  public static final String URL_DECREASE_REMAINING_RETRIES = "/decrease-remaining-retries";
  public static final String URL_BLACKLISTED_EVENTS = "/blacklisted";
  public static final String URL_COUNT_BLACKLISTED_EVENTS = URL_BLACKLISTED_EVENTS + "/count";
  public static final String URL_DELETE_BLACKLISTED_EVENT = URL_BLACKLISTED_EVENTS + "/{eventId}";
}
