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

package se.vgregion.service.innovationsslussen.repository.ideauserlike;

import java.util.List;

import se.vgregion.dao.domain.patterns.repository.Repository;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaUserLike;

/**
 * Repository interface for managing {@code IdeaUserLike}s.
 *
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
public interface IdeaUserLikeRepository extends Repository<IdeaUserLike, Long> {

    /**
     * Find all {@link IdeaUserLike}s for a company.
     *
     * @param companyId the companyid
     * @return a {@link List} of {@link IdeaUserLike}s
     */
    List<IdeaUserLike> findLikesByCompanyId(long companyId);

    /**
     * Find all {@link IdeaUserLike}s for a group in a company.
     *
     * @param companyId the companyid
     * @param groupId   the groupId
     * @return a {@link List} of {@link IdeaUserLike}s
     */
    List<IdeaUserLike> findLikesByGroupId(long companyId, long groupId);

    /**
     * Find all {@link IdeaUserLike}s for certain idea (in a group in a company).
     *
     * @param companyId the companyid
     * @param groupId   the groupId
     * @param ideaId   the ideaId
     * @return a {@link List} of {@link IdeaUserLike}s
     */
    List<IdeaUserLike> findLikesByIdeaId(long companyId, long groupId, long ideaId);

    /**
     * Find all {@link IdeaUserLike}s for certain user (in a group in a company).
     *
     * @param companyId the companyid
     * @param groupId   the groupId
     * @param userId   the userId
     * @return a {@link List} of {@link IdeaUserLike}s
     */
    List<IdeaUserLike> findLikesByUserId(long companyId, long groupId, long userId);

    /**
     * Find all {@link IdeaUserLike}s for certain user and a certain idea (in a group in a company).
     *
     * @param companyId the companyid
     * @param groupId   the groupId
     * @param userId   the userId
     * @param ideaId   the ideaId
     * @return a {@link List} of {@link IdeaUserLike}s
     */
    List<IdeaUserLike> findLikesByUserAndIdea(long companyId, long groupId, long userId, String ideaId);

    /**
     * Remove the {@link IdeaUserLike} with the id.
     *
     * @param ideaUserLikeId the id of the IdeaUserLike to remove
     */
    void remove(long ideaUserLikeId);


    /**
     * Remove the {@link IdeaUserLike} with the id.
     *
     * @param companyId the company id
     * @param groupId the group id
     * @param userId the user id
     * @param ideaId the idea id
     */
    void removeByUserAndIdea(long companyId, long groupId, long userId, String ideaId);

}
