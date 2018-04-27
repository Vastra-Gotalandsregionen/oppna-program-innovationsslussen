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

package se.vgregion.portal.innovationsslussen.util;

import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;

import javax.portlet.RenderRequest;

/**
 * Created with IntelliJ IDEA.
 * User: portaldev
 * Date: 2013-09-24
 * Time: 06:59
 * To change this template use File | Settings | File Templates.
 */
public class IdeaPortletUtilTest {

    private RenderRequest request;

    @Before
    public void setUp() {
        ThemeDisplay themeDisplay = Mockito.mock(ThemeDisplay.class);
        request = Mockito.mock(RenderRequest.class);

        Mockito.when(request.getAttribute(WebKeys.THEME_DISPLAY)).thenReturn(themeDisplay);
        Mockito.when(themeDisplay.getScopeGroupId()).thenReturn(1l);
        Mockito.when(themeDisplay.getCompanyId()).thenReturn(2l);
        Mockito.when(themeDisplay.getUserId()).thenReturn(3l);
    }

    @Test
    public void getIdeaFromRequest() throws Exception {
        Idea r = IdeaPortletUtil.getIdeaFromRequest(request);
        Assert.assertNotNull(r);
    }

}
