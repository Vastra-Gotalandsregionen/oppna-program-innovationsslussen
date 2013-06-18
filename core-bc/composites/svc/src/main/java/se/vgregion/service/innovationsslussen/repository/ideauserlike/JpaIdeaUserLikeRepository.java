package se.vgregion.service.innovationsslussen.repository.ideauserlike;

import se.vgregion.dao.domain.patterns.repository.db.jpa.JpaRepository;
import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaContent;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaUserLike;

/**
 * JPA Repository interface for managing {@link IdeaUserLike}s.
 *
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
public interface JpaIdeaUserLikeRepository extends JpaRepository<IdeaUserLike, Long, Long>,
        IdeaUserLikeRepository {

}
