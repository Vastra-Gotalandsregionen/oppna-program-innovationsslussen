package se.vgregion.portal.innovationsslussen.domain.jpa;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import se.vgregion.dao.domain.patterns.entity.AbstractEntity;
import se.vgregion.portal.innovationsslussen.domain.IdeaContentType;

/**
 * JPA entity class representing a Idea for Innovationsslussen
 *
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
@Entity
@Table(name = "vgr_innovationsslussen_idea", uniqueConstraints = @UniqueConstraint(columnNames = {"url_title"})
)
public class Idea extends AbstractEntity<String> {

    // Primary Key
    @Id
    private String id;

    // Liferay Related
    @Column(name = "company_id")
    private long companyId;

    @Column(name = "group_id")
    private long groupId;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "title")
    private String title;

    @Column(name = "url_title")
    private String urlTitle;

    @Column(name = "phase")
    private int phase;

    @Column(name = "idea_site_link")
    private String ideaSiteLink;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idea")
    @JoinColumn(name = "idea_id")
    private Set<IdeaContent> ideaContents = new HashSet<IdeaContent>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idea")
    @JoinColumn(name = "idea_id")
    private Set<IdeaPerson> ideaPersons = new HashSet<IdeaPerson>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idea")
    @JoinColumn(name = "idea_id")
    private Set<IdeaUserLike> likes = new HashSet<IdeaUserLike>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idea")
    @JoinColumn(name = "idea_id")
    private Set<IdeaUserFavorite> favorites = new HashSet<IdeaUserFavorite>();

    @Column(name = "created", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false,
            updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @Transient
    private IdeaPerson ideaPerson;

    @Transient
    private String bariumUrl;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

        if (ideaPersons.size() > 0) {
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

    public IdeaContent getIdeaContentPublic() {
        return getIdeaContent(IdeaContentType.IDEA_CONTENT_TYPE_PUBLIC);
    }

    public IdeaContent getIdeaContentPrivate() {
        return getIdeaContent(IdeaContentType.IDEA_CONTENT_TYPE_PRIVATE);
    }

    private IdeaContent getIdeaContent(IdeaContentType ideaContentType) {
        IdeaContent content = null;
        for (IdeaContent ideaContent : ideaContents) {
            if (ideaContent.getType().equals(ideaContentType)) {
                if (content == null) {
                    content = ideaContent;
                } else {
                    // Found a second ideaContentPrivate
                    throw new IllegalStateException(
                            "Two ideaContents of the same type were found. Only one is allowed.");
                }
            }
        }
        return content;
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

    public String getBariumUrl() {
        return bariumUrl;
    }

    public void setBariumUrl(String bariumUrl) {
        this.bariumUrl = bariumUrl;
    }

    public void setIdeaSiteLink(String ideaSiteLink) {
        this.ideaSiteLink = ideaSiteLink;
    }

    public String getIdeaSiteLink() {
        return ideaSiteLink;
    }

    public Date getCreated() {
        return created;
    }
}
