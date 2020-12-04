package pro.taskana.camunda.spring.boot;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.ObjectValue;

/**
 * This class is responsible for setting complex variables containing Objects on a camunda user
 * task.
 */
public class ComplexProcessVariableSetter implements JavaDelegate {

  @Override
  public void execute(DelegateExecution delegateExecution) throws Exception {



//String someString = "someString";


    ProcessVariableTestObject testObject =
        new ProcessVariableTestObject("someString", 5, 5.5, true);

    ObjectMapper objectMapper = new ObjectMapper();

    String json = objectMapper.writeValueAsString(testObject);

    ObjectValue jsonObjectStringProcessVariable =
        Variables.objectValue(json)
            .serializationDataFormat(Variables.SerializationDataFormats.JSON)
            .create();

    delegateExecution.setVariable("someVarName", testObject);
  }
}
