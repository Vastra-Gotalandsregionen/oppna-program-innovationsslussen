package se.vgregion.service.innovationsslussen.repository.ideacontent;

import se.vgregion.dao.domain.patterns.repository.db.jpa.JpaRepository;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaContent;

/**
 * JPA Repository interface for managing {@link IdeaContent}s.
 *
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
public interface JpaIdeaContentRepository extends JpaRepository<IdeaContent, Long, Long>,
        IdeaContentRepository {

}
