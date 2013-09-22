package se.vgregion.portal.innovationsslussen.idealist.controller;

import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.ui.ModelMap;
import se.vgregion.service.innovationsslussen.idea.IdeaService;

import javax.portlet.*;

/**
 * Created with IntelliJ IDEA.
 * User: portaldev
 * Date: 2013-09-22
 * Time: 15:37
 * To change this template use File | Settings | File Templates.
 */
public class IdeaListEditControllerTest {

    IdeaListEditController controller;
    private IdeaService ideaService;
    private RenderRequest request;
    private RenderResponse response;
    private ModelMap model;
    private ActionRequest actionReq;
    private ActionResponse actionRes;

    /*        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        long scopeGroupId = themeDisplay.getScopeGroupId();
        long companyId = themeDisplay.getCompanyId();
        boolean isSignedIn = themeDisplay.isSignedIn();
*/

    @Before
    public void setUp() {
        ideaService = Mockito.mock(IdeaService.class);
        request = Mockito.mock(RenderRequest.class);
        response = Mockito.mock(RenderResponse.class);
        model = Mockito.mock(ModelMap.class);
        actionReq = Mockito.mock(ActionRequest.class);
        actionRes = Mockito.mock(ActionResponse.class);

        controller = new IdeaListEditController(ideaService);
    }

    @Test
    public void testShowEdit() throws Exception {
        ThemeDisplay themeDisplay = Mockito.mock(ThemeDisplay.class);
        Mockito.when(themeDisplay.getScopeGroupId()).thenReturn(1l);
        Mockito.when(themeDisplay.getCompanyId()).thenReturn(1l);
        Mockito.when(request.getAttribute(WebKeys.THEME_DISPLAY)).thenReturn(themeDisplay);
        PortletPreferences prefs = Mockito.mock(PortletPreferences.class);
        Mockito.when((request.getPreferences())).thenReturn(prefs);

        controller.showEdit(request, response, model);
    }

    @Test
    public void testSavePreferences() throws Exception {
        ThemeDisplay themeDisplay = Mockito.mock(ThemeDisplay.class);
        PortletPreferences prefs = Mockito.mock(PortletPreferences.class);
        Mockito.when((actionReq.getPreferences())).thenReturn(prefs);

        controller.savePreferences(actionReq, "");
    }
}
