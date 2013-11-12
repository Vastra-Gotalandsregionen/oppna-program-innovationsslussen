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

package se.vgregion.portal.innovationsslussen.idea.controller;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.User;
import com.liferay.portal.theme.ThemeDisplay;
import junit.framework.Assert;
import org.apache.commons.fileupload.FileUploadException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import se.vgregion.portal.innovationsslussen.domain.IdeaContentType;
import se.vgregion.portal.innovationsslussen.domain.IdeaStatus;
import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaContent;
import se.vgregion.service.barium.MockBariumRestClientImpl;
import se.vgregion.service.innovationsslussen.idea.IdeaService;
import se.vgregion.service.innovationsslussen.idea.permission.IdeaPermissionCheckerService;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletResponse;
import se.vgregion.service.innovationsslussen.idea.settings.IdeaSettingsService;

/**
 * Created with IntelliJ IDEA.
 * User: portaldev
 * Date: 2013-08-13
 * Time: 09:04
 * To change this template use File | Settings | File Templates.
 */
public class IdeaViewControllerTest {

    IdeaService ideaService;
    IdeaPermissionCheckerService ideaPermissionCheckerService;
    IdeaSettingsService ideaSettingsService;
    IdeaViewController controller;
    RenderRequest request;
    ModelMap modelMap;
    RenderResponse response;
    ActionRequest actionRequest;
    ActionResponse actionResponse;

    @Before
    public void setUp() {
        ideaService = Mockito.mock(IdeaService.class);
        ideaPermissionCheckerService = Mockito.mock(IdeaPermissionCheckerService.class);
        ideaSettingsService = Mockito.mock(IdeaSettingsService.class);


        controller = new IdeaViewController(ideaService, ideaPermissionCheckerService, ideaSettingsService) {
            @Override
            protected HttpServletResponse getHttpServletResponse(RenderResponse response) {
                return Mockito.mock(HttpServletResponse.class);
            }

            @Override
            protected Layout getFriendlyURLLayout(long scopeGroupId, boolean priv) throws SystemException, PortalException {
                return Mockito.mock(Layout.class);
            }
        };

        request = Mockito.mock(RenderRequest.class);
        response = Mockito.mock(RenderResponse.class);
        actionRequest = Mockito.mock(ActionRequest.class);
        actionResponse = Mockito.mock(ActionResponse.class);
        modelMap = Mockito.mock(ModelMap.class);

        ThemeDisplay themeDisplay = Mockito.mock(ThemeDisplay.class);
        Mockito.when(request.getAttribute(WebKeys.THEME_DISPLAY)).thenReturn(themeDisplay);
        Mockito.when(actionRequest.getAttribute(WebKeys.THEME_DISPLAY)).thenReturn(themeDisplay);
        Mockito.when(themeDisplay.getScopeGroupId()).thenReturn(1l);
        User user = Mockito.mock(User.class);
        Mockito.when(themeDisplay.getUser()).thenReturn(user);
        Mockito.when(themeDisplay.getUserId()).thenReturn(1l);

        Layout layout = Mockito.mock(Layout.class);
        Mockito.when(layout.isPrivateLayout()).thenReturn(true);
        Mockito.when(themeDisplay.getLayout()).thenReturn(layout);

    }

    @Test
    public void showIdea() throws SystemException, PortalException {
        Mockito.when(request.getParameter("urlTitle")).thenReturn("foo://bar/baz");
        String r = controller.showIdea(request, response, modelMap);
        Assert.assertEquals("idea_404", r);

        Idea idea = new Idea();
        idea.setId("test-id");
        idea.setStatus(IdeaStatus.PUBLIC_IDEA);

        IdeaContent ideaContentPublic = new IdeaContent();
        IdeaContent ideaContentPrivate = new IdeaContent();

        ideaContentPublic.setType(IdeaContentType.IDEA_CONTENT_TYPE_PUBLIC);
        ideaContentPrivate.setType(IdeaContentType.IDEA_CONTENT_TYPE_PRIVATE);

        ideaContentPublic.setDescription("test test");
        ideaContentPublic.setIdeaTested("");
        ideaContentPublic.setWantsHelpWith("");
        ideaContentPublic.setSolvesProblem("");

        ideaContentPrivate.setDescription("test test");
        ideaContentPrivate.setIdeaTested("");
        ideaContentPrivate.setWantsHelpWith("");
        ideaContentPrivate.setSolvesProblem("");

        idea.addIdeaContent(ideaContentPublic);
        idea.addIdeaContent(ideaContentPrivate);

        Mockito.when(ideaService.findIdeaByUrlTitle(Mockito.anyString())).thenReturn(idea);

        r = controller.showIdea(request, response, modelMap);
        Assert.assertEquals("view_public", r);
    }

    @Ignore
    @Test
    public void uploadFile() throws FileUploadException, PortalException, SystemException {
        Idea idea = new Idea() {
            @Override
            public IdeaContent getIdeaContentPublic() {
                return new IdeaContent();
            }

            @Override
            public IdeaContent getIdeaContentPrivate() {
                return new IdeaContent();
            }
        };
        idea.setUserId(1l);
        Mockito.when(ideaService.findIdeaByUrlTitle(Mockito.anyString())).thenReturn(idea);
        Mockito.when(actionRequest.getParameter("fileType")).thenReturn("public");
        Model model = Mockito.mock(Model.class);
        String r = controller.uploadFile(request, response, model);
        Assert.assertEquals("upload_file", r);
        controller.uploadFile(actionRequest, actionResponse, model);
    }

}
