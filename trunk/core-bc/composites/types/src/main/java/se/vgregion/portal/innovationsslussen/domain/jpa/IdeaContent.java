package se.vgregion.portal.innovationsslussen.domain.jpa;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import se.vgregion.dao.domain.patterns.entity.AbstractEntity;
import se.vgregion.portal.innovationsslussen.domain.vo.CommentItemVO;

/**
 * JPA entity class representing a IdeaContent for Innovationsslussen
 *
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
@Entity
@Table(name = "vgr_innovationsslussen_idea_content")
public class IdeaContent extends AbstractEntity<Long> {

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
    
    // IdeaContent Related
    
    @Column(name = "intro")
	private String intro;
	
    @Lob
    @Column(name = "description")
    private String description;
    
    @Lob
    @Column(name = "wants_help_with")
    private String wantsHelpWith;

    @Lob
    @Column(name = "solves_problem")
    private String solvesProblem;
    
    @Lob
    @Column(name = "idea_tested")
    private String ideaTested;
    
    @Column(name = "type")
    private int type;
    
    // Foreign
    @ManyToOne
    private Idea idea;
    
    /**
     * Constructor.
     */
    public IdeaContent() {
    }

    public IdeaContent(long companyId, long groupId, long userId) {
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


	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getWantsHelpWith() {
		return wantsHelpWith;
	}


	public void setWantsHelpWith(String wantsHelpWith) {
		this.wantsHelpWith = wantsHelpWith;
	}


	public String getSolvesProblem() {
		return solvesProblem;
	}


	public void setSolvesProblem(String solvesProblem) {
		this.solvesProblem = solvesProblem;
	}


	public String getIdeaTested() {
		return ideaTested;
	}


	public void setIdeaTested(String ideaTested) {
		this.ideaTested = ideaTested;
	}

	public int getType() {
		return type;
	}


	public void setType(int type) {
		this.type = type;
	}

	public void setIdea(Idea idea) {
		this.idea = idea;
	}
	
	public Idea getIdea() {
		return idea;
	}
	
}
