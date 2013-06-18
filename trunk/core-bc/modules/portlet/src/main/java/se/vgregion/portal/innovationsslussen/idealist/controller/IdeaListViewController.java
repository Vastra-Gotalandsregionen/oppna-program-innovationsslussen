package se.vgregion.portal.innovationsslussen.idealist.controller;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;
import se.vgregion.portal.innovationsslussen.util.IdeaPortletsConstants;
import se.vgregion.service.innovationsslussen.IdeaService;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Layout;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;

/**
 * Controller class for the view mode in idealist portlet.
 *
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
@Controller
@RequestMapping(value = "VIEW")
public class IdeaListViewController {

    private static final Logger LOGGER = LoggerFactory.getLogger(IdeaListViewController.class.getName());

    IdeaService ideaService;

    /**
     * Constructor.
     *
     */
    @Autowired
    public IdeaListViewController(IdeaService ideaService) {
        this.ideaService = ideaService;
    }
    

    /**
     * The default render method.
     *
     * @param request  the request
     * @param response the response
     * @param model    the model
     * @return the view
     */
    @RenderMapping()
    public String showIdeaList(RenderRequest request, RenderResponse response, final ModelMap model) {

        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        long scopeGroupId = themeDisplay.getScopeGroupId();
        long companyId = themeDisplay.getCompanyId();
        long userId = themeDisplay.getUserId();
        boolean isSignedIn = themeDisplay.isSignedIn();
        
		try {
			
	        PortletPreferences prefs = request.getPreferences();
	        String ideaListType = prefs.getValue("ideaListType", "0");
	        
			
			Layout ideaLayout = LayoutLocalServiceUtil.getFriendlyURLLayout(scopeGroupId,
					themeDisplay.getLayout().isPrivateLayout(), "/ide");
			
			long ideaPlid = ideaLayout.getPlid();
			
			List<Idea> ideaList = new ArrayList<Idea>();
			
			if(ideaListType.equals(IdeaPortletsConstants.IDEA_LIST_PORTLET_VIEW_OPEN_IDEAS)) {
				// TODO - change to only pull out OPEN ideas
				ideaList = ideaService.findIdeasByGroupId(companyId, scopeGroupId);
			}
			
			else if(ideaListType.equals(IdeaPortletsConstants.IDEA_LIST_PORTLET_VIEW_USER_IDEAS)) {
				ideaList = ideaService.findIdeasByGroupIdAndUserId(companyId, scopeGroupId, userId);
			}
			
			else if(ideaListType.equals(IdeaPortletsConstants.IDEA_LIST_PORTLET_VIEW_USER_FAVORITED_IDEAS)) {
				ideaList = ideaService.findUserFavoritedIdeas(companyId, scopeGroupId, userId);
			}
			
			else if(ideaListType.equals(IdeaPortletsConstants.IDEA_LIST_PORTLET_VIEW_CLOSED_IDEAS)) {
				// TODO - change to only pull out CLOSED ideas
				ideaList = ideaService.findIdeasByGroupId(companyId, scopeGroupId);
			}
			
						
			
			model.addAttribute("ideaPlid", ideaPlid);
			model.addAttribute("ideaPortletName",IdeaPortletsConstants.PORTLET_NAME_IDEA_PORTLET);
			model.addAttribute("ideaList", ideaList);
			model.addAttribute("ideaListType", ideaListType);
			
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
        

        model.addAttribute("foo", "bar");

        return "view";
    }

}

