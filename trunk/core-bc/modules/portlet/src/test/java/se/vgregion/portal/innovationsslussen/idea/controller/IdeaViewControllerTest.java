package se.vgregion.portal.innovationsslussen.idea.controller;

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

    @Ignore
    @Test
    public void showIdea() {
        IdeaService ideaService = Mockito.mock(IdeaService.class);
        IdeaPermissionCheckerService ideaPermissionCheckerService = Mockito.mock(IdeaPermissionCheckerService.class);

        IdeaViewController controller = new IdeaViewController(ideaService, ideaPermissionCheckerService);
        RenderRequest a = Mockito.mock(RenderRequest.class);
        RenderResponse b = Mockito.mock(RenderResponse.class);
        ModelMap c = Mockito.mock(ModelMap.class);

        controller.showIdea(a, b, c);
    }

}
