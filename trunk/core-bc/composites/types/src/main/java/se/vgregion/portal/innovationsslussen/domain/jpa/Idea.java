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

    @Column(name = "barium_id")
    private String bariumId;

    @Column(name = "group_id")
    private long groupId;

    @Column(name = "company_id")
    private long companyId;

    @Column(name = "user_id")
    private long userId;
    
    @Column(name = "resourceprimkey")
    private long resourcePrimKey;
    

    /**
     * Constructor.
     */
    public Idea() {
    }
    
    public Idea(long companyId, long groupId, long userId, long resourcePrimKey, String bariumId) {
    	this.resourcePrimKey = resourcePrimKey;
		this.bariumId = bariumId;
		this.groupId = groupId;
		this.companyId = companyId;
		this.userId = userId;
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

}
