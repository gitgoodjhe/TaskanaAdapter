package camunda2;


import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pro.taskana.adapter.camunda.outbox.rest.config.OutboxRestServiceConfig;
import pro.taskana.adapter.camunda.outbox.rest.controller.CamundaTaskEventsController;
import pro.taskana.adapter.camunda.parselistener.TaskanaParseListenerProcessEnginePlugin;


@Configuration
public class Config {
  @Bean
  @ConditionalOnMissingBean
  public OutboxRestServiceConfig outboxRestServiceConfig() {
    return new OutboxRestServiceConfig();
  }

  @Bean
  @ConditionalOnMissingBean
  public CamundaTaskEventsController camundaTaskEventsController() {
    return new CamundaTaskEventsController();
  }

  @Bean
  @ConditionalOnMissingBean
  public TaskanaParseListenerProcessEnginePlugin taskanaParseListenerProcessEnginePlugin() {
    return new TaskanaParseListenerProcessEnginePlugin();
  }
}
