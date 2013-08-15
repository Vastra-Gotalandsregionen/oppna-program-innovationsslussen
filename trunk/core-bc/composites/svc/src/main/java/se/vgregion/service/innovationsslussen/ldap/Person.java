/**
 *
 */
package se.vgregion.service.innovationsslussen.ldap;



/**
 * Value object for information stored in the user ldap database. The properties are supposed to match those in the
 * db. This enables the LdapService to use this as an structure for searching and packing results from queries from
 * this db.
 * @author claes.lundahl
 */
public class Person {

    private String vgrTilltalskod;
    private String hsaPersonIdentityNumber;
    private String givenName;
    private String vgrObjectStatusTime;
    private String vgrAnstform;
    private String objectClass;
    @ExplicitLdapName("userCertificate;binary")
    private String userCertificateBinary;
    //private String userCertificate;binary;
    private String vgrFormansgrupp;
    private String hsaMifareSerialNumber;
    private String vgrOrgRel;
    private String mail;
    private String vgrObjectSource;
    private String vgrStrukturPersonDN;
    private String cn;
    private String initials;
    private String vgrObjectStatus;
    private String vgrStrukturPerson;
    private String userPrincipalName;
    //private String vgr-id;
    @ExplicitLdapName("vgr-id")
    private String vgrId;
    private String vgrAnsvarsnummer;
    private String vgrAO3kod;
    private String vgrModifyTimestamp;
    private String hsaIdentity;
    private String fullName;
    private String sn;
    private String vgrModifyersName;
    private String hsaStartDate;

    public String getVgrTilltalskod() {
        return vgrTilltalskod;
    }

    public void setVgrTilltalskod(String vgrTilltalskod) {
        this.vgrTilltalskod = vgrTilltalskod;
    }

    public String getHsaPersonIdentityNumber() {
        return hsaPersonIdentityNumber;
    }

    public void setHsaPersonIdentityNumber(String hsaPersonIdentityNumber) {
        this.hsaPersonIdentityNumber = hsaPersonIdentityNumber;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getVgrObjectStatusTime() {
        return vgrObjectStatusTime;
    }

    public void setVgrObjectStatusTime(String vgrObjectStatusTime) {
        this.vgrObjectStatusTime = vgrObjectStatusTime;
    }

    public String getVgrAnstform() {
        return vgrAnstform;
    }

    public void setVgrAnstform(String vgrAnstform) {
        this.vgrAnstform = vgrAnstform;
    }

    public String getObjectClass() {
        return objectClass;
    }

    public void setObjectClass(String objectClass) {
        this.objectClass = objectClass;
    }

    public String getUserCertificateBinary() {
        return userCertificateBinary;
    }

    public void setUserCertificateBinary(String userCertificateBinary) {
        this.userCertificateBinary = userCertificateBinary;
    }

    public String getVgrFormansgrupp() {
        return vgrFormansgrupp;
    }

    public void setVgrFormansgrupp(String vgrFormansgrupp) {
        this.vgrFormansgrupp = vgrFormansgrupp;
    }

    public String getHsaMifareSerialNumber() {
        return hsaMifareSerialNumber;
    }

    public void setHsaMifareSerialNumber(String hsaMifareSerialNumber) {
        this.hsaMifareSerialNumber = hsaMifareSerialNumber;
    }

    public String getVgrOrgRel() {
        return vgrOrgRel;
    }

    public void setVgrOrgRel(String vgrOrgRel) {
        this.vgrOrgRel = vgrOrgRel;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getVgrObjectSource() {
        return vgrObjectSource;
    }

    public void setVgrObjectSource(String vgrObjectSource) {
        this.vgrObjectSource = vgrObjectSource;
    }

    public String getVgrStrukturPersonDN() {
        return vgrStrukturPersonDN;
    }

    public void setVgrStrukturPersonDN(String vgrStrukturPersonDN) {
        this.vgrStrukturPersonDN = vgrStrukturPersonDN;
    }

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getVgrObjectStatus() {
        return vgrObjectStatus;
    }

    public void setVgrObjectStatus(String vgrObjectStatus) {
        this.vgrObjectStatus = vgrObjectStatus;
    }

    public String getVgrStrukturPerson() {
        return vgrStrukturPerson;
    }

    public void setVgrStrukturPerson(String vgrStrukturPerson) {
        this.vgrStrukturPerson = vgrStrukturPerson;
    }

    public String getUserPrincipalName() {
        return userPrincipalName;
    }

    public void setUserPrincipalName(String userPrincipalName) {
        this.userPrincipalName = userPrincipalName;
    }

    public String getVgrId() {
        return vgrId;
    }

    public void setVgrId(String vgrId) {
        this.vgrId = vgrId;
    }

    public String getVgrAnsvarsnummer() {
        return vgrAnsvarsnummer;
    }

    public void setVgrAnsvarsnummer(String vgrAnsvarsnummer) {
        this.vgrAnsvarsnummer = vgrAnsvarsnummer;
    }

    public String getVgrAO3kod() {
        return vgrAO3kod;
    }

    public void setVgrAO3kod(String vgrAO3kod) {
        this.vgrAO3kod = vgrAO3kod;
    }

    public String getVgrModifyTimestamp() {
        return vgrModifyTimestamp;
    }

    public void setVgrModifyTimestamp(String vgrModifyTimestamp) {
        this.vgrModifyTimestamp = vgrModifyTimestamp;
    }

    public String getHsaIdentity() {
        return hsaIdentity;
    }

    public void setHsaIdentity(String hsaIdentity) {
        this.hsaIdentity = hsaIdentity;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getVgrModifyersName() {
        return vgrModifyersName;
    }

    public void setVgrModifyersName(String vgrModifyersName) {
        this.vgrModifyersName = vgrModifyersName;
    }

    public String getHsaStartDate() {
        return hsaStartDate;
    }

    public void setHsaStartDate(String hsaStartDate) {
        this.hsaStartDate = hsaStartDate;
    }

    public Gender getGender() {
        if (hsaPersonIdentityNumber == null
                || "".equals(hsaPersonIdentityNumber)
                || hsaPersonIdentityNumber.length() != 12) {
            return Gender.UNKNOWN;
        }

        char c = hsaPersonIdentityNumber.charAt(10);
        if (!Character.isDigit(c)) {
            return Gender.UNKNOWN;
        }
        int i = Integer.parseInt(Character.toString(c));
        if (i % 2 == 0){
            return Gender.FEMALE;
        }
        return Gender.MALE;
    }

    public static enum Gender {
        MALE, FEMALE, UNKNOWN
    }

}
