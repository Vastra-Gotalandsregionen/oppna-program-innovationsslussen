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

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Layout;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import se.vgregion.portal.innovationsslussen.BaseController;
import se.vgregion.portal.innovationsslussen.domain.IdeaStatus;
import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;
import se.vgregion.portal.innovationsslussen.domain.pageiterator.PageIterator;
import se.vgregion.portal.innovationsslussen.domain.pageiterator.PageIteratorConstants;
import se.vgregion.portal.innovationsslussen.util.IdeaPortletsConstants;
import se.vgregion.service.innovationsslussen.idea.IdeaService;

import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller class for the view mode in idealist portlet.
 *
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
@Controller
@RequestMapping(value = "VIEW")
public class IdeaListViewController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(IdeaListViewController.class.getName());

    private final IdeaService ideaService;


    /**
     * Instantiates a new idea list view controller.
     *
     * @param ideaService the idea service
     */
    @Autowired
    public IdeaListViewController(IdeaService ideaService) {
        this.ideaService = ideaService;
    }


    protected Layout getFriendlyURLLayout(long scopeGroupId, boolean priv) throws SystemException, PortalException {
        return LayoutLocalServiceUtil.getFriendlyURLLayout(scopeGroupId,
                priv, "/ide");
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

        String returnView = "view_open_ideas";

        try {

            PortletPreferences prefs = request.getPreferences();
            String ideaListType = prefs.getValue("ideaListType", "0");
            int entryCount = Integer.valueOf(prefs.getValue("entryCount", "6"));

            Layout ideaLayout = getFriendlyURLLayout(scopeGroupId,
                    themeDisplay.getLayout().isPrivateLayout());

            long ideaPlid = ideaLayout.getPlid();

            List<Idea> ideaList = new ArrayList<Idea>();

            int currentPage = ParamUtil.getInteger(request, "pageNumber",
                    PageIteratorConstants.PAGINATOR_START_DEFAULT);
            int maxPages = PageIteratorConstants.MAX_PAGES_DEFAULT;
            int totalCount = 0;

            int start = (currentPage - 1) * entryCount;

            if (ideaListType.equals(IdeaPortletsConstants.IDEA_LIST_PORTLET_VIEW_OPEN_IDEAS)) {

                ideaList = ideaService.findIdeasByGroupId(companyId,
                        scopeGroupId, IdeaStatus.PUBLIC_IDEA, start, entryCount);

                totalCount = ideaService.findIdeaCountByGroupId(companyId, scopeGroupId, IdeaStatus.PUBLIC_IDEA);
            } else if (ideaListType.equals(IdeaPortletsConstants.IDEA_LIST_PORTLET_VIEW_USER_IDEAS)) {

                if (isSignedIn) {
                    ideaList = ideaService.findIdeasByGroupIdAndUserId(companyId,
                            scopeGroupId, userId, start, entryCount);

                    totalCount = ideaService.findIdeasCountByGroupIdAndUserId(companyId, scopeGroupId, userId);
                }

                returnView = "view_user_ideas";
            } else if (ideaListType.equals(IdeaPortletsConstants.IDEA_LIST_PORTLET_VIEW_USER_FAVORITED_IDEAS)) {

                if (isSignedIn) {
                    ideaList = ideaService.findUserFavoritedIdeas(companyId, scopeGroupId, userId, start, entryCount);

                    totalCount = ideaService.findUserFavoritedIdeasCount(companyId, scopeGroupId, userId);
                }

                returnView = "view_user_favorites";
            } else if (ideaListType.equals(IdeaPortletsConstants.IDEA_LIST_PORTLET_VIEW_CLOSED_IDEAS)) {

                if (isSignedIn) {
                    ideaList = ideaService.findIdeasByGroupId(companyId, scopeGroupId,
                            IdeaStatus.PRIVATE_IDEA, start, entryCount);

                    totalCount = ideaService.findIdeaCountByGroupId(companyId, scopeGroupId, IdeaStatus.PRIVATE_IDEA);
                }

                returnView = "view_closed_ideas";
            }

            PageIterator pageIterator = new PageIterator(totalCount, currentPage, entryCount, maxPages);
            pageIterator.setShowFirst(false);
            pageIterator.setShowLast(false);


            model.addAttribute("ideaPlid", ideaPlid);
            model.addAttribute("ideaPortletName", IdeaPortletsConstants.PORTLET_NAME_IDEA_PORTLET);
            model.addAttribute("ideaList", ideaList);
            model.addAttribute("ideaListType", ideaListType);
            model.addAttribute("isSignedIn", isSignedIn);
            model.addAttribute("pageIterator", pageIterator);

        } catch (PortalException e) {
            e.printStackTrace();
        } catch (SystemException e) {
            e.printStackTrace();
        }

        return returnView;
    }

}