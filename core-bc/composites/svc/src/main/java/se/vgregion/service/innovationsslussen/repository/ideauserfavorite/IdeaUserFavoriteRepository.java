package se.vgregion.service.innovationsslussen.repository.ideauserfavorite;

import java.util.List;

import se.vgregion.dao.domain.patterns.repository.Repository;
import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaUserFavorite;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaUserLike;

/**
 * Repository interface for managing {@code IdeaUserFavorite}s.
 *
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
public interface IdeaUserFavoriteRepository extends Repository<IdeaUserFavorite, Long> {
	
    /**
     * Find all {@link IdeaUserFavorite}s for a company.
     *
     * @param companyId the companyid
     * @return a {@link List} of {@link IdeaUserFavorite}s
     */
    List<IdeaUserFavorite> findFavoritesByCompanyId(long companyId);

    /**
     * Find all {@link IdeaUserFavorite}s for a group in a company.
     *
     * @param companyId the companyid
     * @param groupId   the groupId
     * @return a {@link List} of {@link IdeaUserFavorite}s
     */
    List<IdeaUserFavorite> findFavoritesByGroupId(long companyId, long groupId);
    
    /**
     * Find all {@link IdeaUserFavorite}s for certain idea (in a group in a company).
     *
     * @param companyId the companyid
     * @param groupId   the groupId
     * @param ideaId   the ideaId
     * @return a {@link List} of {@link IdeaUserFavorite}s
     */
    List<IdeaUserFavorite> findFavoritesByIdeaId(long companyId, long groupId, long ideaId);
    
    /**
     * Find all {@link IdeaUserFavorite}s for certain user (in a group in a company).
     *
     * @param companyId the companyid
     * @param groupId   the groupId
     * @param userId   the userId
     * @return a {@link List} of {@link IdeaUserFavorite}s
     */
    List<IdeaUserFavorite> findFavoritesByUserId(long companyId, long groupId, long userId);
    
    /**
     * Find all {@link IdeaUserFavorite}s for certain user and a certain idea (in a group in a company).
     *
     * @param companyId the companyid
     * @param groupId   the groupId
     * @param userId   the userId
     * @param ideaId   the ideaId
     * @return a {@link List} of {@link IdeaUserFavorite}s
     */
    List<IdeaUserFavorite> findFavoritesByUserAndIdea(long companyId, long groupId, long userId, long ideaId);

    /**
     * Remove the {@link IdeaUserFavorite} with the id
     *
     * @param ideaUserFavoriteId the id of the IdeaUserFavorite to remove
     * @return void
     */
    void remove(long ideaUserFavoriteId);
    
    /**
     * Remove the matching {@link IdeaUserFavorite}
     *
     * @return void
     */
    void removeByUserAndIdea(long companyId, long groupId, long userId, long ideaId);    
    
}
