package acceptance.taskrouting;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pro.taskana.common.api.TaskanaEngine;
import pro.taskana.spi.routing.api.TaskRoutingProvider;
import pro.taskana.task.api.models.Task;
import pro.taskana.workbasket.api.models.WorkbasketSummary;

/** This is a sample implementation of TaskRouter. */
public class TestTaskRouterForDomainA implements TaskRoutingProvider {

  private static final Logger LOGGER = LoggerFactory.getLogger(TestTaskRouterForDomainA.class);

  TaskanaEngine theEngine;

  @Override
  public void initialize(TaskanaEngine taskanaEngine) {
    theEngine = taskanaEngine;
  }

  @Override
  public String determineWorkbasketId(Task task) {
return "wronKey";
  }
}
