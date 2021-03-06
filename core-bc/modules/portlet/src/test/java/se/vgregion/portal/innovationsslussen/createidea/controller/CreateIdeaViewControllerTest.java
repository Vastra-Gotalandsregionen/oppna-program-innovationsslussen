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
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;
import se.vgregion.service.innovationsslussen.idea.IdeaService;
import se.vgregion.service.innovationsslussen.idea.permission.IdeaPermissionCheckerService;
import se.vgregion.service.innovationsslussen.idea.settings.IdeaSettingsService;
import se.vgregion.service.innovationsslussen.ldap.AdPerson;
import se.vgregion.service.innovationsslussen.ldap.KivPerson;
import se.vgregion.service.innovationsslussen.ldap.LdapService;
import se.vgregion.service.innovationsslussen.validator.IdeaValidator;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: portaldev
 * Date: 2013-09-24
 * Time: 09:45
 * To change this template use File | Settings | File Templates.
 */
public class CreateIdeaViewControllerTest {

    CreateIdeaViewController controller;
    private IdeaService ideaService;
    private IdeaSettingsService ideaSettingsService;
    private IdeaValidator ideaValidator;
    private LdapService ldapService;
    private LdapService kivLdapService;
    private IdeaPermissionCheckerService ideaPermissionCheckerService;
    private RenderRequest renderRequest;
    private RenderResponse renderResponse;
    private ModelMap model;

    /*

    ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        long scopeGroupId = themeDisplay.getScopeGroupId();
     */

    @Before
    public void setUp() {
        ideaService = Mockito.mock(IdeaService.class);
        ideaSettingsService = Mockito.mock(IdeaSettingsService.class);
        ideaValidator = Mockito.mock(IdeaValidator.class);
        ldapService = Mockito.mock(LdapService.class);
        kivLdapService = Mockito.mock(LdapService.class);
        ideaPermissionCheckerService = Mockito.mock(IdeaPermissionCheckerService.class);


        renderRequest = Mockito.mock(RenderRequest.class);
        renderResponse = Mockito.mock(RenderResponse.class);
        model = Mockito.mock(ModelMap.class);

        ThemeDisplay themeDisplay = Mockito.mock(ThemeDisplay.class);
        Mockito.when(renderRequest.getAttribute(WebKeys.THEME_DISPLAY)).thenReturn(themeDisplay);
        Mockito.when(themeDisplay.getScopeGroupId()).thenReturn(1l);
        User user = Mockito.mock(User.class);
        Mockito.when(themeDisplay.getUser()).thenReturn(user);

        List<AdPerson> persons = new ArrayList<AdPerson>();
        persons.add(new AdPerson());
        Mockito.when(ldapService.find(Mockito.any(AdPerson.class))).thenReturn(persons);

        List<KivPerson> kivPersons = new ArrayList<KivPerson>();
        kivPersons.add(new KivPerson());
        Mockito.when(kivLdapService.find(Mockito.any(KivPerson.class))).thenReturn(kivPersons);

        controller = new CreateIdeaViewController(ideaService, ideaSettingsService, ideaValidator, ldapService, kivLdapService, ideaPermissionCheckerService) {
            @Override
            protected Layout getFriendlyURLLayout(long scopeGroupId, ThemeDisplay themeDisplay) throws SystemException, PortalException {
                return Mockito.mock(Layout.class);
            }

            @Override
            protected void copyRequestParameters(ActionRequest request, ActionResponse response) {

            }

            @Override
            protected HttpServletRequest toHttpServletRequest(ActionRequest request) {
                return Mockito.mock(HttpServletRequest.class);
            }
        };
    }

    @Ignore
    @Test
    public void showConfirmation() throws Exception {
        String r = controller.showConfirmation(renderRequest, renderResponse, model);
        Assert.assertEquals("confirmation", r);
    }

    @Test
    public void createIdea() throws Exception {
        Idea idea = new Idea();
        BindingResult binding = Mockito.mock(BindingResult.class);

        String r = controller.createIdea(renderRequest, renderResponse, model, idea, binding);
        Assert.assertEquals("view", r);

        Mockito.when(ideaService.addIdea(Mockito.any(Idea.class), Mockito.anyString())).thenThrow(new RuntimeException());
        r = controller.createIdea(renderRequest, renderResponse, model, idea, binding);
        Assert.assertEquals("view", r);
    }

    @Test
    public void submitIdea() throws Exception {
        ActionRequest actionRequest = Mockito.mock(ActionRequest.class);
        ActionResponse actionResponse = Mockito.mock(ActionResponse.class);
        Idea idea = new Idea();
        BindingResult binding = Mockito.mock(BindingResult.class);

        ThemeDisplay themeDisplay = Mockito.mock(ThemeDisplay.class);
        Mockito.when(themeDisplay.getScopeGroupId()).thenReturn(1l);
        Mockito.when(themeDisplay.getCompanyId()).thenReturn(1l);
        Mockito.when(actionRequest.getAttribute(WebKeys.THEME_DISPLAY)).thenReturn(themeDisplay);

        controller.submitIdea(actionRequest, actionResponse, model, idea, binding);
    }
}
