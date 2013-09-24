package se.vgregion.service.innovationsslussen.repository.ideafile;

import se.vgregion.dao.domain.patterns.repository.db.jpa.JpaRepository;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaFile;

/**
 * JPA Repository interface for managing {@link Idea}s.
 * 
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
public interface JpaIdeaFileRepository extends JpaRepository<IdeaFile, Long, Long>, IdeaFileRepository {

}
