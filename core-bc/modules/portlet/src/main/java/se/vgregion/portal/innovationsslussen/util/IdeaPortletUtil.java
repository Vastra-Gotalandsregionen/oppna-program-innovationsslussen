package se.vgregion.portal.innovationsslussen.util;

import java.util.HashSet;
import java.util.Set;

import javax.portlet.PortletRequest;

import se.vgregion.portal.innovationsslussen.domain.IdeaConstants;
import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaContent;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaPerson;

import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;

public class IdeaPortletUtil {

	
    public static Idea getIdeaFromRequest(PortletRequest request) {
    	
    	ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
    	
        long companyId = themeDisplay.getCompanyId();
        long groupId = themeDisplay.getScopeGroupId();
        long userId = themeDisplay.getUserId();
        
        String title = ParamUtil.getString(request, "title", "");
        String description = ParamUtil.getString(request, "description", "");
        String ideaTested = ParamUtil.getString(request, "ideaTested", "");
        String solvesProblem = ParamUtil.getString(request, "solvesProblem", "");
        String wantsHelpWith = ParamUtil.getString(request, "wantsHelpWith", "");
        String additionalPersonInfo = ParamUtil.getString(request, "additionalPersonInfo", "");
        String name = ParamUtil.getString(request, "name", "");
        String email = ParamUtil.getString(request, "email", "");
        String phone = ParamUtil.getString(request, "phone", "");
        String phoneMobile = ParamUtil.getString(request, "phoneMobile", "");
        String administrativeUnit = ParamUtil.getString(request, "administrativeUnit", "");
        String jobPosition = ParamUtil.getString(request, "jobPosition", "");
        
        String vgrId = "none";
        
        Idea idea = new Idea(companyId, groupId, userId);
        IdeaContent ideaContentPublic = new IdeaContent(companyId, groupId, userId);
        IdeaContent ideaContentPrivate = new IdeaContent(companyId, groupId, userId);
        IdeaPerson ideaPerson = new IdeaPerson(companyId, groupId, userId);
        
        ideaContentPublic.setType(IdeaConstants.IDEA_CONTENT_TYPE_PUBLIC);
        
        ideaContentPrivate.setType(IdeaConstants.IDEA_CONTENT_TYPE_PRIVATE);
        ideaContentPrivate.setDescription(description);
        ideaContentPrivate.setIdeaTested(ideaTested);
        ideaContentPrivate.setSolvesProblem(solvesProblem);
        ideaContentPrivate.setWantsHelpWith(wantsHelpWith);
        
        ideaPerson.setName(name);
        ideaPerson.setEmail(email);
        ideaPerson.setPhone(phone);
        ideaPerson.setPhoneMobile(phoneMobile);
        ideaPerson.setJobPosition(jobPosition);
        ideaPerson.setVgrId(vgrId);
        ideaPerson.setAdditionalPersonsInfo(additionalPersonInfo);
        //ideaPerson.setAdministrativeUnit(administrativeUnit);
        
        
        idea.setTitle(title);
        
        idea.addIdeaContent(ideaContentPublic);
        idea.addIdeaContent(ideaContentPrivate);
        idea.addIdeaPerson(ideaPerson);
    	
    	return idea;
    }	
	
	
	
	
}
