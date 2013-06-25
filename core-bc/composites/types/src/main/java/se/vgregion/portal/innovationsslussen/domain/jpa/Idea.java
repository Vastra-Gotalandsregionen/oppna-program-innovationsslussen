package se.vgregion.portal.innovationsslussen.domain.jpa;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idea")
    @JoinColumn(name= "idea_id")
    private Set<IdeaUserFavorite> favorites = new HashSet<IdeaUserFavorite>();
    
    @Transient
    private IdeaContent ideaContentPrivate;
    
    @Transient
    private IdeaContent ideaContentPublic;
    
    @Transient
    private IdeaPerson ideaPerson;
    
    

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
    
    // Returns the first IdeaPerson found in set
    public IdeaPerson getIdeaPerson() {
    	
    	if(ideaPersons.size() > 0) {
    		setIdeaPerson(ideaPersons.iterator().next());
    	} else {
    		setIdeaPerson(new IdeaPerson());
    	}
    	
        return ideaPerson;
    }
    
    public void setIdeaPerson(IdeaPerson ideaPerson) {
    	this.ideaPerson = ideaPerson;
    }

    public void addIdeaPerson(IdeaPerson ideaPerson) {
    	
    	ideaPerson.setIdea(this);   	
    	this.ideaPersons.add(ideaPerson);
    }
	
    public IdeaContent getIdeaContentPublic () {
    	
    	if(ideaContentPublic == null) {
    		setIdeaContentPublic(getIdeaContent(IdeaConstants.IDEA_CONTENT_TYPE_PUBLIC));
    	}
    	
    	return ideaContentPublic;
    }
    
    public void setIdeaContentPublic (IdeaContent ideaContentPublic) {
    	this.ideaContentPublic = ideaContentPublic;
    }
    
    public IdeaContent getIdeaContentPrivate () {
    	
    	if(ideaContentPrivate == null) {
    		setIdeaContentPrivate(getIdeaContent(IdeaConstants.IDEA_CONTENT_TYPE_PRIVATE));
    	}
    	
    	return ideaContentPrivate;
    }
    
    public void setIdeaContentPrivate (IdeaContent ideaContentPrivate) {
    	
    	System.out.println("Idea - setIdeaContentPrivate");
    	
    	this.ideaContentPrivate = ideaContentPrivate;
    }
    
    
    private IdeaContent getIdeaContent (int type) {
    	IdeaContent ideaContent = new IdeaContent();
    	
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

    public void addFavorite(IdeaUserFavorite ideaUserFavorite) {
    	ideaUserFavorite.setIdea(this);
    	
    	this.favorites.add(ideaUserFavorite);
    }
    
    public Set<IdeaUserFavorite> getFavorites() {
    	
        return favorites;
    }

    public void addLike(IdeaUserLike ideaUserLike) {
    	ideaUserLike.setIdea(this);
    	
    	this.likes.add(ideaUserLike);
    }
    
    
}
