package se.vgregion.service.innovationsslussen.repository.ideaperson;

import se.vgregion.dao.domain.patterns.repository.db.jpa.JpaRepository;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaPerson;

/**
 * JPA Repository interface for managing {@link IdeaContent}s.
 *
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
public interface JpaIdeaPersonRepository extends JpaRepository<IdeaPerson, Long, Long>,
        IdeaPersonRepository {

}
