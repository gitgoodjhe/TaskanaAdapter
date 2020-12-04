package pro.taskana.camunda.spring.boot;

import java.io.Serializable;

/** Test entity for a complex process variable. */
public class ProcessVariableTestObject implements Serializable {

  private String stringField;
  private int intField;
  private double doubleField;
  private boolean booleanField;

  public ProcessVariableTestObject() {}

  public ProcessVariableTestObject(
      String stringField, int intField, double doubleField, boolean booleanField) {
    this.stringField = stringField;
    this.intField = intField;
    this.doubleField = doubleField;
    this.booleanField = booleanField;
  }

  public String getStringField() {
    return stringField;
  }

  public void setStringField(String stringField) {
    this.stringField = stringField;
  }

  public int getIntField() {
    return intField;
  }

  public void setIntField(int intField) {
    this.intField = intField;
  }

  public double getDoubleField() {
    return doubleField;
  }

  public void setDoubleField(double doubleField) {
    this.doubleField = doubleField;
  }

  public boolean isBooleanField() {
    return booleanField;
  }

  public void setBooleanField(boolean booleanField) {
    this.booleanField = booleanField;
  }

  @Override
  public String toString() {
    return "ProcessVariableTestObject [stringField="
        + stringField
        + ", intField="
        + intField
        + ", doubleField="
        + doubleField
        + ", booleanField="
        + booleanField
        + "]";
  }
}
