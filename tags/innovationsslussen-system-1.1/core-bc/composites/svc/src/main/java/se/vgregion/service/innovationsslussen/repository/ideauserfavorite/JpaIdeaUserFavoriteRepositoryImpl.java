package se.vgregion.service.innovationsslussen.repository.ideauserfavorite;

import java.util.List;

import javax.persistence.Query;

import se.vgregion.dao.domain.patterns.repository.db.jpa.DefaultJpaRepository;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaUserFavorite;

/**
 * Implementation of {@link JpaIdeaUserFavoriteRepository}.
 *
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
public class JpaIdeaUserFavoriteRepositoryImpl extends DefaultJpaRepository<IdeaUserFavorite, Long>
implements JpaIdeaUserFavoriteRepository {

    @Override
    public List<IdeaUserFavorite> findFavoritesByCompanyId(long companyId) {
        String queryString = "SELECT n FROM IdeaUserFavorite n WHERE n.companyId = ?1 ORDER BY n.id ASC";

        Object[] queryObject = new Object[]{companyId};

        List<IdeaUserFavorite> ideaUserFavorites = findByQuery(queryString, queryObject);

        return ideaUserFavorites;
    }

    @Override
    public List<IdeaUserFavorite> findFavoritesByGroupId(long companyId, long groupId) {
        String queryString = "SELECT n FROM IdeaUserFavorite n WHERE n.companyId ="
                + " ?1 AND n.groupId = ?2 ORDER BY n.id ASC";

        Object[] queryObject = new Object[]{companyId, groupId};

        List<IdeaUserFavorite> ideaUserFavorites = findByQuery(queryString, queryObject);

        return ideaUserFavorites;
    }

    @Override
    public List<IdeaUserFavorite> findFavoritesByIdeaId(long companyId, long groupId, long ideaId) {
        String queryString = "SELECT n FROM IdeaUserFavorite n WHERE n.companyId ="
                + " ?1 AND n.groupId = ?2 AND n.idea.id = ?3  ORDER BY n.id ASC";

        Object[] queryObject = new Object[]{companyId, groupId, ideaId};

        List<IdeaUserFavorite> ideaUserFavorites = findByQuery(queryString, queryObject);

        return ideaUserFavorites;
    }

    @Override
    public List<IdeaUserFavorite> findFavoritesByUserId(long companyId, long groupId, long userId) {
        String queryString = "SELECT n FROM IdeaUserFavorite n WHERE n.companyId ="
                + " ?1 AND n.groupId = ?2 AND n.userId = ?3  ORDER BY n.id ASC";

        Object[] queryObject = new Object[]{companyId, groupId, userId};

        List<IdeaUserFavorite> ideaUserFavorites = findByQuery(queryString, queryObject);

        return ideaUserFavorites;
    }

    // Todo companyId och groupId tillför inget här?
    @Override
    public List<IdeaUserFavorite> findFavoritesByUserAndIdea(long companyId, long groupId, long userId, String ideaId) {

        String queryString = ""
                + " SELECT n FROM IdeaUserFavorite n"
                + " WHERE n.companyId = ?1 AND"
                + " n.groupId = ?2 AND"
                + " n.userId = ?3 AND"
                + " n.idea.id = ?4"
                + " ORDER BY n.id ASC";

        Object[] queryObject = new Object[]{companyId, groupId, userId, ideaId};

        List<IdeaUserFavorite> ideaUserFavorites = findByQuery(queryString, queryObject);

        return ideaUserFavorites;
    }

    @Override
    public void remove(long ideaUserFavoriteId) {

        IdeaUserFavorite ideaUserFavorite = find(ideaUserFavoriteId);

        remove(ideaUserFavorite);

    }

    @Override
    public void removeByUserAndIdea(long companyId, long groupId, long userId, String ideaId) {

        String queryString = ""
                + " DELETE FROM IdeaUserFavorite n"
                + " WHERE n.companyId = ?1 AND"
                + " n.groupId = ?2 AND"
                + " n.userId = ?3 AND"
                + " n.idea.id = ?4";

        Object[] queryObject = new Object[]{companyId, groupId, userId, ideaId};

        Query query = entityManager.createQuery(queryString);
        if (queryObject != null) {
            for (int i = 0; i < queryObject.length; i++) {
                query.setParameter(i + 1, queryObject[i]);
            }
        }

        query.executeUpdate();
    }


}
