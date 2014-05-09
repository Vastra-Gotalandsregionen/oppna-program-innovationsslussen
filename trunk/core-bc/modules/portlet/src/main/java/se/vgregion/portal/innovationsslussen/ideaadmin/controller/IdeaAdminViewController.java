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

package se.vgregion.portal.innovationsslussen.ideaadmin.controller;

import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.search.SearchException;
import java.util.Collection;
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
import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;
import se.vgregion.portal.innovationsslussen.util.IdeaPortletsConstants;
import se.vgregion.service.innovationsslussen.exception.RemoveIdeaException;
import se.vgregion.service.innovationsslussen.exception.UpdateIdeaException;
import se.vgregion.service.innovationsslussen.idea.IdeaService;

import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Layout;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;

/**
 * Controller class for the view mode in the idea admin portlet.
 *
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
@Controller
@RequestMapping(value = "VIEW")
public class IdeaAdminViewController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(IdeaAdminViewController.class.getName());
    private static final int SEARCH_CONTAINER_DELTA_DEFAULT = 10;

    private final IdeaService ideaService;


    /**
     * Instantiates a new idea admin view controller.
     *
     * @param ideaService the idea service
     */
    @Autowired
    public IdeaAdminViewController(IdeaService ideaService) {
        this.ideaService = ideaService;
    }

    protected Layout getFriendlyURLLayout(long scopeGroupId) {
        try {
            return LayoutLocalServiceUtil.getFriendlyURLLayout(scopeGroupId, false, "/ide");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
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

        Layout ideaLayout;

        ideaLayout = getFriendlyURLLayout(scopeGroupId);
        long ideaPlid = ideaLayout.getPlid();

        model.addAttribute("ideaPlid", ideaPlid);
        model.addAttribute("ideaPortletName", IdeaPortletsConstants.PORTLET_NAME_IDEA_PORTLET);

        // Search Container parameters
        int searchCur = ParamUtil.getInteger(request, "cur", 1);
        int searchDelta = ParamUtil.getInteger(
                request, "delta", SEARCH_CONTAINER_DELTA_DEFAULT);

        int totalCount = 0;

        int start = (searchCur - 1) * searchDelta;
        int offset = searchDelta;

        List<Idea> ideas = ideaService.findIdeasByGroupId(companyId, scopeGroupId, start, offset);

        totalCount = ideaService.findIdeaCountByGroupId(companyId, scopeGroupId);

        model.addAttribute("delta", searchDelta);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("ideas", ideas);

        return "view";
    }


    /**
     * Method handling Action request.
     *
     * @param request  the request
     * @param response the response
     * @param model    the model
     */
    @ActionMapping("someAction")
    public final void someAction(ActionRequest request, ActionResponse response, final ModelMap model) {

        response.setRenderParameter("view", "view");
    }

    /**
     * Method handling Action request.
     *
     * @param request  the request
     * @param response the response
     * @param model    the model
     */
    @ActionMapping(params = "action=deleteEntry")
    public final void deleteEntry(ActionRequest request, ActionResponse response, final ModelMap model) {

        String ideaId = ParamUtil.getString(request, "entryId");

        try {
            Idea idea = ideaService.find(ideaId);
            ideaService.remove(idea);
        } catch (RemoveIdeaException e) {
            LOGGER.error(e.getMessage(), e);
            model.addAttribute("errorMessage", e.getMessage());
        }

        response.setRenderParameter("view", "view");
    }

    /**
     * Method handling Action request.
     *
     * @param request  the request
     * @param response the response
     * @param model    the model
     */
    @ActionMapping(params = "action=hideEntry")
    public final void hideEntry(ActionRequest request, ActionResponse response, final ModelMap model) {

        String ideaId = ParamUtil.getString(request, "entryId");

        try {
            ideaService.hide(ideaId);
        } catch (RuntimeException e) {
            LOGGER.error(e.getMessage(), e);
            model.addAttribute("errorMessage", e.getMessage());
        }

        response.setRenderParameter("view", "view");
    }

    /**
     * Method handling Action request.
     *
     * @param request  the request
     * @param response the response
     * @param model    the model
     */
    @ActionMapping(params = "action=unhideEntry")
    public final void unhideEntry(ActionRequest request, ActionResponse response, final ModelMap model) {

        String ideaId = ParamUtil.getString(request, "entryId");

        try {
            ideaService.unhide(ideaId);
        } catch (RuntimeException e) {
            LOGGER.error(e.getMessage(), e);
            model.addAttribute("errorMessage", e.getMessage());
        }

        response.setRenderParameter("view", "view");
    }

    /**
     * Sync all from barium.
     */
    @ActionMapping(params = "action=syncAllFromBarium")
    public void syncAllFromBarium() {
        ideaService.updateAllIdeasFromBarium();
    }

    /**
     * Sync idea from barium.
     *
     * @param request the request
     * @param response the response
     * @param model the model
     */
    @ActionMapping(params = "action=syncIdeaFromBarium")
    public void syncIdeaFromBarium(ActionRequest request, ActionResponse response, final ModelMap model) {

        String ideaId = ParamUtil.getString(request, "entryId");

        try {
            Idea idea = ideaService.find(ideaId);
            ideaService.updateFromBarium(idea);
        } catch (UpdateIdeaException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }


    /**
     * Index all ideas
     *
     * @param request the request
     * @param response the response
     * @param model the model
     */
    @ActionMapping(params = "action=indexAllIdeas")
    public void indexAllIdeas(ActionRequest request, ActionResponse response, final ModelMap model) {

        Collection<Idea> ideas = ideaService.findAll();

        Indexer indexer = IndexerRegistryUtil.getIndexer(IDEA_CLASS);

        for (Idea ideaToIndex : ideas) {
            try {
                indexer.reindex(ideaToIndex);
            } catch (SearchException e) {
                LOGGER.error(e.getMessage(), e);
            } catch (RuntimeException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    private static String IDEA_CLASS = "se.vgregion.portal.innovationsslussen.domain.jpa.Idea";


}

