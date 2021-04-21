package pro.taskana.preprocessing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import pro.taskana.spi.task.api.CreateTaskPreprocessor;
import pro.taskana.task.api.models.Task;

public class ArsPreProcessor implements CreateTaskPreprocessor {

  @Override
  public void processTaskBeforeCreation(Task taskToProcess) {

    ObjectMapper objectMapper = new ObjectMapper();

    System.out.println("###########" + taskToProcess.getCustomAttributeMap().size());

    System.out.println("###########" + taskToProcess.getCustomAttributeMap().keySet());
    JSONObject json =
        new JSONObject(taskToProcess.getCustomAttributeMap().get("camunda:attribute1"));

    String val = String.valueOf(json.get("value"));
    JSONObject json2 =
        new JSONObject(val);
    System.out.println("#######" + json.get("value"));
    System.out.println("#######" +json2);
    System.out.println("#######" +json2.toMap().keySet());
    System.out.println("################# " + json2.get("arbeitnehmerNummer"));

    try {
      BelegInfoArs belegInfoArs = objectMapper.readValue(val, BelegInfoArs.class);

      System.out.println("Here comes " + belegInfoArs.getArbeitnehmerNummer());
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }
}
