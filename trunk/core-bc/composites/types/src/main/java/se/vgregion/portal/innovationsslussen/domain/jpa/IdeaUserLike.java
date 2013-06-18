package se.vgregion.portal.innovationsslussen.domain.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import se.vgregion.dao.domain.patterns.entity.AbstractEntity;

/**
 * JPA entity class representing an IdeaUserLike (a user likes an idea) for Innovationsslussen
 *
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
@Entity
@Table(name = "vgr_innovationsslussen_idea_user_like")
public class IdeaUserLike extends AbstractEntity<Long> {

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
    
    // Idea Related
    // Foreign
    @ManyToOne
    private Idea idea;
    
    @Transient
    private long ideaId;
    
    /**
     * Constructor.
     */
    public IdeaUserLike() {
    }
    
    public IdeaUserLike(long companyId, long groupId, long userId) {
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

	public Idea getIdea() {
		return idea;
	}
	
	public void setIdea(Idea idea) {
		this.idea = idea;
	}
	
	public void setIdeaId(long ideaId) {
		this.ideaId = ideaId;
	}
	
	public long getIdeaId() {
		long ideaId = 0;
		
		if(this.idea != null) {
			ideaId = idea.getId();
		}
		
		setIdeaId(ideaId);
		
		return this.ideaId;
	}
    
}
