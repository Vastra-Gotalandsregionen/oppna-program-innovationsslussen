package se.vgregion.portal.innovationsslussen.createidea.controller;

import java.util.HashSet;
import java.util.Set;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaContent;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaPerson;
import se.vgregion.portal.innovationsslussen.util.IdeaPortletUtil;
import se.vgregion.service.innovationsslussen.exception.CreateIdeaException;
import se.vgregion.service.innovationsslussen.idea.IdeaService;
import se.vgregion.service.innovationsslussen.validator.IdeaValidator;

import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;

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
    
    //@Autowired
    private IdeaValidator ideaValidator;

    /**
     * Constructor.
     *
     */
    @Autowired
    public CreateIdeaViewController(IdeaService ideaService, IdeaValidator ideaValidator) {
        this.ideaService = ideaService;
        this.ideaValidator = ideaValidator;
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

        //ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);

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
    public String createIdea(RenderRequest request, RenderResponse response, final ModelMap model, @ModelAttribute Idea idea, BindingResult result) {

        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        boolean isSignedIn = themeDisplay.isSignedIn();
        
        if (model.get("errors") == null) {

        	// No idea exists
        	idea = IdeaPortletUtil.getIdeaFromRequest(request);
        	
            // Set dummy data for person
            if(idea.getIdeaPerson().getName().equals("")) {
                idea.getIdeaPerson().setEmail("erik.andersson@monator.com");
                idea.getIdeaPerson().setJobPosition("Konsult");
                idea.getIdeaPerson().setName("Erik Andersson");
                idea.getIdeaPerson().setPhone("031123456");
                idea.getIdeaPerson().setPhoneMobile("");
                idea.getIdeaPerson().setAdditionalPersonsInfo("");
            }
        } else {
            // Copy binding error from save action
            result.addAllErrors((BindingResult) model.get("errors"));
        }

        model.addAttribute("isSignedIn", isSignedIn);
        model.addAttribute("idea", idea);
        model.addAttribute("ideaClass", Idea.class);

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
    public final void submitIdea(ActionRequest request, ActionResponse response, final ModelMap model, @ModelAttribute Idea idea, BindingResult result) {
    	
    	System.out.println("submitIdea");

        LOGGER.info("submitIdea");

        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);

        idea = IdeaPortletUtil.getIdeaFromRequest(request);
        
        System.out.println("CreateIdeaViewController - submitIdea - idea title is: " + idea.getTitle() );
        
        ideaValidator.validate(idea, result);
        
        if(!result.hasErrors()) {

        	try {
        		idea = ideaService.addIdea(idea);
        		
        		response.setRenderParameter("view", "confirmation");
        		
        	} catch (CreateIdeaException e) {
        		
        		// Add error - create failed
        		
            	PortalUtil.copyRequestParameters(request, response);
            	response.setRenderParameter("view", "view");	
			}
        	
        } else {
        	model.addAttribute("errors", result);
        	
        	PortalUtil.copyRequestParameters(request, response);
        	response.setRenderParameter("view", "view");	
        }
        
        
        


    }

}

