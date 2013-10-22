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

import java.util.HashMap;
import java.util.List;

import javax.persistence.Query;

import se.vgregion.dao.domain.patterns.repository.db.jpa.DefaultJpaRepository;
import se.vgregion.portal.innovationsslussen.domain.IdeaStatus;
import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;

/**
 * Implementation of {@link JpaIdeaRepository}.
 *
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
public class JpaIdeaRepositoryImpl extends DefaultJpaRepository<Idea, String> implements JpaIdeaRepository {


    @Override
    public Idea find(String id) {

        Idea idea = null;

        String queryString = ""
                + " SELECT DISTINCT n FROM Idea n"
                + " LEFT JOIN FETCH n.ideaContents"
                + " LEFT JOIN FETCH n.ideaContents ic"
                + " LEFT JOIN FETCH ic.ideaFiles"
                + " LEFT JOIN FETCH n.ideaPersons"
                + " LEFT JOIN FETCH n.likes"
                + " LEFT JOIN FETCH n.favorites"
                + " WHERE n.id = ?1"
                + " ORDER BY n.id ASC";

        Object[] queryObject = new Object[]{id};

        List<Idea> ideas = findByQuery(queryString, queryObject);

        if (ideas.size() > 0) {
            idea = ideas.get(0);
        }

        return idea;
    }

    @Override
    public List<Idea> findAll() {
        Idea idea = null;

        String queryString = ""
                + " SELECT DISTINCT n FROM Idea n"
                + " LEFT JOIN FETCH n.ideaContents"
                + " LEFT JOIN FETCH n.ideaContents ic"
                + " LEFT JOIN FETCH ic.ideaFiles"
                + " LEFT JOIN FETCH n.ideaPersons"
                + " LEFT JOIN FETCH n.likes"
                + " LEFT JOIN FETCH n.favorites"
                + " ORDER BY n.id ASC";

        List<Idea> ideas = (List<Idea>) findByQuery(queryString, new HashMap<String, Object>());

        return ideas;
    }

    @Override
    public Idea findIdeaByUrlTitle(String urlTitle) {
        String queryString = ""
                + " SELECT DISTINCT n FROM Idea n"
                + " LEFT JOIN FETCH n.ideaContents"
                + " LEFT JOIN FETCH n.ideaContents ic"
                + " LEFT JOIN FETCH ic.ideaFiles"
                + " LEFT JOIN FETCH n.ideaPersons"
                + " LEFT JOIN FETCH n.likes"
                + " LEFT JOIN FETCH n.favorites"
                + " WHERE n.urlTitle = ?1"
                + " ORDER BY n.id ASC";

        Object[] queryObject = new Object[]{urlTitle};

        List<Idea> ideas = findByQuery(queryString, queryObject);


        //        System.out.println("monator ideas - " + ideas.size());
        //
        //        for (Idea idea : ideas) {
        //            System.out.println("monator - ideas " + idea.getTitle());
        //            for (IdeaContent ideaContent : idea.getIdeaContents()) {
        //                System.out.println("monator - ideasCo " + ideaContent.getIdeaFiles().size());
        //                for (IdeaFile ideaFile : ideaContent.getIdeaFiles()) {
        //                    System.out.println("monator - ideasCo " + ideaFile.getName());
        //
        //                }
        //            }
        //        }


        if (ideas.size() > 1) {
            throw new IllegalStateException("There shouldn't be more than one idea with the same title.");
            // TODO Erik, vi får diskutera detta. /Patrik
        } else if (ideas.size() == 1) {
            return ideas.get(0);
        } else {
            return null;
        }

    }

    @Override
    public int findIdeasCountByCompanyId(long companyId) {

        String queryString = ""
                + " SELECT COUNT(DISTINCT n) FROM Idea n"
                + " WHERE n.companyId = ?1";

        Object[] queryObject = new Object[]{companyId};

        int count = findCountByQuery(queryString, queryObject);

        return count;
    }

    @Override
    public List<Idea> findIdeasByCompanyId(long companyId) {

        String queryString = ""
                + " SELECT DISTINCT n FROM Idea n"
                + " LEFT JOIN FETCH n.ideaContents"
                + " LEFT JOIN FETCH n.ideaPersons"
                + " LEFT JOIN FETCH n.likes"
                + " LEFT JOIN FETCH n.favorites"
                + " WHERE n.companyId = ?1"
                + " ORDER BY n.id ASC";

        Object[] queryObject = new Object[]{companyId};

        List<Idea> ideas = findByQuery(queryString, queryObject);

        return ideas;
    }

    @Override
    public List<Idea> findIdeasByCompanyId(long companyId, int start, int offset) {

        String queryString = ""
                + " SELECT DISTINCT n FROM Idea n"
                + " LEFT JOIN FETCH n.ideaContents"
                + " LEFT JOIN FETCH n.ideaPersons"
                + " LEFT JOIN FETCH n.likes"
                + " LEFT JOIN FETCH n.favorites"
                + " WHERE n.companyId = ?1"
                + " ORDER BY n.id ASC";

        Object[] queryObject = new Object[]{companyId};

        List<Idea> ideas = findByPagedQuery(queryString, queryObject, start, offset);

        return ideas;
    }

    @Override
    public int findIdeaCountByGroupId(long companyId, long groupId) {

        String queryString = ""
                + " SELECT COUNT(DISTINCT n) FROM Idea n"
                + " WHERE n.companyId = ?1"
                + " AND n.groupId = ?2";

        Object[] queryObject = new Object[]{companyId, groupId};

        int count = findCountByQuery(queryString, queryObject);

        return count;
    }

    @Override
    public List<Idea> findIdeasByGroupId(long companyId, long groupId) {

        String queryString = ""
                + " SELECT DISTINCT n FROM Idea n"
                + " LEFT JOIN FETCH n.ideaContents"
                + " LEFT JOIN FETCH n.ideaPersons"
                + " LEFT JOIN FETCH n.likes"
                + " LEFT JOIN FETCH n.favorites"
                + " WHERE n.companyId = ?1"
                + " AND n.groupId = ?2"
                + " ORDER BY n.created DESC";

        Object[] queryObject = new Object[]{companyId, groupId};

        List<Idea> ideas = findByQuery(queryString, queryObject);

        return ideas;
    }

    @Override
    public List<Idea> findIdeasByGroupId(long companyId, long groupId, int start, int offset) {

        String queryString = ""
                + " SELECT DISTINCT n FROM Idea n"
                + " LEFT JOIN FETCH n.ideaContents"
                + " LEFT JOIN FETCH n.ideaPersons"
                + " LEFT JOIN FETCH n.likes"
                + " LEFT JOIN FETCH n.favorites"
                + " WHERE n.companyId = ?1"
                + " AND n.groupId = ?2"
                + " ORDER BY n.created DESC";

        Object[] queryObject = new Object[]{companyId, groupId};

        List<Idea> ideas = findByPagedQuery(queryString, queryObject, start, offset);


        return ideas;
    }

    @Override
    public int findIdeaCountByGroupId(long companyId, long groupId, IdeaStatus status) {

        String queryString = ""
                + " SELECT COUNT(DISTINCT n) FROM Idea n"
                + " WHERE n.companyId = ?1"
                + " AND n.groupId = ?2"
                + " AND n.status = ?3";

        Object[] queryObject = new Object[]{companyId, groupId, status};

        int count = findCountByQuery(queryString, queryObject);

        return count;
    }

    @Override
    public List<Idea> findIdeasByGroupId(long companyId, long groupId, IdeaStatus status) {

        String queryString = ""
                + " SELECT DISTINCT n FROM Idea n"
                + " LEFT JOIN FETCH n.ideaContents"
                + " LEFT JOIN FETCH n.ideaPersons"
                + " LEFT JOIN FETCH n.likes"
                + " LEFT JOIN FETCH n.favorites"
                + " WHERE n.companyId = ?1"
                + " AND n.groupId = ?2"
                + " AND n.status = ?3"
                + " ORDER BY n.created DESC";

        Object[] queryObject = new Object[]{companyId, groupId, status};

        List<Idea> ideas = findByQuery(queryString, queryObject);

        return ideas;
    }

    @Override
    public List<Idea> findIdeasByGroupId(long companyId, long groupId, IdeaStatus status, int start, int offset) {

        String queryString = ""
                + " SELECT DISTINCT n FROM Idea n"
                + " LEFT JOIN FETCH n.ideaContents"
                + " LEFT JOIN FETCH n.ideaPersons"
                + " LEFT JOIN FETCH n.likes"
                + " LEFT JOIN FETCH n.favorites"
                + " WHERE n.companyId = ?1"
                + " AND n.groupId = ?2"
                + " AND n.status = ?3"
                + " ORDER BY n.created DESC";

        Object[] queryObject = new Object[]{companyId, groupId, status};

        List<Idea> ideas = findByPagedQuery(queryString, queryObject, start, offset);


        return ideas;
    }

    @Override
    public int findIdeasCountByGroupIdAndUserId(long companyId, long groupId, long userId) {

        String queryString = ""
                + " SELECT COUNT(DISTINCT n) FROM Idea n"
                + " WHERE n.companyId = ?1"
                + " AND n.groupId = ?2"
                + " AND n.userId = ?3";

        Object[] queryObject = new Object[]{companyId, groupId, userId};

        int count = findCountByQuery(queryString, queryObject);

        return count;
    }

    @Override
    public List<Idea> findIdeasByGroupIdAndUserId(long companyId, long groupId, long userId) {

        String queryString = ""
                + " SELECT DISTINCT n FROM Idea n"
                + " LEFT JOIN FETCH n.ideaContents"
                + " LEFT JOIN FETCH n.ideaPersons"
                + " LEFT JOIN FETCH n.likes"
                + " LEFT JOIN FETCH n.favorites"
                + " WHERE n.companyId = ?1"
                + " AND n.groupId = ?2"
                + " AND n.userId = ?3"
                + " ORDER BY n.created DESC";

        Object[] queryObject = new Object[]{companyId, groupId, userId};

        List<Idea> ideas = findByQuery(queryString, queryObject);

        return ideas;
    }

    @Override
    public List<Idea> findIdeasByGroupIdAndUserId(long companyId, long groupId, long userId, int start, int offset) {

        String queryString = ""
                + " SELECT DISTINCT n FROM Idea n"
                + " LEFT JOIN FETCH n.ideaContents"
                + " LEFT JOIN FETCH n.ideaPersons"
                + " LEFT JOIN FETCH n.likes"
                + " LEFT JOIN FETCH n.favorites"
                + " WHERE n.companyId = ?1"
                + " AND n.groupId = ?2"
                + " AND n.userId = ?3"
                + " ORDER BY n.created DESC";

        Object[] queryObject = new Object[]{companyId, groupId, userId};

        List<Idea> ideas = findByPagedQuery(queryString, queryObject, start, offset);

        return ideas;
    }

    @Override
    public int findUserFavoritedIdeasCount(long companyId, long groupId, long userId) {

        String queryString = ""
                + " SELECT COUNT(DISTINCT n) FROM Idea n, IdeaUserFavorite f"
                + " WHERE n.companyId = ?1"
                + " AND n.groupId = ?2"
                + " AND f.userId = ?3"
                + " AND f.idea.id = n.id";

        Object[] queryObject = new Object[]{companyId, groupId, userId};

        int count = findCountByQuery(queryString, queryObject);

        return count;
    }

    @Override
    public List<Idea> findUserFavoritedIdeas(long companyId, long groupId, long userId) {

        String queryString = ""
                + " SELECT DISTINCT n FROM Idea n, IdeaUserFavorite f"
                + " LEFT JOIN FETCH n.ideaContents"
                + " LEFT JOIN FETCH n.ideaPersons"
                + " LEFT JOIN FETCH n.likes"
                + " LEFT JOIN FETCH n.favorites"
                + " WHERE n.companyId = ?1"
                + " AND n.groupId = ?2"
                + " AND f.userId = ?3"
                + " AND f.idea.id = n.id"
                + " ORDER BY n.created DESC";

        Object[] queryObject = new Object[]{companyId, groupId, userId};

        List<Idea> ideas = findByQuery(queryString, queryObject);

        return ideas;
    }

    @Override
    public List<Idea> findUserFavoritedIdeas(long companyId, long groupId, long userId, int start, int offset) {

        String queryString = ""
                + " SELECT DISTINCT n FROM Idea n, IdeaUserFavorite f"
                + " LEFT JOIN FETCH n.ideaContents"
                + " LEFT JOIN FETCH n.ideaPersons"
                + " LEFT JOIN FETCH n.likes"
                + " LEFT JOIN FETCH n.favorites"
                + " WHERE n.companyId = ?1"
                + " AND n.groupId = ?2"
                + " AND f.userId = ?3"
                + " AND f.idea.id = n.id"
                + " ORDER BY n.created DESC";

        Object[] queryObject = new Object[]{companyId, groupId, userId};

        List<Idea> ideas = findByPagedQuery(queryString, queryObject, start, offset);

        return ideas;
    }


    @Override
    public void remove(String ideaId) {

        Idea idea = find(ideaId);

        remove(idea);
    }

    private List<Idea> findByPagedQuery(String queryString, Object[] queryObject, int firstResult, int maxResult) {

        Query query = createQuery(queryString, queryObject);

        query.setFirstResult(firstResult);
        query.setMaxResults(maxResult);

        return query.getResultList();
    }

    private int findCountByQuery(String queryString, Object[] queryObject) {
        Query query = createQuery(queryString, queryObject);

        Number result = (Number) query.getSingleResult();

        return result.intValue();
    }

    private Query createQuery(String queryString, Object[] queryObject) {

        Query query = entityManager.createQuery(queryString);
        if (queryObject != null) {
            for (int i = 0; i < queryObject.length; i++) {
                query.setParameter(i + 1, queryObject[i]);
            }
        }

        return query;
    }
}
