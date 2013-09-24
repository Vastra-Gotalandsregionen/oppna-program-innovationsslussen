package se.vgregion.portal.innovationsslussen.util;

import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;
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
