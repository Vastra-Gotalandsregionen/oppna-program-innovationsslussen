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

package se.vgregion.service.innovationsslussen.repository.idea;

import java.util.List;

import se.vgregion.dao.domain.patterns.repository.Repository;
import se.vgregion.portal.innovationsslussen.domain.IdeaStatus;
import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;

/**
 * Repository interface for managing {@code Idea}s.
 *
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
public interface IdeaRepository extends Repository<Idea, String> {

    /**
     * Find an {@link Idea} with urlTitle specified.
     *
     * @param id the id
     * @return an {@link Idea}
     */
    @Override
    Idea find(String id);


    /**
     * Find an {@link Idea} with urlTitle specified.
     *
     * @param urlTitle the urlTitle
     * @return an {@link Idea}
     */
    Idea findIdeaByUrlTitle(String urlTitle);

    /**
     * Find the number of {@link Idea}s for a company.
     *
     * @param companyId the companyid
     * @return an int with the number of Idea
     */
    int findIdeasCountByCompanyId(long companyId);


    /**
     * Find all {@link Idea}s for a company.
     *
     * @param companyId the companyid
     * @return a {@link List} of {@link Idea}s
     */
    List<Idea> findIdeasByCompanyId(long companyId);

    /**
     * Find {@link Idea}s for a company.
     *
     * @param companyId the companyid
     * @param start - the start position of the list
     * @param offset - the offset from start
     * @return a {@link List} of {@link Idea}s
     */
    List<Idea> findIdeasByCompanyId(long companyId, int start, int offset);

    /**
     * Find the number of {@link Idea}s for a group in a company.
     *
     * @param companyId the companyid
     * @param groupId   the groupId
     * @return an int with the number of Idea
     */
    int findIdeaCountByGroupId(long companyId, long groupId);

    /**
     * Find all {@link Idea}s for a group in a company.
     *
     * @param companyId the companyid
     * @param groupId   the groupId
     * @return a {@link List} of {@link Idea}s
     */
    List<Idea> findIdeasByGroupId(long companyId, long groupId);

    /**
     * Find {@link Idea}s for a group in a company.
     *
     * @param companyId the companyid
     * @param groupId   the groupId
     * @param start - the start position of the list
     * @param offset - the offset from start
     * @return a {@link List} of {@link Idea}s
     */
    List<Idea> findIdeasByGroupId(long companyId, long groupId, int start, int offset);

    /**
     * Find the number of {@link Idea}s for a group in a company.
     *
     * @param companyId the companyid
     * @param groupId   the groupId
     * @param status   the status of the idea (i.e. whether the idea is public or private)
     * @return an int with the number of Idea
     */
    int findVisibleIdeaCountByGroupId(long companyId, long groupId, IdeaStatus status);

    /**
     * Find all {@link Idea}s for a group in a company.
     *
     * @param companyId the companyid
     * @param groupId   the groupId
     * @param status   the status of the idea (i.e. whether the idea is public or private)
     * @return a {@link List} of {@link Idea}s
     */
    List<Idea> findIdeasByGroupId(long companyId, long groupId, IdeaStatus status);

    /**
     * Find {@link Idea}s for a group in a company.
     *
     * @param companyId the companyid
     * @param groupId   the groupId
     * @param status   the status of the idea (i.e. whether the idea is public or private)
     * @param start - the start position of the list
     * @param offset - the offset from start
     * @return a {@link List} of {@link Idea}s
     */
    List<Idea> findVisibleIdeasByGroupId(long companyId, long groupId, IdeaStatus status, int start, int offset);

    /**
     * Find the number of {@link Idea}s for a user in a group in a company.
     *
     * @param companyId the companyid
     * @param groupId   the groupId
     * @param userId   the userId
     * @return a {@link List} of {@link Idea}s
     */
    int findIdeasCountByGroupIdAndUserId(long companyId, long groupId, long userId);


    /**
     * Find all {@link Idea}s for a user in a group in a company.
     *
     * @param companyId the companyid
     * @param groupId   the groupId
     * @param userId   the userId
     * @return a {@link List} of {@link Idea}s
     */
    List<Idea> findIdeasByGroupIdAndUserId(long companyId, long groupId, long userId);

    /**
     * Find {@link Idea}s for a user in a group in a company.
     *
     * @param companyId the companyid
     * @param groupId   the groupId
     * @param userId   the userId
     * @param start - the start position of the list
     * @param offset - the offset from start
     * @return a {@link List} of {@link Idea}s
     */
    List<Idea> findIdeasByGroupIdAndUserId(long companyId, long groupId, long userId, int start, int offset);

    /**
     * Find the number of {@link Idea}s which a user has added as a favorite.
     *
     * @param companyId the companyid
     * @param groupId   the groupId
     * @param userId   the userId
     * @return a {@link List} of {@link Idea}s
     */
    int findVisibleUserFavoritedIdeasCount(long companyId, long groupId, long userId);


    /**
     * Find all {@link Idea}s which a user has added as a favorite.
     *
     * @param companyId the companyid
     * @param groupId   the groupId
     * @param userId   the userId
     * @return a {@link List} of {@link Idea}s
     */
    List<Idea> findUserFavoritedIdeas(long companyId, long groupId, long userId);

    /**
     * Find {@link Idea}s which a user has added as a favorite.
     *
     * @param companyId the companyid
     * @param groupId   the groupId
     * @param userId   the userId
     * @param start - the start position of the list
     * @param offset - the offset from start
     * @return a {@link List} of {@link Idea}s
     */
    List<Idea> findVisibleUserFavoritedIdeas(long companyId, long groupId, long userId, int start, int offset);


    /**
     * Remove the {@link Idea} with the id.
     *
     * @param ideaId the id of the idea to remove
     */
    @Override
    void remove(String ideaId);


    Idea getReference(String ideaId);

    List<Idea> findVisibleIdeasForIdeaTransporters(long companyId, long groupId, int start, int rows, int sort, int phase, int visible, String transporter);
}
