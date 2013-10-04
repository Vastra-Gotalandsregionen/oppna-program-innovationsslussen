package se.vgregion.portal.innovationsslussen.createidea.controller;

import java.sql.SQLException;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import se.vgregion.portal.innovationsslussen.BaseController;
import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaPerson;
import se.vgregion.portal.innovationsslussen.util.IdeaPortletUtil;
import se.vgregion.portal.innovationsslussen.util.IdeaPortletsConstants;
import se.vgregion.service.innovationsslussen.exception.CreateIdeaException;
import se.vgregion.service.innovationsslussen.idea.IdeaService;
import se.vgregion.service.innovationsslussen.idea.permission.IdeaPermissionChecker;
import se.vgregion.service.innovationsslussen.idea.permission.IdeaPermissionCheckerService;
import se.vgregion.service.innovationsslussen.idea.settings.IdeaSettingsService;
import se.vgregion.service.innovationsslussen.idea.settings.util.ExpandoConstants;
import se.vgregion.service.innovationsslussen.ldap.LdapService;
import se.vgregion.service.innovationsslussen.ldap.Person;
import se.vgregion.service.innovationsslussen.validator.IdeaValidator;
import se.vgregion.util.Util;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Layout;
import com.liferay.portal.service.LayoutLocalServiceUtil;
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
public class CreateIdeaViewController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateIdeaViewController.class.getName());

    private final IdeaService ideaService;
    private final IdeaSettingsService ideaSettingsService;
    private final IdeaValidator ideaValidator;
    private final LdapService ldapService;
    private final IdeaPermissionCheckerService ideaPermissionCheckerService;


    /**
     * Instantiates a new creates the idea view controller.
     * @param ideaService the idea service
     * @param ideaSettingsService the idea settings service
     * @param ideaValidator the idea validator
     * @param ldapService the ldap service
     * @param ideaPermissionCheckerService
     */
    @Autowired
    public CreateIdeaViewController(IdeaService ideaService, IdeaSettingsService ideaSettingsService, IdeaValidator ideaValidator, LdapService ldapService, IdeaPermissionCheckerService ideaPermissionCheckerService) {
        this.ideaService = ideaService;
        this.ideaSettingsService = ideaSettingsService;
        this.ideaValidator = ideaValidator;
        this.ldapService = ldapService;
        this.ideaPermissionCheckerService = ideaPermissionCheckerService;
    }


    /**
     * The render method for the confirmation view.
     *
     * @param request  the request
     * @param response the response
     * @param model    the model
     * @return the view
     */
    @RenderMapping(params = "view=confirmation")
    public String showConfirmation(RenderRequest request, RenderResponse response, final ModelMap model) {

        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        long companyId = themeDisplay.getCompanyId();
        long scopeGroupId = themeDisplay.getScopeGroupId();

        try {
            String faqFriendlyURL = ideaSettingsService.getSetting(
                    ExpandoConstants.FRIENDLY_URL_FAQ, companyId, scopeGroupId);
            
            Layout faqLayout = getLayout(scopeGroupId, themeDisplay.getLayout().isPrivateLayout(), faqFriendlyURL);
            Layout ideaLayout = getFriendlyURLLayout(scopeGroupId, themeDisplay);

            long faqPlid = faqLayout.getPlid();
            long ideaPlid = ideaLayout.getPlid();
            String ideaLink = request.getParameter("ideaLink");

            model.addAttribute("urlTitle", ideaLink);
            model.addAttribute("faqPlid", faqPlid);
            model.addAttribute("ideaPlid", ideaPlid);
            model.addAttribute("ideaPortletName", IdeaPortletsConstants.PORTLET_NAME_IDEA_PORTLET);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "confirmation";
    }

    protected Layout getFriendlyURLLayout(long scopeGroupId, ThemeDisplay themeDisplay)
            throws SystemException, PortalException {
        return LayoutLocalServiceUtil.getFriendlyURLLayout(scopeGroupId,
                themeDisplay.getLayout().isPrivateLayout(), "/ide");
    }

    /**
     * The default render method.
     *
     * @param request  the request
     * @param response the response
     * @param model    the model
     * @param idea     the idea
     * @param result   the binding results
     * @return the view
     */
    @RenderMapping()
    public String createIdea(RenderRequest request, RenderResponse response, final ModelMap model,
            @ModelAttribute Idea idea, BindingResult result) {

        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        long scopeGroupId = themeDisplay.getScopeGroupId();
        long userId = themeDisplay.getUserId();
        boolean isSignedIn = themeDisplay.isSignedIn();

        if (model.get("errors") == null) {

            // No idea exists
            idea = IdeaPortletUtil.getIdeaFromRequest(request);


            // Set dummy data for person
            if (idea.getIdeaPerson().getName().equals("")) {

                String screenName = "";
                String otherUserVgrId = (String) model.get("otherUserVgrId");
                if (otherUserVgrId != null){
                    if (!otherUserVgrId.equals("")){
                        screenName = otherUserVgrId;

                    }
                } else {
                    screenName = themeDisplay.getUser().getScreenName();
                }


                IdeaPerson ideaPerson = idea.getIdeaPerson();
                Person criteria = new Person();
                criteria.setCn(screenName);
                List<Person> findings = ldapService.find(criteria);
                if (findings.size() == 1) {
                    Person person = findings.get(0);
                    ideaPerson.setEmail(person.getMail());
                    ideaPerson.setJobPosition(person.getTitle());
                    ideaPerson.setName(person.getDisplayName());
                    ideaPerson.setVgrId(person.getVgrId());
                    //  ideaPerson.setBirthYear(person.getBirthYear());
                    ideaPerson.setAdministrativeUnit(person.getO());

                    //   Person.Gender personGender = person.getGender();
                    //    if (personGender != null) {
                    //        ideaPerson.setGender(IdeaPerson.Gender.valueOf(personGender.name()));
                    //    }
                }
            }
        } else {
            // Copy binding error from save action
            result.addAllErrors((BindingResult) model.get("errors"));
        }

        IdeaPermissionChecker ideaPermissionChecker = ideaPermissionCheckerService.getIdeaPermissionChecker(
                scopeGroupId, userId, idea);

        model.addAttribute("isSignedIn", isSignedIn);
        model.addAttribute("idea", idea);
        model.addAttribute("ideaClass", Idea.class);
        model.addAttribute("otherUserVgrId", idea.getIdeaPerson().getVgrId());
        model.addAttribute("ideaPermissionChecker", ideaPermissionChecker);

        return "view";
    }




    @ActionMapping("loadOtherUser")
    public final void loadOtherUser(ActionRequest request, ActionResponse response,
                       @RequestParam("otherUserVgrId") String otherUserVgrId, final ModelMap model) {

        model.addAttribute("otherUserVgrId",otherUserVgrId);
        response.setRenderParameter("view", "view");

    }

    /**
     * Method handling Action request.
     *
     * @param request  the request
     * @param response the response
     * @param model    the model
     * @param idea     the idea
     * @param result   the binding results
     */
    @ActionMapping("submitIdea")
    public final void submitIdea(ActionRequest request, ActionResponse response, final ModelMap model,
                                 @ModelAttribute Idea idea, BindingResult result) {

        // todo auktoriseringskontroll?

        LOGGER.info("submitIdea");

        idea = IdeaPortletUtil.getIdeaFromRequest(request);

        ideaValidator.validate(idea, result);

        if (!result.hasErrors()) {

            try {
                String schemeServerNamePort = generateSchemeServerNamePort(request);

                //Populate with extra information about the user (from the ldap if at all).
                Person criteria = new Person();
                ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
                criteria.setCn(themeDisplay.getUser().getScreenName());
                List<Person> findings = ldapService.find(criteria);
                if (findings.size() == 1) {
                    Person person = findings.get(0);
                    IdeaPerson ip = idea.getIdeaPerson();
                    ip.setVgrId(person.getVgrId());
                    ip.setAdministrativeUnit(person.getO());
                    ip.setBirthYear(person.getBirthYear());
                    ip.setVgrStrukturPerson(person.getVgrStrukturPerson());

                    Person.Gender gender = person.getGender();
                    if (gender != null && !Person.Gender.UNKNOWN.equals(gender)) {
                        ip.setGender(Person.Gender.MALE.name().equals(gender.name())
                                ? IdeaPerson.Gender.MALE : IdeaPerson.Gender.FEMALE);
                    }
                }

                idea = ideaService.addIdea(idea, schemeServerNamePort);

                response.setRenderParameter("ideaLink", idea.getUrlTitle());
                response.setRenderParameter("view", "confirmation");

            } catch (CreateIdeaException e) {

                // Add error - create failed

                copyRequestParameters(request, response);
                System.out.println("Dubbelfel !!!! 1 ");
                response.setRenderParameter("view", "view");
            } catch (RuntimeException e) {
                SQLException nextException = Util.getNextExceptionFromLastCause(e);
                if (nextException != null) {
                    LOGGER.error(nextException.getMessage(), nextException);
                }
                result.addError(new ObjectError("", "Ett tekniskt fel inträffade."));
                model.addAttribute("errors", result);
                System.out.println("Dubbelfel !!!! 2 ");
                copyRequestParameters(request, response);
                response.setRenderParameter("view", "view");
            }

        } else {
            model.addAttribute("errors", result);

            copyRequestParameters(request, response);
            response.setRenderParameter("view", "view");
        }
    }

    protected void copyRequestParameters(ActionRequest request, ActionResponse response) {
        PortalUtil.copyRequestParameters(request, response);
    }

    protected HttpServletRequest toHttpServletRequest(ActionRequest request) {
        return PortalUtil.getHttpServletRequest(request);
    }


    private String generateSchemeServerNamePort(ActionRequest request) {
        HttpServletRequest httpServletRequest = toHttpServletRequest(request);

        String scheme = httpServletRequest.getScheme();
        String serverName = httpServletRequest.getServerName();
        int serverPort = httpServletRequest.getServerPort();

        final int httpport = 80;
        final int httpsport = 443;

        String serverPortString;
        if (serverPort == httpport || serverPort == httpsport) {
            serverPortString = "";
        } else {
            serverPortString = ":" + serverPort;
        }

        return scheme + "://" + serverName + serverPortString;
    }
    
    private Layout getLayout(long groupId, boolean isPrivateLayout, String friendlyURL) {
    	
    	Layout layout = null;
    	
    	try {
			layout = LayoutLocalServiceUtil.getFriendlyURLLayout(groupId, isPrivateLayout, friendlyURL);
		} catch (PortalException e) {
			// Don't do anything (at least not for now)
		} catch (SystemException e) {
			// Don't do anything (at least not for now)
		}
    	
    	return layout;
    }


}

