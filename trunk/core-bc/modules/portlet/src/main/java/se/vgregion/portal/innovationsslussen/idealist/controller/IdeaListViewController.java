package se.vgregion.portal.innovationsslussen.idealist.controller;

import java.util.List;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import se.vgregion.portal.innovationsslussen.domain.vo.IdeaVO;
import se.vgregion.service.idea.wrapped.WrappedIdeaService;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Layout;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;

/**
 * Controller class for the view mode in idealist portlet.
 *
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
@Controller
@RequestMapping(value = "VIEW")
public class IdeaListViewController {

    private static final Logger LOGGER = LoggerFactory.getLogger(IdeaListViewController.class.getName());

    private WrappedIdeaService wrappedIdeaService;

    /**
     * Constructor.
     *
     */
    @Autowired
    public IdeaListViewController(WrappedIdeaService wrappedIdeaService) {
        this.wrappedIdeaService = wrappedIdeaService;
    }
    

    /**
     * The default render method.
     *
     * @param request  the request
     * @param response the response
     * @param model    the model
     * @return the view
     */
    @RenderMapping()
    public String showIdeaList(RenderRequest request, RenderResponse response, final ModelMap model) {

        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        long scopeGroupId = themeDisplay.getScopeGroupId();
        long companyId = themeDisplay.getCompanyId();
        boolean isSignedIn = themeDisplay.isSignedIn();
        
		// Get plid for idea page
		try {
			Layout ideaLayout = LayoutLocalServiceUtil.getFriendlyURLLayout(scopeGroupId, themeDisplay.getLayout()
                    .isPrivateLayout(), "/ide");
			
			long ideaPlid = ideaLayout.getPlid();
			
			model.addAttribute("ideaPlid", ideaPlid);
			model.addAttribute("ideaPortletName","idea_WAR_innovationsslussenportlet");

			List<IdeaVO> ideaVOList = wrappedIdeaService.getAllBariumIdeas();
			
			model.addAttribute("ideaVOList", ideaVOList);
			
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
        

        model.addAttribute("foo", "bar");

        return "view";
    }

}

