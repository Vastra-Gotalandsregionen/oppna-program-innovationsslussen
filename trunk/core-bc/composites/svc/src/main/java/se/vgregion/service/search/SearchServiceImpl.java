package se.vgregion.service.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.vgregion.portal.innovationsslussen.domain.IdeaContentType;
import se.vgregion.portal.innovationsslussen.domain.IdeaStatus;
import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaContent;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaUserLike;
import se.vgregion.service.search.indexer.util.IdeaField;

/**
 * Created with IntelliJ IDEA.
 * User: simongoransson
 * Date: 2013-11-15
 * Time: 10:50
 * To change this template use File | Settings | File Templates.
 */
@Service
public class SearchServiceImpl implements SearchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchServiceImpl.class);

    @Autowired
    private IdeaSolrQuery ideaSolrQuery;


    public Map<String, Object> getPublicVisibleIdeas(long companyId, long groupId, int start, int rows, int sort, int phase) {

        ideaSolrQuery.findAllPublicVisibleIdeasQuery(companyId, groupId, start, rows);

        // Filter ideas on phase.
        switch (phase) {
            case 0: //No filter needed.
                break;
            case 2:
                ideaSolrQuery.filterIdeasOnPhases(0, 1, 2);
                break;
            case 3:
                ideaSolrQuery.filterIdeasOnTwoPhases(3, 4);
                break;
            case 5:
                ideaSolrQuery.filterIdeasOnTwoPhases(5, 6);
                break;
        }

        switch (sort) {
            case 0:
                ideaSolrQuery.addSortField(IdeaField.CREATE_DATE, SolrQuery.ORDER.desc);
                break;
            case 1:
                ideaSolrQuery.addSortField(IdeaField.PUBLIC_COMMENT_COUNT, SolrQuery.ORDER.desc);
                break;
            case 2:
                ideaSolrQuery.addSortField(IdeaField.PUBLIC_LAST_COMMENT_DATE, SolrQuery.ORDER.desc);
                break;
            case 3:
                ideaSolrQuery.addSortField(IdeaField.PUBLIC_LIKES_COUNT, SolrQuery.ORDER.desc);
                break;
        }

        QueryResponse queryResponse = ideaSolrQuery.query();
        Map<String, Object> returnMap = parseSolrResponse(queryResponse, true);
        return returnMap;
    }


    public Map<String, Object> getVisibleIdeasForIdeaTransporters(long companyId, long groupId, int start, int rows, int sort, int phase, int visible, String transporter) {

        ideaSolrQuery.findAllVisibleIdeasQuery(companyId, groupId, start, rows);

        switch (phase) {
            case 0: //No filter needed.
                break;
            case 1:
                ideaSolrQuery.filterIdeasOnPhases(0, 1, 2); //Idé
                break;
            case 2:
                ideaSolrQuery.filterIdeasOnTwoPhases(3, 4); //Mognad
                break;
            case 3:
                ideaSolrQuery.filterIdeasOnTwoPhases(5, 6); //Färdig
                break;
        }

        switch (visible) {
            case 0: //No filter needed.
                break;
            case 1:
                ideaSolrQuery.filterIdeasOnClosed(); //Öppna idéer
                break;
            case 2:
                ideaSolrQuery.filterIdeasOnOpen(); //Stängda idéer
                break;
        }

        switch (sort) {
            case 0:
                ideaSolrQuery.addSortField(IdeaField.CREATE_DATE, SolrQuery.ORDER.desc);
                break;
            case 1:
                ideaSolrQuery.addSortField(IdeaField.PRIVATE_COMMENT_COUNT, SolrQuery.ORDER.desc);
                break;
            case 2:
                ideaSolrQuery.addSortField(IdeaField.PRIVATE_LAST_COMMENT_DATE, SolrQuery.ORDER.desc);
                break;
        }

        if (!transporter.equals("0")) {
            String[] split = transporter.split("\\(");
            String ideaTranspoterFilter = split[0].substring(0, split[0].length() - 1);
            ideaSolrQuery.filterIdeasOnTransporter(ideaTranspoterFilter);
        }

        QueryResponse queryResponse = ideaSolrQuery.query();
        Map<String, Object> returnMap = parseSolrResponse(queryResponse, false);

        return returnMap;
    }


    private Map<String, Object> parseSolrResponse(QueryResponse queryResponse, boolean getForPublicView) {

        Map<String, Object> returnMap = new HashMap<String, Object>();
        List<Idea> ideaList = new ArrayList<Idea>();

        SolrDocumentList solrDocumentList = queryResponse.getResults();

        for (SolrDocument document : solrDocumentList) {
            document.get(IdeaField.UID);

            Idea idea = new Idea();

            String fieldName = IdeaField.IDEA_ID;
            idea.setId(getFieldValue(document, fieldName));
            idea.setTitle(getFieldValue(document, IdeaField.TITLE));
            idea.setUrlTitle(getFieldValue(document, IdeaField.URL_TITLE));
            idea.setPhase(getFieldValue(document, IdeaField.PHASE));

            String status = getFieldValue(document, IdeaField.STATUS);

            IdeaStatus ideaStatus;

            if (status.equals("PUBLIC_IDEA")) {
                ideaStatus = IdeaStatus.PUBLIC_IDEA;
            } else {
                ideaStatus = IdeaStatus.PRIVATE_IDEA;
            }

            //Comment count.
            if (getForPublicView) {
                idea.setCommentsCount((Integer) document.getFieldValue(IdeaField.PUBLIC_COMMENT_COUNT));
            } else {
                idea.setCommentsCount((Integer) document.getFieldValue(IdeaField.PRIVATE_COMMENT_COUNT));
            }

            idea.setStatus(ideaStatus);
            IdeaContent ideaContent = new IdeaContent();
            ideaContent.setIntro(getFieldValue(document, IdeaField.PUBLIC_INTRO));
            ideaContent.setDescription(getFieldValue(document, IdeaField.PUBLIC_DESCRIPTION));

            Set<IdeaUserLike> likes = idea.getLikes();

            int likeCount = (Integer) document.getFieldValue(IdeaField.PUBLIC_LIKES_COUNT);

            for (int i = 0; i < likeCount; i++) {
                likes.add(new IdeaUserLike());
            }

            ideaContent.setType(IdeaContentType.IDEA_CONTENT_TYPE_PUBLIC);
            idea.getIdeaContents().add(ideaContent);

            // idea.setIdeaContentPublic();
            ideaList.add(idea);
        }

        List<FacetField> limitingFacets = queryResponse.getLimitingFacets();
        if (limitingFacets != null && limitingFacets.isEmpty()) {
            limitingFacets = queryResponse.getFacetFields();
        }

        returnMap.put("ideaTranspoterFacets", limitingFacets);
        returnMap.put("ideas", ideaList);
        returnMap.put("totalIdeasCount", queryResponse.getResults().getNumFound());

        return returnMap;
    }

    private String getFieldValue(SolrDocument document, String fieldName) {
        Object fieldValue = document.getFieldValue(fieldName);

        if (fieldValue instanceof String) {
            return (String) fieldValue;
        } else if (fieldValue instanceof List && ((List) fieldValue).size() == 1) {
            return (String) ((List) fieldValue).get(0);
        } else if (fieldValue == null) {
            return null;
        } else {
            throw new RuntimeException("Unexpected fieldValue. This type of fieldValue needs to be implemented for.");
        }
    }
}
