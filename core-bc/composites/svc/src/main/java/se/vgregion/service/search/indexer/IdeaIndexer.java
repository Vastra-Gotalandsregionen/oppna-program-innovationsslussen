package se.vgregion.service.search.indexer;

import com.liferay.portal.kernel.search.BaseIndexer;
import com.liferay.portal.kernel.search.DocumentImpl;
import com.liferay.portal.kernel.search.SearchEngineUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;


import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.Summary;
import javax.portlet.PortletURL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;
import se.vgregion.service.innovationsslussen.idea.IdeaService;
import se.vgregion.service.innovationsslussen.idea.IdeaServiceImpl;
import se.vgregion.service.search.indexer.util.Field;
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

        SearchEngineUtil.deleteDocument(idea.getCompanyId(), document.get(Field.UID));
    }

    @Override
    protected Document doGetDocument(Object obj) throws Exception {

        Idea idea = (Idea) obj;

        Document document = new DocumentImpl();

        document.addUID(PORTLET_ID, idea.getId());
        document.addDate(Field.CREATED, idea.getCreated());
        document.addKeyword(Field.COMPANY_ID, idea.getCompanyId());
        document.addKeyword(Field.PORTLET_ID, PORTLET_ID);
        document.addKeyword(Field.GROUP_ID, idea.getGroupId());
        document.addKeyword(Field.USER_ID, idea.getUserId());
        document.addText(Field.USER_NAME, idea.getIdeaPerson().getName());
        document.addText(Field.URL_TITLE, idea.getUrlTitle());
        document.addText(Field.TITLE, idea.getTitle());
        document.addText(Field.CONTENT, idea.getIdeaContentPrivate().getDescription());
        document.addKeyword(Field.ENTRY_CLASS_NAME, Idea.class.getName());

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
        throw new Exception("Indexing not supported for Idea with String className and long classPK ");
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
        return CLASS_NAMES;
    }
}