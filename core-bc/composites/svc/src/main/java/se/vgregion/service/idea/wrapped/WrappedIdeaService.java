package se.vgregion.service.idea.wrapped;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.model.ResourceConstants;

import se.vgregion.portal.innovationsslussen.domain.IdeaObjectFields;
import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;
import se.vgregion.portal.innovationsslussen.domain.vo.IdeaVO;
import se.vgregion.service.barium.BariumService;
import se.vgregion.service.idea.IdeaService;

/**
 * Service interface for managing {@link Idea}s.
 *
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
public class WrappedIdeaService {

	private static final Logger LOGGER = LoggerFactory.getLogger(WrappedIdeaService.class);
	
	private static final int IDEA_SHORT_DESCRIPTION_MAX_LENGTH = 100;
	
	private BariumService bariumService;
	private IdeaService ideaService;
	
	public WrappedIdeaService(BariumService bariumService, IdeaService ideaService) {
		this.bariumService = bariumService;
		this.ideaService = ideaService;
	}
	
	public void createIdea(IdeaVO ideaVO) {
		
		System.out.println("WrappedIdeaService - createIdea - should create new idea now!");
		
		String bariumId = ideaVO.getBariumId();
		long userId = ideaVO.getUserId();
		long companyId = ideaVO.getCompanyId();
		long groupId = ideaVO.getGroupId();
		
		// Create idea in Liferay
		ideaService.addIdea(bariumId, userId, companyId, groupId);
		
		//Collection<Idea> allIdeas = ideaService.findAll();
	}
	
	public void deleteIdea(long ideaId) {
		
		//ideaService.
		
	}
	
	public void deleteIdea(Idea idea) {
		
		/*
		// Resources
		resourceLocalService.deleteResource(
			entry.getCompanyId(), LabsEntry.class.getName(),
			ResourceConstants.SCOPE_INDIVIDUAL, entry.getResourcePrimKey());

		// LabsEntries
		labsEntryPersistence.removeByResourcePrimKey(
			entry.getResourcePrimKey());
		
		// Message boards
		mbMessageLocalService.deleteDiscussionMessages(
			LabsEntry.class.getName(), entry.getResourcePrimKey());

		// Social
		socialActivityLocalService.deleteActivities(
			LabsEntry.class.getName(), entry.getResourcePrimKey());

		// Indexer
		Indexer indexer = IndexerRegistryUtil.getIndexer(LabsEntry.class);
		indexer.delete(entry);
		
		// Asset
		assetEntryLocalService.deleteEntry(
			LabsEntry.class.getName(), entry.getEntryId());
		*/
		
		
	}

	public List<IdeaVO> getAllIdeas() {
		
		List<IdeaObjectFields> bariumIdeas = bariumService.getAllIdeas();
		
		List<IdeaVO> ideas = getIdeasFromBariumIdeas(bariumIdeas);
		
		return ideas;
	}
	
	
	private List<IdeaVO> getIdeasFromBariumIdeas(List<IdeaObjectFields> bariumIdeas) {
		
		ArrayList<IdeaVO> ideas = new ArrayList<IdeaVO>();
		
		for(IdeaObjectFields bariumIdea : bariumIdeas) {
			
			
			
			IdeaVO idea = new IdeaVO();
			
			String title = bariumIdea.getInstanceName();
			String descriptionShort = bariumIdea.getIde();
			
			if(descriptionShort.length() > (IDEA_SHORT_DESCRIPTION_MAX_LENGTH-3)) {
				descriptionShort = descriptionShort.substring(0, (IDEA_SHORT_DESCRIPTION_MAX_LENGTH - 3));
				descriptionShort += "...";
			}
			
			int phase = 1;
			
			idea.setTitle(title);
			idea.setDescriptionShort(descriptionShort);
			idea.setPhase(phase);
			
			ideas.add(idea);
		}
		
		return ideas;
	}
	
}