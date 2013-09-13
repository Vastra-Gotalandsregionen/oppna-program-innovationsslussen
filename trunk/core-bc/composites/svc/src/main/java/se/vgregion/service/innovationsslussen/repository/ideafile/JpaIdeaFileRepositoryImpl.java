package se.vgregion.service.innovationsslussen.repository.ideafile;

import se.vgregion.dao.domain.patterns.repository.db.jpa.DefaultJpaRepository;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaFile;

/**
 * Implementation of {@link JpaIdeaFileRepository}.
 * 
 * @author Simon Göransson
 * @company Monator Technologies AB
 */
public class JpaIdeaFileRepositoryImpl extends DefaultJpaRepository<IdeaFile, Long> implements
        JpaIdeaFileRepository {

}
