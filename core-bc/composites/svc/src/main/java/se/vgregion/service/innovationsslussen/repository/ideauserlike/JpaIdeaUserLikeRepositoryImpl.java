package se.vgregion.service.innovationsslussen.repository.ideauserlike;

import java.util.List;

import javax.persistence.Query;

import se.vgregion.dao.domain.patterns.repository.db.jpa.DefaultJpaRepository;
import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaUserLike;

/**
 * Implementation of {@link JpaIdeaUserLikeRepository}.
 *
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
public class JpaIdeaUserLikeRepositoryImpl extends DefaultJpaRepository<IdeaUserLike, Long> implements JpaIdeaUserLikeRepository {
	
    @Override
    public List<IdeaUserLike> findLikesByCompanyId(long companyId) {
        String queryString = "SELECT n FROM IdeaUserLike n WHERE n.companyId = ?1 ORDER BY n.id ASC";
        
        Object[] queryObject = new Object[]{companyId};

        List<IdeaUserLike> ideaUserLike = findByQuery(queryString, queryObject);

        return ideaUserLike;
    }

    @Override
    public List<IdeaUserLike> findLikesByGroupId(long companyId, long groupId) {
        String queryString = "SELECT n FROM IdeaUserLike n WHERE n.companyId = ?1 AND n.groupId = ?2 ORDER BY n.id ASC";
        
        System.out.println("JpaIdeaUserLikeRepositoryImpl - findIdeaUserLikesByGroupId");
        
        Object[] queryObject = new Object[]{companyId, groupId};

        List<IdeaUserLike> ideaUserLike = findByQuery(queryString, queryObject);
        
        return ideaUserLike;
    }
    
    @Override
    public List<IdeaUserLike> findLikesByIdeaId(long companyId, long groupId, long ideaId) {
        String queryString = "SELECT n FROM IdeaUserLike n WHERE n.companyId = ?1 AND n.groupId = ?2 AND n.idea.id = ?3  ORDER BY n.id ASC";
        
        System.out.println("JpaIdeaUserLikeRepositoryImpl - findLikesByIdeaId");
        
        Object[] queryObject = new Object[]{companyId, groupId, ideaId};

        List<IdeaUserLike> ideaUserLike = findByQuery(queryString, queryObject);
        
        return ideaUserLike;
    }

    @Override
    public List<IdeaUserLike> findLikesByUserId(long companyId, long groupId, long userId) {
        String queryString = "SELECT n FROM IdeaUserLike n WHERE n.companyId = ?1 AND n.groupId = ?2 AND n.userId = ?3  ORDER BY n.id ASC";
        
        System.out.println("JpaIdeaUserLikeRepositoryImpl - findLikesByIdeaId");
        
        Object[] queryObject = new Object[]{companyId, groupId, userId};

        List<IdeaUserLike> ideaUserLike = findByQuery(queryString, queryObject);
        
        return ideaUserLike;
    }

    @Override
    public List<IdeaUserLike> findLikesByUserAndIdea(long companyId, long groupId, long userId, long ideaId) {
        
    	String queryString = ""
    			+ " SELECT n FROM IdeaUserLike n" 
    			+ " WHERE n.companyId = ?1 AND" 
    			+ " n.groupId = ?2 AND" 
    			+ " n.userId = ?3 AND"
    			+ " n.idea.id = ?4"
    			+ " ORDER BY n.id ASC";
        
        System.out.println("JpaIdeaUserLikeRepositoryImpl - findLikesByUserAndIdea");
        
        Object[] queryObject = new Object[]{companyId, groupId, userId, ideaId};

        List<IdeaUserLike> ideaUserLike = findByQuery(queryString, queryObject);
        
        return ideaUserLike;
    }
    
    @Override
    public void remove(long ideaUserLikeId) {
    	
    	IdeaUserLike ideaUserLike = find(ideaUserLikeId);
    	
    	remove(ideaUserLike);
    	
    }
    
    @Override
    public void removeByUserAndIdea(long companyId, long groupId, long userId, long ideaId) {
    	
    	String queryString = ""
    			+ " DELETE FROM IdeaUserLike n" 
    			+ " WHERE n.companyId = ?1 AND" 
    			+ " n.groupId = ?2 AND" 
    			+ " n.userId = ?3 AND"
    			+ " n.idea.id = ?4";
    	
    	Object[] queryObject = new Object[]{companyId, groupId, userId, ideaId};
    	
        Query query = entityManager.createQuery(queryString);
        if (queryObject != null) {
            for (int i = 0; i < queryObject.length; i++) {
                query.setParameter(i + 1, queryObject[i]);
            }
        }
    	
    	query.executeUpdate();
    }
    

}
