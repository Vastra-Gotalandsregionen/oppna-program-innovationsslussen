package se.vgregion.service.search.indexer;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.search.BaseIndexer;
import com.liferay.portal.kernel.search.DocumentImpl;
import com.liferay.portal.kernel.search.SearchEngineUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portlet.messageboards.model.MBMessage;
import com.liferay.portlet.messageboards.service.MBMessageLocalServiceUtil;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.Summary;
import javax.portlet.PortletURL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaContent;
import se.vgregion.portal.innovationsslussen.domain.vo.CommentItemVO;
import se.vgregion.service.innovationsslussen.idea.IdeaService;
import se.vgregion.service.search.indexer.util.IdeaField;
import se.vgregion.service.spring.ContextUtil;

/**
 * @author Simon Göransson
 */
public class IdeaIndexer extends BaseIndexer {

    private static final Logger LOGGER = LoggerFactory.getLogger(IdeaIndexer.class);

    private static String IDEA_CLASS = "se.vgregion.portal.innovationsslussen.domain.jpa.Idea";
    public static final String[] CLASS_NAMES = {IDEA_CLASS };
    public static final String PORTLET_ID = "idea";

    private static int DEFAULT_INDEX_INTERVAL = 100;
    private static final String _DATE_FORMAT_PATTERN = "yyyyMMddHHmmss";


    IdeaService ideaService;

    public IdeaIndexer(){
        ideaService = ContextUtil.getApplicationContext().getBean(IdeaService.class);
    }


    @Override
    protected void doDelete(Object obj) throws Exception {
        Idea idea = (Idea) obj;
        Document document = new DocumentImpl();
        document.addUID(PORTLET_ID, idea.getId());

        SearchEngineUtil.deleteDocument(idea.getCompanyId(), document.get(IdeaField.UID));
    }

    @Override
    protected Document doGetDocument(Object obj) throws Exception {

        Idea idea = (Idea) obj;

        Document document = new DocumentImpl();

        //General
        document.addUID(PORTLET_ID, idea.getId());
        document.addKeyword(IdeaField.PORTLET_ID, PORTLET_ID);
        document.addKeyword(IdeaField.ENTRY_CLASS_NAME, Idea.class.getName());
        document.addKeyword(IdeaField.COMPANY_ID, idea.getCompanyId());
        document.addKeyword(IdeaField.GROUP_ID, idea.getGroupId());
        document.addKeyword(IdeaField.USER_ID, idea.getUserId());

        //Idea
        document.addKeyword(IdeaField.IDEA_ID, idea.getId());
        document.addText(IdeaField.TITLE, idea.getTitle());
        document.addText(IdeaField.URL_TITLE, idea.getUrlTitle());
        document.addKeyword(IdeaField.PHASE, idea.getPhase());
        document.addKeyword(IdeaField.STATUS, idea.getStatus().toString());

        //Idea Person
        document.addKeyword(IdeaField.VGRID, idea.getIdeaPerson().getVgrId());
        document.addKeyword(IdeaField.USER_NAME, idea.getIdeaPerson().getName());
        document.addKeyword(IdeaField.EMAIL, idea.getIdeaPerson().getEmail());


        //Idea Content
        document.addText(IdeaField.PUBLIC_INTRO, idea.getIdeaContentPublic().getIntro());
        document.addText(IdeaField.PUBLIC_DESCRIPTION, idea.getIdeaContentPublic().getDescription());
        document.addText(IdeaField.PUBLIC_SOLVES_PROBLEM, idea.getIdeaContentPublic().getSolvesProblem());
        document.addText(IdeaField.PUBLIC_WANTS_HELP_WITH, idea.getIdeaContentPublic().getWantsHelpWith());
        document.addText(IdeaField.PUBLIC_IDEA_TESTED, idea.getIdeaContentPublic().getIdeaTested());

        document.addText(IdeaField.PRIVATE_INTRO, idea.getIdeaContentPrivate().getIntro());
        document.addText(IdeaField.PRIVATE_DESCRIPTION, idea.getIdeaContentPrivate().getDescription());
        document.addText(IdeaField.PRIVATE_SOLVES_PROBLEM, idea.getIdeaContentPrivate().getSolvesProblem());
        document.addText(IdeaField.PRIVATE_WANTS_HELP_WITH, idea.getIdeaContentPrivate().getWantsHelpWith());
        document.addText(IdeaField.PRIVATE_IDEA_TESTED, idea.getIdeaContentPrivate().getIdeaTested());
        document.addText(IdeaField.PRIVATE_IDEA_TRANSPORTER, idea.getIdeaContentPrivate().getIdeTansportor());

        //Count
        document.addKeyword(IdeaField.PUBLIC_LIKES_COUNT, idea.getLikes().size());
        document.addKeyword(IdeaField.FAVOURITES_COUNT, idea.getFavorites().size());
        document.addKeyword(IdeaField.PUBLIC_COMMENT_COUNT, idea.getCommentsCount());

        //Date
        document.addDate(IdeaField.CREATE_DATE, idea.getCreated());

        //Comments
        document = indexPublicComments(idea, document);
        document = indexPrivateComments(idea, document);

        List<CommentItemVO> commentsList = null;

        return document;

    }

    private Document indexPrivateComments(Idea idea, Document document) throws SystemException {
        List<CommentItemVO> commentsList = ideaService.getPrivateComments(idea);
        document.addKeyword(IdeaField.PRIVATE_COMMENT_COUNT, commentsList.size());
        return document;
    }

    private Document indexPublicComments(Idea idea, Document document) throws PortalException, SystemException {

        if (idea.getIdeaContentPublic() != null){

            List<CommentItemVO> commentsList = ideaService.getPublicComments(idea);

            DateFormat df = new SimpleDateFormat(_DATE_FORMAT_PATTERN);

            int messagesCount = commentsList.size();

            if (messagesCount < 1) {
                return document;
            }

            // The first comment in all discussions have ClassPK
            // as subject and body
            String[] commentAuthorIds = new String[messagesCount - 1];
            String[] commentAuthorScreenNames = new String[messagesCount - 1];
            String[] commentCreateDates = new String[messagesCount - 1];
            String[] commentIds = new String[messagesCount - 1];
            String[] commentTexts = new String[messagesCount - 1];

            Date lastCommentDate = new Date(0);

            int i = 0;

            for (CommentItemVO message : commentsList) {
                if (i != 0) {
                    long userId = message.getUserId();

                    User user = UserLocalServiceUtil.getUser(userId);
                    String userScreenName = message.getName();
                    long messageId = message.getMessageId();

                    String messageBody = message.getCommentText();

                    Date messageCreateDate = message.getCreateDate();

                    if (messageCreateDate.after(lastCommentDate)){
                        lastCommentDate = messageCreateDate;
                    }

                    String messageCreateDateStr = df.format(messageCreateDate);

                    commentAuthorIds[i - 1] = String.valueOf(userId);
                    commentAuthorScreenNames[i - 1] = userScreenName;
                    commentCreateDates[i - 1] = String.valueOf(messageCreateDateStr);
                    commentIds[i - 1] = String.valueOf(messageId);
                    commentTexts[i - 1] = HtmlUtil.extractText(messageBody);

                }
                i++;
            }
            String lastCommentDateStr = df.format(lastCommentDate);

            document.addKeyword(IdeaField.PUBLIC_COMMENT_COUNT, commentsList.size());
            document.addKeyword(IdeaField.PUBLIC_LAST_COMMENT_DATE, lastCommentDateStr);

            document.addKeyword(IdeaField.PUBLIC_COMMENT_AUTHOR_IDS, commentAuthorIds);
            document.addKeyword(IdeaField.PUBLIC_COMMENT_AUTHOR_SCREEN_NAMES, commentAuthorScreenNames);
            document.addKeyword(IdeaField.PUBLIC_COMMENT_CREATE_DATES, commentCreateDates);
            document.addKeyword(IdeaField.PUBLIC_COMMENT_IDS, commentIds);
            document.addKeyword(IdeaField.PUBLIC_COMMENT_TEXTS, commentTexts);

        }

        return document;
    }

    @Override
    protected Summary doGetSummary(Document document, Locale locale, String snippet, PortletURL portletURL) throws Exception {
        return null;
    }

    @Override
    protected void doReindex(Object obj) throws Exception {
        Idea idea = (Idea) obj;
        Document document = getDocument(idea);
        SearchEngineUtil.updateDocument(idea.getCompanyId(), document);
    }

    @Override
    protected void doReindex(String className, long classPK) throws Exception {
       //We don´t use this.
    }

    @Override
    protected void doReindex(String[] ids) throws Exception {
        long companyId = GetterUtil.getLong(ids[0]);
        reindexIdeas(companyId);
    }

    @Override
    protected String getPortletId(SearchContext searchContext) {
        return PORTLET_ID;
    }

    protected void reindexIdeas(long companyId) throws Exception {

        int count = ideaService.findIdeasCountByCompanyId(companyId);
        int pages = count / DEFAULT_INDEX_INTERVAL;

        LOGGER.info("Starting reindexing cards and comments, batch size: " + DEFAULT_INDEX_INTERVAL);

        for (int i = 0; i <= pages; i++) {
            int start = (i * DEFAULT_INDEX_INTERVAL);
            int end = start + DEFAULT_INDEX_INTERVAL;

            LOGGER.info("Reindexing cards, batch: " + i);

            reindexIdeas(companyId, start, end);
        }

        LOGGER.info("Finished reindexing cards and comments. Count: " + count);
    }

    protected void reindexIdeas(long companyId, int start, int end) throws Exception {

        List<Idea> ideas = ideaService.findIdeasByCompanyId(companyId, start, end);

        if (ideas.isEmpty()) {
            return;
        }

        Collection<Document> documents = new ArrayList<Document>();

        for (Idea entry : ideas) {
            LOGGER.trace("Reindexing card: " + entry);

            Document document = getDocument(entry);
            documents.add(document);
        }

        SearchEngineUtil.updateDocuments(companyId, documents);
    }

    @Override
    public String[] getClassNames() {
        System.out.println("IdeaIndexer - CLASS_NAMES " + CLASS_NAMES[0]);
        return CLASS_NAMES;
    }
}