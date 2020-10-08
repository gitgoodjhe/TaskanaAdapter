package pro.taskana.adapter.systemconnector.camunda.api.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CamundaTaskEventBlacklister {

  private static final Logger LOGGER = LoggerFactory.getLogger(CamundaTaskEventBlacklister.class);
  @Autowired HttpHeaderProvider httpHeaderProvider;
  @Autowired private RestTemplate restTemplate;

  public void decreaseRemainingRetriesForReferencedTasks(
      List<String> failedCreationTaskIds, String camundaSystemTaskEventUrl) {

    LOGGER.debug(
        "entry to decreaseRemainingRetriesForReferencedTasks, CamundSystemURL = {}", camundaSystemTaskEventUrl);

    String requestUrl =
        camundaSystemTaskEventUrl + CamundaSystemConnectorImpl.URL_CAMUNDA_EVENT_DECREASE_REMAINING_RETRIES;

    String failedIds = "{\"taskCreationIds\":[" + String.join(",", failedCreationTaskIds) + "]}";

    LOGGER.debug("flag Events url {} ", requestUrl);

    decreaseRemainingRetries(requestUrl, failedIds);

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("exit from flagEventsForReferencedTaskIds.");
    }
  }

  private void decreaseRemainingRetries(
      String requestUrl, String failedIds) {

    HttpHeaders headers = httpHeaderProvider.getHttpHeadersForOutboxRestApi();

    HttpEntity<String> request =
        new HttpEntity<>(failedIds, headers);

    restTemplate.postForObject(requestUrl, request, String.class);
  }
}
