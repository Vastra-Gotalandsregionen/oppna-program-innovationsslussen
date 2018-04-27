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

package se.vgregion.portal.innovationsslussen.createidea.controller;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;
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
import se.vgregion.service.innovationsslussen.ldap.AdPerson;
import se.vgregion.service.innovationsslussen.ldap.KivPerson;
import se.vgregion.service.innovationsslussen.ldap.LdapService;
import se.vgregion.service.innovationsslussen.validator.IdeaValidator;
import se.vgregion.util.Util;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

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
    private final LdapService kivLdapService;
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
    public CreateIdeaViewController(IdeaService ideaService, IdeaSettingsService ideaSettingsService, IdeaValidator ideaValidator, LdapService ldapService, LdapService kivLdapService, IdeaPermissionCheckerService ideaPermissionCheckerService) {
        this.ideaService = ideaService;
        this.ideaSettingsService = ideaSettingsService;
        this.ideaValidator = ideaValidator;
        this.ldapService = ldapService;
        this.kivLdapService = kivLdapService;
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
                AdPerson adCriteria = new AdPerson();
                adCriteria.setCn(screenName);
                List<AdPerson> findings = ldapService.find(adCriteria);
                if (findings.size() == 1) {
                    AdPerson person = findings.get(0);
                    ideaPerson.setEmail(person.getMail());
                    ideaPerson.setJobPosition(person.getTitle());
                    ideaPerson.setName(person.getDisplayName());
                    ideaPerson.setAdministrativeUnit(person.getDivision());
                    ideaPerson.setPhoneMobile(person.getMobile());
                    ideaPerson.setPhone(person.getTelephoneNumber());
                    ideaPerson.setVgrId(person.getCn());
                    ideaPerson.setName(person.getDisplayName());
                    model.addAttribute("otherUserVgrId", idea.getIdeaPerson().getVgrId());
                }

                populateValuesFromKiv(screenName, ideaPerson);
            }
        } else {
            // Copy binding error from save action
            result.addAllErrors((BindingResult) model.get("errors"));

            if (idea.getIdeaPerson() != null){
                String vgrId = idea.getIdeaPerson().getVgrId();

                if (vgrId != null && !vgrId.isEmpty()){
                    model.addAttribute("otherUserVgrId", vgrId);
                }
            }
        }

        IdeaPermissionChecker ideaPermissionChecker = ideaPermissionCheckerService.getIdeaPermissionChecker(
                scopeGroupId, userId, idea);

        model.addAttribute("isSignedIn", isSignedIn);
        model.addAttribute("idea", idea);
        model.addAttribute("ideaClass", Idea.class);
        model.addAttribute("ideaPermissionChecker", ideaPermissionChecker);

        return "view";
    }

    void populateValuesFromKiv(String screenName, IdeaPerson ideaPerson) {
        List<KivPerson> kivFinds = kivLdapService.find(new KivPerson(screenName));
        if (kivFinds.size() == 1) {
            KivPerson kivPerson = kivFinds.get(0);
            ideaPerson.setGender(kivPerson.getGender());
            ideaPerson.setBirthYear(kivPerson.getBirthYear());
            ideaPerson.setVgrId(screenName);
        }
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

        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);

        // todo auktoriseringskontroll?

        LOGGER.info("submitIdea");

        idea = IdeaPortletUtil.getIdeaFromRequest(request);
        idea.setCreated(new Date());
        idea.setHidden(false);

        ideaValidator.validate(idea, result);

        if (!result.hasErrors()) {

            try {
                String schemeServerNamePort = generateSchemeServerNamePort(request);

                //Populate with extra information about the user (from the ldap if at all).
                AdPerson criteria = new AdPerson();

                String otherUserVgrId = "";

                IdeaPermissionChecker ideaPermissionChecker = ideaPermissionCheckerService.getIdeaPermissionChecker(
                        themeDisplay.getScopeGroupId(), themeDisplay.getUserId(), idea);

                if (ideaPermissionChecker.isHasPermissionCreateIdeaForOtherUser()){
                    otherUserVgrId = idea.getIdeaPerson().getVgrId();
                }

                if (otherUserVgrId != null && !otherUserVgrId.isEmpty()) {
                    criteria.setCn(otherUserVgrId);
                    idea.setOriginalUserId(themeDisplay.getUserId());
                } else {
                    criteria.setCn(themeDisplay.getUser().getScreenName());
                }

                List<AdPerson> findings = ldapService.find(criteria);
                if (findings.size() == 1) {

                    AdPerson person = findings.get(0);
                    IdeaPerson ip = idea.getIdeaPerson();

                    ip.setName(person.getDisplayName());
                    ip.setVgrId(person.getCn());
                    ip.setAdministrativeUnit(person.getDivision());
                    //ip.setBirthYear(person.getBirthYear());

                    ip.setVgrStrukturPerson(person.getFormattedVgrStrukturPerson());
                    //ip.setPhoneMobile(person.getMobile());
                    //ip.setPhone(person.getTelephoneNumber());

                    ip.setVgrId(person.getCn());
                    ip.setName(person.getDisplayName());
                }

                populateValuesFromKiv(idea.getIdeaPerson().getVgrId(), idea.getIdeaPerson());

                idea = ideaService.addIdea(idea, schemeServerNamePort);

                response.setRenderParameter("ideaLink", idea.getUrlTitle());
                response.setRenderParameter("view", "confirmation");

            } catch (CreateIdeaException e) {

                // Add error - create failed

                result.addError(new ObjectError("", "Hoppsan nu gick något fel, vi får inte kontakt med ett " +
                                                    "bakomliggande system. Var god försök igen senare."));
                model.addAttribute("errors", result);
                copyRequestParameters(request, response);
                response.setRenderParameter("view", "view");
                e.printStackTrace();
            } catch (SystemException e) {
                result.addError(new ObjectError("", "Hoppsan nu gick något fel, användaren som du försöker skapa " +
                        "idén för finns inte eller går inte att skapa."));
                e.printStackTrace();
            } catch (RuntimeException e) {
                SQLException nextException = Util.getNextExceptionFromLastCause(e);
                if (nextException != null) {
                    LOGGER.error(nextException.getMessage(), nextException);
                }
                result.addError(new ObjectError("", "Hoppsan nu gick något fel, vi får inte kontakt med ett " +
                                                    "bakomliggande system. Var god försök igen senare."));
                model.addAttribute("errors", result);
                copyRequestParameters(request, response);
                response.setRenderParameter("view", "view");
                e.printStackTrace();
            } catch (PortalException e) {
                result.addError(new ObjectError("", "Hoppsan nu gick något fel, användaren som du försöker skapa " +
                        "idén för finns inte eller går inte att skapa."));
                e.printStackTrace();
            }

        } else {
            model.addAttribute("otherUserVgrId", idea.getIdeaPerson().getVgrId());
            model.addAttribute("errors", result);
            model.addAttribute("idea", idea);

            //copyRequestParameters(request, response);
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

