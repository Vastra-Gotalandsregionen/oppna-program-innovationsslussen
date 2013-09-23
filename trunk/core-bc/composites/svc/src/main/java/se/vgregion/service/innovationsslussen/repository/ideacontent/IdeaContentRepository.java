package se.vgregion.service.innovationsslussen.repository.ideacontent;

import java.util.List;

import se.vgregion.dao.domain.patterns.repository.Repository;
import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaContent;

/**
 * Repository interface for managing {@code Idea}s.
 *
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
public interface IdeaContentRepository extends Repository<IdeaContent, Long> {

    /**
     * Find all {@link IdeaContent}s for a company.
     *
     * @param companyId the companyid
     * @return a {@link List} of {@link Idea}s
     */
    List<IdeaContent> findIdeaContentsByCompanyId(long companyId);

    /**
     * Find all {@link IdeaContent}s for a group in a company.
     *
     * @param companyId the companyid
     * @param groupId   the groupId
     * @return a {@link List} of {@link IdeaContent}s
     */
    List<IdeaContent> findIdeaContentsByGroupId(long companyId, long groupId);
}
