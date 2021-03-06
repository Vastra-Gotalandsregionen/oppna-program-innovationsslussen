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

import javax.persistence.*;

import se.vgregion.dao.domain.patterns.entity.AbstractEntity;

/**
 * JPA entity class representing a IdeaFile for Innovationsslussen.
 * 
 * @author Simon Göransson - simon.goransson@monator.com - vgrid: simgo3
 * @company Monator Technologies AB
 */
@Entity
@Table(name = "vgr_innovationsslussen_idea_file")
public class IdeaFile extends AbstractEntity<Long> {

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

    @Column(name = "name")
    private String name;

    @Column(name = "file_type")
    private String fileType;

    //Baruim
    @Column(unique = false, name = "barium_id")
    private String bariumId;

    /**
     * Constructor.
     */
    public IdeaFile() {
    }

    /**
     * Instantiates a new idea file.
     *
     * @param companyId the company id
     * @param groupId the group id
     * @param userId the user id
     * @param name the name
     * @param fileType the file type
     */
    public IdeaFile(long companyId, long groupId, long userId, String name, String fileType) {
        super();
        this.companyId = companyId;
        this.groupId = groupId;
        this.userId = userId;
        this.name = name;
        this.fileType = fileType;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getBariumId() {
        return bariumId;
    }

    public void setBariumId(String bariumId) {
        this.bariumId = bariumId;
    }
}
