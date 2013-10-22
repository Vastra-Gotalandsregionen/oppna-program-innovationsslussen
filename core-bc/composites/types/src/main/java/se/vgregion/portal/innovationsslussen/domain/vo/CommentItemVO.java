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

package se.vgregion.portal.innovationsslussen.domain.vo;

import java.util.Date;

public class CommentItemVO {

    /**
     * Instantiates a new comment item vo.
     */
    public CommentItemVO() {
    }

    /**
     * Instantiates a new comment item vo.
     *
     * @param id the id
     * @param commentText the comment text
     * @param createDate the create date
     * @param name the name
     */
    public CommentItemVO(long id, String commentText, Date createDate, String name) {
        this.id = id;
        this.commentText = commentText;
        this.createDate = createDate;
        this.name = name;
    }

    private long id;
    private String commentText;
    private Date createDate;
    private boolean userCreator;
    private boolean userIdeaTransporter;
	private boolean userInnovationsslussenEmployee;
    private boolean userPrioCouncilMember;
    private String name;
    private long userId;

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getCommentText() {
        return this.commentText;
    }
    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }
    public Date getCreateDate() {
        return this.createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public boolean isUserCreator() {
        return this.userCreator;
    }

    public void setUserCreator(boolean userCreator) {
        this.userCreator = userCreator;
    }
    
    public boolean isUserIdeaTransporter() {
		return userIdeaTransporter;
	}

	public void setUserIdeaTransporter(boolean userIdeaTransporter) {
		this.userIdeaTransporter = userIdeaTransporter;
	}

    public boolean isUserPrioCouncilMember() {
        return this.userPrioCouncilMember;
    }

    public void setUserPrioCouncilMember(boolean userPrioCouncilMember) {
        this.userPrioCouncilMember = userPrioCouncilMember;
    }

    public boolean isUserInnovationsslussenEmployee() {
        return this.userInnovationsslussenEmployee;
    }

    public void setUserInnovationsslussenEmployee(boolean userInnovationsslussenEmployee) {
        this.userInnovationsslussenEmployee = userInnovationsslussenEmployee;
    }

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
}