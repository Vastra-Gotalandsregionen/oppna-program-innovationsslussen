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

package se.vgregion.portal.innovationsslussen.idea.controller;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.model.Group;
import com.liferay.portal.service.UserLocalServiceUtil;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.portlet.PortletFileUpload;
import org.codehaus.jackson.annotate.JsonValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import se.vgregion.portal.innovationsslussen.BaseController;
import se.vgregion.portal.innovationsslussen.domain.IdeaContentType;
import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;
import se.vgregion.portal.innovationsslussen.domain.json.ObjectEntry;
import se.vgregion.portal.innovationsslussen.domain.vo.CommentItemVO;
import se.vgregion.portal.innovationsslussen.util.IdeaPortletUtil;
import se.vgregion.portal.innovationsslussen.util.IdeaPortletsConstants;
import se.vgregion.service.barium.BariumException;
import se.vgregion.service.innovationsslussen.exception.UpdateIdeaException;
import se.vgregion.service.innovationsslussen.idea.IdeaService;
import se.vgregion.service.innovationsslussen.idea.permission.IdeaPermissionChecker;
import se.vgregion.service.innovationsslussen.idea.permission.IdeaPermissionCheckerService;
import se.vgregion.service.innovationsslussen.idea.settings.IdeaSettingsService;
import se.vgregion.service.innovationsslussen.idea.settings.util.ExpandoConstants;
import se.vgregion.service.innovationsslussen.ldap.LdapService;
import se.vgregion.util.Util;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.ParamUtil;

import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.Role;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.RoleLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.messageboards.service.MBMessageLocalServiceUtil;

/**
 * Controller class for the view mode in idea portlet.
 *
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
@Controller
@RequestMapping(value = "VIEW")
public class IdeaViewController extends BaseController {

    private final IdeaService ideaService;
    private final IdeaPermissionCheckerService ideaPermissionCheckerService;
    private final IdeaSettingsService ideaSettingsService;
    private LdapService ldapService;

    private final Set<String> rolesMayUploadFile = new HashSet<String>(
            Arrays.asList("Idea Innovationsslussen", "Idea Priority Council", "Idea Transporter"));

    private static final Logger LOGGER = LoggerFactory.getLogger(IdeaViewController.class.getName());

    private int defaultCommentCount;

    /**
     * Instantiates a new idea view controller.
     *
     * @param ideaService                  the idea service
     * @param ideaPermissionCheckerService the idea permission checker service
     * @param ideaSettingsService the idea idea settings service
     */
    @Autowired
    public IdeaViewController(IdeaService ideaService, IdeaPermissionCheckerService ideaPermissionCheckerService,
                              IdeaSettingsService ideaSettingsService) {
        this.ideaService = ideaService;
        this.ideaPermissionCheckerService = ideaPermissionCheckerService;
        this.ideaSettingsService = ideaSettingsService;
        initDefaultCommentCount();
    }

    private void initDefaultCommentCount() {
        try {
            defaultCommentCount = Integer.parseInt(ideaService.getDefaultCommentCount());
        } catch (Exception e) {
            defaultCommentCount = 10;
        }
    }

    protected Layout getFriendlyURLLayout(long scopeGroupId, boolean priv) throws SystemException, PortalException {
        return LayoutLocalServiceUtil.getFriendlyURLLayout(scopeGroupId,
                priv, "/ide");
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
    public String showIdea(RenderRequest request, RenderResponse response, final ModelMap model) throws PortalException, SystemException {

        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        long scopeGroupId = themeDisplay.getScopeGroupId();
        long companyId = themeDisplay.getCompanyId();
        long userId = themeDisplay.getUserId();
        boolean isSignedIn = themeDisplay.isSignedIn();
        String ideaType = ParamUtil.getString(request, "type", "public");
        String urlTitle = ParamUtil.getString(request, "urlTitle", "");

        Layout ideaLayout = getFriendlyURLLayout(scopeGroupId,
                themeDisplay.getLayout().isPrivateLayout());
        long ideaPlid = ideaLayout.getPlid();
        model.addAttribute("ideaPlid", ideaPlid);

        int maxCommentCountDisplay = ParamUtil.getInteger(request, "maxCommentCountDisplay", defaultCommentCount);
        boolean moreComments = ParamUtil.getString(request, "moreComments") != null;
        if (moreComments) {
            maxCommentCountDisplay += defaultCommentCount;
        }

        String returnView = "view_public";

        if (!urlTitle.equals("")) {
            Idea idea = ideaService.findIdeaByUrlTitle(urlTitle);
            if (idea != null) {

                boolean isIdeaUserLiked = ideaService.getIsIdeaUserLiked(companyId, scopeGroupId, userId, urlTitle);
                boolean isIdeaUserFavorite = ideaService.getIsIdeaUserFavorite(companyId, scopeGroupId,
                        userId, urlTitle);

                List<CommentItemVO> commentsList = null;

                if (ideaType.equals("private")) {
					commentsList = ideaService.getPrivateComments(idea);
                } else {
					commentsList = ideaService.getPublicComments(idea);
                }

                IdeaPermissionChecker ideaPermissionChecker = ideaPermissionCheckerService.getIdeaPermissionChecker(
                        scopeGroupId, userId, idea);

                model.addAttribute("commentCount", commentsList.size());
                commentsList = commentsList.subList(0, Math.min(maxCommentCountDisplay, commentsList.size()));


                idea.getIdeaContentPrivate().setDescription(idea.getIdeaContentPrivate().getDescription().replaceAll("/n", "</br>"));

                model.addAttribute("idea", idea);
                model.addAttribute("commentsList", commentsList);
                model.addAttribute("commentsDelta", 1);
                model.addAttribute("isIdeaUserFavorite", isIdeaUserFavorite);
                model.addAttribute("isIdeaUserLiked", isIdeaUserLiked);
                model.addAttribute("urlTitle", urlTitle);
                model.addAttribute("userId", userId);

                model.addAttribute("isSignedIn", isSignedIn);
                model.addAttribute("ideaPermissionChecker", ideaPermissionChecker);

                model.addAttribute("ideaType", ideaType);

                model.addAttribute("maxCommentCountDisplay", maxCommentCountDisplay);
                model.addAttribute("defaultCommentCount", defaultCommentCount);

                model.addAttribute("ideaPortletName", IdeaPortletsConstants.PORTLET_NAME_IDEA_PORTLET);

                if (ideaType.equals("private") && (ideaPermissionChecker.getHasPermissionViewIdeaPrivate()
                        || idea.getUserId() == userId)) {
                    returnView = "view_private";
                }

                // If a user trying to access the public or private part of an idea that still is private.
                // The user dose not have premmisions and is not the creator of the idea.
                // Showing 404 view.
                if (!idea.isPublic() && !(ideaPermissionChecker.getHasPermissionViewIdeaPrivate()
                        || idea.getUserId() == userId)) {
                    HttpServletResponse httpServletResponse = PortalUtil.getHttpServletResponse(response);
                    httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    returnView = "idea_404";
                }
            } else {
                HttpServletResponse httpServletResponse = getHttpServletResponse(response);
                httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
                returnView = "idea_404";
            }
        }

        return returnView;
    }

    protected HttpServletResponse getHttpServletResponse(RenderResponse response) {
        return PortalUtil.getHttpServletResponse(response);
    }

    /**
     * Upload file render.
     *
     * @param request  the request
     * @param response the response
     * @param model    the model
     * @return the string
     */
    @RenderMapping(params = "showView=showUploadFile")
    public String uploadFile(RenderRequest request, RenderResponse response, Model model) {
        model.addAttribute("urlTitle", request.getParameter("urlTitle"));
        model.addAttribute("ideaType", request.getParameter("ideaType"));
        return "upload_file";
    }

    /**
     * Method handling Action request.
     *
     * @param request  the request
     * @param response the response
     * @param model    the model
     */
    @ActionMapping("someAction")
    public final void someAction(ActionRequest request, ActionResponse response, final ModelMap model) {
        LOGGER.info("someAction");
        response.setRenderParameter("view", "view");
    }

    /**
     * Method handling Action request.
     *
     * @param request  the request
     * @param response the response
     * @param model    the model
     */
    @ActionMapping(params = "action=addComment")
    public final void addComment(ActionRequest request, ActionResponse response, final ModelMap model) {

        LOGGER.info("addComment");
        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        long groupId = themeDisplay.getScopeGroupId();
        long userId = themeDisplay.getUserId();
        IdeaContentType ideaContentType = IdeaContentType.valueOf(ParamUtil.getString(request, "ideaContentType"));
        String urlTitle = ParamUtil.getString(request, "urlTitle", "");
        String comment = ParamUtil.getString(request, "comment", "");

        if (!comment.equals("")) {

            Idea idea = ideaService.findIdeaByUrlTitle(urlTitle);
            try {
                //Add a mbMessage
                if (ideaContentType == IdeaContentType.IDEA_CONTENT_TYPE_PUBLIC) {
                    IdeaPortletUtil.addMBMessage(request, groupId, userId, comment, idea.getIdeaContentPublic().getId());
                    //Send email notification.
                    sendEmailNotification(idea, true);
                } else if (ideaContentType == IdeaContentType.IDEA_CONTENT_TYPE_PRIVATE) {
                    IdeaPortletUtil.addMBMessage(request, groupId, userId, comment, idea.getIdeaContentPrivate().getId());
                    //Send email notification.
                    sendEmailNotification(idea, false);
                }
            } catch (PortalException e) {
                LOGGER.error(e.getMessage(), e);
            } catch (SystemException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }

        if (ideaContentType == IdeaContentType.IDEA_CONTENT_TYPE_PRIVATE) {
            response.setRenderParameter("type", "private");
        }
        response.setRenderParameter("urlTitle", urlTitle);
    }



    /**
     * Method handling Action request.
     *
     * @param request  the request
     * @param response the response
     * @param model    the model
     */
    @ActionMapping(params = "action=addFavorite")
    public final void addFavorite(ActionRequest request, ActionResponse response, final ModelMap model) {

        LOGGER.info("addFavorite");

        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        long companyId = themeDisplay.getCompanyId();
        long groupId = themeDisplay.getScopeGroupId();
        long userId = themeDisplay.getUserId();

        IdeaContentType ideaContentType = IdeaContentType.valueOf(ParamUtil.getString(request, "ideaContentType"));
        String urlTitle = ParamUtil.getString(request, "urlTitle", "");

        if (themeDisplay.isSignedIn()) {
            ideaService.addFavorite(companyId, groupId, userId, urlTitle);
        }

        if (ideaContentType == IdeaContentType.IDEA_CONTENT_TYPE_PRIVATE) {
            response.setRenderParameter("type", "private");
        }

        response.setRenderParameter("urlTitle", urlTitle);
    }


    /**
     * Method handling Action request.
     *
     * @param request  the request
     * @param response the response
     * @param model    the model
     */
    @ActionMapping(params = "action=addLike")
    public final void addLike(ActionRequest request, ActionResponse response, final ModelMap model) {

        LOGGER.info("addLike");

        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        long companyId = themeDisplay.getCompanyId();
        long groupId = themeDisplay.getScopeGroupId();
        long userId = themeDisplay.getUserId();

        IdeaContentType ideaContentType = IdeaContentType.valueOf(ParamUtil.getString(request, "ideaContentType"));
        String urlTitle = ParamUtil.getString(request, "urlTitle", "");

        if (themeDisplay.isSignedIn()) {
            ideaService.addLike(companyId, groupId, userId, urlTitle);
        }

        if (ideaContentType == IdeaContentType.IDEA_CONTENT_TYPE_PRIVATE) {
            response.setRenderParameter("type", "private");
        }

        response.setRenderParameter("urlTitle", urlTitle);
    }

    /**
     * Method handling Action request.
     *
     * @param request  the request
     * @param response the response
     * @param model    the model
     */
    @ActionMapping(params = "action=deleteComment")
    public final void deleteComment(ActionRequest request, ActionResponse response, final ModelMap model) {

        LOGGER.info("deleteComment");

        IdeaContentType ideaContentType = IdeaContentType.valueOf(ParamUtil.getString(request, "ideaContentType"));
        String urlTitle = ParamUtil.getString(request, "urlTitle", "");

        long commentId = ParamUtil.getLong(request, "commentId", 0);

        if (commentId != 0) {

            try {
                Idea idea = ideaService.findIdeaByUrlTitle(urlTitle);

                // TODO use permission checker to verify that user has delete permissions
                //IdeaPermissionChecker ideaPermissionChecker = ideaPermissionCheckerService.getIdeaPermissionChecker(
                //        groupId, userId, idea);


                MBMessageLocalServiceUtil.deleteDiscussionMessage(commentId);

            } catch (PortalException e) {
                LOGGER.error(e.getMessage(), e);
            } catch (SystemException e) {
                LOGGER.error(e.getMessage(), e);
            }

        }

        if (ideaContentType == IdeaContentType.IDEA_CONTENT_TYPE_PRIVATE) {
            response.setRenderParameter("type", "private");
        }

        response.setRenderParameter("urlTitle", urlTitle);

    }

    /**
     * Method handling Action request.
     *
     * @param request  the request
     * @param response the response
     * @param model    the model
     */
    @ActionMapping(params = "action=removeFavorite")
    public final void removeFavorite(ActionRequest request, ActionResponse response, final ModelMap model) {

        LOGGER.info("removeFavorite");

        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        long companyId = themeDisplay.getCompanyId();
        long groupId = themeDisplay.getScopeGroupId();
        long userId = themeDisplay.getUserId();

        IdeaContentType ideaContentType = IdeaContentType.valueOf(ParamUtil.getString(request, "ideaContentType"));
        String urlTitle = ParamUtil.getString(request, "urlTitle", "");

        if (themeDisplay.isSignedIn()) {
            ideaService.removeFavorite(companyId, groupId, userId, urlTitle);
        }

        if (ideaContentType == IdeaContentType.IDEA_CONTENT_TYPE_PRIVATE) {
            response.setRenderParameter("type", "private");
        }

        response.setRenderParameter("urlTitle", urlTitle);

    }

    /**
     * Method handling Action request.
     *
     * @param request  the request
     * @param response the response
     * @param model    the model
     */
    @ActionMapping(params = "action=removeLike")
    public final void removeLike(ActionRequest request, ActionResponse response, final ModelMap model) {

        LOGGER.info("removeLike");

        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        long companyId = themeDisplay.getCompanyId();
        long groupId = themeDisplay.getScopeGroupId();
        long userId = themeDisplay.getUserId();

        IdeaContentType ideaContentType = IdeaContentType.valueOf(ParamUtil.getString(request, "ideaContentType"));
        String urlTitle = ParamUtil.getString(request, "urlTitle", "");

        if (themeDisplay.isSignedIn()) {
            ideaService.removeLike(companyId, groupId, userId, urlTitle);
        }

        if (ideaContentType == IdeaContentType.IDEA_CONTENT_TYPE_PRIVATE) {
            response.setRenderParameter("type", "private");
        }

        response.setRenderParameter("urlTitle", urlTitle);
    }

    /**
     * Method handling Action request.
     *
     * @param request  the request
     * @param response the response
     * @param model    the model
     */
    @ActionMapping(params = "action=updateFromBarium")
    public final void updateFromBarium(ActionRequest request, ActionResponse response, final ModelMap model) {

        LOGGER.info("updateFromBarium");

        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);

        IdeaContentType ideaContentType = IdeaContentType.valueOf(ParamUtil.getString(request, "ideaContentType"));
        String id = ParamUtil.getString(request, "id");
        String urlTitle = ParamUtil.getString(request, "urlTitle", "");

        if (themeDisplay.isSignedIn()) {
            try {
                Idea idea = ideaService.find(id);
                boolean updated = ideaService.updateFromBarium(idea).isChanged();
                model.put("updateFromBariumOutcome", updated);
            } catch (UpdateIdeaException e) {
                LOGGER.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }

        if (ideaContentType == IdeaContentType.IDEA_CONTENT_TYPE_PRIVATE) {
            response.setRenderParameter("type", "private");
        }

        response.setRenderParameter("urlTitle", urlTitle);
    }

    /**
     * Upload file action.
     *
     * @param request  the request
     * @param response the response
     * @param model    the model
     * @throws FileUploadException the file upload exception
     */
    @ActionMapping(params = "action=uploadFile")
    public void uploadFile(ActionRequest request, ActionResponse response, Model model) throws FileUploadException, SystemException, PortalException {
        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        long scopeGroupId = themeDisplay.getScopeGroupId();
        long userId = themeDisplay.getUserId();

        String urlTitle = request.getParameter("urlTitle");

        String fileType = request.getParameter("fileType");
        boolean publicIdea;
        if (fileType.equals("public")) {
            publicIdea = true;
        } else if (fileType.equals("private")) {
            publicIdea = false;
        } else {

            throw new IllegalArgumentException("Unknown filetype: " + fileType);
        }

        //TODO onödig slagning? Cacha?
        Idea idea = ideaService.findIdeaByUrlTitle(urlTitle);
        //TODO Kan det inte finnas flera med samma titel i olika communities?

        boolean mayUploadFile = idea.getUserId() == userId;

        IdeaPermissionChecker ideaPermissionChecker = ideaPermissionCheckerService.getIdeaPermissionChecker(
                scopeGroupId, userId, idea);

        if (!mayUploadFile) {
            if (publicIdea){
                mayUploadFile = ideaPermissionChecker.isHasPermissionAddDocumentPublic();
            } else {
                mayUploadFile = ideaPermissionChecker.isHasPermissionAddDocumentPrivate();
            }
        }


        if (mayUploadFile){
            response.setRenderParameter("urlTitle", urlTitle);

            PortletFileUpload p = new PortletFileUpload();

            try {
                FileItemIterator itemIterator = p.getItemIterator(request);

                while (itemIterator.hasNext()) {
                    FileItemStream fileItemStream = itemIterator.next();

                    if (fileItemStream.getFieldName().equals("file")) {

                        InputStream is = fileItemStream.openStream();
                        BufferedInputStream bis = new BufferedInputStream(is);

                        String fileName = fileItemStream.getName();
                        String contentType = fileItemStream.getContentType();

                        ideaService.uploadFile(idea, publicIdea, fileName, contentType, bis);
                    }
                }
            } catch (FileUploadException e) {
                doExceptionStuff(e, response, model);
                return;
            } catch (IOException e) {
                doExceptionStuff(e, response, model);
                return;
            } catch (se.vgregion.service.innovationsslussen.exception.FileUploadException e) {
                doExceptionStuff(e, response, model);
                return;
            } catch (RuntimeException e) {
                Throwable lastCause = Util.getLastCause(e);
                if (lastCause instanceof SQLException) {
                    SQLException nextException = ((SQLException) lastCause).getNextException();
                    if (nextException != null) {
                        LOGGER.error(nextException.getMessage(), nextException);
                    }
                }
            } finally {
                response.setRenderParameter("ideaType", fileType);
            }
        } else {
            throw new FileUploadException("The user is not authorized to upload to this idea instance.");
        }

        response.setRenderParameter("urlTitle", urlTitle);
        response.setRenderParameter("type", fileType);
        response.setRenderParameter("showView", "showIdea");
    }

    private void doExceptionStuff(Exception e, ActionResponse response, Model model) {
        LOGGER.error(e.getMessage(), e);
        response.setRenderParameter("showView", "showUploadFile");
        model.addAttribute("errorMessage", "Uppladdningen misslyckades");
    }

    /**
     * Download file resource mapping.
     *
     * @param id       the id
     * @param response the response
     * @throws BariumException the barium exception
     * @throws IOException     Signals that an I/O exception has occurred.
     */
    @ResourceMapping("downloadFile")
    public void downloadFile(@RequestParam("id") String id, ResourceResponse response) throws BariumException,
            IOException {
        ObjectEntry objectEntry = ideaService.getObject(id);
        String name = objectEntry.getName();

        //set http headers to instruct the browser how it should deliver the content
        String fileType = objectEntry.getFileType();
        if (fileType == null || "".equals(fileType)) {
            fileType = "octet-stream";
        }

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/" + fileType);
        response.setProperty("Content-Disposition", "inline; filename=" + name);

        BufferedOutputStream bos = null;
        OutputStream pos = null;
        BufferedInputStream bis = null;
        InputStream is = null;
        try {
            is = ideaService.downloadFile(id);
            bis = new BufferedInputStream(is);

            pos = response.getPortletOutputStream();
            bos = new BufferedOutputStream(pos);

            final int oneKB = 1024;

            byte[] buf = new byte[oneKB];
            int n;
            while ((n = bis.read(buf)) != -1) {
                bos.write(buf, 0, n);
            }
        } finally {
            Util.closeClosables(bos, pos, bis, is);
        }
    }

    public void sendEmailNotification(Idea idea, boolean publicbody){

        if (ideaSettingsService.getSettingBoolean(ExpandoConstants.NOTIFICATION_EMAIL_ACTIVE,
                idea.getCompanyId(), idea.getGroupId())){

            JSONObject jsonObject = JSONFactoryUtil.createJSONObject();
            jsonObject.put("companyId", idea.getCompanyId());

            LinkedList<String> toEmail = ideaService.getUsersToEmail(idea);
            JSONArray emailTo = JSONFactoryUtil.createJSONArray();

            for (String email : toEmail) {
                emailTo.put(email);
            }

            jsonObject.put("emailTo", emailTo);
            jsonObject.put("emailFrom",ideaSettingsService.getSetting(ExpandoConstants.NOTIFICATION_EMAIL_FROM,
                    idea.getCompanyId(), idea.getGroupId()));
            jsonObject.put("subject",
                    replaceTokens(ideaSettingsService.getSetting(ExpandoConstants.NOTIFICATION_EMAIL_SUBJECT,
                            idea.getCompanyId(), idea.getGroupId()), idea));
            if (publicbody){
                jsonObject.put("body",
                        replaceTokens(ideaSettingsService.getSetting(ExpandoConstants.NOTIFICATION_EMAIL_PUBLIC_BODY,
                                idea.getCompanyId(), idea.getGroupId()), idea));
            } else {
                jsonObject.put("body",
                        replaceTokens(ideaSettingsService.getSetting(ExpandoConstants.NOTIFICATION_EMAIL_PRIVATE_BODY,
                                idea.getCompanyId(), idea.getGroupId()), idea));
            }

            Message message = new Message();
            message.setPayload(jsonObject);
            MessageBusUtil.sendMessage("vgr/email/notification", message);
        }

    }

    public String replaceTokens(String in, Idea idea){
        String out = "";

        String serverNameUrl = ideaSettingsService.getSetting(ExpandoConstants.SERVER_NAME_URL,
                idea.getCompanyId(), idea.getGroupId());

        in = in.replaceAll("\\[\\$PERSON_NAME\\$\\]",idea.getIdeaPerson().getName());

        String link = "<a href=\"" + serverNameUrl + idea.getUrlTitle() + "\">" + idea.getTitle() +"</a>";
        in = in.replaceAll("\\[\\$IDEA_NAME_AND_LINK\\$\\]", link);

        String urlLink =  "<a href=\"" + serverNameUrl + idea.getUrlTitle() + "\">" + serverNameUrl + idea.getUrlTitle() + "</a>";
        in = in.replaceAll("\\[\\$IDEA_URL\\$\\]",urlLink );
        out = in.replaceAll("\\[\\$IDEA_NAME\\$\\]", idea.getTitle());

        return out;
    }

}

