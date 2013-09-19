package se.vgregion.portal.innovationsslussen.createidea.controller;

import java.sql.SQLException;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.Layout;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import se.vgregion.portal.innovationsslussen.BaseController;
import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaPerson;
import se.vgregion.portal.innovationsslussen.util.IdeaPortletUtil;
import se.vgregion.portal.innovationsslussen.util.IdeaPortletsConstants;
import se.vgregion.service.innovationsslussen.exception.CreateIdeaException;
import se.vgregion.service.innovationsslussen.idea.IdeaService;
import se.vgregion.service.innovationsslussen.ldap.LdapService;
import se.vgregion.service.innovationsslussen.ldap.Person;
import se.vgregion.service.innovationsslussen.validator.IdeaValidator;

import com.liferay.portal.kernel.util.WebKeys;
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

    //@Autowired
    private final IdeaValidator ideaValidator;

    private final LdapService ldapService;

    /**
     * Constructor.
     *
     */
    @Autowired
    public CreateIdeaViewController(IdeaService ideaService, IdeaValidator ideaValidator, LdapService ldapService) {
        this.ideaService = ideaService;
        this.ideaValidator = ideaValidator;
        this.ldapService = ldapService;
    }


    /**
     * The render method for the confirmation view
     *
     * @param request  the request
     * @param response the response
     * @param model    the model
     * @return the view
     */
    @RenderMapping(params = "view=confirmation")
    public String showConfirmation(RenderRequest request, RenderResponse response, final ModelMap model) {

        //ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);

        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        long scopeGroupId = themeDisplay.getScopeGroupId();

        Layout ideaLayout = null;
        try {
            ideaLayout = LayoutLocalServiceUtil.getFriendlyURLLayout(scopeGroupId,
                    themeDisplay.getLayout().isPrivateLayout(), "/ide");

            long ideaPlid = ideaLayout.getPlid();
            String ideaLink = request.getParameter("ideaLink");

            model.addAttribute("urlTitle", ideaLink);
            model.addAttribute("ideaPlid", ideaPlid);
            model.addAttribute("ideaPortletName", IdeaPortletsConstants.PORTLET_NAME_IDEA_PORTLET);

        } catch (PortalException e) {
            e.printStackTrace();
        } catch (SystemException e) {
            e.printStackTrace();
        }


        return "confirmation";
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
    public String createIdea(RenderRequest request, RenderResponse response, final ModelMap model, @ModelAttribute Idea idea, BindingResult result) {

        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        boolean isSignedIn = themeDisplay.isSignedIn();

        if (model.get("errors") == null) {

            // No idea exists
            idea = IdeaPortletUtil.getIdeaFromRequest(request);



            // Set dummy data for person
            if(idea.getIdeaPerson().getName().equals("")) {
                String screenName = themeDisplay.getUser().getScreenName();
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
                    ideaPerson.setBirthYear(person.getBirthYear());
                    ideaPerson.setAdministrativeUnit(person.getO());

                    Person.Gender personGender = person.getGender();
                    if (personGender != null) {
                        ideaPerson.setGender(IdeaPerson.Gender.valueOf(personGender.name()));
                    }
                }
            }
        } else {
            // Copy binding error from save action
            result.addAllErrors((BindingResult) model.get("errors"));
        }

        model.addAttribute("isSignedIn", isSignedIn);
        model.addAttribute("idea", idea);
        model.addAttribute("ideaClass", Idea.class);

        return "view";
    }


    /**
     * Method handling Action request.
     *
     * @param request  the request
     * @param response the response
     * @param model    the model
     */
    @ActionMapping("submitIdea")
    public final void submitIdea(ActionRequest request, ActionResponse response, final ModelMap model,
            @ModelAttribute Idea idea, BindingResult result) {

        // todo auktoriseringskontroll?

        LOGGER.info("submitIdea");

        idea = IdeaPortletUtil.getIdeaFromRequest(request);

        System.out.println("CreateIdeaViewController - submitIdea - idea title is: " + idea.getTitle() );

        ideaValidator.validate(idea, result);

        if(!result.hasErrors()) {

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
                        ip.setGender(Person.Gender.MALE.name().equals(gender.name()) ? IdeaPerson.Gender.MALE : IdeaPerson.Gender.FEMALE);
                    }
                }

                idea = ideaService.addIdea(idea, schemeServerNamePort);

                response.setRenderParameter("ideaLink", idea.getUrlTitle());
                response.setRenderParameter("view", "confirmation");

            } catch (CreateIdeaException e) {

                // Add error - create failed

                PortalUtil.copyRequestParameters(request, response);
                response.setRenderParameter("view", "view");
            } catch (RuntimeException e) {
                Throwable lastCause = getLastCause(e);
                if (lastCause instanceof SQLException) {
                    SQLException nextException = ((SQLException) lastCause).getNextException();
                    if (nextException != null) {
                        LOGGER.error(nextException.getMessage(), nextException);
                    }
                }
                result.addError(new ObjectError("", "Ett tekniskt fel inträffade."));
                model.addAttribute("errors", result);

                PortalUtil.copyRequestParameters(request, response);
                response.setRenderParameter("view", "view");
            }

        } else {
            model.addAttribute("errors", result);

            PortalUtil.copyRequestParameters(request, response);
            response.setRenderParameter("view", "view");
        }
    }

    private String generateSchemeServerNamePort(ActionRequest request) {
        HttpServletRequest httpServletRequest = PortalUtil.getHttpServletRequest(request);

        String scheme = httpServletRequest.getScheme();
        String serverName = httpServletRequest.getServerName();
        int serverPort = httpServletRequest.getServerPort();

        String serverPortString;
        if (serverPort == 80 || serverPort == 443) {
            serverPortString = "";
        } else {
            serverPortString = ":" + serverPort;
        }

        return scheme + "://" + serverName + serverPortString;
    }



}

