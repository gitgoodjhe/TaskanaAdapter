package pro.taskana.adapter.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ContextConfiguration;

import pro.taskana.adapter.camunda.outbox.rest.model.CamundaTaskEvent;
import pro.taskana.adapter.test.TaskanaAdapterTestApplication;
import pro.taskana.impl.configuration.DbCleaner;
import pro.taskana.impl.configuration.DbCleaner.ApplicationDatabaseType;
import pro.taskana.security.JaasExtension;
import pro.taskana.security.WithAccessId;

/** Test class to test failed task creation scenarios from camunda to TASKANA. */
@SpringBootTest(
    classes = TaskanaAdapterTestApplication.class,
    webEnvironment = WebEnvironment.DEFINED_PORT)
@AutoConfigureWebTestClient
@ExtendWith(JaasExtension.class)
@ContextConfiguration
@SuppressWarnings("checkstyle:LineLength")
public class TestFailedTaskCreation extends AbsIntegrationTest {

  @BeforeEach
  @WithAccessId(userName = "admin")
  public void resetOutbox(){
    DbCleaner cleaner = new DbCleaner();
    cleaner.clearDb(camundaBpmDataSource, ApplicationDatabaseType.OUTBOX);
  }

  @WithAccessId(
      userName = "teamlead_1",
      groupNames = {"admin"})
  @Test
  void should_CountDownRetriesAndAddToFailedEvents_When_TaskCreationFailedInTaskana()
      throws Exception {

    String processInstanceId =
        this.camundaProcessengineRequester.startCamundaProcessAndReturnId(
            "simple_user_task_process_with_incorrect_workbasket_key", "");

    List<String> camundaTaskIds =
        this.camundaProcessengineRequester.getTaskIdsFromProcessInstanceId(processInstanceId);

    assertThat(camundaTaskIds).hasSize(3);

    Thread.sleep((long) (this.adapterTaskPollingInterval * 1.2));

    // retries still above 0

    List<CamundaTaskEvent> failedEvents = taskanaOutboxRequester.getFailedEvents();

    assertThat(failedEvents).hasSize(0);

    // adapter makes retries
    Thread.sleep(this.adapterRetryAndBlockingInterval);

    failedEvents = taskanaOutboxRequester.getFailedEvents();
    // retries = 0, no retries left
    assertThat(failedEvents).hasSize(3);
  }

  @WithAccessId(
      userName = "teamlead_1",
      groupNames = {"admin"})
  @Test
  void should_LogError_When_TaskCreationFailedInTaskana() throws Exception {

    String processInstanceId =
        this.camundaProcessengineRequester.startCamundaProcessAndReturnId(
            "simple_user_task_process_with_incorrect_workbasket_key", "");

    List<String> camundaTaskIds =
        this.camundaProcessengineRequester.getTaskIdsFromProcessInstanceId(processInstanceId);

    assertThat(camundaTaskIds).hasSize(3);

    Thread.sleep((long) (this.adapterTaskPollingInterval * 1.2));

    List<CamundaTaskEvent> failedEvents = taskanaOutboxRequester.getFailedEvents();

    // retries still above 0
    assertThat(failedEvents).hasSize(0);

    // adapter makes retries
    Thread.sleep(this.adapterRetryAndBlockingInterval);

    failedEvents = taskanaOutboxRequester.getFailedEvents();
    // retries = 0, no retries left

    assertThat(failedEvents).hasSize(3);

    assertThat(failedEvents)
        .extracting(CamundaTaskEvent::getError)
        .extracting(error -> error.split(":")[0])
        .containsAll(camundaTaskIds);

    assertThat(failedEvents)
        .extracting(CamundaTaskEvent::getError)
        .extracting(error -> error.split(":")[1])
        .contains("pro.taskana.workbasket.api.exceptions.WorkbasketNotFoundException");
  }

  @WithAccessId(
      userName = "teamlead_1",
      groupNames = {"admin"})
  @Test
  void should_DeleteFailedEvent_When_CallingDeleteEndpoint() throws Exception {

    String processInstanceId =
        this.camundaProcessengineRequester.startCamundaProcessAndReturnId(
            "simple_user_task_process_with_incorrect_workbasket_key", "");

    List<String> camundaTaskIds =
        this.camundaProcessengineRequester.getTaskIdsFromProcessInstanceId(processInstanceId);

    assertThat(camundaTaskIds).hasSize(3);

    // adapter makes retries
    Thread.sleep(
        (long) (this.adapterTaskPollingInterval * 1.2 + this.adapterRetryAndBlockingInterval));

    // retries = 0, no retries left
    List<CamundaTaskEvent> failedEvents = taskanaOutboxRequester.getFailedEvents();

    assertThat(failedEvents).hasSize(3);

    boolean eventDeleted = taskanaOutboxRequester.deleteFailedEvent(failedEvents.get(0).getId());

    assertThat(eventDeleted).isTrue();

    assertThat(taskanaOutboxRequester.getFailedEvents()).hasSize(2);
  }

  @WithAccessId(
      userName = "teamlead_1",
      groupNames = {"admin"})
  @Test
  void should_DeleteAllFailedEvents_When_CallingDeleteAllFailedEndpoint() throws Exception {

    String processInstanceId =
        this.camundaProcessengineRequester.startCamundaProcessAndReturnId(
            "simple_user_task_process_with_incorrect_workbasket_key", "");

    List<String> camundaTaskIds =
        this.camundaProcessengineRequester.getTaskIdsFromProcessInstanceId(processInstanceId);

    assertThat(camundaTaskIds).hasSize(3);

    Thread.sleep(
        (long) (this.adapterTaskPollingInterval * 1.2 + this.adapterRetryAndBlockingInterval));

    // retries = 0, no retries left

    List<CamundaTaskEvent> failedEvents = taskanaOutboxRequester.getFailedEvents();
    assertThat(failedEvents).hasSize(3);

    boolean eventsDeleted = taskanaOutboxRequester.deleteAllFailedEvents();

    assertThat(eventsDeleted).isTrue();

    failedEvents = taskanaOutboxRequester.getFailedEvents();

    assertThat(failedEvents).hasSize(0);
  }

  @WithAccessId(
      userName = "teamlead_1",
      groupNames = {"admin"})
  @Test
  void should_SetRetryForFailedEvent_When_CallingSetRetriesEndpoint() throws Exception {

    String processInstanceId =
        this.camundaProcessengineRequester.startCamundaProcessAndReturnId(
            "simple_user_task_process_with_incorrect_workbasket_key", "");

    List<String> camundaTaskIds =
        this.camundaProcessengineRequester.getTaskIdsFromProcessInstanceId(processInstanceId);

    assertThat(camundaTaskIds).hasSize(3);

    Thread.sleep(
        (long) (this.adapterTaskPollingInterval * 1.2 + this.adapterRetryAndBlockingInterval));

    // retries = 0, no retries left
    List<CamundaTaskEvent> failedEvents = taskanaOutboxRequester.getFailedEvents();

    assertThat(failedEvents).hasSize(3);

    // reset specific failedEvent
    boolean remainingRetriesSet =
        taskanaOutboxRequester.setRemainingRetries(failedEvents.get(0).getId(), 3);

    assertThat(remainingRetriesSet).isTrue();

    failedEvents = taskanaOutboxRequester.getFailedEvents();

    assertThat(failedEvents).hasSize(2);
  }

  @WithAccessId(
      userName = "teamlead_1",
      groupNames = {"admin"})
  @Test
  void should_SetRetryForAllFailedEvents_When_CallingSetRetriesForAllFailedEndpoint()
      throws Exception {

    String processInstanceId =
        this.camundaProcessengineRequester.startCamundaProcessAndReturnId(
            "simple_user_task_process_with_incorrect_workbasket_key", "");

    List<String> camundaTaskIds =
        this.camundaProcessengineRequester.getTaskIdsFromProcessInstanceId(processInstanceId);

    assertThat(camundaTaskIds).hasSize(3);

    Thread.sleep(
        (long) (this.adapterTaskPollingInterval * 1.2 + this.adapterRetryAndBlockingInterval));

    // retries = 0, no retries left
    List<CamundaTaskEvent> failedEvents = taskanaOutboxRequester.getFailedEvents();

    assertThat(failedEvents).hasSize(3);

    // reset specific failedEvent
    boolean remainingRetriesSet = taskanaOutboxRequester.setRemainingRetriesForAll(3);

    assertThat(remainingRetriesSet).isTrue();

    failedEvents = taskanaOutboxRequester.getFailedEvents();

    assertThat(failedEvents).hasSize(0);
  }
}
