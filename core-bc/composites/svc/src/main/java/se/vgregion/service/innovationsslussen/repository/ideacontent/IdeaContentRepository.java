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

package se.vgregion.service.innovationsslussen.repository.ideacontent;

import java.util.List;

import se.vgregion.dao.domain.patterns.repository.Repository;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaContent;

/**
 * Repository interface for managing {@code se.vgregion.portal.innovationsslussen.domain.jpa.Idea}s.
 *
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
public interface IdeaContentRepository extends Repository<IdeaContent, Long> {

    /**
     * Find all {@link IdeaContent}s for a company.
     *
     * @param companyId the companyid
     * @return a {@link List} of {@link Idea}s
     */
    List<IdeaContent> findIdeaContentsByCompanyId(long companyId);

    /**
     * Find all {@link IdeaContent}s for a group in a company.
     *
     * @param companyId the companyid
     * @param groupId   the groupId
     * @return a {@link List} of {@link IdeaContent}s
     */
    List<IdeaContent> findIdeaContentsByGroupId(long companyId, long groupId);
}
