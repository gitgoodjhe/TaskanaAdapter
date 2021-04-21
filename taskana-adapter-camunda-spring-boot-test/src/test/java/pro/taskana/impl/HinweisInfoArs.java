package pro.taskana.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.io.Serializable;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.MINIMAL_CLASS,
    include = JsonTypeInfo.As.PROPERTY,
    property = "@class")
public class HinweisInfoArs implements Serializable {

  private static final long serialVersionUID = -5902180416415414763L;

  private Long
      id; // ID des Hinweises in der DB, ist eindeutig und kann als Klärfall – ID verwendet werden

  private String ergebnisCodeText; // Enum-Konstante als String, z.B. "ZU_HOHE_URLAUBSVERGUETUNG"

  private String ergebnisCode; // Ergebniscode als String, z.B. "13002"

  private String hinweisText; // Mit Parametern gefüllter Hinweistext

  public static long getSerialVersionUID() {
    return serialVersionUID;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getErgebnisCodeText() {
    return ergebnisCodeText;
  }

  public void setErgebnisCodeText(String ergebnisCodeText) {
    this.ergebnisCodeText = ergebnisCodeText;
  }

  public String getErgebnisCode() {
    return ergebnisCode;
  }

  public void setErgebnisCode(String ergebnisCode) {
    this.ergebnisCode = ergebnisCode;
  }

  public String getHinweisText() {
    return hinweisText;
  }

  public void setHinweisText(String hinweisText) {
    this.hinweisText = hinweisText;
  }
}
