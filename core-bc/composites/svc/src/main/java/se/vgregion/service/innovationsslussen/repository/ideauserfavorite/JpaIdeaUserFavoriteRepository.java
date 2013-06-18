package se.vgregion.service.innovationsslussen.repository.ideauserfavorite;

import se.vgregion.dao.domain.patterns.repository.db.jpa.JpaRepository;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaUserFavorite;

/**
 * JPA Repository interface for managing {@link IdeaUserFavorite}s.
 *
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
public interface JpaIdeaUserFavoriteRepository extends JpaRepository<IdeaUserFavorite, Long, Long>,
        IdeaUserFavoriteRepository {

}
