package se.vgregion.portal.innovationsslussen.domain.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import se.vgregion.dao.domain.patterns.entity.AbstractEntity;

/**
 * JPA entity class representing an IdeaUserFavorite (a user has an idea as a favorite) for Innovationsslussen.
 *
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
@Entity
@Table(name = "vgr_innovationsslussen_idea_user_favorite")
public class IdeaUserFavorite extends AbstractEntity<Long> {

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

    /**
     * Constructor.
     */
    public IdeaUserFavorite() {
    }

    /**
     * Instantiates a new idea user favorite.
     *
     * @param companyId the company id
     * @param groupId the group id
     * @param userId the user id
     */
    public IdeaUserFavorite(long companyId, long groupId, long userId) {
        this.companyId = companyId;
        this.groupId = groupId;
        this.userId = userId;
    }

    @Override
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

}
