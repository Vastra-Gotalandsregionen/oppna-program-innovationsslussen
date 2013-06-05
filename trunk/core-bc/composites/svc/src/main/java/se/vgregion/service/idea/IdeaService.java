package se.vgregion.service.idea;

import java.util.Collection;
import java.util.List;

import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;

/**
 * Service interface for managing {@link Idea}s.
 *
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
public interface IdeaService {

    /**
     * Add a {@link Idea}.
     *
     * @param userId    the userId of the user who creates the {@link Idea}
     * @param companyId the companyId
     * @param groupId   the groupId
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
     * Remove a {@link Idea}.
     *
     * @param idea the primaryKey (id) of hte {@link Idea} to remove
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
}