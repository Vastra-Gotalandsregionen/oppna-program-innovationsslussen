package se.vgregion.portal.innovationsslussen.util;

import javax.portlet.PortletRequest;

import se.vgregion.portal.innovationsslussen.domain.IdeaContentType;
import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaContent;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaPerson;

import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;

public final class IdeaPortletUtil {

    private IdeaPortletUtil() {

    }

    /**
     * Gets the idea from request.
     *
     * @param request the request
     * @return the idea from request
     */
    public static Idea getIdeaFromRequest(PortletRequest request) {

        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);

        long companyId = themeDisplay.getCompanyId();
        long groupId = themeDisplay.getScopeGroupId();
        long userId = themeDisplay.getUserId();

        String title = ParamUtil.getString(request, "title", "");
        String description = ParamUtil.getString(request, "ideaContentPrivate.description", "");
        String ideaTested = ParamUtil.getString(request, "ideaContentPrivate.ideaTested", "");
        String solvesProblem = ParamUtil.getString(request, "ideaContentPrivate.solvesProblem", "");
        String wantsHelpWith = ParamUtil.getString(request, "ideaContentPrivate.wantsHelpWith", "");
        String additionalPersonInfo = ParamUtil.getString(request, "ideaPerson.additionalPersonInfo", "");
        String name = ParamUtil.getString(request, "ideaPerson.name", "");
        String email = ParamUtil.getString(request, "ideaPerson.email", "");
        String phone = ParamUtil.getString(request, "ideaPerson.phone", "");
        String phoneMobile = ParamUtil.getString(request, "ideaPerson.phoneMobile", "");
        String administrativeUnit = ParamUtil.getString(request, "administrativeUnit", "");
        String jobPosition = ParamUtil.getString(request, "ideaPerson.jobPosition", "");
        String gender = ParamUtil.getString(request, "ideaPerson.gender");

        String vgrId = "none";

        Idea idea = new Idea(companyId, groupId, userId);
        IdeaContent ideaContentPublic = new IdeaContent(companyId, groupId, userId);
        IdeaContent ideaContentPrivate = new IdeaContent(companyId, groupId, userId);
        IdeaPerson ideaPerson = new IdeaPerson(companyId, groupId, userId);

        ideaContentPublic.setType(IdeaContentType.IDEA_CONTENT_TYPE_PUBLIC);

        ideaContentPrivate.setType(IdeaContentType.IDEA_CONTENT_TYPE_PRIVATE);
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
        ideaPerson.setAdministrativeUnit(administrativeUnit);
        ideaPerson.setGender((gender == null || "".equals(gender) ? IdeaPerson.Gender.UNKNOWN
                : IdeaPerson.Gender.valueOf(gender)));

        idea.setTitle(title);

        idea.addIdeaContent(ideaContentPublic);
        idea.addIdeaContent(ideaContentPrivate);
        idea.addIdeaPerson(ideaPerson);

        return idea;
    }




}
