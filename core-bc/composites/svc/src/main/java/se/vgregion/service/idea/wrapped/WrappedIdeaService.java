package se.vgregion.service.idea.wrapped;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.vgregion.portal.innovationsslussen.domain.IdeaObjectFields;
import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;
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
	
	private BariumService bariumService;
	private IdeaService ideaService;
	
	public WrappedIdeaService(BariumService bariumService, IdeaService ideaService) {
		this.bariumService = bariumService;
		this.ideaService = ideaService;
	}
	
	public void getAllBariumIdeas() {
		
		List<IdeaObjectFields> bariumIdeas = bariumService.getAllIdeas();
		
		LOGGER.info("WrappedIdeaService - getAllBariumIdeas - size is: " + bariumIdeas.size());
		
	}
	
	
}