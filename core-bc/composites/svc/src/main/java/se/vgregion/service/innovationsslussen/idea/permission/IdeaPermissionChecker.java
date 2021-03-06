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

package se.vgregion.service.innovationsslussen.idea.permission;

public class IdeaPermissionChecker {

    private boolean hasPermissionAddCommentPublic;
    private boolean hasPermissionAddCommentPrivate;
	private boolean hasPermissionAddDocumentPublic;
	private boolean hasPermissionAddDocumentPrivate;

    private boolean hasPermissionAddLike;
    private boolean hasPermissionAddFavorite;

    private boolean hasPermissionCreateIdeaForOtherUser;

    private boolean hasPermissionDeleteCommentPublic;
    private boolean hasPermissionDeleteCommentPrivate;

    private boolean hasPermissionDeleteLike;
    private boolean hasPermissionDeleteFavorite;

    private boolean hasPermissionUpdateFromBarium;

    private boolean hasPermissionViewCommentPublic;
    private boolean hasPermissionViewCommentPrivate;

    private boolean hasPermissionViewIdeaPublic;
    private boolean hasPermissionViewIdeaPrivate;

    private boolean hasPermissionViewInBarium;

    private boolean isIdeaOwner;

    private boolean userPrioCouncilMember;
    private boolean userInnovationsslussenEmployee;
    private boolean userIdeaTransporter;

    /**
     * Instantiates a new idea permission checker.
     */
    public IdeaPermissionChecker() {
    }

    public boolean isHasPermissionCreateIdeaForOtherUser() {
        return hasPermissionCreateIdeaForOtherUser;
    }

    public void setHasPermissionCreateIdeaForOtherUser(boolean hasPermissionCreateIdeaForOtherUser) {
        this.hasPermissionCreateIdeaForOtherUser = hasPermissionCreateIdeaForOtherUser;
    }

    public boolean getHasPermissionViewIdeaPublic() {
        return hasPermissionViewIdeaPublic;
    }

    public void setHasPermissionViewIdeaPublic(boolean hasPermissionViewIdeaPublic) {
        this.hasPermissionViewIdeaPublic = hasPermissionViewIdeaPublic;
    }

    public boolean getHasPermissionViewIdeaPrivate() {
        return hasPermissionViewIdeaPrivate;
    }

    public void setHasPermissionViewIdeaPrivate(boolean hasPermissionViewIdeaPrivate) {
        this.hasPermissionViewIdeaPrivate = hasPermissionViewIdeaPrivate;
    }

    public boolean getHasPermissionAddLike() {
        return hasPermissionAddLike;
    }

    public void setHasPermissionAddLike(boolean hasPermissionAddLike) {
        this.hasPermissionAddLike = hasPermissionAddLike;
    }

    public boolean getHasPermissionAddFavorite() {
        return hasPermissionAddFavorite;
    }

    public void setHasPermissionAddFavorite(boolean hasPermissionAddFavorite) {
        this.hasPermissionAddFavorite = hasPermissionAddFavorite;
    }

    public boolean getHasPermissionDeleteLike() {
        return hasPermissionDeleteLike;
    }

    public void setHasPermissionDeleteLike(boolean hasPermissionDeleteLike) {
        this.hasPermissionDeleteLike = hasPermissionDeleteLike;
    }

    public boolean getHasPermissionDeleteFavorite() {
        return hasPermissionDeleteFavorite;
    }

    public void setHasPermissionDeleteFavorite(boolean hasPermissionDeleteFavorite) {
        this.hasPermissionDeleteFavorite = hasPermissionDeleteFavorite;
    }

    public boolean getHasPermissionAddCommentPublic() {
        return hasPermissionAddCommentPublic;
    }

    public void setHasPermissionAddCommentPublic(
            boolean hasPermissionAddCommentPublic) {
        this.hasPermissionAddCommentPublic = hasPermissionAddCommentPublic;
    }

    public boolean getHasPermissionAddCommentPrivate() {
        return hasPermissionAddCommentPrivate;
    }

    public void setHasPermissionAddCommentPrivate(
            boolean hasPermissionAddCommentPrivate) {
        this.hasPermissionAddCommentPrivate = hasPermissionAddCommentPrivate;
    }

    public boolean getHasPermissionViewCommentPublic() {
        return hasPermissionViewCommentPublic;
    }

    public void setHasPermissionViewCommentPublic(
            boolean hasPermissionViewCommentPublic) {
        this.hasPermissionViewCommentPublic = hasPermissionViewCommentPublic;
    }

    public boolean getHasPermissionViewCommentPrivate() {
        return hasPermissionViewCommentPrivate;
    }

    public void setHasPermissionViewCommentPrivate(
            boolean hasPermissionViewCommentPrivate) {
        this.hasPermissionViewCommentPrivate = hasPermissionViewCommentPrivate;
    }

    public boolean isHasPermissionViewInBarium() {
        return hasPermissionViewInBarium;
    }

    public void setHasPermissionViewInBarium(boolean hasPermissionViewInBarium) {
        this.hasPermissionViewInBarium = hasPermissionViewInBarium;
    }

    public boolean isHasPermissionDeleteCommentPublic() {
        return hasPermissionDeleteCommentPublic;
    }

    public void setHasPermissionDeleteCommentPublic(
            boolean hasPermissionDeleteCommentPublic) {
        this.hasPermissionDeleteCommentPublic = hasPermissionDeleteCommentPublic;
    }

    public boolean isHasPermissionDeleteCommentPrivate() {
        return hasPermissionDeleteCommentPrivate;
    }

    public void setHasPermissionDeleteCommentPrivate(
            boolean hasPermissionDeleteCommentPrivate) {
        this.hasPermissionDeleteCommentPrivate = hasPermissionDeleteCommentPrivate;
    }

    public boolean getHasPermissionUpdateFromBarium() {
        return hasPermissionUpdateFromBarium;
    }

    public void setHasPermissionUpdateFromBarium(
            boolean hasPermissionUpdateFromBarium) {
        this.hasPermissionUpdateFromBarium = hasPermissionUpdateFromBarium;
    }

    public boolean getIsIdeaOwner() {
        return isIdeaOwner;
    }

    public void setIsIdeaOwner(boolean isIdeaOwner) {
        this.isIdeaOwner = isIdeaOwner;
    }

	public boolean isHasPermissionAddDocumentPublic() {
		return hasPermissionAddDocumentPublic;
	}

	public void setHasPermissionAddDocumentPublic(
			boolean hasPermissionAddDocumentPublic) {
		this.hasPermissionAddDocumentPublic = hasPermissionAddDocumentPublic;
	}

	public boolean isHasPermissionAddDocumentPrivate() {
		return hasPermissionAddDocumentPrivate;
	}

	public void setHasPermissionAddDocumentPrivate(
			boolean hasPermissionAddDocumentPrivate) {
		this.hasPermissionAddDocumentPrivate = hasPermissionAddDocumentPrivate;
	}

	public boolean getIsUserPrioCouncilMember() {
        return userPrioCouncilMember;
    }

    public void setUserPrioCouncilMember(boolean userPrioCouncilMember) {
        this.userPrioCouncilMember = userPrioCouncilMember;
    }

    public boolean isUserInnovationsslussenEmployee() {
        return userInnovationsslussenEmployee;
    }

    public void setUserInnovationsslussenEmployee(boolean userInnovationsslussenEmployee) {
        this.userInnovationsslussenEmployee = userInnovationsslussenEmployee;
    }

    public boolean isUserIdeaTransporter() {
        return userIdeaTransporter;
    }

    public void setUserIdeaTransporter(boolean userIdeaTransporter) {
        this.userIdeaTransporter = userIdeaTransporter;
    }

}
