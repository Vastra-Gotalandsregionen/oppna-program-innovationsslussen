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

package se.vgregion.portal.innovationsslussen.idealist.controller;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Layout;
import com.liferay.portal.theme.ThemeDisplay;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import se.vgregion.service.innovationsslussen.idea.IdeaService;

import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * Created with IntelliJ IDEA.
 * User: portaldev
 * Date: 2013-09-23
 * Time: 07:25
 * To change this template use File | Settings | File Templates.
 */
public class IdeaListViewControllerTest {

    IdeaListViewController controller;
    private IdeaService idesService;
    private RenderRequest renderReq;
    private RenderResponse renderRes;
    private ModelMap model;

    private ThemeDisplay display;
    private PortletPreferences prefs;

    @Before
    public void setUp() {
        idesService = Mockito.mock(IdeaService.class);
        renderReq = Mockito.mock(RenderRequest.class);
        renderRes = Mockito.mock(RenderResponse.class);

        display = Mockito.mock(ThemeDisplay.class);
        Mockito.when(renderReq.getAttribute(WebKeys.THEME_DISPLAY)).thenReturn(display);
        Mockito.when(display.getScopeGroupId()).thenReturn(1l);
        Mockito.when(display.getUserId()).thenReturn(1l);
        Mockito.when(display.isSignedIn()).thenReturn(true);

        prefs = Mockito.mock(PortletPreferences.class);
        Mockito.when(prefs.getValue("ideaListType", "0")).thenReturn("foo");
        Mockito.when(prefs.getValue("entryCount", "6")).thenReturn("6");
        Mockito.when(renderReq.getPreferences()).thenReturn(prefs);

        Layout layout = Mockito.mock(Layout.class);
        Mockito.when(layout.isPrivateLayout()).thenReturn(false);
        Mockito.when(display.getLayout()).thenReturn(layout);

        model = Mockito.mock(ModelMap.class);

        controller = new IdeaListViewController(idesService) {
            @Override
            protected Layout getFriendlyURLLayout(long scopeGroupId, boolean priv) throws SystemException, PortalException {
                return Mockito.mock(Layout.class);
            }
        };
    }

    @Test
    public void showIdeaList() {
        controller.showIdeaList(renderReq, renderRes, model);
    }

}
