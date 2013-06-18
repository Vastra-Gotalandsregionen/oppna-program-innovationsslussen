package se.vgregion.service.innovationsslussen;

import java.util.Collection;
import java.util.List;

import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;

/**
 * Service interface for managing {@link Idea}s.
 *
 * @author Erik Andersson
 * @company Monator Technologies ABŒ
 */
public interface IdeaService {

    /**
     * Add an {@link IdeaUserFavorite}.
     *
     */
    void addFavorite(long companyId, long groupId, long userId, String urlTitle);
	
    /**
     * Add an {@link IdeaUserLike}.
     *
     */
    void addLike(long companyId, long groupId, long userId, String urlTitle);
	
	
    /**
     * Add a {@link Idea}.
     *
     */
    Idea addIdea(Idea idea);
    
    /**
     * Find an {@link Idea}.
     *
     * @param ideaId - the primaryKey of the idea
     *
     * @return the {@link Idea}.
     */
    Idea find(long ideaId);
    
    /**
     * Find all {@link Idea}s.
     *
     * @return all {@link Idea}s.
     */
    Collection<Idea> findAll();

    /**
     * Find all {@link Idea} for a given company.
     *
     * @param companyId the companyId
     * @return all {@link Idea} for a given company
     */
    List<Idea> findIdeasByCompanyId(long companyId);

    /**
     * Find {@link Idea}s by company and group.
     *
     * @param companyId the companyId
     * @param groupId   the groupId
     * @return a {@link List} of {@link Idea}s
     */
    List<Idea> findIdeasByGroupId(long companyId, long groupId);
    
    /**
     * Find {@link Idea}s by company, group and user.
     *
     * @param companyId the companyId
     * @param groupId   the groupId
     * @param userId   the userId
     * @return a {@link List} of {@link Idea}s
     */
    List<Idea> findIdeasByGroupIdAndUserId(long companyId, long groupId, long userId);
    
    /**
     * Find {@link Idea}s by company, group and user that the user has favorited.
     *
     * @param companyId the companyId
     * @param groupId   the groupId
     * @param userId   the userId
     * @return a {@link List} of {@link Idea}s
     */
    List<Idea> findUserFavoritedIdeas(long companyId, long groupId, long userId);
    
    
    
    /**
     * Find {@link Idea} by urlTitle
     *
     * @param urlTitle the urlTitle
     * @param groupId   the groupId
     * @return a {@link List} of {@link Idea}s
     */
    Idea findIdeaByUrlTitle(String urlTitle);

    /**
     * Get whether user has added an idea as a favorite
     *
     */
    boolean getIsIdeaUserFavorite(long companyId, long groupId, long userId, String urlTitle);
    
    
    /**
     * Get whether user has liked a certain idea or not
     *
     */
    boolean getIsIdeaUserLiked(long companyId, long groupId, long userId, String urlTitle);
    

    /**
     * Remove a {@link Idea}.
     *
     * @param idea the primaryKey (id) of the {@link Idea} to remove
     */
    void remove(long ideaId);
    
    
    /**
     * Remove a {@link Idea}.
     *
     * @param idea the {@link Idea} to remove
     */
    void remove(Idea idea);

    /**
     * Remove all {@link Idea}s.
     */
    void removeAll();
    
    /**
     * Remove an {@link IdeaUserFavorite}.
     *
     */
    void removeFavorite(long companyId, long groupId, long userId, String urlTitle);
    
    
    /**
     * Remove an {@link IdeaUserLike}.
     *
     */
    void removeLike(long companyId, long groupId, long userId, String urlTitle);
    
    /**
     * Updates an {@link Idea}.
     *
     */
    Idea updateIdea(Idea idea);
    
    
}
