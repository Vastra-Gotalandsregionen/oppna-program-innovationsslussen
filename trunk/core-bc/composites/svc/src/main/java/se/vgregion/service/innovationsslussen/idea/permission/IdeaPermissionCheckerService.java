package se.vgregion.service.innovationsslussen.idea.permission;

import com.liferay.portal.model.User;
import com.liferay.portal.security.permission.PermissionChecker;

public interface IdeaPermissionCheckerService {

	
	IdeaPermissionChecker getIdeaPermissionChecker(long scopeGroupId, long userId, long ideaId);
	
}
