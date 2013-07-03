package se.vgregion.service.innovationsslussen.idea.permission;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;
import se.vgregion.service.innovationsslussen.exception.PermissionCheckerException;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.User;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.service.UserLocalServiceUtil;

public class IdeaPermissionCheckerServiceImpl implements IdeaPermissionCheckerService {

	private static final Logger LOGGER = LoggerFactory.getLogger(IdeaPermissionCheckerServiceImpl.class);
	
	@Override
	public IdeaPermissionChecker getIdeaPermissionChecker(long scopeGroupId, long userId, long ideaId) {
		
		
		IdeaPermissionChecker ideaPermissionChecker = new IdeaPermissionChecker();
		
		try {
			User user = UserLocalServiceUtil.getUser(userId);
			
			PermissionChecker permissionChecker = getPermissionChecker(user);
			
			// Get add permissions
			boolean hasPermissionAddCommentPublic = permissionChecker.hasPermission(scopeGroupId, Idea.class.getName(), ideaId, IdeaActionKeys.ADD_COMMENT_PUBLIC);
			boolean hasPermissionAddCommentPrivate = permissionChecker.hasPermission(scopeGroupId, Idea.class.getName(), ideaId, IdeaActionKeys.ADD_COMMENT_PRIVATE);		
			boolean hasPermissionAddFavorite = permissionChecker.hasPermission(scopeGroupId, Idea.class.getName(), ideaId, IdeaActionKeys.ADD_FAVORITE);
			boolean hasPermissionAddLike = permissionChecker.hasPermission(scopeGroupId, Idea.class.getName(), ideaId, IdeaActionKeys.ADD_LIKE);
			
			// Get delete permissions
			boolean hasPermissionDeleteFavorite= permissionChecker.hasPermission(scopeGroupId, Idea.class.getName(), ideaId, IdeaActionKeys.DELETE_FAVORITE);
			boolean hasPermissionDeleteLike= permissionChecker.hasPermission(scopeGroupId, Idea.class.getName(), ideaId, IdeaActionKeys.DELETE_LIKE);
			
			// Get view permissions
			boolean hasPermissionViewCommentPublic = permissionChecker.hasPermission(scopeGroupId, Idea.class.getName(), ideaId, IdeaActionKeys.VIEW_COMMENT_PUBLIC);
			boolean hasPermissionViewCommentPrivate = permissionChecker.hasPermission(scopeGroupId, Idea.class.getName(), ideaId, IdeaActionKeys.VIEW_COMMENT_PRIVATE);
            boolean hasPermissionViewIdeaPublic = permissionChecker.hasPermission(scopeGroupId, Idea.class.getName(), ideaId, IdeaActionKeys.VIEW_IDEA_PUBLIC);
            boolean hasPermissionViewIdeaPrivate = permissionChecker.hasPermission(scopeGroupId, Idea.class.getName(), ideaId, IdeaActionKeys.VIEW_IDEA_PRIVATE);
            
            boolean hasPermissionViewInBarium = permissionChecker.hasPermission(scopeGroupId, Idea.class.getName(), ideaId, IdeaActionKeys.VIEW_IN_BARIUM);

            // Set add permissions
            ideaPermissionChecker.setHasPermissionAddCommentPrivate(hasPermissionAddCommentPrivate);
            ideaPermissionChecker.setHasPermissionAddCommentPublic(hasPermissionAddCommentPublic);
            ideaPermissionChecker.setHasPermissionAddFavorite(hasPermissionAddFavorite);
            ideaPermissionChecker.setHasPermissionAddLike(hasPermissionAddLike);
            
            // Set delete permissions
            ideaPermissionChecker.setHasPermissionDeleteFavorite(hasPermissionDeleteFavorite);
            ideaPermissionChecker.setHasPermissionDeleteLike(hasPermissionDeleteLike);
            
            // Set view permissions
            ideaPermissionChecker.setHasPermissionViewCommentPublic(hasPermissionViewCommentPublic);
            ideaPermissionChecker.setHasPermissionViewCommentPrivate(hasPermissionViewCommentPrivate);
            ideaPermissionChecker.setHasPermissionViewIdeaPublic(hasPermissionViewIdeaPublic);
            ideaPermissionChecker.setHasPermissionViewIdeaPrivate(hasPermissionViewIdeaPrivate);
            ideaPermissionChecker.setHasPermissionViewInBarium(hasPermissionViewInBarium);
            
			
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
		
		boolean checkGuest = false;
		
		PermissionChecker permissionChecker;
		try {
			permissionChecker = PermissionCheckerFactoryUtil.create(user, checkGuest);
		} catch (Exception e) {
			throw new PermissionCheckerException(e.getMessage());
		}
		
		return permissionChecker;
	}
	
}
