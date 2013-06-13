package se.vgregion.portal.innovationsslussen.domain.jpa;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import se.vgregion.dao.domain.patterns.entity.AbstractEntity;

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
    
    @Column(name = "resourceprimkey")
    private long resourcePrimKey;
    
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
    private Set<IdeaContent> ideaContents = new HashSet<IdeaContent>();
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idea")
    private Set<IdeaPerson> ideaPersons = new HashSet<IdeaPerson>();

    /**
     * Constructor.
     */
    public Idea() {
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

	public long getResourcePrimKey() {
		return resourcePrimKey;
	}

	public void setResourcePrimKey(long resourcePrimKey) {
		this.resourcePrimKey = resourcePrimKey;
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

    public void setIdeaContents(Set<IdeaContent> ideaContents) {
        this.ideaContents = ideaContents;
    }

    public Set<IdeaPerson> getIdeaPersons() {
        return ideaPersons;
    }

    public void setIdeaPersons(Set<IdeaPerson> ideaPersons) {
        this.ideaPersons = ideaPersons;
    }
	
    
}
