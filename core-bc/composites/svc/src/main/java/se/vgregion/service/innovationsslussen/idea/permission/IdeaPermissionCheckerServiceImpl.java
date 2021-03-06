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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;
import se.vgregion.service.innovationsslussen.exception.PermissionCheckerException;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import se.vgregion.service.innovationsslussen.idea.IdeaService;

public class IdeaPermissionCheckerServiceImpl implements IdeaPermissionCheckerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IdeaPermissionCheckerServiceImpl.class);

    private IdeaService ideaService;

    @Override
    public IdeaPermissionChecker getIdeaPermissionChecker(long scopeGroupId, long userId, Idea idea) {


        IdeaPermissionChecker ideaPermissionChecker = new IdeaPermissionChecker();

        try {
            String ideaId = idea.getId();

            if(ideaId == null) {
                ideaId = "0";
            }

            User user = UserLocalServiceUtil.getUser(userId);

            PermissionChecker permissionChecker = getPermissionChecker(user);

            // Get create permissions
            boolean hasPermissionCreateIdeaForOtherUser = permissionChecker.hasPermission(scopeGroupId,
                    Idea.class.getName(), ideaId, IdeaActionKeys.CREATE_IDEA_FOR_OTHER_USER);



            // Get add permissions
            boolean hasPermissionAddCommentPublic = permissionChecker.hasPermission(scopeGroupId,
                    Idea.class.getName(), ideaId, IdeaActionKeys.ADD_COMMENT_PUBLIC);
            boolean hasPermissionAddCommentPrivate = permissionChecker.hasPermission(scopeGroupId,
                    Idea.class.getName(), ideaId, IdeaActionKeys.ADD_COMMENT_PRIVATE);

            boolean hasPermissionAddDocumentPublic = permissionChecker.hasPermission(scopeGroupId,
                    Idea.class.getName(), ideaId, IdeaActionKeys.ADD_DOCUMENT_PUBLIC);



            boolean hasPermissionAddDocumentPrivate = permissionChecker.hasPermission(scopeGroupId,
                    Idea.class.getName(), ideaId, IdeaActionKeys.ADD_DOCUMENT_PRIVATE);
            boolean hasPermissionAddFavorite = permissionChecker.hasPermission(scopeGroupId, Idea.class.getName(),
                    ideaId, IdeaActionKeys.ADD_FAVORITE);
            boolean hasPermissionAddLike = permissionChecker.hasPermission(scopeGroupId, Idea.class.getName(),
                    ideaId, IdeaActionKeys.ADD_LIKE);


            // Get delete permissions
            boolean hasPermissionDeleteCommentPublic = permissionChecker.hasPermission(scopeGroupId,
                    Idea.class.getName(), ideaId, IdeaActionKeys.DELETE_COMMENT_PUBLIC);
            boolean hasPermissionDeleteCommentPrivate = permissionChecker.hasPermission(scopeGroupId,
                    Idea.class.getName(), ideaId, IdeaActionKeys.DELETE_COMMENT_PRIVATE);

            boolean hasPermissionDeleteFavorite = permissionChecker.hasPermission(scopeGroupId,
                    Idea.class.getName(), ideaId, IdeaActionKeys.DELETE_FAVORITE);
            boolean hasPermissionDeleteLike = permissionChecker.hasPermission(scopeGroupId,
                    Idea.class.getName(), ideaId, IdeaActionKeys.DELETE_LIKE);

            // Get update permissions
            boolean hasPermissionUpdateFromBarium = permissionChecker.hasPermission(scopeGroupId,
                    Idea.class.getName(), ideaId, IdeaActionKeys.UPDATE_FROM_BARIUM);

            // Get view permissions
            boolean hasPermissionViewCommentPublic = permissionChecker.hasPermission(scopeGroupId,
                    Idea.class.getName(), ideaId, IdeaActionKeys.VIEW_COMMENT_PUBLIC);
            boolean hasPermissionViewCommentPrivate = permissionChecker.hasPermission(scopeGroupId,
                    Idea.class.getName(), ideaId, IdeaActionKeys.VIEW_COMMENT_PRIVATE);
            boolean hasPermissionViewIdeaPublic = permissionChecker.hasPermission(scopeGroupId,
                    Idea.class.getName(), ideaId, IdeaActionKeys.VIEW_IDEA_PUBLIC);
            boolean hasPermissionViewIdeaPrivate = permissionChecker.hasPermission(scopeGroupId,
                    Idea.class.getName(), ideaId, IdeaActionKeys.VIEW_IDEA_PRIVATE);
            boolean hasPermissionViewInBarium = permissionChecker.hasPermission(scopeGroupId,
                    Idea.class.getName(), ideaId, IdeaActionKeys.VIEW_IN_BARIUM);



            boolean userPrioCouncilMember = ideaService.isUserPrioCouncilMember(userId, scopeGroupId);
            boolean userInnovationsslussenEmployee = ideaService.isUserInnovationsslussenEmployee(userId, scopeGroupId);
            boolean userIdeaTransporter = ideaService.isUserIdeaTransporter(userId, scopeGroupId);

            // Get owner permissions
            boolean isIdeaOwner = (userId == idea.getUserId());

            // Set add permissions
            ideaPermissionChecker.setHasPermissionAddCommentPrivate(hasPermissionAddCommentPrivate);
            ideaPermissionChecker.setHasPermissionAddCommentPublic(hasPermissionAddCommentPublic);
            ideaPermissionChecker.setHasPermissionAddDocumentPrivate(hasPermissionAddDocumentPrivate);
            ideaPermissionChecker.setHasPermissionAddDocumentPublic(hasPermissionAddDocumentPublic);
            ideaPermissionChecker.setHasPermissionAddFavorite(hasPermissionAddFavorite);
            ideaPermissionChecker.setHasPermissionAddLike(hasPermissionAddLike);

            //Set create premissions
            ideaPermissionChecker.setHasPermissionCreateIdeaForOtherUser(hasPermissionCreateIdeaForOtherUser);


            // Set delete permissions
            ideaPermissionChecker.setHasPermissionDeleteCommentPrivate(hasPermissionDeleteCommentPrivate);
            ideaPermissionChecker.setHasPermissionDeleteCommentPublic(hasPermissionDeleteCommentPublic);
            ideaPermissionChecker.setHasPermissionDeleteFavorite(hasPermissionDeleteFavorite);
            ideaPermissionChecker.setHasPermissionDeleteLike(hasPermissionDeleteLike);

            // Set update permissions
            ideaPermissionChecker.setHasPermissionUpdateFromBarium(hasPermissionUpdateFromBarium);

            // Set view permissions
            ideaPermissionChecker.setHasPermissionViewCommentPublic(hasPermissionViewCommentPublic);
            ideaPermissionChecker.setHasPermissionViewCommentPrivate(hasPermissionViewCommentPrivate);
            ideaPermissionChecker.setHasPermissionViewIdeaPublic(hasPermissionViewIdeaPublic);
            ideaPermissionChecker.setHasPermissionViewIdeaPrivate(hasPermissionViewIdeaPrivate);
            ideaPermissionChecker.setHasPermissionViewInBarium(hasPermissionViewInBarium);

            // Set owner permissions
            ideaPermissionChecker.setIsIdeaOwner(isIdeaOwner);

            // Set info about roles.
            ideaPermissionChecker.setUserPrioCouncilMember(userPrioCouncilMember);
            ideaPermissionChecker.setUserInnovationsslussenEmployee(userInnovationsslussenEmployee);
            ideaPermissionChecker.setUserIdeaTransporter(userIdeaTransporter);

        } catch (PortalException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (SystemException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (PermissionCheckerException e) {
            LOGGER.error(e.getMessage(), e);
        }

        return ideaPermissionChecker;
    }

    private PermissionChecker getPermissionChecker(User user) throws PermissionCheckerException {

        PermissionChecker permissionChecker;
        try {
            permissionChecker = PermissionCheckerFactoryUtil.create(user);
        } catch (Exception e) {
            throw new PermissionCheckerException(e.getMessage());
        }

        return permissionChecker;
    }

    public IdeaService getIdeaService() {
        return ideaService;
    }

    public void setIdeaService(IdeaService ideaService) {
        this.ideaService = ideaService;
    }
}
