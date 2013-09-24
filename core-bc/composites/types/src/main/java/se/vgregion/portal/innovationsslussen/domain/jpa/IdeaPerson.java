package se.vgregion.portal.innovationsslussen.domain.jpa;

import javax.persistence.*;

import se.vgregion.dao.domain.patterns.entity.AbstractEntity;

/**
 * JPA entity class representing a IdeaPerson for Innovationsslussen.
 *
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
@Entity
@Table(name = "vgr_innovationsslussen_idea_person")
public class IdeaPerson extends AbstractEntity<Long> {

    // Primary Key

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // Liferay Related

    @Column(name = "company_id")
    private long companyId;

    @Column(name = "group_id")
    private long groupId;

    @Column(name = "user_id")
    private long userId;

    // IdeaPerson Related

    @Lob
    @Column(name = "additional_persons_info")
    private String additionalPersonsInfo;

    @Column(name = "vgr_id")
    private String vgrId;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "job_position")
    private String jobPosition;

    @Column(name = "phone")
    private String phone;

    @Column(name = "phone_mobile")
    private String phoneMobile;

    //@Column(name = "birth_year")
    @Transient
    private Short birthYear;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "administrative_unit")
    private String administrativeUnit;

    @Transient
    @Column(name = "vgr_struktur_person")
    private String vgrStrukturPerson;

    // Foreign
    @ManyToOne
    private Idea idea;


    /**
     * Constructor.
     */
    public IdeaPerson() {
    }

    /**
     * Instantiates a new idea person.
     *
     * @param companyId the company id
     * @param groupId the group id
     * @param userId the user id
     */
    public IdeaPerson(long companyId, long groupId, long userId) {
        this.companyId = companyId;
        this.groupId = groupId;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getAdditionalPersonsInfo() {
        return additionalPersonsInfo;
    }

    public void setAdditionalPersonsInfo(String additionalPersonsInfo) {
        this.additionalPersonsInfo = additionalPersonsInfo;
    }

    public String getVgrId() {
        return vgrId;
    }

    public void setVgrId(String vgrId) {
        this.vgrId = vgrId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJobPosition() {
        return jobPosition;
    }

    public void setJobPosition(String jobPosition) {
        this.jobPosition = jobPosition;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoneMobile() {
        return phoneMobile;
    }

    public void setPhoneMobile(String phoneMobile) {
        this.phoneMobile = phoneMobile;
    }

    public Idea getIdea() {
        return idea;
    }

    public void setIdea(Idea idea) {
        this.idea = idea;
    }

    public Short getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Short birthYear) {
        this.birthYear = birthYear;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getAdministrativeUnit() {
        return administrativeUnit;
    }

    public void setAdministrativeUnit(String administrativeUnit) {
        this.administrativeUnit = administrativeUnit;
    }

    public String getVgrStrukturPerson() {
        return vgrStrukturPerson;
    }

    public void setVgrStrukturPerson(String vgrStrukturPerson) {
        this.vgrStrukturPerson = vgrStrukturPerson;
    }

    public static enum Gender {
        MALE, FEMALE, UNKNOWN
    }

}
