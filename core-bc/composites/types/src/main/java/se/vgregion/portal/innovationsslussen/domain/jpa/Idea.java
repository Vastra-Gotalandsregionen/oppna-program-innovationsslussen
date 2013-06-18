package se.vgregion.portal.innovationsslussen.domain.jpa;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import se.vgregion.dao.domain.patterns.entity.AbstractEntity;
import se.vgregion.portal.innovationsslussen.domain.IdeaConstants;

/**
 * JPA entity class representing a Idea for Innovationsslussen
 *
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
@Entity
@Table(name = "vgr_innovationsslussen_idea", uniqueConstraints = @UniqueConstraint(columnNames = {"url_title"})
)
public class Idea extends AbstractEntity<Long> {

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
    
    @Column(name = "barium_id")
	private String bariumId;
	
    @Column(name = "title")
    private String title;
    
    @Column(name = "url_title")
    private String urlTitle;

    @Column(name = "phase")
    private int phase;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idea")
    @JoinColumn(name= "idea_id")
    private Set<IdeaContent> ideaContents = new HashSet<IdeaContent>();
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idea")
    @JoinColumn(name= "idea_id")
    private Set<IdeaPerson> ideaPersons = new HashSet<IdeaPerson>();
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idea")
    @JoinColumn(name= "idea_id")
    private Set<IdeaUserLike> likes = new HashSet<IdeaUserLike>();
    

    /**
     * Constructor.
     */
    public Idea() {
    }
    
    public Idea(long companyId, long groupId, long userId) {
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

	public String getBariumId() {
		return bariumId;
	}

	public void setBariumId(String bariumId) {
		this.bariumId = bariumId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrlTitle() {
		return urlTitle;
	}

	public void setUrlTitle(String urlTitle) {
		this.urlTitle = urlTitle;
	}

	public int getPhase() {
		return phase;
	}

	public void setPhase(int phase) {
		this.phase = phase;
	}
	
    public Set<IdeaContent> getIdeaContents() {
    	
        return ideaContents;
    }

    public void addIdeaContent(IdeaContent ideaContent) {
    	ideaContent.setIdea(this);
    	
    	this.ideaContents.add(ideaContent);
    }
    
    public Set<IdeaPerson> getIdeaPersons() {
    	
        return ideaPersons;
    }

    public void addIdeaPerson(IdeaPerson ideaPerson) {
    	
    	ideaPerson.setIdea(this);   	
    	this.ideaPersons.add(ideaPerson);
    }
	
    public IdeaContent getIdeaContentPublic () {
    	return getIdeaContent(IdeaConstants.IDEA_CONTENT_TYPE_PUBLIC);
    }
    
    public IdeaContent getIdeaContentPrivate () {
    	return getIdeaContent(IdeaConstants.IDEA_CONTENT_TYPE_PRIVATE);
    }
    
    private IdeaContent getIdeaContent (int type) {
    	IdeaContent ideaContent = null;
    	
    	for(IdeaContent ic : ideaContents) {
    		if(ic.getType() == type) {
    			ideaContent = ic;
    		}
    	}
    	
    	return ideaContent;
    }
    
    public Set<IdeaUserLike> getLikes() {
    	
        return likes;
    }

    public void addLike(IdeaUserLike ideaUserLike) {
    	ideaUserLike.setIdea(this);
    	
    	this.likes.add(ideaUserLike);
    }
    
    
    
}
