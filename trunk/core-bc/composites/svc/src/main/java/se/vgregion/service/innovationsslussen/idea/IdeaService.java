package se.vgregion.service.innovationsslussen.idea;

import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;
import se.vgregion.portal.innovationsslussen.domain.json.ObjectEntry;
import se.vgregion.portal.innovationsslussen.domain.vo.CommentItemVO;
import se.vgregion.service.barium.BariumException;
import se.vgregion.service.innovationsslussen.exception.CreateIdeaException;
import se.vgregion.service.innovationsslussen.exception.FileUploadException;
import se.vgregion.service.innovationsslussen.exception.UpdateIdeaException;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;

/**
 * Service interface for managing {@link Idea}s.
 *
 * @author Erik Andersson
 * @company Monator Technologies ABŒ
 */
public interface IdeaService {

    /**
     * 
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
    Idea addIdea(Idea idea, String schemeServerNamePort) throws CreateIdeaException;
    
    /**
     * Find an {@link Idea}.
     *
     * @param ideaId - the primaryKey of the idea
     *
     * @return the {@link Idea}.
     */
    Idea find(String ideaId);
    
    /**
     * Find all {@link Idea}s.
     *
     * @return all {@link Idea}s.
     */
    Collection<Idea> findAll();
    
    /**
     * Find the number of {@link Idea}s for a company.
     *
     * @param companyId the companyid
     * @return an int with the number of Idea
     */
    int findIdeasCountByCompanyId(long companyId);

    /**
     * Find all {@link Idea} for a given company.
     *
     * @param companyId the companyId
     * @return all {@link Idea} for a given company
     */
    List<Idea> findIdeasByCompanyId(long companyId);
    
    /**
     * Find {@link Idea}s for a company.
     *
     * @param companyId the companyid
     * @return a {@link List} of {@link Idea}s
     */
    List<Idea> findIdeasByCompanyId(long companyId, int start, int offset);

    /**
     * Find the number of {@link Idea}s for a group in a company.
     *
     * @param companyId the companyid
     * @param groupId   the groupId
     * @return an int with the number of Idea
     */
    int findIdeaCountByGroupId(long companyId, long groupId);
    
    /**
     * Find {@link Idea}s by company and group.
     *
     * @param companyId the companyId
     * @param groupId   the groupId
     * @return a {@link List} of {@link Idea}s
     */
    List<Idea> findIdeasByGroupId(long companyId, long groupId);
    
    /**
     * Find {@link Idea}s for a group in a company.
     *
     * @param companyId the companyid
     * @param groupId   the groupId
     * @return a {@link List} of {@link Idea}s
     */
    List<Idea> findIdeasByGroupId(long companyId, long groupId, int start, int offset);
    
    /**
     * Find the number of {@link Idea}s for a user in a group in a company.
     *
     * @param companyId the companyid
     * @param groupId   the groupId
     * @param userId   the userId
     * @return a {@link List} of {@link Idea}s
     */
    int findIdeasCountByGroupIdAndUserId(long companyId, long groupId, long userId);
    
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
     * Find {@link Idea}s for a user in a group in a company.
     *
     * @param companyId the companyid
     * @param groupId   the groupId
     * @param userId   the userId
     * @return a {@link List} of {@link Idea}s
     */
    List<Idea> findIdeasByGroupIdAndUserId(long companyId, long groupId, long userId, int start, int offset);
    
    /**
     * Find the number of {@link Idea}s which a user has added as a favorite.
     *
     * @param companyId the companyid
     * @param groupId   the groupId
     * @param userId   the userId
     * @return a {@link List} of {@link Idea}s
     */
    int findUserFavoritedIdeasCount(long companyId, long groupId, long userId);
    
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
     * Find {@link Idea}s which a user has added as a favorite.
     *
     * @param companyId the companyid
     * @param groupId   the groupId
     * @param userId   the userId
     * @return a {@link List} of {@link Idea}s
     */
    List<Idea> findUserFavoritedIdeas(long companyId, long groupId, long userId, int start, int offset);
    
    /**
     * Find {@link Idea} by urlTitle
     *
     * @param urlTitle the urlTitle
     * @return a {@link List} of {@link Idea}s
     */
    Idea findIdeaByUrlTitle(String urlTitle);
    
    /**
     * Find {@link Idea} by urlTitle
     *
     * @param urlTitle the urlTitle
     * @param getBariumUrl   whether or not to get barium url for idea
     * @return a {@link List} of {@link Idea}s
     */
    Idea findIdeaByUrlTitle(String urlTitle, boolean getBariumUrl);
    
    
    /**
     * Get all public comments for an {@link Idea} {@link CommentItemVO}
     *
     * @param {@link Idea} the idea
     * @return a {@link Idea} of {@link Idea}s
     */
    
    List<CommentItemVO> getPublicComments(Idea idea);
    
    /**
     * Get all private comments for an {@link Idea} {@link CommentItemVO}
     *
     * @param {@link Idea} the idea
     * @return a {@link Idea} of {@link Idea}s
     */
    
    List<CommentItemVO> getPrivateComments(Idea idea);
    

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
    void remove(String ideaId);
    
    
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
    
    /**
     * Updates an {@link Idea} from Barium
     *
     */
    Idea updateFromBarium(Idea id) throws UpdateIdeaException;
//    Idea updateFromBarium(long companyId, long groupId, String urlTitle) throws UpdateIdeaException;

    void updateAllIdeasFromBarium();

    @Deprecated // Probably not needed?
    void uploadFile(Idea idea, String fileName, InputStream bis) throws FileUploadException;

    void uploadFile(Idea idea, String folderName, String fileName, InputStream inputStream) throws FileUploadException;

    ObjectEntry getObject(String id);

    InputStream downloadFile(String id) throws BariumException;

    String generateNewUrlTitle(String title);

    List<ObjectEntry> getIdeaFiles(Idea idea, String folderName) throws BariumException;
}
