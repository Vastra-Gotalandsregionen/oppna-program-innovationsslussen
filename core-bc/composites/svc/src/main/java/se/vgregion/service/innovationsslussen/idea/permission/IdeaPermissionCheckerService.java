package se.vgregion.service.innovationsslussen.idea.permission;

import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;

public interface IdeaPermissionCheckerService {

	//IdeaPermissionChecker getIdeaPermissionChecker(long scopeGroupId, long userId, String ideaId);
	
	IdeaPermissionChecker getIdeaPermissionChecker(long scopeGroupId, long userId, Idea idea);
	
}
