package se.vgregion.service.innovationsslussen.repository.ideaperson;

import java.util.List;

import se.vgregion.dao.domain.patterns.repository.db.jpa.DefaultJpaRepository;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaPerson;

/**
 * Implementation of {@link JpaIdeaPersonRepository}.
 *
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
public class JpaIdeaPersonRepositoryImpl extends DefaultJpaRepository<IdeaPerson, Long>
implements JpaIdeaPersonRepository {

    @Override
    public List<IdeaPerson> findIdeaPersonsByCompanyId(long companyId) {
        String queryString = "SELECT n FROM Idea n WHERE n.companyId = ?1 ORDER BY n.id ASC";

        Object[] queryObject = new Object[]{companyId};

        List<IdeaPerson> ideaPersons = findByQuery(queryString, queryObject);

        return ideaPersons;
    }

    @Override
    public List<IdeaPerson> findIdeaPersonsByGroupId(long companyId, long groupId) {
        String queryString = "SELECT n FROM Idea n WHERE n.companyId = ?1 AND n.groupId = ?2 ORDER BY "
                + "n.id ASC";

        Object[] queryObject = new Object[]{companyId, groupId};

        List<IdeaPerson> ideaPersons = findByQuery(queryString, queryObject);

        return ideaPersons;
    }

}
