package se.vgregion.portal.innovationsslussen.idea.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.liferay.portal.util.PortalUtil;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.portlet.PortletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaContent;
import se.vgregion.portal.innovationsslussen.domain.json.ObjectEntry;
import se.vgregion.portal.innovationsslussen.domain.vo.CommentItemVO;
import se.vgregion.service.barium.BariumException;
import se.vgregion.service.innovationsslussen.exception.UpdateIdeaException;
import se.vgregion.service.innovationsslussen.idea.IdeaService;
import se.vgregion.service.innovationsslussen.idea.permission.IdeaPermissionChecker;
import se.vgregion.service.innovationsslussen.idea.permission.IdeaPermissionCheckerService;
import se.vgregion.service.innovationsslussen.ldap.LdapService;
import se.vgregion.util.Util;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.messageboards.model.MBMessageDisplay;
import com.liferay.portlet.messageboards.model.MBThread;
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

    IdeaService ideaService;
    IdeaPermissionCheckerService ideaPermissionCheckerService;
    LdapService ldapService;

    private static final Logger LOGGER = LoggerFactory.getLogger(IdeaViewController.class.getName());

    /**
     * Constructor.
     */
    @Autowired
    public IdeaViewController(IdeaService ideaService, IdeaPermissionCheckerService ideaPermissionCheckerService) {
        this.ideaService = ideaService;
        this.ideaPermissionCheckerService = ideaPermissionCheckerService;
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
    public String showIdea(RenderRequest request, RenderResponse response, final ModelMap model) {
        System.out.println("ldapService " + ldapService);

        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        long scopeGroupId = themeDisplay.getScopeGroupId();
        long companyId = themeDisplay.getCompanyId();
        long userId = themeDisplay.getUserId();
        boolean isSignedIn = themeDisplay.isSignedIn();
        String ideaType = ParamUtil.getString(request, "type", "public");
        String urlTitle = ParamUtil.getString(request, "urlTitle", "");

        String returnView = "view_public";

        if (!urlTitle.equals("")){
            Idea idea = ideaService.findIdeaByUrlTitle(urlTitle);
            if (idea != null){

                boolean isIdeaUserLiked = ideaService.getIsIdeaUserLiked(companyId, scopeGroupId, userId, urlTitle);
                boolean isIdeaUserFavorite = ideaService.getIsIdeaUserFavorite(companyId, scopeGroupId, userId, urlTitle);
                IdeaContent visibility = idea.getIdeaContentPrivate();

                List<CommentItemVO> commentsList = null;

                if (ideaType.equals("private")) {
                    commentsList = ideaService.getPrivateComments(idea);
                } else {
                    commentsList = ideaService.getPublicComments(idea);
                }

                IdeaPermissionChecker ideaPermissionChecker = ideaPermissionCheckerService.getIdeaPermissionChecker(
                        scopeGroupId, userId, idea);

                model.addAttribute("idea", idea);
                model.addAttribute("commentsList", commentsList);
                model.addAttribute("commentsDelta", 1);
                model.addAttribute("isIdeaUserFavorite", isIdeaUserFavorite);
                model.addAttribute("isIdeaUserLiked", isIdeaUserLiked);
                model.addAttribute("urlTitle", urlTitle);

                model.addAttribute("isSignedIn", isSignedIn);
                model.addAttribute("ideaPermissionChecker", ideaPermissionChecker);

                model.addAttribute("isIdeaOwner", idea.getUserId() == userId);
                model.addAttribute("ideaType", ideaType);

                if (ideaType.equals("private")  && (ideaPermissionChecker.getHasPermissionViewIdeaPrivate() || idea.getUserId() == userId)) {
                    returnView = "view_private";
                }

                // If a user trying to access the public or private part of an idea that still is private.
                // The user dose not have premmisions and is not the creator of the idea.
                // Showing 404 view.
                if ( !idea.getIsPublic() && !(ideaPermissionChecker.getHasPermissionViewIdeaPrivate() || idea.getUserId() == userId)) {
                    HttpServletResponse httpServletResponse = PortalUtil.getHttpServletResponse(response);
                    httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    returnView = "idea_404";
                }
            } else {
                HttpServletResponse httpServletResponse = PortalUtil.getHttpServletResponse(response);
                httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
                returnView = "idea_404";
            }
        }

        return returnView;
    }

    @RenderMapping(params = "showView=showUploadFile")
    public String uploadFile(RenderRequest request, RenderResponse response, Model model) {
        model.addAttribute("urlTitle", request.getParameter("urlTitle"));
        model.addAttribute("ideaType", request.getParameter("ideaType"));
        return "upload_file";
    }

    /**
     * The default render method.
     *
     * @param request  the request
     * @param response the response
     * @param model    the model
     * @return the view
     */
    /*
    @RenderMapping(params = "type=private")
    public String showIdeaPrivate(RenderRequest request, RenderResponse response, final ModelMap model) {

    	ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        long scopeGroupId = themeDisplay.getScopeGroupId();
        long companyId = themeDisplay.getCompanyId();
        long userId = themeDisplay.getUserId();
        boolean isSignedIn = themeDisplay.isSignedIn();

        String urlTitle = ParamUtil.getString(request, "urlTitle", "");

        if(!urlTitle.equals("") && isSignedIn) {
            Idea idea = ideaService.findIdeaByUrlTitle(urlTitle);
            boolean isIdeaUserFavorite = ideaService.getIsIdeaUserFavorite(companyId, scopeGroupId, userId, urlTitle);
            boolean isIdeaUserLiked = ideaService.getIsIdeaUserLiked(companyId, scopeGroupId, userId, urlTitle);

            model.addAttribute("idea", idea);
            model.addAttribute("isIdeaUserFavorite", isIdeaUserFavorite);
            model.addAttribute("isIdeaUserLiked", isIdeaUserLiked);
        }

        return "view_private";
    }
     */

    /**
     * Method handling Action request.
     *
     * @param request  the request
     * @param response the response
     * @param model    the model
     */
    @ActionMapping("someAction")
    public final void someAction(ActionRequest request, ActionResponse response, final ModelMap model) {
        System.out.println("someAction");
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

        System.out.println("addComment");

        LOGGER.info("addComment");

        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        long companyId = themeDisplay.getCompanyId();
        long groupId = themeDisplay.getScopeGroupId();
        long userId = themeDisplay.getUserId();

        IdeaContentType ideaContentType = IdeaContentType.valueOf(ParamUtil.getString(request, "ideaContentType"));
        String urlTitle = ParamUtil.getString(request, "urlTitle", "");
        String comment = ParamUtil.getString(request, "comment", "");

        if (!comment.equals("")) {

            try {
                Idea idea = ideaService.findIdeaByUrlTitle(urlTitle);

                long ideaCommentClassPK = -1;

                if (ideaContentType == IdeaContentType.IDEA_CONTENT_TYPE_PUBLIC) {
                    ideaCommentClassPK = idea.getIdeaContentPublic().getId();
                } else if (ideaContentType == IdeaContentType.IDEA_CONTENT_TYPE_PRIVATE) {
                    ideaCommentClassPK = idea.getIdeaContentPrivate().getId();
                }

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

                String commentSubject = comment;
                commentSubject = StringUtil.shorten(commentSubject, 50);
                commentSubject += "...";

                // TODO - validate comment and preserve line breaks
                MBMessageLocalServiceUtil.addDiscussionMessage(
                        userId, user.getScreenName(), groupId,
                        IdeaContent.class.getName(), ideaCommentClassPK, threadId,
                        rootThreadId, commentSubject, commentContentCleaned,
                        serviceContext);

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

        System.out.println("addFavorite");
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

        System.out.println("addLike");
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

        System.out.println("deleteComment");

        LOGGER.info("deleteComment");

        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        long companyId = themeDisplay.getCompanyId();
        long groupId = themeDisplay.getScopeGroupId();
        long userId = themeDisplay.getUserId();

        IdeaContentType ideaContentType = IdeaContentType.valueOf(ParamUtil.getString(request, "ideaContentType"));
        String urlTitle = ParamUtil.getString(request, "urlTitle", "");

        long commentId = ParamUtil.getLong(request, "commentId", 0);

        if (commentId != 0) {

            try {
                Idea idea = ideaService.findIdeaByUrlTitle(urlTitle);

                // TODO: use permission checker to verify that user has delete permissions
                IdeaPermissionChecker ideaPermissionChecker = ideaPermissionCheckerService.getIdeaPermissionChecker(
                        groupId, userId, idea);


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

        System.out.println("removeFavorite");
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

        System.out.println("removeLike");
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

        System.out.println("updateFromBarium");
        LOGGER.info("updateFromBarium");

        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);

        IdeaContentType ideaContentType = IdeaContentType.valueOf(ParamUtil.getString(request, "ideaContentType"));
        String id = ParamUtil.getString(request, "id");
        String urlTitle = ParamUtil.getString(request, "urlTitle", "");

        if (themeDisplay.isSignedIn()) {
            System.out.println("Should now do update");

            try {
                Idea idea = ideaService.find(id);
                ideaService.updateFromBarium(idea);
            } catch (UpdateIdeaException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }

        if (ideaContentType == IdeaContentType.IDEA_CONTENT_TYPE_PRIVATE) {
            response.setRenderParameter("type", "private");
        }

        response.setRenderParameter("urlTitle", urlTitle);
    }

    @ActionMapping(params = "action=uploadFile")
    public void uploadFile(ActionRequest request, ActionResponse response, Model model) throws FileUploadException {
        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
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

        // todo onödig slagning? Cacha?
        Idea idea = ideaService.findIdeaByUrlTitle(urlTitle);// todo Kan det inte finnas flera med samma titel i olika communities?

        boolean isIdeaOwner = idea.getUserId() == userId;

        if (!isIdeaOwner) {
            throw new FileUploadException("The user is not authorized to upload to this idea instance.");
        }

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
            Throwable lastCause = getLastCause(e);
            if (lastCause instanceof SQLException) {
                SQLException nextException = ((SQLException) lastCause).getNextException();
                if (nextException != null) {
                    LOGGER.error(nextException.getMessage(), nextException);
                }
            }
        } finally {
            response.setRenderParameter("ideaType", fileType);
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

            byte[] buf = new byte[1024];
            int n;
            while ((n = bis.read(buf)) != -1) {
                bos.write(buf, 0, n);
            }
        } finally {
            Util.closeClosables(bos, pos, bis, is);
        }
    }
}

