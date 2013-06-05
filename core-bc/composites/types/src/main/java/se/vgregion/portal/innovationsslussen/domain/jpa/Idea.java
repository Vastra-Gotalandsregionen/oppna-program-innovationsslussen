package se.vgregion.portal.innovationsslussen.domain.jpa;

import se.vgregion.dao.domain.patterns.entity.AbstractEntity;

import javax.persistence.*;

/**
 * JPA entity class representing an Idea for Innovationsslussen
 *
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
@Entity
@Table(name = "vgr_innovationsslussen_idea"
        //,uniqueConstraints = @UniqueConstraint(columnNames = {"foo", "bar"})
)
public class Idea extends AbstractEntity<Long> {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "company_id")
    private long companyId;
	
    @Column(name = "group_id")
    private long groupId;

    @Column(name = "user_id")
    private long userId;
    
    @Column(name = "resourceprimkey")
    private long resourcePrimKey;
    
    @Column(name = "barium_id")
    private String bariumId;
    
    @Column(name = "description")
    @Lob
	private String description;
	
    @Column(name = "description_short")
    @Lob
    private String descriptionShort;
	
    @Column(name = "phase")
    private int phase;
	
    @Column(name = "solves_problem")
    @Lob
    private String solvesProblem;
	
    @Column(name = "title")
    private String title;
    
    @Column(name = "wants_help_with")
    @Lob
	private String wantsHelpWith;
	
    @Column(name = "vgr_id")
	private String vgrId;
    
    @Column(name = "name")
	private String name;
    
    @Column(name = "phone")
	private String phone;
	
    @Column(name = "administrative_unit")
    private String administrativeUnit;
    
    @Column(name = "job_position")
	private String jobPosition;    
    

    /**
     * Constructor.
     */
    public Idea() {
    }
    
    public Idea(long companyId, long groupId, long userId, long resourcePrimKey, String bariumId) {
		this.companyId = companyId;
		this.groupId = groupId;
		this.userId = userId;
		this.resourcePrimKey = resourcePrimKey;
		this.bariumId = bariumId;
	}
    
	public Idea(long companyId, long groupId, long userId, String description, String solvesProblem,
			String title, String wantsHelpWith, String vgrId, String name,
			String phone, String administrativeUnit, String jobPosition) {
		this.companyId = companyId;
		this.groupId = groupId;
		this.userId = userId;
		this.description = description;
		this.phase = phase;
		this.solvesProblem = solvesProblem;
		this.title = title;
		this.wantsHelpWith = wantsHelpWith;
		this.vgrId = vgrId;
		this.name = name;
		this.phone = phone;
		this.administrativeUnit = administrativeUnit;
		this.jobPosition = jobPosition;
	}
    
    
    public Long getId() {
        return id;
    }

	public void setId(Long id) {
		this.id = id;
	}

	public String getBariumId() {
		return bariumId;
	}

	public void setBariumId(String bariumId) {
		this.bariumId = bariumId;
	}

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getResourcePrimKey() {
		return resourcePrimKey;
	}

	public void setResourcePrimKey(long resourcePrimKey) {
		this.resourcePrimKey = resourcePrimKey;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescriptionShort() {
		return descriptionShort;
	}

	public void setDescriptionShort(String descriptionShort) {
		this.descriptionShort = descriptionShort;
	}

	public int getPhase() {
		return phase;
	}

	public void setPhase(int phase) {
		this.phase = phase;
	}

	public String getSolvesProblem() {
		return solvesProblem;
	}

	public void setSolvesProblem(String solvesProblem) {
		this.solvesProblem = solvesProblem;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getWantsHelpWith() {
		return wantsHelpWith;
	}

	public void setWantsHelpWith(String wantsHelpWith) {
		this.wantsHelpWith = wantsHelpWith;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAdministrativeUnit() {
		return administrativeUnit;
	}

	public void setAdministrativeUnit(String administrativeUnit) {
		this.administrativeUnit = administrativeUnit;
	}

	public String getJobPosition() {
		return jobPosition;
	}

	public void setJobPosition(String jobPosition) {
		this.jobPosition = jobPosition;
	}

}
