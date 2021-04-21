package pro.taskana.camunda.spring.boot;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.io.Serializable;
import java.util.List;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.MINIMAL_CLASS,
    include = JsonTypeInfo.As.PROPERTY,
    property = "@class")
public class BelegInfoArs implements Serializable {

  private static final long serialVersionUID = -5902180416415414763L;

  private Long id; // ID des Beleges in der DB

  private String belegTyp; // derzeit nur URMEL, RAMEL, ANMEL, siehe auch BelegEntity.BelegTyp

  private String bkn;

  private String arbeitnehmerNummer;

  private Integer abrechnungsMonat; // nur bei Urmel

  private Integer abrechnungsJahr; // nur bei Urmel

  private String
      paginierNummer; // jeder Beleg hat eine eindeutige Paginiernummer, die auch in anderen
                      // Systemen wie AEB, FSCD oder NMK verwendet wird

  private Integer korrekturNummer; // 0 = Originalbeleg, >0 Korrekturbelege

  private Integer
      korrekturNummerSB; // wenn Sachbearbeiter einen Eigenbeleg zur Korrektur eines Kundenbeleges
                         // erzeugt, wird diese Nummer erzeugt,

  // NULL wenn kein Eigenbeleg vorhanden ist

  private Boolean
      klaerfallUebergabe; // siehe nächsten Abschnitt: true, wenn es sich um die Übergabe an den
                          // Sachbearbeiter handelt

  private Long
      paginierNummerVorgangId; // die Paginiernummer des Vorgangs, zu der dieser Beleg gehört. Er
                               // ist auch in anderen Systemen bekannt

  private List<HinweisInfoArs> hinweise; // Liste der ungeklärten Hinweisobjekte, siehe unten

  public static long getSerialVersionUID() {
    return serialVersionUID;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getBelegTyp() {
    return belegTyp;
  }

  public void setBelegTyp(String belegTyp) {
    this.belegTyp = belegTyp;
  }

  public String getBkn() {
    return bkn;
  }

  public void setBkn(String bkn) {
    this.bkn = bkn;
  }

  public String getArbeitnehmerNummer() {
    return arbeitnehmerNummer;
  }

  public void setArbeitnehmerNummer(String arbeitnehmerNummer) {
    this.arbeitnehmerNummer = arbeitnehmerNummer;
  }

  public Integer getAbrechnungsMonat() {
    return abrechnungsMonat;
  }

  public void setAbrechnungsMonat(Integer abrechnungsMonat) {
    this.abrechnungsMonat = abrechnungsMonat;
  }

  public Integer getAbrechnungsJahr() {
    return abrechnungsJahr;
  }

  public void setAbrechnungsJahr(Integer abrechnungsJahr) {
    this.abrechnungsJahr = abrechnungsJahr;
  }

  public String getPaginierNummer() {
    return paginierNummer;
  }

  public void setPaginierNummer(String paginierNummer) {
    this.paginierNummer = paginierNummer;
  }

  public Integer getKorrekturNummer() {
    return korrekturNummer;
  }

  public void setKorrekturNummer(Integer korrekturNummer) {
    this.korrekturNummer = korrekturNummer;
  }

  public Integer getKorrekturNummerSB() {
    return korrekturNummerSB;
  }

  public void setKorrekturNummerSB(Integer korrekturNummerSB) {
    this.korrekturNummerSB = korrekturNummerSB;
  }

  public Boolean getKlaerfallUebergabe() {
    return klaerfallUebergabe;
  }

  public void setKlaerfallUebergabe(Boolean klaerfallUebergabe) {
    this.klaerfallUebergabe = klaerfallUebergabe;
  }

  public Long getPaginierNummerVorgangId() {
    return paginierNummerVorgangId;
  }

  public void setPaginierNummerVorgangId(Long paginierNummerVorgangId) {
    this.paginierNummerVorgangId = paginierNummerVorgangId;
  }

  public List<HinweisInfoArs> getHinweise() {
    return hinweise;
  }

  public void setHinweise(List<HinweisInfoArs> hinweise) {
    this.hinweise = hinweise;
  }
}
