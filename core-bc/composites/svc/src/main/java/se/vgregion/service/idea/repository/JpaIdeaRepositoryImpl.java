package se.vgregion.service.idea.repository;

import se.vgregion.dao.domain.patterns.repository.db.jpa.DefaultJpaRepository;
import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;

import java.util.List;

/**
 * Implementation of {@link JpaIdeaRepository}.
 *
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
public class JpaIdeaRepositoryImpl extends DefaultJpaRepository<Idea, Long>
        implements JpaIdeaRepository {
	
    @Override
    public Idea findIdeaByUrlTitle(String urlTitle) {
    	Idea idea = null;
    	
        String queryString = "SELECT n FROM Idea n WHERE n.urlTitle = ?1 ORDER BY n.id ASC";
        
        Object[] queryObject = new Object[]{urlTitle};

        List<Idea> ideas = findByQuery(queryString, queryObject);
        
        if(ideas.size() > 0) {
        	idea = ideas.get(0);
        }

        return idea;
    }
	

    @Override
    public List<Idea> findIdeasByCompanyId(long companyId) {
        String queryString = "SELECT n FROM Idea n WHERE n.companyId = ?1 ORDER BY n.id ASC";
        
        Object[] queryObject = new Object[]{companyId};

        List<Idea> ideas = findByQuery(queryString, queryObject);

        return ideas;
    }

    @Override
    public List<Idea> findIdeasByGroupId(long companyId, long groupId) {
        String queryString = "SELECT n FROM Idea n WHERE n.companyId = ?1 AND n.groupId = ?2 ORDER BY "
                + "n.id ASC";
        
        System.out.println("JpaIdeaRepositoryImpl - findIdeasByGroupId");
        
        Object[] queryObject = new Object[]{companyId, groupId};

        List<Idea> ideas = findByQuery(queryString, queryObject);
        
        System.out.println("JpaIdeaRepositoryImpl - findIdeasByGroupId ideas.size is: " + ideas.size() + " (companyId is: " + companyId + " and groupId is: " + groupId + ")");

        return ideas;
    }

}
