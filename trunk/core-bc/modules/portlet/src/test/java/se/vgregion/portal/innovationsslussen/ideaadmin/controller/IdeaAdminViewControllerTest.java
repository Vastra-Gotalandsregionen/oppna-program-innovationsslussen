package se.vgregion.portal.innovationsslussen.ideaadmin.controller;

import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Layout;
import com.liferay.portal.theme.ThemeDisplay;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.ui.ModelMap;
import se.vgregion.service.innovationsslussen.idea.IdeaService;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * Created with IntelliJ IDEA.
 * User: portaldev
 * Date: 2013-09-22
 * Time: 14:52
 * To change this template use File | Settings | File Templates.
 */
public class IdeaAdminViewControllerTest {

    IdeaAdminViewController controller;

    IdeaService ideaService;
    private RenderRequest request;
    private RenderResponse response;
    private ModelMap model;
    ActionRequest actionReq;
    ActionResponse actionRes;

    @Before
    public void setUp() {
        ideaService = Mockito.mock(IdeaService.class);
        request = Mockito.mock(RenderRequest.class);
        response = Mockito.mock(RenderResponse.class);
        model = Mockito.mock(ModelMap.class);
        actionReq = Mockito.mock(ActionRequest.class);
        actionRes = Mockito.mock(ActionResponse.class);

        controller = new IdeaAdminViewController(ideaService) {
            @Override
            protected Layout getFriendlyURLLayout(long scopeGroupId) {
                return Mockito.mock(Layout.class);
            }
        };
    }

    @Test
    public void testShowIdeaAdmin() throws Exception {
        ThemeDisplay themeDisplay = Mockito.mock(ThemeDisplay.class);
        Mockito.when(themeDisplay.getScopeGroupId()).thenReturn(1l);
        Mockito.when(themeDisplay.getCompanyId()).thenReturn(1l);
        Mockito.when(request.getAttribute(WebKeys.THEME_DISPLAY)).thenReturn(themeDisplay);

        String result = controller.showIdeaAdmin(request, response, model);
        Assert.assertEquals("view", result);
    }

    @Test
    public void testSomeAction() throws Exception {
        controller.someAction(actionReq, actionRes, model);
    }

    @Test
    public void testDeleteEntry() throws Exception {
        controller.deleteEntry(actionReq, actionRes, model);

    }

    @Test
    public void testSyncAllFromBarium() throws Exception {
        controller.syncAllFromBarium();
    }

    @Test
    public void testSyncIdeaFromBarium() throws Exception {
        controller.syncIdeaFromBarium(actionReq, actionRes, model);
    }
}