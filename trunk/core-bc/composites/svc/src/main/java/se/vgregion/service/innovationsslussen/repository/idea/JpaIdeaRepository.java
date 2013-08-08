package se.vgregion.service.innovationsslussen.repository.idea;

import se.vgregion.dao.domain.patterns.repository.db.jpa.JpaRepository;
import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;

/**
 * JPA Repository interface for managing {@link Idea}s.
 *
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
public interface JpaIdeaRepository extends JpaRepository<Idea, String, String>,
        IdeaRepository {

}
