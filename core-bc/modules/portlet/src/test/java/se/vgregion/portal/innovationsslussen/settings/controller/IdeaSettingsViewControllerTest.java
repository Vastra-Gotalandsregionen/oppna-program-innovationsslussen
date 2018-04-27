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

package se.vgregion.portal.innovationsslussen.settings.controller;

import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;
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
