package se.vgregion.portal.innovationsslussen.idealist.controller;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.PortletPreferences;
import javax.portlet.ReadOnlyException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ValidatorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import se.vgregion.portal.innovationsslussen.BaseController;
import se.vgregion.service.innovationsslussen.idea.IdeaService;

import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;

/**
 * Controller class for the edit mode in idealist portlet.
 *
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
@Controller
@RequestMapping(value = "EDIT")
public class IdeaListEditController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(IdeaListEditController.class.getName());

    IdeaService ideaService;

    /**
     * Constructor.
     *
     */
    @Autowired
    public IdeaListEditController(IdeaService ideaService) {
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
    @RenderMapping
    public String showEdit(RenderRequest request, RenderResponse response, final ModelMap model) {

        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        long scopeGroupId = themeDisplay.getScopeGroupId();
        long companyId = themeDisplay.getCompanyId();
        boolean isSignedIn = themeDisplay.isSignedIn();
        
        PortletPreferences prefs = request.getPreferences();
        
        String ideaListType = prefs.getValue("ideaListType", "0");

        model.addAttribute("ideaListType", ideaListType);

        return "edit";
    }
    
    /**
     * Store the tags from the posted form.
     * 
     * @param request
     *            the request
     *            the tags entries
     */
    @ActionMapping(params = "action=save")
    public void savePreferences(ActionRequest request,
            @RequestParam("ideaListType") String ideaListType) {

        try {
            PortletPreferences prefs = request.getPreferences();
            prefs.setValue("ideaListType", ideaListType);
            prefs.store();
        } catch (ReadOnlyException e) {
            LOGGER.error("could not store preferences in edit mode.", e);
        } catch (ValidatorException e) {
        	LOGGER.error("could not store preferences in edit mode.", e);
        } catch (IOException e) {
        	LOGGER.error("could not store preferences in edit mode.", e);
        }
    }    

}

