package se.vgregion.portal.innovationsslussen.usersettings.controller;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.User;
import com.liferay.portal.theme.ThemeDisplay;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.ui.ModelMap;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * Created with IntelliJ IDEA.
 * User: portaldev
 * Date: 2013-09-24
 * Time: 06:44
 * To change this template use File | Settings | File Templates.
 */
public class UserSettingsViewControllerTest {

    UserSettingsViewController controller;

    private RenderRequest request;

    private RenderResponse response;

    private ModelMap model;

    @Before
    public void setUp() {
        ThemeDisplay themeDisplay = Mockito.mock(ThemeDisplay.class);
        request = Mockito.mock(RenderRequest.class);
        response = Mockito.mock(RenderResponse.class);
        model = Mockito.mock(ModelMap.class);

        Mockito.when(request.getAttribute(WebKeys.THEME_DISPLAY)).thenReturn(themeDisplay);
        Mockito.when(themeDisplay.getScopeGroupId()).thenReturn(1l);
        Mockito.when(themeDisplay.getCompanyId()).thenReturn(2l);
        Mockito.when(themeDisplay.getUserId()).thenReturn(3l);
    }

    @Test
    public void showIdeaWithError() throws Exception {
        controller = new UserSettingsViewController();
        showIdea(controller);
    }

    @Test
    public void showIdea() throws Exception {
        controller = new UserSettingsViewController() {
            @Override
            protected User lookupUser(long userId) throws SystemException, PortalException {
                return Mockito.mock(User.class);
            }
        };
        showIdea(controller);
    }

    public void showIdea(UserSettingsViewController controller) throws Exception {
        String r = controller.showIdea(request, response, model);
        Assert.assertEquals("view", r);
    }

}
