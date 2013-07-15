package se.vgregion.service.innovationsslussen.repository.idea;

import java.util.List;

import javax.persistence.Query;

import se.vgregion.dao.domain.patterns.repository.db.jpa.DefaultJpaRepository;
import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;

/**
 * Implementation of {@link JpaIdeaRepository}.
 *
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
public class JpaIdeaRepositoryImpl extends DefaultJpaRepository<Idea, Long> implements JpaIdeaRepository {

	
    @Override
    public Idea find(long id) {
    	Idea idea = null;
    	
        String queryString = "" 
        		+ " SELECT DISTINCT n FROM Idea n" 
        		+ " LEFT JOIN FETCH n.ideaContents" 
        		+ " LEFT JOIN FETCH n.ideaPersons"
        		+ " LEFT JOIN FETCH n.likes"
        		+ " LEFT JOIN FETCH n.favorites"
        		+ " WHERE n.id = ?1" 
        		+ " ORDER BY n.id ASC";
        
        Object[] queryObject = new Object[]{id};

        List<Idea> ideas = findByQuery(queryString, queryObject);
        
        if(ideas.size() > 0) {
        	idea = ideas.get(0);
        }

        return idea;
    }
	
	
    @Override
    public Idea findIdeaByUrlTitle(String urlTitle) {
    	Idea idea = null;
    	
        String queryString = "" 
        		+ " SELECT DISTINCT n FROM Idea n" 
        		+ " LEFT JOIN FETCH n.ideaContents" 
        		+ " LEFT JOIN FETCH n.ideaPersons"
        		+ " LEFT JOIN FETCH n.likes"
        		+ " LEFT JOIN FETCH n.favorites"
        		+ " WHERE n.urlTitle = ?1" 
        		+ " ORDER BY n.id ASC";
        
        Object[] queryObject = new Object[]{urlTitle};

        List<Idea> ideas = findByQuery(queryString, queryObject);
        
        if(ideas.size() > 0) {
        	idea = ideas.get(0);
        }

        return idea;
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
        		+ " ORDER BY n.id ASC";
        
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
        		+ " ORDER BY n.id ASC";
        
        Object[] queryObject = new Object[]{companyId, groupId};

        List<Idea> ideas = findByPagedQuery(queryString, queryObject, start, offset);
        

        return ideas;
    }
    
    @Override
    public int findIdeasCountByGroupIdAndUserId(long companyId, long groupId, long userId) {
    	
        String queryString = "" 
        		+ " SELECT COUNT(DISTINCT n) FROM Idea n" 
        		+ " WHERE n.companyId = ?1"
        		+ " AND n.groupId = ?2"
        		+ " AND n.userId = ?3"
        		+ " ORDER BY n.id ASC";
        
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
        		+ " ORDER BY n.id ASC";
        
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
        		+ " ORDER BY n.id ASC";
        
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
        		+ " AND f.idea.id = n.id"
        		+ " ORDER BY n.id ASC";    	
        
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
        		+ " ORDER BY n.id ASC";    	
        
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
        		+ " ORDER BY n.id ASC";    	
        
        Object[] queryObject = new Object[]{companyId, groupId, userId};

        List<Idea> ideas = findByPagedQuery(queryString, queryObject, start, offset);

        return ideas;
    }
    
    
    @Override
    public void remove(long ideaId) {
    	
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
