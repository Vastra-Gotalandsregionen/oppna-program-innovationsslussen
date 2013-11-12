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

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portlet.messageboards.model.MBMessageDisplay;
import com.liferay.portlet.messageboards.model.MBThread;
import com.liferay.portlet.messageboards.service.MBMessageLocalServiceUtil;
import javax.portlet.ActionRequest;
import javax.portlet.PortletRequest;

import se.vgregion.portal.innovationsslussen.domain.IdeaContentType;
import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaContent;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaPerson;

import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;
import se.vgregion.service.innovationsslussen.idea.settings.IdeaSettingsService;

public final class IdeaPortletUtil {

    private IdeaPortletUtil(IdeaSettingsService ideaSettingsService) {
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
        String additionalPersonInfo = ParamUtil.getString(request, "idea.ideaContentPrivate.additionalIdeaOriginators", "");
        String name = ParamUtil.getString(request, "ideaPerson.name", "");
        String email = ParamUtil.getString(request, "ideaPerson.email", "");
        String phone = ParamUtil.getString(request, "ideaPerson.phone", "");
        String phoneMobile = ParamUtil.getString(request, "ideaPerson.phoneMobile", "");
        String administrativeUnit = ParamUtil.getString(request, "administrativeUnit", "");
        String jobPosition = ParamUtil.getString(request, "ideaPerson.jobPosition", "");
        String gender = ParamUtil.getString(request, "ideaPerson.gender");

        String vgrId = ParamUtil.getString(request, "ideaPerson.vgrId");

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

        ideaContentPrivate.setAdditionalIdeaOriginators(additionalPersonInfo);

        ideaPerson.setAdministrativeUnit(administrativeUnit);
        ideaPerson.setGender((gender == null || "".equals(gender) ? IdeaPerson.Gender.UNKNOWN
                : IdeaPerson.Gender.valueOf(gender)));

        idea.setTitle(title);

        idea.addIdeaContent(ideaContentPublic);
        idea.addIdeaContent(ideaContentPrivate);
        idea.addIdeaPerson(ideaPerson);

        return idea;
    }

    public static void addMBMessage(ActionRequest request, long groupId, long userId, String comment, long ideaCommentClassPK) throws PortalException, SystemException {

        ServiceContext serviceContext = ServiceContextFactory.getInstance(Idea.class.getName(), request);

        User user = UserLocalServiceUtil.getUser(userId);

        String threadView = PropsKeys.DISCUSSION_THREAD_VIEW;

        MBMessageDisplay messageDisplay = MBMessageLocalServiceUtil.getDiscussionMessageDisplay(
                userId, groupId, IdeaContent.class.getName(), ideaCommentClassPK,
                WorkflowConstants.STATUS_ANY, threadView);

        MBThread thread = messageDisplay.getThread();

        long threadId = thread.getThreadId();
        long rootThreadId = thread.getRootMessageId();

        String commentContentCleaned = comment;

        final int maxLenghtCommentSubject = 50;

        String commentSubject = comment;
        commentSubject = StringUtil.shorten(commentSubject, maxLenghtCommentSubject);
        commentSubject += "...";

        // TODO - validate comment and preserve line breaks
        MBMessageLocalServiceUtil.addDiscussionMessage(
                userId, user.getScreenName(), groupId,
                IdeaContent.class.getName(), ideaCommentClassPK, threadId,
                rootThreadId, commentSubject, commentContentCleaned,
                serviceContext);
    }


    public static Idea replaceBreaklines(Idea idea){

        if (idea.getIdeaContentPrivate() != null){
            idea.getIdeaContentPrivate().setDescription(replaceBreakline(idea.getIdeaContentPrivate().getDescription()));
            idea.getIdeaContentPrivate().setIdeaTested(replaceBreakline(idea.getIdeaContentPrivate().getIdeaTested()));
            idea.getIdeaContentPrivate().setSolvesProblem(replaceBreakline(idea.getIdeaContentPrivate().getSolvesProblem()));
            idea.getIdeaContentPrivate().setWantsHelpWith(replaceBreakline(idea.getIdeaContentPrivate().getWantsHelpWith()));
        }

        if (idea.getIdeaContentPublic() != null){
            idea.getIdeaContentPublic().setDescription(replaceBreakline(idea.getIdeaContentPublic().getDescription()));
            idea.getIdeaContentPublic().setIdeaTested(replaceBreakline(idea.getIdeaContentPublic().getIdeaTested()));
            idea.getIdeaContentPublic().setSolvesProblem(replaceBreakline(idea.getIdeaContentPublic().getSolvesProblem()));
            idea.getIdeaContentPublic().setWantsHelpWith(replaceBreakline(idea.getIdeaContentPublic().getWantsHelpWith()));
        }

        return idea;

    }

    private static String replaceBreakline(String s){
        if (s != null && !s.isEmpty()){
            s = s.replaceAll("\\n", "</br></br>");
        }

        return s;
    }

}
