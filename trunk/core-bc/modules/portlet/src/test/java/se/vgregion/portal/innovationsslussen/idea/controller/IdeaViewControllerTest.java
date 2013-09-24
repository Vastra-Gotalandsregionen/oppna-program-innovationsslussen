package se.vgregion.portal.innovationsslussen.idea.controller;

import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.User;
import com.liferay.portal.theme.ThemeDisplay;
import junit.framework.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.ui.ModelMap;
import se.vgregion.service.barium.MockBariumRestClientImpl;
import se.vgregion.service.innovationsslussen.idea.IdeaService;
import se.vgregion.service.innovationsslussen.idea.permission.IdeaPermissionCheckerService;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * Created with IntelliJ IDEA.
 * User: portaldev
 * Date: 2013-08-13
 * Time: 09:04
 * To change this template use File | Settings | File Templates.
 */
public class IdeaViewControllerTest {

    @Test
    public void showIdea() {
        IdeaService ideaService = Mockito.mock(IdeaService.class);
        IdeaPermissionCheckerService ideaPermissionCheckerService = Mockito.mock(IdeaPermissionCheckerService.class);

        IdeaViewController controller = new IdeaViewController(ideaService, ideaPermissionCheckerService);
        RenderRequest a = Mockito.mock(RenderRequest.class);
        RenderResponse b = Mockito.mock(RenderResponse.class);
        ModelMap c = Mockito.mock(ModelMap.class);

        ThemeDisplay themeDisplay = Mockito.mock(ThemeDisplay.class);
        Mockito.when(a.getAttribute(WebKeys.THEME_DISPLAY)).thenReturn(themeDisplay);
        Mockito.when(themeDisplay.getScopeGroupId()).thenReturn(1l);
        User user = Mockito.mock(User.class);
        Mockito.when(themeDisplay.getUser()).thenReturn(user);

        String r = controller.showIdea(a, b, c);
        Assert.assertEquals("view_public", r);
    }

}
