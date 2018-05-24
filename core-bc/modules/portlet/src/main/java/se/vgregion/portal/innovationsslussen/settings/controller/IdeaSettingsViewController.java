/**
 * Copyright 2010 Västra Götalandsregionen
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of version 2.1 of the GNU Lesser General Public
 *   License as published by the Free Software Foundation.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the
 *   Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *   Boston, MA 02111-1307  USA
 *
 */

package se.vgregion.portal.innovationsslussen.settings.controller;

import java.util.ArrayList;
import java.util.List;
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
 * @author Simon Göransson
 * @company Monator Technologies AB
 */
@Controller
@RequestMapping(value = "VIEW")
public class IdeaSettingsViewController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(IdeaSettingsViewController.class.getName());

    private final IdeaSettingsService ideaSettingsService;

    private  List<String> textExpandos = new ArrayList<String>();
    private  List<String> booleanExpandos = new ArrayList<String>();

    /**
     * Instantiates a new idea settings view controller.
     *
     * @param ideaSettingsService the idea settings service
     */
    @Autowired
    public IdeaSettingsViewController(IdeaSettingsService ideaSettingsService) {
        this.ideaSettingsService = ideaSettingsService;

        textExpandos.add(ExpandoConstants.ADD_THIS_CODE);
        textExpandos.add(ExpandoConstants.BARIUM_DETAILS_VIEW_URL_PREFIX);
        textExpandos.add(ExpandoConstants.FRIENDLY_URL_CREATE_IDEA);
        textExpandos.add(ExpandoConstants.PIWIK_CODE);
        textExpandos.add(ExpandoConstants.NOTIFICATION_EMAIL_FROM);
        textExpandos.add(ExpandoConstants.NOTIFICATION_EMAIL_SUBJECT);
        textExpandos.add(ExpandoConstants.NOTIFICATION_EMAIL_PUBLIC_BODY);
        textExpandos.add(ExpandoConstants.NOTIFICATION_EMAIL_PRIVATE_BODY);
        textExpandos.add(ExpandoConstants.SERVER_NAME_URL);

        booleanExpandos.add(ExpandoConstants.NOTIFICATION_EMAIL_ACTIVE);
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

        for (String expando : textExpandos) {
            String value = ideaSettingsService.getSetting(expando, companyId, scopeGroupId);
            model.addAttribute(expando, value);
        }

        for (String expando : booleanExpandos) {
            boolean value = ideaSettingsService.getSettingBoolean(expando, companyId, scopeGroupId);
            model.addAttribute(expando, value);
        }

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

        for (String expando : textExpandos) {
            String value = ParamUtil.getString(request, expando, "");
            ideaSettingsService.setSetting(value, expando, companyId, groupId);
        }

        for (String expando : booleanExpandos) {
            Boolean value = ParamUtil.getBoolean(request, expando, false);
            ideaSettingsService.setSettingBoolean(value, expando, companyId, groupId);
        }
    }
}

