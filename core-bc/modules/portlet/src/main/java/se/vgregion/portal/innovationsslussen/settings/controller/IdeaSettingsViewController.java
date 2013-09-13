package se.vgregion.portal.innovationsslussen.settings.controller;

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

import se.vgregion.portal.innovationsslussen.BaseController;
import se.vgregion.service.innovationsslussen.idea.settings.IdeaSettingsService;
import se.vgregion.service.innovationsslussen.idea.settings.util.ExpandoConstants;

import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;

/**
 * Controller class for the view mode in the settings portlet.
 *
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
@Controller
@RequestMapping(value = "VIEW")
public class IdeaSettingsViewController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(IdeaSettingsViewController.class.getName());
    
    private IdeaSettingsService ideaSettingsService;
    
    /**
     * Constructor.
     *
     */
    @Autowired
    public IdeaSettingsViewController(IdeaSettingsService ideaSettingsService) {
        this.ideaSettingsService = ideaSettingsService;
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
    public String showIdeaAdmin(RenderRequest request, RenderResponse response, final ModelMap model) {

        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        long scopeGroupId = themeDisplay.getScopeGroupId();
        long companyId = themeDisplay.getCompanyId();
        
        String bariumDetailsViewUrlPrefix =ideaSettingsService.getSetting(
        		ExpandoConstants.BARIUM_DETAILS_VIEW_URL_PREFIX, companyId, scopeGroupId);
        
        model.addAttribute("bariumDetailsViewUrlPrefix", bariumDetailsViewUrlPrefix);

        return "view";
    }
    
    /**
     * This method saves the settings for the settings portlet.
     * 
     * @param request
     *            the request
     * @param response
     *            the response
     */
    @ActionMapping(params = "action=save")
    public void save(ActionRequest request, ActionResponse response) {

        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        long companyId = themeDisplay.getCompanyId();
        long groupId = themeDisplay.getScopeGroup().getGroupId();
        
        String bariumDetailsViewUrlPrefix = ParamUtil.getString(request, "bariumDetailsViewUrlPrefix", "");
        
        ideaSettingsService.setSetting(bariumDetailsViewUrlPrefix, ExpandoConstants.BARIUM_DETAILS_VIEW_URL_PREFIX, companyId, groupId);
    }
    
    

}

