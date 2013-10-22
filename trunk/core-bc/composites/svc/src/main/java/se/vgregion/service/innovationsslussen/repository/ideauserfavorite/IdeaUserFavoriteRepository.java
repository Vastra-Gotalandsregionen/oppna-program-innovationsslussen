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

package se.vgregion.service.innovationsslussen.repository.ideauserfavorite;

import java.util.List;

import se.vgregion.dao.domain.patterns.repository.Repository;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaUserFavorite;

/**
 * Repository interface for managing {@code IdeaUserFavorite}s.
 *
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
public interface IdeaUserFavoriteRepository extends Repository<IdeaUserFavorite, Long> {

    /**
     * Find all {@link IdeaUserFavorite}s for a company.
     *
     * @param companyId the companyid
     * @return a {@link List} of {@link IdeaUserFavorite}s
     */
    List<IdeaUserFavorite> findFavoritesByCompanyId(long companyId);

    /**
     * Find all {@link IdeaUserFavorite}s for a group in a company.
     *
     * @param companyId the companyid
     * @param groupId   the groupId
     * @return a {@link List} of {@link IdeaUserFavorite}s
     */
    List<IdeaUserFavorite> findFavoritesByGroupId(long companyId, long groupId);

    /**
     * Find all {@link IdeaUserFavorite}s for certain idea (in a group in a company).
     *
     * @param companyId the companyid
     * @param groupId   the groupId
     * @param ideaId   the ideaId
     * @return a {@link List} of {@link IdeaUserFavorite}s
     */
    List<IdeaUserFavorite> findFavoritesByIdeaId(long companyId, long groupId, long ideaId);

    /**
     * Find all {@link IdeaUserFavorite}s for certain user (in a group in a company).
     *
     * @param companyId the companyid
     * @param groupId   the groupId
     * @param userId   the userId
     * @return a {@link List} of {@link IdeaUserFavorite}s
     */
    List<IdeaUserFavorite> findFavoritesByUserId(long companyId, long groupId, long userId);

    /**
     * Find all {@link IdeaUserFavorite}s for certain user and a certain idea (in a group in a company).
     *
     * @param companyId the companyid
     * @param groupId   the groupId
     * @param userId   the userId
     * @param ideaId   the ideaId
     * @return a {@link List} of {@link IdeaUserFavorite}s
     */
    List<IdeaUserFavorite> findFavoritesByUserAndIdea(long companyId, long groupId, long userId, String ideaId);

    /**
     * Remove the {@link IdeaUserFavorite} with the id.
     *
     * @param ideaUserFavoriteId the id of the IdeaUserFavorite to remove
     */
    void remove(long ideaUserFavoriteId);


    /**
     * Remove the matching {@link IdeaUserFavorite}.
     *
     * @param companyId the company id
     * @param groupId the group id
     * @param userId the user id
     * @param ideaId the idea id
     */
    void removeByUserAndIdea(long companyId, long groupId, long userId, String ideaId);

}
