package se.vgregion.portal.innovationsslussen.ideaadmin.controller;

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
import se.vgregion.portal.innovationsslussen.domain.IdeaStatus;
import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;
import se.vgregion.portal.innovationsslussen.util.IdeaPortletsConstants;
import se.vgregion.service.innovationsslussen.exception.RemoveIdeaException;
import se.vgregion.service.innovationsslussen.exception.UpdateIdeaException;
import se.vgregion.service.innovationsslussen.idea.IdeaService;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
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

    IdeaService ideaService;

    /**
     * Constructor.
     */
    @Autowired
    public IdeaAdminViewController(IdeaService ideaService) {
        this.ideaService = ideaService;
    }

    protected Layout getFriendlyURLLayout(long scopeGroupId) {
        try {
            return LayoutLocalServiceUtil.getFriendlyURLLayout(scopeGroupId, false, "/ide");
        } catch (PortalException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (SystemException e) {
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

        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        /*long companyId = themeDisplay.getCompanyId();
        long groupId = themeDisplay.getScopeGroupId();
        long userId = themeDisplay.getUserId();*/

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

        model.addAttribute("hasErrorMessage", false);

        try {
            ideaService.remove(ideaId);
        } catch (RemoveIdeaException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }

        response.setRenderParameter("view", "view");
    }

    @ActionMapping(params = "action=syncAllFromBarium")
    public void syncAllFromBarium() {
        ideaService.updateAllIdeasFromBarium();
    }

    @ActionMapping(params = "action=syncIdeaFromBarium")
    public void syncIdeaFromBarium(ActionRequest request, ActionResponse response, final ModelMap model) {

        String ideaId = ParamUtil.getString(request, "entryId");

        try {
            ideaService.updateFromBarium(ideaId);
        } catch (UpdateIdeaException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }


}

