package se.vgregion.service.innovationsslussen.idea.permission;

import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;

public interface IdeaPermissionCheckerService {

    /**
     * Gets the idea permission checker.
     *
     * @param scopeGroupId the scope group id
     * @param userId the user id
     * @param idea the idea
     * @return the idea permission checker
     */
    IdeaPermissionChecker getIdeaPermissionChecker(long scopeGroupId, long userId, Idea idea);

}
