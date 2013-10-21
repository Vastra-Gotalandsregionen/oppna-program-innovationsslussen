package se.vgregion.portal.innovationsslussen.usersettings.controller;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import se.vgregion.portal.innovationsslussen.BaseController;
import se.vgregion.service.innovationsslussen.idea.IdeaService;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;

/**
 * Controller class for the view mode in idea portlet.
 *
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
@Controller
@RequestMapping(value = "VIEW")
public class UserSettingsViewController extends BaseController {

    private IdeaService ideaService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserSettingsViewController.class.getName());

    /**
     * Constructor.
     *
     */
    /*
    @Autowired
    public UserSettingsViewController(IdeaService ideaService) {
        this.ideaService = ideaService;
    }
     */

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
        long userId = themeDisplay.getUserId();

        try {
            User user = lookupUser(userId);

            String userFullName = user.getFullName();

            model.addAttribute("userFullName", userFullName);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

        return "view";
    }

    protected User lookupUser(long userId) throws SystemException, PortalException {
        return UserLocalServiceUtil.getUser(userId);
    }

}

