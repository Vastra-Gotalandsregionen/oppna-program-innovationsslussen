package se.vgregion.service.innovationsslussen.repository.ideauserlike;

import java.util.List;

import se.vgregion.dao.domain.patterns.repository.Repository;
import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaUserLike;

/**
 * Repository interface for managing {@code Idea}s.
 *
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
public interface IdeaUserLikeRepository extends Repository<IdeaUserLike, Long> {
	
    /**
     * Find all {@link IdeaUserLike}s for a company.
     *
     * @param companyId the companyid
     * @return a {@link List} of {@link IdeaUserLike}s
     */
    List<IdeaUserLike> findLikesByCompanyId(long companyId);

    /**
     * Find all {@link IdeaUserLike}s for a group in a company.
     *
     * @param companyId the companyid
     * @param groupId   the groupId
     * @return a {@link List} of {@link IdeaUserLike}s
     */
    List<IdeaUserLike> findLikesByGroupId(long companyId, long groupId);
    
    /**
     * Find all {@link IdeaUserLike}s for certain idea (in a group in a company).
     *
     * @param companyId the companyid
     * @param groupId   the groupId
     * @param ideaId   the ideaId
     * @return a {@link List} of {@link IdeaUserLike}s
     */
    List<IdeaUserLike> findLikesByIdeaId(long companyId, long groupId, long ideaId);
    
    /**
     * Find all {@link IdeaUserLike}s for certain user (in a group in a company).
     *
     * @param companyId the companyid
     * @param groupId   the groupId
     * @param userId   the userId
     * @return a {@link List} of {@link IdeaUserLike}s
     */
    List<IdeaUserLike> findLikesByUserId(long companyId, long groupId, long userId);
    
    /**
     * Find all {@link IdeaUserLike}s for certain user and a certain idea (in a group in a company).
     *
     * @param companyId the companyid
     * @param groupId   the groupId
     * @param userId   the userId
     * @param ideaId   the ideaId
     * @return a {@link List} of {@link IdeaUserLike}s
     */
    List<IdeaUserLike> findLikesByUserAndIdea(long companyId, long groupId, long userId, long ideaId);

    /**
     * Remove the {@link IdeaUserLike} with the id
     *
     * @param ideaUserLikeId the id of the IdeaUserLike to remove
     * @return void
     */
    void remove(long ideaUserLikeId);
    
    /**
     * Remove the {@link IdeaUserLike} with the id
     *
     * @param ideaUserLikeId the id of the IdeaUserLike to remove
     * @return void
     */
    void removeByUserAndIdea(long companyId, long groupId, long userId, long ideaId);    
    
}
