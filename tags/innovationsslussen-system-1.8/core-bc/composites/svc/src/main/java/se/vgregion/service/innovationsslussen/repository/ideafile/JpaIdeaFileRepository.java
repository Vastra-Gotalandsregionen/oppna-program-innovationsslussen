/**
 * Copyright 2010 Västra Götalandsregionen
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of version 2.1 of the GNU Lesser General Public
 *   License as published by the Free Software Foundation.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the
 *   Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *   Boston, MA 02111-1307  USA
 *
 */

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
