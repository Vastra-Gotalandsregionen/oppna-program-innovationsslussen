package se.vgregion.portal.innovationsslussen.settings.controller;

import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.ui.ModelMap;
import se.vgregion.service.innovationsslussen.idea.settings.IdeaSettingsService;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * Created with IntelliJ IDEA.
 * User: portaldev
 * Date: 2013-09-23
 * Time: 07:50
 * To change this template use File | Settings | File Templates.
 */
public class IdeaSettingsViewControllerTest {

    IdeaSettingsViewController controller;
    private IdeaSettingsService service;
    private RenderRequest renderReq;
    private RenderResponse renderRes;
    private ModelMap model;
    private ThemeDisplay display;

    @Before
    public void setUp() {
        renderReq = Mockito.mock(RenderRequest.class);
        renderRes = Mockito.mock(RenderResponse.class);
        model = Mockito.mock(ModelMap.class);
        service = Mockito.mock(IdeaSettingsService.class);

        display = Mockito.mock(ThemeDisplay.class);
        Mockito.when(renderReq.getAttribute(WebKeys.THEME_DISPLAY)).thenReturn(display);
        Mockito.when(display.getScopeGroupId()).thenReturn(1l);
        Mockito.when(display.getCompanyId()).thenReturn(1l);


        controller = new IdeaSettingsViewController(service);
    }

    @Test
    public void showIdeaAdmin() throws Exception {
        controller.showIdeaAdmin(renderReq, renderRes, model);
    }

    @Test
    public void save() throws Exception {

    }
}
