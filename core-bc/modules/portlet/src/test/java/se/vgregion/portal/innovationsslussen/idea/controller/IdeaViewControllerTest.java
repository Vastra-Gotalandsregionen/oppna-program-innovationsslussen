package se.vgregion.portal.innovationsslussen.idea.controller;

import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.User;
import com.liferay.portal.theme.ThemeDisplay;
import junit.framework.Assert;
import org.apache.commons.fileupload.FileUploadException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import se.vgregion.portal.innovationsslussen.domain.IdeaStatus;
import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;
import se.vgregion.service.barium.MockBariumRestClientImpl;
import se.vgregion.service.innovationsslussen.idea.IdeaService;
import se.vgregion.service.innovationsslussen.idea.permission.IdeaPermissionCheckerService;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletResponse;

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

        controller = new IdeaViewController(ideaService, ideaPermissionCheckerService) {
            @Override
            protected HttpServletResponse getHttpServletResponse(RenderResponse response) {
                return Mockito.mock(HttpServletResponse.class);
            }
        };

        request = Mockito.mock(RenderRequest.class);
        response = Mockito.mock(RenderResponse.class);
        actionRequest = Mockito.mock(ActionRequest.class);
        actionResponse = Mockito.mock(ActionResponse.class);
        modelMap = Mockito.mock(ModelMap.class);

        ThemeDisplay themeDisplay = Mockito.mock(ThemeDisplay.class);
        Mockito.when(request.getAttribute(WebKeys.THEME_DISPLAY)).thenReturn(themeDisplay);
        Mockito.when(themeDisplay.getScopeGroupId()).thenReturn(1l);
        User user = Mockito.mock(User.class);
        Mockito.when(themeDisplay.getUser()).thenReturn(user);
        Mockito.when(themeDisplay.getUserId()).thenReturn(1l);

    }

    @Test
    public void showIdea() {

        Mockito.when(request.getParameter("urlTitle")).thenReturn("foo://bar/baz");


        String r = controller.showIdea(request, response, modelMap);
        Assert.assertEquals("idea_404", r);

        Idea idea = new Idea();
        idea.setStatus(IdeaStatus.PUBLIC_IDEA);
        Mockito.when(ideaService.findIdeaByUrlTitle(Mockito.anyString())).thenReturn(idea);

        r = controller.showIdea(request, response, modelMap);
        Assert.assertEquals("view_public", r);
    }

    @Ignore
    @Test
    public void uploadFile() throws FileUploadException {
        Model model = Mockito.mock(Model.class);
        String r = controller.uploadFile(request, response, model);

        controller.uploadFile(actionRequest, actionResponse, model);
    }

}
