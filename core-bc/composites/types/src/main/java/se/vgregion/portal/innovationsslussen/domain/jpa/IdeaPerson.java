package se.vgregion.portal.innovationsslussen.domain.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import se.vgregion.dao.domain.patterns.entity.AbstractEntity;

/**
 * JPA entity class representing a IdeaPerson for Innovationsslussen
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
    
    // Foreign
    @ManyToOne
    private Idea idea;
    
    /**
     * Constructor.
     */
    public IdeaPerson() {
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
	
	public Idea getIdea() {
		return idea;
	}
    
}
