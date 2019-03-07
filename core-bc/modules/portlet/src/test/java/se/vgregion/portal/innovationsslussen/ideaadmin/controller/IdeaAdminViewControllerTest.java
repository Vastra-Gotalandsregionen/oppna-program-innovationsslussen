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

package se.vgregion.portal.innovationsslussen.ideaadmin.controller;

import com.liferay.portal.kernel.configuration.Filter;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Props;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.WebKeys;
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
import java.util.Properties;

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

        com.liferay.portal.kernel.util.ParamUtil p = new ParamUtil();

        PropsUtil.setProps(new Props() {
            @Override
            public boolean contains(String key) {
                return false;
            }

            @Override
            public String get(String key) {
                return key;
            }

            @Override
            public String get(String key, Filter filter) {
                return key;
            }

            @Override
            public String[] getArray(String key) {
                return new String[0];
            }

            @Override
            public String[] getArray(String key, Filter filter) {
                return new String[0];
            }

            @Override
            public Properties getProperties() {
                return new Properties();
            }

            @Override
            public Properties getProperties(String prefix, boolean removePrefix) {
                return new Properties();
            }
        });
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
