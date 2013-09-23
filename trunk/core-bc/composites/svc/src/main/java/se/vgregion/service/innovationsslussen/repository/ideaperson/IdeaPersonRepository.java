package se.vgregion.service.innovationsslussen.repository.ideaperson;

import java.util.List;

import se.vgregion.dao.domain.patterns.repository.Repository;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaPerson;

/**
 * Repository interface for managing {@code IdeaPerson}s.
 *
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
public interface IdeaPersonRepository extends Repository<IdeaPerson, Long> {

    /**
     * Find all {@link IdeaPerson}s for a company.
     *
     * @param companyId the companyid
     * @return a {@link List} of {@link IdeaPerson}s
     */
    List<IdeaPerson> findIdeaPersonsByCompanyId(long companyId);

    /**
     * Find all {@link IdeaPerson}s for a group in a company.
     *
     * @param companyId the companyid
     * @param groupId   the groupId
     * @return a {@link List} of {@link IdeaPerson}s
     */
    List<IdeaPerson> findIdeaPersonsByGroupId(long companyId, long groupId);
}
