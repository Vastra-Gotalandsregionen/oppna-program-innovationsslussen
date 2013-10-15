package se.vgregion.service.innovationsslussen.idea;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import se.vgregion.portal.innovationsslussen.domain.IdeaStatus;
import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaFile;
import se.vgregion.portal.innovationsslussen.domain.json.ObjectEntry;
import se.vgregion.portal.innovationsslussen.domain.vo.CommentItemVO;
import se.vgregion.service.barium.BariumException;
import se.vgregion.service.innovationsslussen.exception.CreateIdeaException;
import se.vgregion.service.innovationsslussen.exception.FileUploadException;
import se.vgregion.service.innovationsslussen.exception.RemoveIdeaException;
import se.vgregion.service.innovationsslussen.exception.UpdateIdeaException;

/**
 * Service interface for managing {@link Idea}s.
 *
 * @author Erik Andersson
 * @company Monator Technologies ABŒ
 */
public interface IdeaService {




    /**
     * Add an {@link se.vgregion.portal.innovationsslussen.domain.jpa.IdeaUserFavorite}.
     *
     * @param companyId the company id
     * @param groupId the group id
     * @param userId the user id
     * @param urlTitle the url title
     */
    void addFavorite(long companyId, long groupId, long userId, String urlTitle);


    /**
     * Add an {@link se.vgregion.portal.innovationsslussen.domain.jpa.IdeaUserLike}.
     *
     * @param companyId the company id
     * @param groupId the group id
     * @param userId the user id
     * @param urlTitle the url title
     */
    void addLike(long companyId, long groupId, long userId, String urlTitle);



    /**
     * Add a {@link Idea}.
     *
     * @param idea the idea
     * @param schemeServerNamePort the scheme server name port
     * @return the idea
     * @throws CreateIdeaException the create idea exception
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
     * @param companyId the company id
     * @param start the start
     * @param offset the offset
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
     * @param start the start
     * @param offset the offset
     * @return a {@link List} of {@link Idea}s
     */
    List<Idea> findIdeasByGroupId(long companyId, long groupId, int start, int offset);


    /**
     * Find the number of {@link Idea}s for a group in a company.
     *
     * @param companyId the companyid
     * @param groupId   the groupId
     * @param status   the status of the idea (i.e. whether the idea is public or private)
     * @return an int with the number of Idea
     */
    int findIdeaCountByGroupId(long companyId, long groupId, IdeaStatus status);

    /**
     * Find {@link Idea}s by company and group.
     *
     * @param companyId the companyId
     * @param groupId   the groupId
     * @param status   the status of the idea (i.e. whether the idea is public or private)
     * @return a {@link List} of {@link Idea}s
     */
    List<Idea> findIdeasByGroupId(long companyId, long groupId, IdeaStatus status);

    /**
     * Find {@link Idea}s for a group in a company.
     *
     * @param companyId the companyid
     * @param groupId   the groupId
     * @param start the start
     * @param offset the offset
     * @param status   the status of the idea (i.e. whether the idea is public or private)
     * @return a {@link List} of {@link Idea}s
     */
    List<Idea> findIdeasByGroupId(long companyId, long groupId, IdeaStatus status, int start, int offset);

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
     * @param start the start
     * @param offset the offset
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
     * @param start the start
     * @param offset the offset
     * @return a {@link List} of {@link Idea}s
     */
    List<Idea> findUserFavoritedIdeas(long companyId, long groupId, long userId, int start, int offset);

    /**
     * Find {@link Idea} by urlTitle.
     *
     * @param urlTitle the urlTitle
     * @return a {@link List} of {@link Idea}s
     */
    Idea findIdeaByUrlTitle(String urlTitle);

    /**
     * Find {@link Idea} by urlTitle.
     *
     * @param urlTitle the urlTitle
     * @param getBariumUrl   whether or not to get barium url for idea
     * @return a {@link List} of {@link Idea}s
     */
    Idea findIdeaByUrlTitle(String urlTitle, boolean getBariumUrl);


    /**
     * Get all public comments for an {@link Idea} {@link CommentItemVO}.
     *
     * @param idea {@link Idea} the idea
     * @param count max count of comments.
     * @return a {@link Idea} of {@link Idea}s
     */

    List<CommentItemVO> getPublicComments(Idea idea, int count);

    /**
     * Get all private comments for an {@link Idea} {@link CommentItemVO}.
     *
     * @param idea {@link Idea} the idea
     * @param count max count of comments.
     * @return a {@link Idea} of {@link Idea}s
     */

    List<CommentItemVO> getPrivateComments(Idea idea, int count);



    /**
     * Get whether user has added an idea as a favorite.
     *
     * @param companyId the company id
     * @param groupId the group id
     * @param userId the user id
     * @param urlTitle the url title
     * @return the checks if is idea user favorite
     */
    boolean getIsIdeaUserFavorite(long companyId, long groupId, long userId, String urlTitle);



    /**
     * Get whether user has liked a certain idea or not.
     *
     * @param companyId the company id
     * @param groupId the group id
     * @param userId the user id
     * @param urlTitle the url title
     * @return the checks if is idea user liked
     */
    boolean getIsIdeaUserLiked(long companyId, long groupId, long userId, String urlTitle);


    /**
     * Remove an {@link Idea} from both Liferay and Barium.
     *
     * @param ideaId the primaryKey (id) of the {@link Idea} to remove
     * @throws RemoveIdeaException the remove idea exception
     */
    void remove(String ideaId) throws RemoveIdeaException;

    /**
     * Remove an {@link Idea} from both Liferay and Barium.
     *
     * @param idea the {@link Idea} to remove
     * @throws RemoveIdeaException the remove idea exception
     */
    void remove(Idea idea) throws RemoveIdeaException;

    /**
     * Remove an {@link Idea} from Liferay.
     *
     * @param ideaId the primaryKey (id) of the {@link Idea} to remove
     */
    void removeFromLiferay(String ideaId);

    /**
     * Remove an {@link Idea} from Liferay.
     *
     * @param idea the {@link Idea} to remove
     */
    void removeFromLiferay(Idea idea);


    /**
     * Remove all {@link Idea}s.
     */
    void removeAll();


    /**
     * Remove an {@link IdeaUserFavorite}.
     *
     * @param companyId the company id
     * @param groupId the group id
     * @param userId the user id
     * @param urlTitle the url title
     */
    void removeFavorite(long companyId, long groupId, long userId, String urlTitle);



    /**
     * Remove an {@link se.vgregion.portal.innovationsslussen.domain.jpa.IdeaUserLike}.
     *
     * @param companyId the company id
     * @param groupId the group id
     * @param userId the user id
     * @param urlTitle the url title
     */
    void removeLike(long companyId, long groupId, long userId, String urlTitle);


    /**
     * Updates an {@link Idea} from Barium.
     *
     * @param idea the idea
     * @return the idea
     * @throws UpdateIdeaException the update idea exception
     */
    UpdateFromBariumResult updateFromBarium(Idea idea) throws UpdateIdeaException;

    boolean isUserIdeaTransporter(long userId, long groupId);

    boolean isUserInnovationsslussenEmployee(long userId, long groupId);

    boolean isUserPrioCouncilMember(long userId, long groupId);

    public static class UpdateFromBariumResult implements Serializable {

        private boolean changed;

        private Idea newIdea;

        private Idea oldIdea;

        public boolean isChanged() {
            return changed;
        }

        public void setChanged(boolean changed) {
            this.changed = changed;
        }

        public Idea getNewIdea() {
            return newIdea;
        }

        public void setNewIdea(Idea newIdea) {
            this.newIdea = newIdea;
        }

        public Idea getOldIdea() {
            return oldIdea;
        }

        public void setOldIdea(Idea oldIdea) {
            this.oldIdea = oldIdea;
        }
    }


    /**
     * Updates an {@link Idea} from Barium.
     *
     * @param ideaId the idea id
     * @return the idea
     * @throws UpdateIdeaException the update idea exception
     */
    Idea updateFromBarium(String ideaId) throws UpdateIdeaException;


    //    Idea updateFromBarium(long companyId, long groupId, String urlTitle) throws UpdateIdeaException;

    /**
     * Update all ideas from barium.
     */
    void updateAllIdeasFromBarium();

    /**
     * Updates the ideas with the specified ids.
     *
     * @param ideaIds the idea ids
     */
    void updateIdeasFromBarium(List<String> ideaIds);

    /**
     * Upload file.
     *
     * @param idea the idea
     * @param fileName the file name
     * @param bis the bis
     * @throws FileUploadException the file upload exception
     */
    @Deprecated // Probably not needed?
    void uploadFile(Idea idea, String fileName, InputStream bis) throws FileUploadException;

    /**
     * Upload file.
     *
     * @param idea the idea
     * @param publicIdea the public idea
     * @param fileName the file name
     * @param contentType the content type
     * @param inputStream the input stream
     * @return the idea file
     * @throws FileUploadException the file upload exception
     */
    IdeaFile uploadFile(Idea idea,  boolean publicIdea, String fileName, String contentType, InputStream inputStream)
            throws FileUploadException;

    /**
     * Gets the object.
     *
     * @param id the id
     * @return the object
     * @throws BariumException the barium exception
     */
    ObjectEntry getObject(String id) throws BariumException;

    /**
     * Download file.
     *
     * @param id the id
     * @return the input stream
     * @throws BariumException the barium exception
     */
    InputStream downloadFile(String id) throws BariumException;

    /**
     * Generate new url title.
     *
     * @param title the title
     * @return the string
     */
    String generateNewUrlTitle(String title);

    /**
     * Getter for defaultCommentCount.
     * @return the integer value.
     */
    public String getDefaultCommentCount();

    /**
     * Setter for defaultCommentCount.
     * @param defaultCommentCount new value for defaultCommentCount.
     */
    public void setDefaultCommentCount(String defaultCommentCount);
}
