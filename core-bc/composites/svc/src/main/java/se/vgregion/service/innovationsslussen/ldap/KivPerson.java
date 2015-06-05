package se.vgregion.service.innovationsslussen.ldap;

/**
 * Created by clalu4 on 2015-06-01.
 */
public class KivPerson extends Person {

    @ExplicitLdapName("vgrTilltalskod")
    private String vgrTilltalskod;
    @ExplicitLdapName("vgrModifyersName")
    private String vgrModifyersName;
    @ExplicitLdapName("hsaIdentity")
    private String hsaIdentity;
    @ExplicitLdapName("mail")
    private String mail;
    @ExplicitLdapName("vgrStrukturPersonDN")
    private String vgrStrukturPersonDN;
    @ExplicitLdapName("vgrAnstform")
    private String vgrAnstform;
    @ExplicitLdapName("vgrModifyTimestamp")
    private String vgrModifyTimestamp;
    @ExplicitLdapName("vgrObjectStatus")
    private String vgrObjectStatus;
    @ExplicitLdapName("hsaPersonIdentityNumber")
    private String hsaPersonIdentityNumber;
    @ExplicitLdapName("sn")
    private String sn;
    @ExplicitLdapName("userPrincipalName")
    private String userPrincipalName;
    @ExplicitLdapName("vgrObjectSource")
    private String vgrObjectSource;
    @ExplicitLdapName("hsaStartDate")
    private String hsaStartDate;
    @ExplicitLdapName("vgrFormansgrupp")
    private String vgrFormansgrupp;
    @ExplicitLdapName("initials")
    private String initials;
    @ExplicitLdapName("givenName")
    private String givenName;
    @ExplicitLdapName("vgrStrukturPerson")
    private String vgrStrukturPerson;
    @ExplicitLdapName("vgrObjectStatusTime")
    private String vgrObjectStatusTime;
    @ExplicitLdapName("objectClass")
    private String objectClass;
    @ExplicitLdapName("fullName")
    private String fullName;
    @ExplicitLdapName("cn")
    private String cn;
    @ExplicitLdapName("hsaMifareSerialNumber")
    private String hsaMifareSerialNumber;
    @ExplicitLdapName("vgrObjectDisplayName")
    private String vgrObjectDisplayName;
    @ExplicitLdapName("vgrAnsvarsnummer")
    private String vgrAnsvarsnummer;
    @ExplicitLdapName("vgrOrgRel")
    private String vgrOrgRel;
    @ExplicitLdapName("vgrAO3kod")
    private String vgrAO3kod;
    @ExplicitLdapName("vgr-id")
    private String vgrId;
    @ExplicitLdapName("userCertificate;binary")
    private String userCertificateBinary;

    public KivPerson() {
        super();
    }

    public KivPerson(String vgrId) {
        super();
        setVgrId(vgrId);
    }

    public String getVgrTilltalskod() {
        return vgrTilltalskod;
    }

    public void setVgrTilltalskod(String vgrTilltalskod) {
        this.vgrTilltalskod = vgrTilltalskod;
    }

    public String getVgrModifyersName() {
        return vgrModifyersName;
    }

    public void setVgrModifyersName(String vgrModifyersName) {
        this.vgrModifyersName = vgrModifyersName;
    }

    public String getHsaIdentity() {
        return hsaIdentity;
    }

    public void setHsaIdentity(String hsaIdentity) {
        this.hsaIdentity = hsaIdentity;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getVgrStrukturPersonDN() {
        return vgrStrukturPersonDN;
    }

    public void setVgrStrukturPersonDN(String vgrStrukturPersonDN) {
        this.vgrStrukturPersonDN = vgrStrukturPersonDN;
    }

    public String getVgrAnstform() {
        return vgrAnstform;
    }

    public void setVgrAnstform(String vgrAnstform) {
        this.vgrAnstform = vgrAnstform;
    }

    public String getVgrModifyTimestamp() {
        return vgrModifyTimestamp;
    }

    public void setVgrModifyTimestamp(String vgrModifyTimestamp) {
        this.vgrModifyTimestamp = vgrModifyTimestamp;
    }

    public String getVgrObjectStatus() {
        return vgrObjectStatus;
    }

    public void setVgrObjectStatus(String vgrObjectStatus) {
        this.vgrObjectStatus = vgrObjectStatus;
    }

    public String getHsaPersonIdentityNumber() {
        return hsaPersonIdentityNumber;
    }

    public void setHsaPersonIdentityNumber(String hsaPersonIdentityNumber) {
        this.hsaPersonIdentityNumber = hsaPersonIdentityNumber;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getUserPrincipalName() {
        return userPrincipalName;
    }

    public void setUserPrincipalName(String userPrincipalName) {
        this.userPrincipalName = userPrincipalName;
    }

    public String getVgrObjectSource() {
        return vgrObjectSource;
    }

    public void setVgrObjectSource(String vgrObjectSource) {
        this.vgrObjectSource = vgrObjectSource;
    }

    public String getHsaStartDate() {
        return hsaStartDate;
    }

    public void setHsaStartDate(String hsaStartDate) {
        this.hsaStartDate = hsaStartDate;
    }

    public String getVgrFormansgrupp() {
        return vgrFormansgrupp;
    }

    public void setVgrFormansgrupp(String vgrFormansgrupp) {
        this.vgrFormansgrupp = vgrFormansgrupp;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getVgrStrukturPerson() {
        return vgrStrukturPerson;
    }

    public void setVgrStrukturPerson(String vgrStrukturPerson) {
        this.vgrStrukturPerson = vgrStrukturPerson;
    }

    public String getVgrObjectStatusTime() {
        return vgrObjectStatusTime;
    }

    public void setVgrObjectStatusTime(String vgrObjectStatusTime) {
        this.vgrObjectStatusTime = vgrObjectStatusTime;
    }

    public String getObjectClass() {
        return objectClass;
    }

    public void setObjectClass(String objectClass) {
        this.objectClass = objectClass;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }

    public String getHsaMifareSerialNumber() {
        return hsaMifareSerialNumber;
    }

    public void setHsaMifareSerialNumber(String hsaMifareSerialNumber) {
        this.hsaMifareSerialNumber = hsaMifareSerialNumber;
    }

    public String getVgrObjectDisplayName() {
        return vgrObjectDisplayName;
    }

    public void setVgrObjectDisplayName(String vgrObjectDisplayName) {
        this.vgrObjectDisplayName = vgrObjectDisplayName;
    }

    public String getVgrAnsvarsnummer() {
        return vgrAnsvarsnummer;
    }

    public void setVgrAnsvarsnummer(String vgrAnsvarsnummer) {
        this.vgrAnsvarsnummer = vgrAnsvarsnummer;
    }

    public String getVgrOrgRel() {
        return vgrOrgRel;
    }

    public void setVgrOrgRel(String vgrOrgRel) {
        this.vgrOrgRel = vgrOrgRel;
    }

    public String getVgrAO3kod() {
        return vgrAO3kod;
    }

    public void setVgrAO3kod(String vgrAO3kod) {
        this.vgrAO3kod = vgrAO3kod;
    }

    public String getVgrId() {
        return vgrId;
    }

    public void setVgrId(String vgrId) {
        this.vgrId = vgrId;
    }

    public String getUserCertificateBinary() {
        return userCertificateBinary;
    }

    public void setUserCertificateBinary(String userCertificateBinary) {
        this.userCertificateBinary = userCertificateBinary;
    }

    @Override
    public String getPersonalNumber() {
        return getHsaPersonIdentityNumber();
    }
}
