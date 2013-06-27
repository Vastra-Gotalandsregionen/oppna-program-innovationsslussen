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
			
            boolean hasPermissionViewIdeaPublic = permissionChecker.hasPermission(scopeGroupId, Idea.class.getName(), ideaId, IdeaActionKeys.VIEW_IDEA_PUBLIC);
            boolean hasOwnerPermissionViewIdeaPublic = permissionChecker.hasOwnerPermission(scopeGroupId, Idea.class.getName(), ideaId, userId, IdeaActionKeys.VIEW_IDEA_PUBLIC);
            
            boolean hasPermissionViewIdeaPrivate = permissionChecker.hasPermission(scopeGroupId, Idea.class.getName(), ideaId, IdeaActionKeys.VIEW_IDEA_PRIVATE);
            boolean hasOwnerPermissionViewIdeaPrivate = permissionChecker.hasOwnerPermission(scopeGroupId, Idea.class.getName(), ideaId, userId, IdeaActionKeys.VIEW_IDEA_PRIVATE);
            
            ideaPermissionChecker.setHasPermissionViewIdeaPublic(hasPermissionViewIdeaPublic || hasOwnerPermissionViewIdeaPublic);
            ideaPermissionChecker.setHasPermissionViewIdeaPrivate(hasPermissionViewIdeaPrivate || hasOwnerPermissionViewIdeaPrivate);
			
			
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
