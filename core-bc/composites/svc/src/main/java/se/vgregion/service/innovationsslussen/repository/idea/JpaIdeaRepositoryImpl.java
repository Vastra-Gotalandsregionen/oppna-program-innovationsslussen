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

import se.vgregion.dao.domain.patterns.repository.db.jpa.DefaultJpaRepository;
import se.vgregion.portal.innovationsslussen.domain.IdeaStatus;
import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

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
    public int findVisibleIdeaCountByGroupId(long companyId, long groupId, IdeaStatus status) {

        String queryString = ""
                + " SELECT COUNT(DISTINCT n) FROM Idea n"
                + " WHERE n.companyId = ?1"
                + " AND n.groupId = ?2"
                + " AND n.status = ?3"
                + " AND n.hidden = false";

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
    public List<Idea> findVisibleIdeasByGroupId(long companyId, long groupId, IdeaStatus status, int start, int offset) {

        String queryString = ""
                + " SELECT DISTINCT n FROM Idea n"
                + " LEFT JOIN FETCH n.ideaContents"
                + " LEFT JOIN FETCH n.ideaPersons"
                + " LEFT JOIN FETCH n.likes"
                + " LEFT JOIN FETCH n.favorites"
                + " WHERE n.companyId = ?1"
                + " AND n.groupId = ?2"
                + " AND n.status = ?3"
                + " AND n.hidden = false"
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
    public int findVisibleUserFavoritedIdeasCount(long companyId, long groupId, long userId) {

        String queryString = ""
                + " SELECT COUNT(DISTINCT n) FROM Idea n, IdeaUserFavorite f"
                + " WHERE n.companyId = ?1"
                + " AND n.groupId = ?2"
                + " AND f.userId = ?3"
                + " AND f.idea.id = n.id"
                + " AND f.idea.hidden = false";

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
    public List<Idea> findVisibleUserFavoritedIdeas(long companyId, long groupId, long userId, int start, int offset) {

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
                + " AND f.idea.hidden = false"
                + " ORDER BY n.created DESC";

        Object[] queryObject = new Object[]{companyId, groupId, userId};

        List<Idea> ideas = findByPagedQuery(queryString, queryObject, start, offset);

        return ideas;
    }

    @Override
    public List<Idea> findVisibleIdeasForIdeaTransporters(long companyId, long groupId, int start, int rows, int sort, int phase, int visible, String transporter) {

        if (!"0".equals(transporter)) {
            String[] split = transporter.split("\\(");
            transporter = split[0].substring(0, split[0].length() - 1);
        }

        List<String> phases = null;
        switch (phase) {
            case 0: //No filter needed.
                break;
            case 1:
                phases = Arrays.asList("0", "1", "2");
                break;
            case 2:
                phases = Arrays.asList("3", "4");
                break;
            case 3:
                phases = Arrays.asList("5", "6");
                break;
        }

        IdeaStatus ideaStatus = null;
        switch (visible) {
            case 0: //No filter needed.
                break;
            case 1:
                ideaStatus = IdeaStatus.PRIVATE_IDEA;
                break;
            case 2:
                ideaStatus = IdeaStatus.PUBLIC_IDEA;
                break;
        }

        int n = 3;
        String statusCondition = "";
        if (ideaStatus != null) {
            statusCondition = " AND n.status = ?" + n++;
        }

        String phasesCondition = "";
        if (phases != null && phases.size() > 0) {
            phasesCondition = " AND n.phase IN (?" + n++ + ")";
        }

        String queryString = ""
                + " SELECT DISTINCT n FROM Idea n"
                + " LEFT JOIN FETCH n.ideaContents"
                + " LEFT JOIN FETCH n.ideaPersons"
                + " LEFT JOIN FETCH n.likes"
                + " LEFT JOIN FETCH n.favorites"
                + " WHERE n.companyId = ?1"
                + " AND n.groupId = ?2"
                + statusCondition
                + phasesCondition
                + " AND n.hidden = false";

        List<Object> queryObjects = new ArrayList<>();
        queryObjects.add(companyId);
        queryObjects.add(groupId);
        if (ideaStatus != null) {
            queryObjects.add(ideaStatus);
        }
        if (phases != null && phases.size() > 0) {
            queryObjects.add(phases);
        }

        Object[] queryObject = queryObjects.toArray();// = new Object[]{companyId, groupId, ideaStatus};

        List<Idea> ideas = findByPagedQuery(queryString, queryObject, 0, Integer.MAX_VALUE);

        Iterator<Idea> iterator = ideas.iterator();

        if (!"0".equals(transporter)) {
            while(iterator.hasNext()) {
                Idea next = iterator.next();
                if (!transporter.equals(next.getIdeaContentPrivate().getIdeTansportor())) {
                    iterator.remove();
                }
            }
        }

        switch (sort) {
            case 0:
                Collections.sort(ideas, new Comparator<Idea>() {
                    @Override
                    public int compare(Idea o1, Idea o2) {
                        return -o1.getCreated().compareTo(o2.getCreated());
                    }
                });
                break;
            case 1:
                Collections.sort(ideas, new Comparator<Idea>() {
                    @Override
                    public int compare(Idea o1, Idea o2) {
                        int c1 = o1.getCommentsCount() != null ? o1.getCommentsCount() : 0;
                        int c2 = o2.getCommentsCount() != null ? o2.getCommentsCount() : 0;

                        if (c1 == c2) {
                            return 0;
                        }

                        return c1 < c2 ? 1 : -1;
                    }
                });
                break;
            case 2:
                Collections.sort(ideas, new Comparator<Idea>() {
                    @Override
                    public int compare(Idea o1, Idea o2) {
                        Date d1 = o1.getLastPrivateCommentDate() != null ? o1.getLastPrivateCommentDate() : new Date(0);
                        Date d2 = o2.getLastPrivateCommentDate() != null ? o2.getLastPrivateCommentDate() : new Date(0);
                        return -d1.compareTo(d2);
                    }
                });
                break;
            case 3:
//                ideaSolrQuery.addSortField(IdeaField.PUBLIC_LIKES_COUNT, SolrQuery.ORDER.desc);
                break;
        }

        ideas = ideas.subList(start, start + Math.min(rows, ideas.size() - start));

        return ideas;
    }

    @Override
    public void remove(String ideaId) {
        Idea reference = entityManager.getReference(Idea.class, ideaId);
        remove(reference);
    }

    @Override
    public void remove(Idea idea){
        Idea reference = entityManager.getReference(Idea.class, idea.getId());
        entityManager.remove(reference);
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

    @Override
    public Idea getReference(String ideaId) {
        return entityManager.getReference(Idea.class, ideaId);
    }
}
