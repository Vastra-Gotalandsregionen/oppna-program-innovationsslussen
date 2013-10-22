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

import se.vgregion.dao.domain.patterns.repository.db.jpa.DefaultJpaRepository;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaContent;

/**
 * Implementation of {@link JpaIdeaContentRepository}.
 *
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
public class JpaIdeaContentRepositoryImpl extends DefaultJpaRepository<IdeaContent, Long>
implements JpaIdeaContentRepository {

    @Override
    public List<IdeaContent> findIdeaContentsByCompanyId(long companyId) {
        String queryString = "SELECT n FROM Idea n WHERE n.companyId = ?1 ORDER BY n.id ASC";

        Object[] queryObject = new Object[]{companyId};

        List<IdeaContent> ideaContents = findByQuery(queryString, queryObject);

        return ideaContents;
    }

    @Override
    public List<IdeaContent> findIdeaContentsByGroupId(long companyId, long groupId) {
        String queryString = "SELECT n FROM Idea n WHERE n.companyId = ?1 AND n.groupId = ?2 ORDER BY "
                + "n.id ASC";

        Object[] queryObject = new Object[]{companyId, groupId};

        List<IdeaContent> ideaContents = findByQuery(queryString, queryObject);

        return ideaContents;
    }

}
