package se.vgregion.service.innovationsslussen.repository.idea;

import java.util.List;

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
    	
        //String queryString = "SELECT DISTINCT n, (select count(*) from Like ) into foo.countPROPERTY  FROM Idea n LEFT JOIN FETCH n.ideaContents LEFT JOIN FETCH n.ideaPersons WHERE n.id = ?1";
    	
        String queryString = "" 
        		+ " SELECT DISTINCT n FROM Idea n" 
        		+ " LEFT JOIN FETCH n.ideaContents" 
        		+ " LEFT JOIN FETCH n.ideaPersons"
        		+ " LEFT JOIN FETCH n.likes"
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
    public List<Idea> findIdeasByCompanyId(long companyId) {
        String queryString = "SELECT DISTINCT n FROM Idea n LEFT JOIN FETCH n.ideaContents LEFT JOIN FETCH n.ideaPersons WHERE n.companyId = ?1 ORDER BY n.id ASC";
        
        Object[] queryObject = new Object[]{companyId};

        List<Idea> ideas = findByQuery(queryString, queryObject);

        return ideas;
    }

    @Override
    public List<Idea> findIdeasByGroupId(long companyId, long groupId) {
        String queryString = "SELECT DISTINCT n FROM Idea n LEFT JOIN FETCH n.ideaContents LEFT JOIN FETCH n.ideaPersons WHERE n.companyId = ?1 AND n.groupId = ?2 ORDER BY "
                + "n.id ASC";
        
        Object[] queryObject = new Object[]{companyId, groupId};

        List<Idea> ideas = findByQuery(queryString, queryObject);

        return ideas;
    }
    
    @Override
    public void remove(long ideaId) {
    	
    	Idea idea = find(ideaId);
    	
    	remove(idea);
    	
    }

}
