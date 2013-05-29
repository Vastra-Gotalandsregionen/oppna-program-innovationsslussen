package se.vgregion.portal.innovatinosslussen.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.vgregion.portal.innovatinosslussen.domain.json.ObjectField;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Patrik Bergström
 */
public class IdeaObjectFields {

    private static final Logger LOGGER = LoggerFactory.getLogger(IdeaObjectFields.class);

    private String behov;
    private String beslutsforslagsskommentar;
    private String bilaga;
    private String epost; // e-post
    private String fodelsear;
    private String godkprio1;
    private String godkprio2;
    private String hsaIdKivEnhet; // HSA-ID.KIVenhet - Förvaltning
    private String ide;
    private String idetranportorensKommentar;
    private String instanceName; // instance.name
    private String kommavidare;
    private String koordinatornsskommentar;
    private String prio1kommentar;
    private String prio2kommentar;
    private String prioriteringsradsmote;
    private String telefonnummer;
    private String vgrIdFullname; // VGR-ID.fullname
    private String vgrIdHsaPostalAdress; // VGR-ID.hsapostaladress
    private String vgrIdTitel; // VGR-ID.titel
    private String vgrId;

    private Map<String, String> specialFieldMappings = new HashMap<String, String>();

    {
        specialFieldMappings.put("e-post", "epost");
        specialFieldMappings.put("instance.name", "instanceName");
        specialFieldMappings.put("Idetranportorenskommentar", "idetranportorensKommentar");
        specialFieldMappings.put("HSA-ID.KIVenhet", "hsaIdKivEnhet");
        specialFieldMappings.put("VGR-ID.fullname", "vgrIdFullname");
        specialFieldMappings.put("VGR-ID.hsapostaladress", "vgrIdHsaPostalAdress");
        specialFieldMappings.put("VGR-ID.titel", "vgrIdTitel");
    }


    public void populate(List<ObjectField> objectFields) {
        for (ObjectField objectField : objectFields) {
            Field declaredField = null;
            try {
                // First we try to get the declared field directly by objectField.getId(). In the cases where
                // objectField.getId() contains characters not valid in Java variables we use the custom mappings to
                // find the field.
                declaredField = this.getClass().getDeclaredField(objectField.getId());
            } catch (NoSuchFieldException e) {
                try {
                    declaredField = this.getClass().getDeclaredField(specialFieldMappings.get(objectField.getId()));
                } catch (NoSuchFieldException e1) {

                } catch (NullPointerException e2) {

                }
            }

            if (declaredField != null) {
                declaredField.setAccessible(true);
                try {
                    declaredField.set(this, objectField.getValue());
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            } else {
                LOGGER.warn("Couldn't find a class field for Barium field with id=" + objectField.getId());
            }

        }
    }

    public String getBehov() {
        return behov;
    }

    public void setBehov(String behov) {
        this.behov = behov;
    }

    public String getBeslutsforslagsskommentar() {
        return beslutsforslagsskommentar;
    }

    public void setBeslutsforslagsskommentar(String beslutsforslagsskommentar) {
        this.beslutsforslagsskommentar = beslutsforslagsskommentar;
    }

    public String getBilaga() {
        return bilaga;
    }

    public void setBilaga(String bilaga) {
        this.bilaga = bilaga;
    }

    public String getEpost() {
        return epost;
    }

    public void setEpost(String epost) {
        this.epost = epost;
    }

    public String getFodelsear() {
        return fodelsear;
    }

    public void setFodelsear(String fodelsear) {
        this.fodelsear = fodelsear;
    }

    public String getGodkprio1() {
        return godkprio1;
    }

    public void setGodkprio1(String godkprio1) {
        this.godkprio1 = godkprio1;
    }

    public String getGodkprio2() {
        return godkprio2;
    }

    public void setGodkprio2(String godkprio2) {
        this.godkprio2 = godkprio2;
    }

    public String getHsaIdKivEnhet() {
        return hsaIdKivEnhet;
    }

    public void setHsaIdKivEnhet(String hsaIdKivEnhet) {
        this.hsaIdKivEnhet = hsaIdKivEnhet;
    }

    public String getIde() {
        return ide;
    }

    public void setIde(String ide) {
        this.ide = ide;
    }

    public String getIdetranportorensKommentar() {
        return idetranportorensKommentar;
    }

    public void setIdetranportorensKommentar(String idetranportorensKommentar) {
        this.idetranportorensKommentar = idetranportorensKommentar;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getKommavidare() {
        return kommavidare;
    }

    public void setKommavidare(String kommavidare) {
        this.kommavidare = kommavidare;
    }

    public String getKoordinatornsskommentar() {
        return koordinatornsskommentar;
    }

    public void setKoordinatornsskommentar(String koordinatornsskommentar) {
        this.koordinatornsskommentar = koordinatornsskommentar;
    }

    public String getPrio1kommentar() {
        return prio1kommentar;
    }

    public void setPrio1kommentar(String prio1kommentar) {
        this.prio1kommentar = prio1kommentar;
    }

    public String getPrio2kommentar() {
        return prio2kommentar;
    }

    public void setPrio2kommentar(String prio2kommentar) {
        this.prio2kommentar = prio2kommentar;
    }

    public String getPrioriteringsradsmote() {
        return prioriteringsradsmote;
    }

    public void setPrioriteringsradsmote(String prioriteringsradsmote) {
        this.prioriteringsradsmote = prioriteringsradsmote;
    }

    public String getTelefonnummer() {
        return telefonnummer;
    }

    public void setTelefonnummer(String telefonnummer) {
        this.telefonnummer = telefonnummer;
    }

    public String getVgrIdFullname() {
        return vgrIdFullname;
    }

    public void setVgrIdFullname(String vgrIdFullname) {
        this.vgrIdFullname = vgrIdFullname;
    }

    public String getVgrIdHsaPostalAdress() {
        return vgrIdHsaPostalAdress;
    }

    public void setVgrIdHsaPostalAdress(String vgrIdHsaPostalAdress) {
        this.vgrIdHsaPostalAdress = vgrIdHsaPostalAdress;
    }

    public String getVgrIdTitel() {
        return vgrIdTitel;
    }

    public void setVgrIdTitel(String vgrIdTitel) {
        this.vgrIdTitel = vgrIdTitel;
    }

    public void setVgrId(String vgrId) {
        this.vgrId = vgrId;
    }

    public String getVgrId() {
        return vgrId;
    }
}
