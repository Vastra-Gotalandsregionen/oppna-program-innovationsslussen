package se.vgregion.portal.innovationsslussen.idea.controller;

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
import se.vgregion.service.idea.IdeaService;

import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;

/**
 * Controller class for the view mode in idea portlet.
 *
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
@Controller
@RequestMapping(value = "VIEW")
public class IdeaViewController {

	private IdeaService ideaService;
	
    private static final Logger LOGGER = LoggerFactory.getLogger(IdeaViewController.class.getName());

    /**
     * Constructor.
     *
     */
    @Autowired
    public IdeaViewController(IdeaService ideaService) {
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
    public String showIdea(RenderRequest request, RenderResponse response, final ModelMap model) {

        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        long scopeGroupId = themeDisplay.getScopeGroupId();
        long companyId = themeDisplay.getCompanyId();
        boolean isSignedIn = themeDisplay.isSignedIn();
        
        String urlTitle = ParamUtil.getString(request, "urlTitle", "");
        
        System.out.println("IdeaViewController - showIdea - urlTitle is: " + urlTitle);
        
        Idea idea = ideaService.findIdeaByUrlTitle(urlTitle);
        model.addAttribute("idea", idea);

        return "view";
    }

}

