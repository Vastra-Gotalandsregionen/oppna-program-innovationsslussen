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

    /** The idea service. */
    private final IdeaService ideaService;


    /**
     * Instantiates a new idea list edit controller.
     *
     * @param ideaService the idea service
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

        PortletPreferences prefs = request.getPreferences();
        String ideaListType = prefs.getValue("ideaListType", "0");
        String entryCount = prefs.getValue("entryCount", "6");
        model.addAttribute("ideaListType", ideaListType);
        model.addAttribute("entryCount", entryCount);

        return "edit";
    }

    /**
     * Save preferences.
     *
     * @param request the request
     * @param entryCount the number of entry to display
     * @param ideaListType the idea list type
     */
    @ActionMapping(params = "action=save")
    public void savePreferences(ActionRequest request,
            @RequestParam("entryCount") String  entryCount,
            @RequestParam("ideaListType") String ideaListType) {

        try {
            PortletPreferences prefs = request.getPreferences();
            prefs.setValue("ideaListType", ideaListType);
            prefs.setValue("entryCount", entryCount);
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

