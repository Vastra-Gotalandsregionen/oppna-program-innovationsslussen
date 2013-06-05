package se.vgregion.portal.innovationsslussen.createidea.controller;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;
import se.vgregion.service.idea.IdeaService;

import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;

/**
 * Controller class for the view mode in the create idea portlet.
 *
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
@Controller
@RequestMapping(value = "VIEW")
public class CreateIdeaViewController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateIdeaViewController.class.getName());
    
    private IdeaService ideaService;

    /**
     * Constructor.
     *
     */
    @Autowired
    public CreateIdeaViewController(IdeaService ideaService) {
        this.ideaService = ideaService;
    }


    /**
     * The render method for the confirmation view
     *
     * @param request  the request
     * @param response the response
     * @param model    the model
     * @return the view
     */
    @RenderMapping(params = "view=confirmation")
    public String showConfirmation(RenderRequest request, RenderResponse response, final ModelMap model) {

        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        long scopeGroupId = themeDisplay.getScopeGroupId();
        long companyId = themeDisplay.getCompanyId();
        boolean isSignedIn = themeDisplay.isSignedIn();

        model.addAttribute("foo", "bar");

        return "confirmation";
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

        model.addAttribute("isSignedIn", isSignedIn);

        return "view";
    }
    
    
    /**
     * Method handling Action request.
     *
     * @param request  the request
     * @param response the response
     * @param model    the model
     */
    @ActionMapping("submitIdea")
    public final void submitIdea(ActionRequest request, ActionResponse response, final ModelMap model) {
    	
    	System.out.println("submitIdea");

        LOGGER.info("submitIdea");

        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        long companyId = themeDisplay.getCompanyId();
        long groupId = themeDisplay.getScopeGroupId();
        long userId = themeDisplay.getUserId();
        
        String title = ParamUtil.getString(request, "title", "");
        String description = ParamUtil.getString(request, "description", "");
        String solvesProblem = ParamUtil.getString(request, "solvesProblem", "");
        String wantsHelpWith = ParamUtil.getString(request, "wantsHelpWith", "");
        String name = ParamUtil.getString(request, "name", "");
        String email = ParamUtil.getString(request, "email", "");
        String phone = ParamUtil.getString(request, "phone", "");
        String administrativeUnit = ParamUtil.getString(request, "administrativeUnit", "");
        String jobPosition = ParamUtil.getString(request, "jobPosition", "");
        
        String vgrId = "none";
        
        Idea idea = new Idea(companyId, groupId, userId, description, solvesProblem, title, wantsHelpWith, vgrId, name, email, phone, administrativeUnit, jobPosition);
        
        ideaService.addIdea(idea);

        response.setRenderParameter("view", "confirmation");


    }
    

}

