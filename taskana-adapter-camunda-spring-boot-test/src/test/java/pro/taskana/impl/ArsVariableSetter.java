package pro.taskana.impl;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.Variables.SerializationDataFormats;
import org.camunda.bpm.engine.variable.value.ObjectValue;

public class ArsVariableSetter implements JavaDelegate {


  @Override
  public void execute(DelegateExecution delegateExecution) throws Exception {

    BelegInfoArs belegInfoArs = new BelegInfoArs();

    belegInfoArs.setAbrechnungsJahr(2021);
    belegInfoArs.setBkn("87654321");
    belegInfoArs.setArbeitnehmerNummer("192 283 32");

    ObjectValue customerDataValue =
        Variables.objectValue(belegInfoArs)
            .serializationDataFormat(SerializationDataFormats.JSON)
            .create();

    delegateExecution.setVariable("attribute1", customerDataValue);
  }
}
