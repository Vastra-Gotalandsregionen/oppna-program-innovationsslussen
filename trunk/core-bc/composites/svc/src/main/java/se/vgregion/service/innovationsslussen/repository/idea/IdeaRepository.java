package se.vgregion.service.innovationsslussen.repository.idea;

import java.util.List;

import se.vgregion.dao.domain.patterns.repository.Repository;
import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;

/**
 * Repository interface for managing {@code Idea}s.
 *
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
public interface IdeaRepository extends Repository<Idea, Long> {

    /**
     * Find an {@link Idea} with urlTitle specified
     *
     * @param id the id
     * @return an {@link Idea}
     */
	Idea find(long id);
	
	
    /**
     * Find an {@link Idea} with urlTitle specified
     *
     * @param urlTitle the urlTitle
     * @return an {@link Idea}
     */
	Idea findIdeaByUrlTitle(String urlTitle);
	
	
    /**
     * Find all {@link Idea}s for a company.
     *
     * @param companyId the companyid
     * @return a {@link List} of {@link Idea}s
     */
    List<Idea> findIdeasByCompanyId(long companyId);

    /**
     * Find all {@link Idea}s for a group in a company.
     *
     * @param companyId the companyid
     * @param groupId   the groupId
     * @return a {@link List} of {@link Idea}s
     */
    List<Idea> findIdeasByGroupId(long companyId, long groupId);
    
    /**
     * Remove the {@link Idea} with the id
     *
     * @param ideaId the id of the idea to remove
     * @return void
     */
    void remove(long ideaId);
    
    
}
