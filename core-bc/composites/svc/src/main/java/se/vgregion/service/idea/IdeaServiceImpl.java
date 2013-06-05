package se.vgregion.service.idea;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import se.vgregion.portal.innovationsslussen.domain.IdeaObjectFields;
import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;
import se.vgregion.service.barium.BariumService;
import se.vgregion.service.idea.exception.CreateIdeaException;
import se.vgregion.service.idea.repository.IdeaRepository;
import se.vgregion.service.idea.util.FriendlyURLNormalizer;

import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.model.ResourceConstants;
import com.liferay.portal.service.ResourceLocalServiceUtil;
import com.liferay.portlet.asset.service.AssetEntryLocalServiceUtil;
import com.liferay.portlet.messageboards.service.MBMessageLocalServiceUtil;

/**
 * Implementation of {@link IdeaService}.
 *
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
public class IdeaServiceImpl implements IdeaService {

	private BariumService bariumService;
    private IdeaRepository ideaRepository;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(IdeaServiceImpl.class);
	
	private static final char[] URL_TITLE_REPLACE_CHARS = new char[] {'.', '/'};
	
    /**
     * Constructor.
     *
     * @param ideaRepository the {@link IdeaRepository}
     */
    @Autowired
    public IdeaServiceImpl(BariumService bariumService, IdeaRepository ideaRepository) {
    	this.bariumService = bariumService;
        this.ideaRepository = ideaRepository;
    }
    
    @Override
    @Transactional(rollbackFor = CreateIdeaException.class)
    public Idea addIdea(Idea idea) {
    	
        // Create Liferay Asset
        // Create Liferay Resource
        // If any of these fails, Listen to Liferay exception and throw CreateIdeaException
        // https://bitbucket.org/martinlau/spring-liferay-integration
    	
        try {
        	
        	String bariumId = bariumService.createIdea(idea);
        	
        	long ideaId = CounterLocalServiceUtil.increment(Idea.class.getName());
        	long resourcePrimKey = CounterLocalServiceUtil.increment();
        	String urlTitle = generateUrlTitle(ideaId, idea.getTitle());
        	
        	idea.setBariumId(bariumId);
        	idea.setId(ideaId);
        	idea.setResourcePrimKey(resourcePrimKey);
        	idea.setUrlTitle(urlTitle);
        	
            idea = ideaRepository.merge(idea);

            /* */
            // Need to specify resource-actions in a file that typically resides in src in a folder called resource-actions. File should be called default.xml
            // The use of these resource actions should be specified in a file called portlet.properties like this:
            // resource.actions.configs=resource-actions/default.xml
            // Add resources
            String[] communityPermissions = new String[0];
            String[] guestPermissions = new String[0];
        	
            ResourceLocalServiceUtil.addModelResources(idea.getCompanyId(), idea.getGroupId(), idea.getUserId(),
            		Idea.class.getName(), idea.getId(), communityPermissions, guestPermissions);
            
            
            // Add asset
            long[] categoryIds = new long [0];
            String[] tagNames = new String [0];
            
            AssetEntryLocalServiceUtil.updateEntry(idea.getUserId(), idea.getGroupId(), Idea.class.getName(),
            		idea.getId(), "", categoryIds, tagNames,
            		true, null, null, null, null,
            		ContentTypes.TEXT_HTML,
            		"", "", "", null, 0, 0, null, false);
            
    		// Message Boards
    		MBMessageLocalServiceUtil.addDiscussionMessage(
    			idea.getUserId(), String.valueOf(idea.getUserId()), idea.getGroupId(), Idea.class.getName(),
    			resourcePrimKey, WorkflowConstants.ACTION_PUBLISH);        
            
        } catch (SystemException e) {
        	LOGGER.error(e.getMessage(), e);
        } catch (PortalException e) {
        	LOGGER.error(e.getMessage(), e);
        }

        return idea;
    }    
    
    @Override
    public Idea find(long ideaId) {
        return ideaRepository.find(ideaId);
    }
    
    
    @Override
    public Collection<Idea> findAll() {
    	
        return ideaRepository.findAll();
    }

    @Override
    public List<Idea> findIdeasByCompanyId(long companyId) {
        return ideaRepository.findIdeasByCompanyId(companyId);
    }

    @Override
    public List<Idea> findIdeasByGroupId(long companyId, long groupId) {
    	
        return ideaRepository.findIdeasByGroupId(companyId, groupId);
    }
    
    @Override
    public Idea findIdeaByUrlTitle(String urlTitle) {
    	
        return ideaRepository.findIdeaByUrlTitle(urlTitle);
    }
    
    @Override
    public List<IdeaObjectFields> getAllBariumIdeas() {
    	return bariumService.getAllIdeas();
    }

    @Override
    @Transactional
    public void remove(long ideaId) {
    	Idea idea = ideaRepository.find(ideaId);
        ideaRepository.remove(idea);
    }

    @Override
    @Transactional
    public void remove(Idea idea) {
    	   	
    	try {
    		/* */
    		// Delete resources
			ResourceLocalServiceUtil.deleteResource(idea.getCompanyId(), Idea.class.getName(),
					ResourceConstants.SCOPE_INDIVIDUAL, idea.getResourcePrimKey());
			
	    	// Delete message board entries
	    	MBMessageLocalServiceUtil.deleteDiscussionMessages(Idea.class.getName(), idea.getResourcePrimKey());
	    	
	    	// Delete Asset
	    	AssetEntryLocalServiceUtil.deleteEntry(Idea.class.getName(), idea.getId());
	    	
	    	// Delete idea
	        ideaRepository.remove(idea);
			
		} catch (PortalException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (SystemException e) {
			LOGGER.error(e.getMessage(), e);
		}
    	
    }
    
    @Override
    @Transactional
    public void removeAll() {
        Collection<Idea> all = ideaRepository.findAll();
        for (Idea idea : all) {
            ideaRepository.remove(idea);
        }
    }
    
	
	protected String generateUrlTitle(long entryId, String title) throws PortalException, SystemException {
		title = title.trim().toLowerCase();

		if (Validator.isNull(title) || Validator.isNumber(title) || title.equals("")) {
			return String.valueOf(entryId);
		}
		else {
			String urlTitle = FriendlyURLNormalizer.normalize(
				title, URL_TITLE_REPLACE_CHARS);

			boolean isUnique = isUniqueUrlTitle(urlTitle);
			
			if(!isUnique) {
				String newUrlTitle = "";
				
				int i = 2;
				while(!isUnique) {
					newUrlTitle = urlTitle + "-" + i;
					isUnique = isUniqueUrlTitle(newUrlTitle);
					i++;
				}
				urlTitle = newUrlTitle;
			}
			
			return urlTitle;
		}
	}
    
	protected boolean isUniqueUrlTitle(String urlTitle) {
		boolean isUnique = false;
		
		Idea idea = findIdeaByUrlTitle(urlTitle);
		
		if(idea == null) {
			isUnique = true;
		}
		
		return isUnique;
	}
	

}
