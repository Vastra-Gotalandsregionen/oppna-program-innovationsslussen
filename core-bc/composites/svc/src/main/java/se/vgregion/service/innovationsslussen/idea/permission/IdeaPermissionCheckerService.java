package se.vgregion.service.innovationsslussen.idea.permission;

public interface IdeaPermissionCheckerService {

	IdeaPermissionChecker getIdeaPermissionChecker(long scopeGroupId, long userId, String ideaId);
	
}
