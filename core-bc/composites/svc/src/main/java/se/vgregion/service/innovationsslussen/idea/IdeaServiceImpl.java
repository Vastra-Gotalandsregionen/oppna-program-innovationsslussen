package se.vgregion.service.innovationsslussen.idea;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import se.vgregion.portal.innovationsslussen.domain.BariumResponse;
import se.vgregion.portal.innovationsslussen.domain.IdeaObjectFields;
import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaContent;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaPerson;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaUserFavorite;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaUserLike;
import se.vgregion.portal.innovationsslussen.domain.vo.CommentItemVO;
import se.vgregion.service.barium.BariumService;
import se.vgregion.service.innovationsslussen.exception.CreateIdeaException;
import se.vgregion.service.innovationsslussen.exception.FavoriteException;
import se.vgregion.service.innovationsslussen.exception.LikeException;
import se.vgregion.service.innovationsslussen.exception.UpdateIdeaException;
import se.vgregion.service.innovationsslussen.idea.settings.IdeaSettingsService;
import se.vgregion.service.innovationsslussen.idea.settings.util.ExpandoConstants;
import se.vgregion.service.innovationsslussen.repository.idea.IdeaRepository;
import se.vgregion.service.innovationsslussen.repository.ideacontent.IdeaContentRepository;
import se.vgregion.service.innovationsslussen.repository.ideaperson.IdeaPersonRepository;
import se.vgregion.service.innovationsslussen.repository.ideauserfavorite.IdeaUserFavoriteRepository;
import se.vgregion.service.innovationsslussen.repository.ideauserlike.IdeaUserLikeRepository;
import se.vgregion.service.innovationsslussen.util.FriendlyURLNormalizer;
import se.vgregion.service.innovationsslussen.util.IdeaServiceConstants;

import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.model.ResourceConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.service.ResourceLocalServiceUtil;
import com.liferay.portal.service.UserGroupRoleLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portlet.messageboards.model.MBMessage;
import com.liferay.portlet.messageboards.model.MBMessageDisplay;
import com.liferay.portlet.messageboards.model.MBThread;
import com.liferay.portlet.messageboards.service.MBMessageLocalServiceUtil;
import com.liferay.portlet.messageboards.util.comparator.MessageCreateDateComparator;

/**
 * Implementation of {@link IdeaService}.
 *
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
public class IdeaServiceImpl implements IdeaService {

    private IdeaRepository ideaRepository;
    private IdeaContentRepository ideaContentRepository;
    private IdeaPersonRepository ideaPersonRepository;
    private IdeaUserLikeRepository ideaUserLikeRepository;
    private IdeaUserFavoriteRepository ideaUserFavoriteRepository;
    private BariumService bariumService;
    private IdeaSettingsService ideaSettingsService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(IdeaServiceImpl.class);
	
	private static final char[] URL_TITLE_REPLACE_CHARS = new char[] {'.', '/'};
	
    /**
     * Constructor.
     *
     * @param ideaRepository the {@link IdeaRepository}
     * @param ideaContentRepository the {@link IdeaContentRepository}
     * @param ideaPersonRepository the {@link ideaPersonRepository}
     * @param bariumService the {@link BariumService}
     */
    @Autowired
    public IdeaServiceImpl(IdeaRepository ideaRepository, IdeaContentRepository ideaContentRepository,
    		IdeaPersonRepository ideaPersonRepository, IdeaUserLikeRepository ideaUserLikeRepository,
    		IdeaUserFavoriteRepository ideaUserFavoriteRepository, BariumService bariumService,
    		IdeaSettingsService ideaSettingsService) {
        this.ideaRepository = ideaRepository;
        this.ideaContentRepository = ideaContentRepository;
        this.ideaPersonRepository = ideaPersonRepository;
        this.ideaUserLikeRepository = ideaUserLikeRepository;
        this.ideaUserFavoriteRepository = ideaUserFavoriteRepository;
        this.bariumService = bariumService;
        this.ideaSettingsService = ideaSettingsService;
    }
    
    @Override
    @Transactional(rollbackFor = FavoriteException.class)
    public void addFavorite(long companyId, long groupId, long userId, String urlTitle) {
    	
    	boolean isIdeaUserFavorite = getIsIdeaUserFavorite(companyId, groupId, userId, urlTitle);
    	
    	if(isIdeaUserFavorite) {
    		// User already has the idea as a favorite
    		return;
    	}
    	
    	Idea idea = findIdeaByUrlTitle(urlTitle);
    	
    	IdeaUserFavorite ideaUserFavorite = new IdeaUserFavorite(companyId, groupId, userId);
    	ideaUserFavorite.setIdea(idea);
    	
    	ideaUserFavorite = ideaUserFavoriteRepository.merge(ideaUserFavorite);
    }
    
    
    @Override
    @Transactional(rollbackFor = LikeException.class)
    public void addLike(long companyId, long groupId, long userId, String urlTitle) {
    	
    	boolean isIdeaUserLiked = getIsIdeaUserLiked(companyId, groupId, userId, urlTitle);
    	
    	if(isIdeaUserLiked) {
    		// User already likes the idea
    		return;
    	}
    	
    	Idea idea = findIdeaByUrlTitle(urlTitle);
    	
    	IdeaUserLike ideaUserLike = new IdeaUserLike(companyId, groupId, userId);
    	ideaUserLike.setIdea(idea);
    	
    	ideaUserLike = ideaUserLikeRepository.merge(ideaUserLike);
    }
    
    @Override
    @Transactional(rollbackFor = CreateIdeaException.class)
    public Idea addIdea(Idea idea) throws CreateIdeaException {
    	
    	// Create Barium Idea
    	//BariumResponse bariumResponse = bariumService.createIdea(idea);
    	BariumResponse bariumResponse = new BariumResponse(true, "MOCKAD-12345666", "");
    	
    	if(bariumResponse.getSuccess()) {
    		
    		String bariumId = bariumResponse.getInstanceId();
    		
        		try {
        			idea.setBariumId(bariumId);
        			
            		idea.setUrlTitle(generateUrlTitle(idea.getTitle()));
        			
            		// Persist idea
            		idea = ideaRepository.merge(idea);
            		
            		// Get references to persisted IdeaContent (public and private)
            		IdeaContent ideaContentPublic = idea.getIdeaContentPublic();
            		IdeaContent ideaContentPrivate = idea.getIdeaContentPrivate();
            		
            		// Get references to persisted IdeaPerson
            		IdeaPerson ideaPerson = idea.getIdeaPerson();
            		
            		boolean addCommunityPermissions = true;
            		boolean addGuestPermissions = true;
            		
            		// Add resource for Idea
            		ResourceLocalServiceUtil.addResources(
            			idea.getCompanyId(), idea.getGroupId(), idea.getUserId(),
            			Idea.class.getName(), idea.getId(), false,
            			addCommunityPermissions, addGuestPermissions);
            		
        			// Add public discussion
        			MBMessageLocalServiceUtil.addDiscussionMessage(
        				idea.getUserId(), String.valueOf(ideaContentPublic.getUserId()), ideaContentPublic.getGroupId(), IdeaContent.class.getName(),
        				ideaContentPublic.getId(), WorkflowConstants.ACTION_PUBLISH);           
        			
        			// Add private discussion
        			MBMessageLocalServiceUtil.addDiscussionMessage(
        				idea.getUserId(), String.valueOf(ideaContentPrivate.getUserId()), ideaContentPrivate.getGroupId(), IdeaContent.class.getName(),
        				ideaContentPrivate.getId(), WorkflowConstants.ACTION_PUBLISH);
        			
            	} catch (SystemException e) {
            		LOGGER.error(e.getMessage(), e);
            		
            		// Delete idea from Barium
            		// Delete message board threads
            		// Set BariumId to blank for idea
            		
            		throw new CreateIdeaException(e);
            	} catch (PortalException e) {

            		LOGGER.error(e.getMessage(), e);
            		
            		// Delete idea from Barium
            		// Delete message board threads
            		// Set BariumId to blank for idea
            		
            		idea.setBariumId("");
            		
            		throw new CreateIdeaException(e);
            	}            		
        		
        	} else {
        		throw new CreateIdeaException("Failed to create idea in Barium, bariumRestClient returned: " + bariumResponse.getJsonString());
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
    public int findIdeasCountByCompanyId(long companyId) {
    	return ideaRepository.findIdeasCountByCompanyId(companyId);
    }

    @Override
    public List<Idea> findIdeasByCompanyId(long companyId) {
        return ideaRepository.findIdeasByCompanyId(companyId);
    }
    
    @Override
    public List<Idea> findIdeasByCompanyId(long companyId, int start, int offset) {
        return ideaRepository.findIdeasByCompanyId(companyId, start, offset);
    }

    @Override
    public int findIdeaCountByGroupId(long companyId, long groupId) {
    	return ideaRepository.findIdeaCountByGroupId(companyId, groupId);
    }
    
    
    @Override
    public List<Idea> findIdeasByGroupId(long companyId, long groupId) {
        return ideaRepository.findIdeasByGroupId(companyId, groupId);
    }
    
    @Override
    public List<Idea> findIdeasByGroupId(long companyId, long groupId, int start, int offset) {
        return ideaRepository.findIdeasByGroupId(companyId, groupId, start, offset);
    }
    
    @Override
    public int findIdeasCountByGroupIdAndUserId(long companyId, long groupId, long userId) {
    	return ideaRepository.findIdeasCountByGroupIdAndUserId(companyId, groupId, userId);
    }
    
    @Override
    public List<Idea> findIdeasByGroupIdAndUserId(long companyId, long groupId, long userId) {
        return ideaRepository.findIdeasByGroupIdAndUserId(companyId, groupId, userId);
    }
    
    @Override
    public List<Idea> findIdeasByGroupIdAndUserId(long companyId, long groupId, long userId, int start, int offset) {
        return ideaRepository.findIdeasByGroupIdAndUserId(companyId, groupId, userId, start, offset);
    }
    
    @Override
    public int findUserFavoritedIdeasCount(long companyId, long groupId, long userId) {
    	return ideaRepository.findUserFavoritedIdeasCount(companyId, groupId, userId);
    }
    
    @Override
    public List<Idea> findUserFavoritedIdeas(long companyId, long groupId, long userId) {
        return ideaRepository.findUserFavoritedIdeas(companyId, groupId, userId);
    }
    
    @Override
    public List<Idea> findUserFavoritedIdeas(long companyId, long groupId, long userId, int start, int offset) {
        return ideaRepository.findUserFavoritedIdeas(companyId, groupId, userId, start, offset);
    }
    
    @Override
    public Idea findIdeaByUrlTitle(String urlTitle) {
    	
    	Idea idea = ideaRepository.findIdeaByUrlTitle(urlTitle);
    	
    	idea.setBariumUrl(getBariumUrl(idea));
    	
        return idea;
    }
    
    @Override
    public Idea findIdeaByUrlTitle(String urlTitle, boolean getBariumUrl) {
    	
    	Idea idea = ideaRepository.findIdeaByUrlTitle(urlTitle);
    	
    	if(getBariumUrl) {
    		idea.setBariumUrl(getBariumUrl(idea));	
    	}
    	
        return idea;
    }
    
    
    @Override
	public List<CommentItemVO> getPublicComments(Idea idea) {
		return getComments(idea.getIdeaContentPublic());
	}
	
    @Override
	public List<CommentItemVO> getPrivateComments(Idea idea) {
		return getComments(idea.getIdeaContentPrivate());
	}
    
    
    @Override
    public boolean getIsIdeaUserFavorite(long companyId, long groupId, long userId, String urlTitle) {
    	boolean isIdeaUserFavorite = false;
    	
    	Idea idea = findIdeaByUrlTitle(urlTitle);
    	long ideaId = idea.getId();
    	
    	List<IdeaUserFavorite> ideaUserFavorites = ideaUserFavoriteRepository.findFavoritesByUserAndIdea(companyId, groupId, userId, ideaId);
    	
    	if(ideaUserFavorites.size() > 0 ) {
    		isIdeaUserFavorite = true;
    	}
    	
    	return isIdeaUserFavorite;
    }
    
    
    @Override
    public boolean getIsIdeaUserLiked(long companyId, long groupId, long userId, String urlTitle) {
    	boolean isIdeaUserLiked = false;
    	
    	Idea idea = findIdeaByUrlTitle(urlTitle);
    	long ideaId = idea.getId();
    	
    	List<IdeaUserLike> ideaUserLikes = ideaUserLikeRepository.findLikesByUserAndIdea(companyId, groupId, userId, ideaId);
    	
    	if(ideaUserLikes.size() > 0 ) {
    		isIdeaUserLiked = true;
    	}
    	
    	return isIdeaUserLiked;
    }
    
    @Override
    @Transactional
    public void remove(long ideaId) {
    	Idea idea = ideaRepository.find(ideaId);
        
    	remove(idea);
    	
    	//ideaRepository.remove(idea);
    }

    @Override
    @Transactional
    public void remove(Idea idea) {
    	
    	System.out.println("IdeaServiceImpl - remove");
    	
    	try {
    		
    		IdeaContent ideaContentPublic = idea.getIdeaContentPublic();
    		IdeaContent ideaContentPrivate = idea.getIdeaContentPrivate();
    		IdeaPerson ideaPerson = idea.getIdeaPerson();
    		
    		// Delete resource for Idea
    		ResourceLocalServiceUtil.deleteResource(
    				idea.getCompanyId(), Idea.class.getName(), ResourceConstants.SCOPE_INDIVIDUAL, idea.getId());
    		
	    	// Delete message board entries
	    	MBMessageLocalServiceUtil.deleteDiscussionMessages(IdeaContent.class.getName(), idea.getIdeaContentPublic().getId());
	    	MBMessageLocalServiceUtil.deleteDiscussionMessages(IdeaContent.class.getName(), idea.getIdeaContentPrivate().getId());
	    	
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
//        Collection<Idea> all = ideaRepository.findAll();
//        for (Idea idea : all) {
//            ideaRepository.remove(idea);
//        }
    }
    
    @Override
    @Transactional
    public void removeFavorite(long companyId, long groupId, long userId, String urlTitle) {
    	
    	Idea idea = ideaRepository.findIdeaByUrlTitle(urlTitle);
    	
    	if(idea != null) {
    		ideaUserFavoriteRepository.removeByUserAndIdea(companyId, groupId, userId, idea.getId());	
    	}
    }
    
    @Override
    @Transactional
    public void removeLike(long companyId, long groupId, long userId, String urlTitle) {
    	
    	Idea idea = ideaRepository.findIdeaByUrlTitle(urlTitle);
    	
    	if(idea != null) {
    		ideaUserLikeRepository.removeByUserAndIdea(companyId, groupId, userId, idea.getId());	
    	}
    }
    
    @Override
    public Idea updateIdea(Idea idea) {
    	
//        idea = ideaRepository.merge(idea);

        return idea;
    }
    
    @Override
    @Transactional(rollbackFor = UpdateIdeaException.class)
    public Idea updateFromBarium(long companyId, long groupId, String urlTitle) throws UpdateIdeaException {
    	
    	System.out.println("IdeaServiceImpl - updateFromBarium");
    	
    	Idea idea = findIdeaByUrlTitle(urlTitle);
    	
    	String bariumId = idea.getBariumId();
    	
    	IdeaObjectFields ideaObjectFields = bariumService.getBariumIdea(bariumId);
    	
    	/*
    	String title = ideaObjectFields.getInstanceName();
    	
    	System.out.println("IdeaServiceImpl - updateFromBarium - title for fetched idea is: " + title);
    	*/
    	
    	return idea;
    }
	
	protected String generateUrlTitle(String title) throws PortalException, SystemException {
		title = title.trim().toLowerCase();

		if (Validator.isNull(title) || Validator.isNumber(title) || title.equals("")) {
			long generatedId = CounterLocalServiceUtil.increment();
			return String.valueOf(generatedId);
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
	
	protected String getBariumUrl(Idea idea) {
		String bariumUrl = "";
		
		/*
        String bariumDetailsViewUrlPrefix =ideaSettingsService.getSetting(
        		ExpandoConstants.BARIUM_DETAILS_VIEW_URL_PREFIX, idea.getCompanyId(), idea.getGroupId());
		
        if(!bariumDetailsViewUrlPrefix.equals("")) {
        	bariumUrl = bariumDetailsViewUrlPrefix + idea.getBariumId();	
        }
        */
		
		return bariumUrl;
	}
	
	protected List<CommentItemVO> getComments(IdeaContent ideaContent) {
		
		ArrayList<CommentItemVO> commentsList = new ArrayList<CommentItemVO>();
		
		try {
			
			MBMessageDisplay messageDisplay = null;
			
			try {
				messageDisplay = MBMessageLocalServiceUtil.getDiscussionMessageDisplay(
						ideaContent.getUserId(), ideaContent.getGroupId(), IdeaContent.class.getName(),
						ideaContent.getId(), WorkflowConstants.STATUS_ANY);
			} catch(NullPointerException e) {
				return commentsList;
			}
			
			Idea idea = ideaContent.getIdea();
			
			MBThread thread = messageDisplay.getThread();
			
			long threadId = thread.getThreadId();
			long rootMessageId = thread.getRootMessageId();
			
			MessageCreateDateComparator messageComparator = new MessageCreateDateComparator(false);
			
			List<MBMessage> mbMessages = MBMessageLocalServiceUtil.getThreadMessages(
					threadId, WorkflowConstants.STATUS_ANY, messageComparator);
			
			for(MBMessage mbMessage : mbMessages) {
				 
				String curCommentText = mbMessage.getBody();
				Date createDate = mbMessage.getCreateDate();
				long commentId = mbMessage.getMessageId();
				
				if(commentId != rootMessageId) {
					long curCommentUserId = mbMessage.getUserId();
					long scopeGroupId = mbMessage.getGroupId();
					User curCommentUser = UserLocalServiceUtil.getUser(curCommentUserId);
					String curCommentUserFullName = curCommentUser.getFullName();
					
					boolean isUserIdeaCreator = isUserIdeaCreator(curCommentUserId, idea);
					boolean isUserPrioCouncilMember = isUserPrioCouncilMember(curCommentUserId, scopeGroupId);
					boolean isUserInnovationsslussenEmployee = isUserInnovationsslussenEmployee(curCommentUserId, scopeGroupId);
					
					CommentItemVO commentItem = new CommentItemVO();
					commentItem.setCommentText(curCommentText);
					commentItem.setCreateDate(createDate);
					commentItem.setId(commentId);
					commentItem.setName(curCommentUserFullName);
					commentItem.setUserCreator(isUserIdeaCreator);
					commentItem.setUserPrioCouncilMember(isUserPrioCouncilMember);
					commentItem.setUserInnovationsslussenEmployee(isUserInnovationsslussenEmployee);

					commentsList.add(commentItem);
				}
			}			
			
		} catch (PortalException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (SystemException e) {
			LOGGER.error(e.getMessage(), e);
		}
		
		return commentsList;
	}
	
	protected boolean isUniqueUrlTitle(String urlTitle) {
		boolean isUnique = false;
		
		Idea idea = findIdeaByUrlTitle(urlTitle, false);
		
		if(idea == null) {
			isUnique = true;
		}
		
		return isUnique;
	}
	
	protected boolean isUserIdeaCreator(long userId, Idea idea) {
		boolean isUserIdeaCreator = false;
		
		if(userId == idea.getUserId()) {
			isUserIdeaCreator = true;
		}
		
		return isUserIdeaCreator;
	}
	
	protected boolean isUserInnovationsslussenEmployee(long userId, long groupId) {
		
		boolean isUserInnovationsslussenEmployee = false;
		
		boolean checkInheritedRoles = true;

		try {
			isUserInnovationsslussenEmployee = UserGroupRoleLocalServiceUtil.hasUserGroupRole(userId, groupId, IdeaServiceConstants.ROLE_NAME_COMMUNITY_INNOVATIONSSLUSSEN, checkInheritedRoles);
		} catch (PortalException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (SystemException e) {
			LOGGER.error(e.getMessage(), e);
		}		
		
		return isUserInnovationsslussenEmployee;

	}
	
	
	protected boolean isUserPrioCouncilMember(long userId, long groupId) {
		
		boolean isUserPrioCouncilMember = false;
		
		boolean checkInheritedRoles = true;

		try {
			isUserPrioCouncilMember = UserGroupRoleLocalServiceUtil.hasUserGroupRole(userId, groupId, IdeaServiceConstants.ROLE_NAME_COMMUNITY_PRIO_COUNCIL, checkInheritedRoles);
		} catch (PortalException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (SystemException e) {
			LOGGER.error(e.getMessage(), e);
		}		
		
		return isUserPrioCouncilMember;

	}
	
	

}
