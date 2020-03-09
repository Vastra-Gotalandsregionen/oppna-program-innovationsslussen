/**
 * Copyright 2010 Västra Götalandsregionen
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of version 2.1 of the GNU Lesser General Public
 *   License as published by the Free Software Foundation.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the
 *   Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *   Boston, MA 02111-1307  USA
 *
 */

package se.vgregion.portal.innovationsslussen.domain.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import se.vgregion.dao.domain.patterns.entity.AbstractEntity;

/**
 * JPA entity class representing an IdeaUserLike (a user likes an idea) for Innovationsslussen.
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
    @JoinColumn(name = "idea_id")
    private Idea idea;

    /**
     * Constructor.
     */
    public IdeaUserLike() {
    }

    /**
     * Instantiates a new idea user like.
     *
     * @param companyId the company id
     * @param groupId the group id
     * @param userId the user id
     */
    public IdeaUserLike(long companyId, long groupId, long userId) {
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
