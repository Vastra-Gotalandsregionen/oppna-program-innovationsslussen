package se.vgregion.service.innovationsslussen.ldap;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by clalu4 on 2015-06-01.
 */
public class AdPerson {

    @ExplicitLdapName("logonCount")
    private String logonCount;
    @ExplicitLdapName("hsaIdentity")
    private String hsaIdentity;
    @ExplicitLdapName("lastLogonTimestamp")
    private String lastLogonTimestamp;
    @ExplicitLdapName("publicDelegatesBL")
    private String publicDelegatesBL;
    @ExplicitLdapName("division")
    private String division;
    @ExplicitLdapName("msRTCSIP-UserEnabled")
    private String msRTCSIPUserEnabled;
    @ExplicitLdapName("homeMTA")
    private String homeMTA;
    @ExplicitLdapName("msExchRecipientDisplayType")
    private String msExchRecipientDisplayType;
    @ExplicitLdapName("msExchVersion")
    private String msExchVersion;
    @ExplicitLdapName("userAccountControl")
    private String userAccountControl;
    @ExplicitLdapName("mailNickname")
    private String mailNickname;
    @ExplicitLdapName("whenCreated")
    private String whenCreated;
    @ExplicitLdapName("msExchUsageLocation")
    private String msExchUsageLocation;
    @ExplicitLdapName("givenName")
    private String givenName;
    @ExplicitLdapName("homeDrive")
    private String homeDrive;
    @ExplicitLdapName("objectClass")
    private String objectClass;
    @ExplicitLdapName("whenChanged")
    private String whenChanged;
    @ExplicitLdapName("proxyAddresses")
    private String proxyAddresses;
    @ExplicitLdapName("departmentNumber")
    private String departmentNumber;
    @ExplicitLdapName("objectSid")
    private String objectSid;
    @ExplicitLdapName("msDS-AuthenticatedAtDC")
    private String msDSAuthenticatedAtDC;
    @ExplicitLdapName("objectCategory")
    private String objectCategory;
    @ExplicitLdapName("protocolSettings")
    private String protocolSettings;
    @ExplicitLdapName("telephoneNumber")
    private String telephoneNumber;
    @ExplicitLdapName("displayName")
    private String displayName;
    @ExplicitLdapName("showInAddressBook")
    private String showInAddressBook;
    @ExplicitLdapName("msExchWhenMailboxCreated")
    private String msExchWhenMailboxCreated;
    @ExplicitLdapName("msRTCSIP-PrimaryHomeServer")
    private String msRTCSIPPrimaryHomeServer;
    @ExplicitLdapName("countryCode")
    private String countryCode;
    @ExplicitLdapName("dSCorePropagationData")
    private String dSCorePropagationData;
    @ExplicitLdapName("sn")
    private String sn;
    @ExplicitLdapName("memberOf")
    private String memberOf;
    @ExplicitLdapName("codePage")
    private String codePage;
    @ExplicitLdapName("lockoutTime")
    private String lockoutTime;
    @ExplicitLdapName("msExchRBACPolicyLink")
    private String msExchRBACPolicyLink;
    @ExplicitLdapName("badPasswordTime")
    private String badPasswordTime;
    @ExplicitLdapName("manager")
    private String manager;
    @ExplicitLdapName("vgrStrukturPerson")
    private String vgrStrukturPerson;
    @ExplicitLdapName("instanceType")
    private String instanceType;
    @ExplicitLdapName("msRTCSIP-OptionFlags")
    private String msRTCSIPOptionFlags;
    @ExplicitLdapName("msExchUserCulture")
    private String msExchUserCulture;
    @ExplicitLdapName("cn")
    private String cn;
    @ExplicitLdapName("l")
    private String l;
    @ExplicitLdapName("homeMDB")
    private String homeMDB;
    @ExplicitLdapName("msRTCSIP-DeploymentLocator")
    private String msRTCSIPDeploymentLocator;
    @ExplicitLdapName("streetAddress")
    private String streetAddress;
    @ExplicitLdapName("msExchTextMessagingState")
    private String msExchTextMessagingState;
    @ExplicitLdapName("primaryGroupID")
    private String primaryGroupID;
    @ExplicitLdapName("mDBUseDefaults")
    private String mDBUseDefaults;
    @ExplicitLdapName("vgrDS-AAD-IsPresent")
    private String vgrDSAADIsPresent;
    @ExplicitLdapName("lastLogoff")
    private String lastLogoff;
    @ExplicitLdapName("mail")
    private String mail;
    @ExplicitLdapName("msRTCSIP-PrimaryUserAddress")
    private String msRTCSIPPrimaryUserAddress;
    @ExplicitLdapName("accountExpires")
    private String accountExpires;
    @ExplicitLdapName("postalCode")
    private String postalCode;
    @ExplicitLdapName("msExchRecipientTypeDetails")
    private String msExchRecipientTypeDetails;
    @ExplicitLdapName("msExchHomeServerName")
    private String msExchHomeServerName;
    @ExplicitLdapName("msExchProvisioningFlags")
    private String msExchProvisioningFlags;
    @ExplicitLdapName("msExchMailboxGuid")
    private String msExchMailboxGuid;
    @ExplicitLdapName("legacyExchangeDN")
    private String legacyExchangeDN;
    @ExplicitLdapName("msExchUserAccountControl")
    private String msExchUserAccountControl;
    @ExplicitLdapName("msExchSafeSendersHash")
    private String msExchSafeSendersHash;
    @ExplicitLdapName("physicalDeliveryOfficeName")
    private String physicalDeliveryOfficeName;
    @ExplicitLdapName("uSNChanged")
    private String uSNChanged;
    @ExplicitLdapName("sAMAccountName")
    private String sAMAccountName;
    @ExplicitLdapName("initials")
    private String initials;
    @ExplicitLdapName("managedObjects")
    private String managedObjects;
    @ExplicitLdapName("msRTCSIP-UserRoutingGroupId")
    private String msRTCSIPUserRoutingGroupId;
    @ExplicitLdapName("userCertificate")
    private String userCertificate;
    @ExplicitLdapName("msExchDelegateListBL")
    private String msExchDelegateListBL;
    @ExplicitLdapName("vgrDS-AAD-AlternateLoginID")
    private String vgrDSAADAlternateLoginID;
    @ExplicitLdapName("msExchELCMailboxFlags")
    private String msExchELCMailboxFlags;
    @ExplicitLdapName("name")
    private String name;
    @ExplicitLdapName("vgrOrgRel")
    private String vgrOrgRel;
    @ExplicitLdapName("msExchMailboxSecurityDescriptor")
    private String msExchMailboxSecurityDescriptor;
    @ExplicitLdapName("pwdLastSet")
    private String pwdLastSet;
    @ExplicitLdapName("msExchMobileMailboxFlags")
    private String msExchMobileMailboxFlags;
    @ExplicitLdapName("distinguishedName")
    private String distinguishedName;
    @ExplicitLdapName("title")
    private String title;
    @ExplicitLdapName("thumbnailPhoto")
    private String thumbnailPhoto;
    @ExplicitLdapName("objectGUID")
    private String objectGUID;
    @ExplicitLdapName("company")
    private String company;
    @ExplicitLdapName("uSNCreated")
    private String uSNCreated;
    @ExplicitLdapName("department")
    private String department;
    @ExplicitLdapName("userPrincipalName")
    private String userPrincipalName;
    @ExplicitLdapName("msRTCSIP-InternetAccessEnabled")
    private String msRTCSIPInternetAccessEnabled;
    @ExplicitLdapName("lastLogon")
    private String lastLogon;
    @ExplicitLdapName("homeDirectory")
    private String homeDirectory;
    @ExplicitLdapName("msRTCSIP-FederationEnabled")
    private String msRTCSIPFederationEnabled;
    @ExplicitLdapName("msRTCSIP-UserPolicies")
    private String msRTCSIPUserPolicies;
    @ExplicitLdapName("mobile")
    private String mobile;
    @ExplicitLdapName("facsimileTelephoneNumber")
    private String facsimileTelephoneNumber;
    @ExplicitLdapName("employeeType")
    private String employeeType;
    @ExplicitLdapName("sAMAccountType")
    private String sAMAccountType;
    @ExplicitLdapName("msExchUMDtmfMap")
    private String msExchUMDtmfMap;
    @ExplicitLdapName("msExchPoliciesExcluded")
    private String msExchPoliciesExcluded;
    @ExplicitLdapName("badPwdCount")
    private String badPwdCount;

    public AdPerson() {
        super();
    }


    public AdPerson(String cn) {
        super();
        setCn(cn);
    }

    public String getLogonCount() {
        return logonCount;
    }

    public void setLogonCount(String logonCount) {
        this.logonCount = logonCount;
    }

    public String getHsaIdentity() {
        return hsaIdentity;
    }

    public void setHsaIdentity(String hsaIdentity) {
        this.hsaIdentity = hsaIdentity;
    }

    public String getLastLogonTimestamp() {
        return lastLogonTimestamp;
    }

    public void setLastLogonTimestamp(String lastLogonTimestamp) {
        this.lastLogonTimestamp = lastLogonTimestamp;
    }

    public String getPublicDelegatesBL() {
        return publicDelegatesBL;
    }

    public void setPublicDelegatesBL(String publicDelegatesBL) {
        this.publicDelegatesBL = publicDelegatesBL;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getMsRTCSIPUserEnabled() {
        return msRTCSIPUserEnabled;
    }

    public void setMsRTCSIPUserEnabled(String msRTCSIPUserEnabled) {
        this.msRTCSIPUserEnabled = msRTCSIPUserEnabled;
    }

    public String getHomeMTA() {
        return homeMTA;
    }

    public void setHomeMTA(String homeMTA) {
        this.homeMTA = homeMTA;
    }

    public String getMsExchRecipientDisplayType() {
        return msExchRecipientDisplayType;
    }

    public void setMsExchRecipientDisplayType(String msExchRecipientDisplayType) {
        this.msExchRecipientDisplayType = msExchRecipientDisplayType;
    }

    public String getMsExchVersion() {
        return msExchVersion;
    }

    public void setMsExchVersion(String msExchVersion) {
        this.msExchVersion = msExchVersion;
    }

    public String getUserAccountControl() {
        return userAccountControl;
    }

    public void setUserAccountControl(String userAccountControl) {
        this.userAccountControl = userAccountControl;
    }

    public String getMailNickname() {
        return mailNickname;
    }

    public void setMailNickname(String mailNickname) {
        this.mailNickname = mailNickname;
    }

    public String getWhenCreated() {
        return whenCreated;
    }

    public void setWhenCreated(String whenCreated) {
        this.whenCreated = whenCreated;
    }

    public String getMsExchUsageLocation() {
        return msExchUsageLocation;
    }

    public void setMsExchUsageLocation(String msExchUsageLocation) {
        this.msExchUsageLocation = msExchUsageLocation;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getHomeDrive() {
        return homeDrive;
    }

    public void setHomeDrive(String homeDrive) {
        this.homeDrive = homeDrive;
    }

    public String getObjectClass() {
        return objectClass;
    }

    public void setObjectClass(String objectClass) {
        this.objectClass = objectClass;
    }

    public String getWhenChanged() {
        return whenChanged;
    }

    public void setWhenChanged(String whenChanged) {
        this.whenChanged = whenChanged;
    }

    public String getProxyAddresses() {
        return proxyAddresses;
    }

    public void setProxyAddresses(String proxyAddresses) {
        this.proxyAddresses = proxyAddresses;
    }

    public String getDepartmentNumber() {
        return departmentNumber;
    }

    public void setDepartmentNumber(String departmentNumber) {
        this.departmentNumber = departmentNumber;
    }

    public String getObjectSid() {
        return objectSid;
    }

    public void setObjectSid(String objectSid) {
        this.objectSid = objectSid;
    }

    public String getMsDSAuthenticatedAtDC() {
        return msDSAuthenticatedAtDC;
    }

    public void setMsDSAuthenticatedAtDC(String msDSAuthenticatedAtDC) {
        this.msDSAuthenticatedAtDC = msDSAuthenticatedAtDC;
    }

    public String getObjectCategory() {
        return objectCategory;
    }

    public void setObjectCategory(String objectCategory) {
        this.objectCategory = objectCategory;
    }

    public String getProtocolSettings() {
        return protocolSettings;
    }

    public void setProtocolSettings(String protocolSettings) {
        this.protocolSettings = protocolSettings;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getShowInAddressBook() {
        return showInAddressBook;
    }

    public void setShowInAddressBook(String showInAddressBook) {
        this.showInAddressBook = showInAddressBook;
    }

    public String getMsExchWhenMailboxCreated() {
        return msExchWhenMailboxCreated;
    }

    public void setMsExchWhenMailboxCreated(String msExchWhenMailboxCreated) {
        this.msExchWhenMailboxCreated = msExchWhenMailboxCreated;
    }

    public String getMsRTCSIPPrimaryHomeServer() {
        return msRTCSIPPrimaryHomeServer;
    }

    public void setMsRTCSIPPrimaryHomeServer(String msRTCSIPPrimaryHomeServer) {
        this.msRTCSIPPrimaryHomeServer = msRTCSIPPrimaryHomeServer;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getdSCorePropagationData() {
        return dSCorePropagationData;
    }

    public void setdSCorePropagationData(String dSCorePropagationData) {
        this.dSCorePropagationData = dSCorePropagationData;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getMemberOf() {
        return memberOf;
    }

    public void setMemberOf(String memberOf) {
        this.memberOf = memberOf;
    }

    public String getCodePage() {
        return codePage;
    }

    public void setCodePage(String codePage) {
        this.codePage = codePage;
    }

    public String getLockoutTime() {
        return lockoutTime;
    }

    public void setLockoutTime(String lockoutTime) {
        this.lockoutTime = lockoutTime;
    }

    public String getMsExchRBACPolicyLink() {
        return msExchRBACPolicyLink;
    }

    public void setMsExchRBACPolicyLink(String msExchRBACPolicyLink) {
        this.msExchRBACPolicyLink = msExchRBACPolicyLink;
    }

    public String getBadPasswordTime() {
        return badPasswordTime;
    }

    public void setBadPasswordTime(String badPasswordTime) {
        this.badPasswordTime = badPasswordTime;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getVgrStrukturPerson() {
        return vgrStrukturPerson;
    }

    public void setVgrStrukturPerson(String vgrStrukturPerson) {
        this.vgrStrukturPerson = vgrStrukturPerson;
    }

    public String getInstanceType() {
        return instanceType;
    }

    public void setInstanceType(String instanceType) {
        this.instanceType = instanceType;
    }

    public String getMsRTCSIPOptionFlags() {
        return msRTCSIPOptionFlags;
    }

    public void setMsRTCSIPOptionFlags(String msRTCSIPOptionFlags) {
        this.msRTCSIPOptionFlags = msRTCSIPOptionFlags;
    }

    public String getMsExchUserCulture() {
        return msExchUserCulture;
    }

    public void setMsExchUserCulture(String msExchUserCulture) {
        this.msExchUserCulture = msExchUserCulture;
    }

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }

    public String getL() {
        return l;
    }

    public void setL(String l) {
        this.l = l;
    }

    public String getHomeMDB() {
        return homeMDB;
    }

    public void setHomeMDB(String homeMDB) {
        this.homeMDB = homeMDB;
    }

    public String getMsRTCSIPDeploymentLocator() {
        return msRTCSIPDeploymentLocator;
    }

    public void setMsRTCSIPDeploymentLocator(String msRTCSIPDeploymentLocator) {
        this.msRTCSIPDeploymentLocator = msRTCSIPDeploymentLocator;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getMsExchTextMessagingState() {
        return msExchTextMessagingState;
    }

    public void setMsExchTextMessagingState(String msExchTextMessagingState) {
        this.msExchTextMessagingState = msExchTextMessagingState;
    }

    public String getPrimaryGroupID() {
        return primaryGroupID;
    }

    public void setPrimaryGroupID(String primaryGroupID) {
        this.primaryGroupID = primaryGroupID;
    }

    public String getmDBUseDefaults() {
        return mDBUseDefaults;
    }

    public void setmDBUseDefaults(String mDBUseDefaults) {
        this.mDBUseDefaults = mDBUseDefaults;
    }

    public String getVgrDSAADIsPresent() {
        return vgrDSAADIsPresent;
    }

    public void setVgrDSAADIsPresent(String vgrDSAADIsPresent) {
        this.vgrDSAADIsPresent = vgrDSAADIsPresent;
    }

    public String getLastLogoff() {
        return lastLogoff;
    }

    public void setLastLogoff(String lastLogoff) {
        this.lastLogoff = lastLogoff;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getMsRTCSIPPrimaryUserAddress() {
        return msRTCSIPPrimaryUserAddress;
    }

    public void setMsRTCSIPPrimaryUserAddress(String msRTCSIPPrimaryUserAddress) {
        this.msRTCSIPPrimaryUserAddress = msRTCSIPPrimaryUserAddress;
    }

    public String getAccountExpires() {
        return accountExpires;
    }

    public void setAccountExpires(String accountExpires) {
        this.accountExpires = accountExpires;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getMsExchRecipientTypeDetails() {
        return msExchRecipientTypeDetails;
    }

    public void setMsExchRecipientTypeDetails(String msExchRecipientTypeDetails) {
        this.msExchRecipientTypeDetails = msExchRecipientTypeDetails;
    }

    public String getMsExchHomeServerName() {
        return msExchHomeServerName;
    }

    public void setMsExchHomeServerName(String msExchHomeServerName) {
        this.msExchHomeServerName = msExchHomeServerName;
    }

    public String getMsExchProvisioningFlags() {
        return msExchProvisioningFlags;
    }

    public void setMsExchProvisioningFlags(String msExchProvisioningFlags) {
        this.msExchProvisioningFlags = msExchProvisioningFlags;
    }

    public String getMsExchMailboxGuid() {
        return msExchMailboxGuid;
    }

    public void setMsExchMailboxGuid(String msExchMailboxGuid) {
        this.msExchMailboxGuid = msExchMailboxGuid;
    }

    public String getLegacyExchangeDN() {
        return legacyExchangeDN;
    }

    public void setLegacyExchangeDN(String legacyExchangeDN) {
        this.legacyExchangeDN = legacyExchangeDN;
    }

    public String getMsExchUserAccountControl() {
        return msExchUserAccountControl;
    }

    public void setMsExchUserAccountControl(String msExchUserAccountControl) {
        this.msExchUserAccountControl = msExchUserAccountControl;
    }

    public String getMsExchSafeSendersHash() {
        return msExchSafeSendersHash;
    }

    public void setMsExchSafeSendersHash(String msExchSafeSendersHash) {
        this.msExchSafeSendersHash = msExchSafeSendersHash;
    }

    public String getPhysicalDeliveryOfficeName() {
        return physicalDeliveryOfficeName;
    }

    public void setPhysicalDeliveryOfficeName(String physicalDeliveryOfficeName) {
        this.physicalDeliveryOfficeName = physicalDeliveryOfficeName;
    }

    public String getuSNChanged() {
        return uSNChanged;
    }

    public void setuSNChanged(String uSNChanged) {
        this.uSNChanged = uSNChanged;
    }

    public String getsAMAccountName() {
        return sAMAccountName;
    }

    public void setsAMAccountName(String sAMAccountName) {
        this.sAMAccountName = sAMAccountName;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getManagedObjects() {
        return managedObjects;
    }

    public void setManagedObjects(String managedObjects) {
        this.managedObjects = managedObjects;
    }

    public String getMsRTCSIPUserRoutingGroupId() {
        return msRTCSIPUserRoutingGroupId;
    }

    public void setMsRTCSIPUserRoutingGroupId(String msRTCSIPUserRoutingGroupId) {
        this.msRTCSIPUserRoutingGroupId = msRTCSIPUserRoutingGroupId;
    }

    public String getUserCertificate() {
        return userCertificate;
    }

    public void setUserCertificate(String userCertificate) {
        this.userCertificate = userCertificate;
    }

    public String getMsExchDelegateListBL() {
        return msExchDelegateListBL;
    }

    public void setMsExchDelegateListBL(String msExchDelegateListBL) {
        this.msExchDelegateListBL = msExchDelegateListBL;
    }

    public String getVgrDSAADAlternateLoginID() {
        return vgrDSAADAlternateLoginID;
    }

    public void setVgrDSAADAlternateLoginID(String vgrDSAADAlternateLoginID) {
        this.vgrDSAADAlternateLoginID = vgrDSAADAlternateLoginID;
    }

    public String getMsExchELCMailboxFlags() {
        return msExchELCMailboxFlags;
    }

    public void setMsExchELCMailboxFlags(String msExchELCMailboxFlags) {
        this.msExchELCMailboxFlags = msExchELCMailboxFlags;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVgrOrgRel() {
        return vgrOrgRel;
    }

    public void setVgrOrgRel(String vgrOrgRel) {
        this.vgrOrgRel = vgrOrgRel;
    }

    public String getMsExchMailboxSecurityDescriptor() {
        return msExchMailboxSecurityDescriptor;
    }

    public void setMsExchMailboxSecurityDescriptor(String msExchMailboxSecurityDescriptor) {
        this.msExchMailboxSecurityDescriptor = msExchMailboxSecurityDescriptor;
    }

    public String getPwdLastSet() {
        return pwdLastSet;
    }

    public void setPwdLastSet(String pwdLastSet) {
        this.pwdLastSet = pwdLastSet;
    }

    public String getMsExchMobileMailboxFlags() {
        return msExchMobileMailboxFlags;
    }

    public void setMsExchMobileMailboxFlags(String msExchMobileMailboxFlags) {
        this.msExchMobileMailboxFlags = msExchMobileMailboxFlags;
    }

    public String getDistinguishedName() {
        return distinguishedName;
    }

    public void setDistinguishedName(String distinguishedName) {
        this.distinguishedName = distinguishedName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnailPhoto() {
        return thumbnailPhoto;
    }

    public void setThumbnailPhoto(String thumbnailPhoto) {
        this.thumbnailPhoto = thumbnailPhoto;
    }

    public String getObjectGUID() {
        return objectGUID;
    }

    public void setObjectGUID(String objectGUID) {
        this.objectGUID = objectGUID;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getuSNCreated() {
        return uSNCreated;
    }

    public void setuSNCreated(String uSNCreated) {
        this.uSNCreated = uSNCreated;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getUserPrincipalName() {
        return userPrincipalName;
    }

    public void setUserPrincipalName(String userPrincipalName) {
        this.userPrincipalName = userPrincipalName;
    }

    public String getMsRTCSIPInternetAccessEnabled() {
        return msRTCSIPInternetAccessEnabled;
    }

    public void setMsRTCSIPInternetAccessEnabled(String msRTCSIPInternetAccessEnabled) {
        this.msRTCSIPInternetAccessEnabled = msRTCSIPInternetAccessEnabled;
    }

    public String getLastLogon() {
        return lastLogon;
    }

    public void setLastLogon(String lastLogon) {
        this.lastLogon = lastLogon;
    }

    public String getHomeDirectory() {
        return homeDirectory;
    }

    public void setHomeDirectory(String homeDirectory) {
        this.homeDirectory = homeDirectory;
    }

    public String getMsRTCSIPFederationEnabled() {
        return msRTCSIPFederationEnabled;
    }

    public void setMsRTCSIPFederationEnabled(String msRTCSIPFederationEnabled) {
        this.msRTCSIPFederationEnabled = msRTCSIPFederationEnabled;
    }

    public String getMsRTCSIPUserPolicies() {
        return msRTCSIPUserPolicies;
    }

    public void setMsRTCSIPUserPolicies(String msRTCSIPUserPolicies) {
        this.msRTCSIPUserPolicies = msRTCSIPUserPolicies;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFacsimileTelephoneNumber() {
        return facsimileTelephoneNumber;
    }

    public void setFacsimileTelephoneNumber(String facsimileTelephoneNumber) {
        this.facsimileTelephoneNumber = facsimileTelephoneNumber;
    }

    public String getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(String employeeType) {
        this.employeeType = employeeType;
    }

    public String getsAMAccountType() {
        return sAMAccountType;
    }

    public void setsAMAccountType(String sAMAccountType) {
        this.sAMAccountType = sAMAccountType;
    }

    public String getMsExchUMDtmfMap() {
        return msExchUMDtmfMap;
    }

    public void setMsExchUMDtmfMap(String msExchUMDtmfMap) {
        this.msExchUMDtmfMap = msExchUMDtmfMap;
    }

    public String getMsExchPoliciesExcluded() {
        return msExchPoliciesExcluded;
    }

    public void setMsExchPoliciesExcluded(String msExchPoliciesExcluded) {
        this.msExchPoliciesExcluded = msExchPoliciesExcluded;
    }

    public String getBadPwdCount() {
        return badPwdCount;
    }

    public void setBadPwdCount(String badPwdCount) {
        this.badPwdCount = badPwdCount;
    }


    public String getFormattedVgrStrukturPerson() {
        return toFormattedVgrStrukturPersonText(getVgrStrukturPerson());
    }

    protected String toFormattedVgrStrukturPersonText(String s) {
        // ou=Arkitektur & Utveckling,ou=Applikation & Utveckling,ou=VGR IT,ou=Koncernkontoret,ou=Regionstyrelsen,ou=Org,o=VGR
        // -> VGR/Org/Regionstyrelsen/Koncernkontoret/VGR IT/Applikation & Utveckling/Arkitektur & Utveckling
        if (s == null || s.isEmpty()) {
            return "";
        }
        s = s.replaceAll(Pattern.quote("ou=") + "|" + Pattern.quote("o="), "");
        String[] parts = s.split(Pattern.quote(","));
        List<String> fragTexts = new ArrayList<String>(Arrays.asList(parts));
        Collections.reverse(fragTexts);
        return StringUtils.join(fragTexts.toArray(), '/');
    }
}
