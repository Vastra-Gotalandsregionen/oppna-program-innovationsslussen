package se.vgregion.service.innovationsslussen.repository.ideacontent;

import java.util.List;

import se.vgregion.dao.domain.patterns.repository.db.jpa.DefaultJpaRepository;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaContent;

/**
 * Implementation of {@link JpaIdeaContentRepository}.
 *
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
public class JpaIdeaContentRepositoryImpl extends DefaultJpaRepository<IdeaContent, Long>
implements JpaIdeaContentRepository {

    @Override
    public List<IdeaContent> findIdeaContentsByCompanyId(long companyId) {
        String queryString = "SELECT n FROM Idea n WHERE n.companyId = ?1 ORDER BY n.id ASC";

        Object[] queryObject = new Object[]{companyId};

        List<IdeaContent> ideaContents = findByQuery(queryString, queryObject);

        return ideaContents;
    }

    @Override
    public List<IdeaContent> findIdeaContentsByGroupId(long companyId, long groupId) {
        String queryString = "SELECT n FROM Idea n WHERE n.companyId = ?1 AND n.groupId = ?2 ORDER BY "
                + "n.id ASC";

        System.out.println("JpaIdeaRepositoryImpl - findIdeasByGroupId");

        Object[] queryObject = new Object[]{companyId, groupId};

        List<IdeaContent> ideaContents = findByQuery(queryString, queryObject);

        return ideaContents;
    }

}
