package se.vgregion.service.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
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
public class SearchServiceImpl implements SearchService{

    @Autowired
    private IdeaSolrQuery ideaSolrQuery;


    public Map<String, Object> getPublicIdeas(long companyId, long groupId, int start, int rows, int sort, int phase){

        //Find all public ideas.
        ideaSolrQuery.findAllPublicIdeasQuery(companyId, groupId, start, rows);

        // Filter ideas on phase.
        switch (phase) {
            case 0: //No filter needed.
                break;
            case 2: ideaSolrQuery.filterIdeasOnTwoPhases(0,2);
                break;
            case 3: ideaSolrQuery.filterIdeasOnTwoPhases(3,4);
                break;
            case 5:  ideaSolrQuery.filterIdeasOnTwoPhases(5, 6);
                break;
        }

        switch (sort) {
            case 0: ideaSolrQuery.addSortField(IdeaField.CREATE_DATE, SolrQuery.ORDER.desc);
                break;
            case 1: ideaSolrQuery.addSortField(IdeaField.PUBLIC_COMMENT_COUNT, SolrQuery.ORDER.desc);
                break;
            case 2: ideaSolrQuery.addSortField(IdeaField.PUBLIC_LAST_COMMENT_DATE, SolrQuery.ORDER.desc);
                break;
            case 3: ideaSolrQuery.addSortField(IdeaField.PUBLIC_LIKES_COUNT, SolrQuery.ORDER.desc);
                break;
        }

        QueryResponse queryResponse = ideaSolrQuery.query();
        Map<String,Object> returnMap = parseSolrResponse(queryResponse);
        return returnMap;

    }


    public Map<String, Object> getIdeasForIdeaTransporters(long companyId, long groupId, int start, int rows, int sort, int phase, String transporter){

        ideaSolrQuery.findAllIdeasQuery(companyId, groupId, start, rows);

        switch (phase) {
            case 0: //No filter needed.
                break;
            case 2: ideaSolrQuery.filterIdeasOnTwoPhases(0,2); //Idé
               break;
            case 3: ideaSolrQuery.filterIdeasOnTwoPhases(3,4); //Mognad
                break;
            case 5:  ideaSolrQuery.filterIdeasOnTwoPhases(5,6); //Färdig
                break;
            case 10:  ideaSolrQuery.filterIdeasOnClosed(); //Stängd
                break;
        }

        switch (sort) {
            case 0: ideaSolrQuery.addSortField(IdeaField.CREATE_DATE, SolrQuery.ORDER.desc);
                break;
            case 1: ideaSolrQuery.addSortField(IdeaField.PUBLIC_COMMENT_COUNT, SolrQuery.ORDER.desc);
                break;
            case 2: ideaSolrQuery.addSortField(IdeaField.PUBLIC_LAST_COMMENT_DATE, SolrQuery.ORDER.desc);
                break;
            case 3: ideaSolrQuery.addSortField(IdeaField.PUBLIC_LIKES_COUNT, SolrQuery.ORDER.desc);
                break;
        }

        QueryResponse queryResponse = ideaSolrQuery.query();
        Map<String,Object> returnMap = parseSolrResponse(queryResponse);
        return returnMap;

    }


    private Map<String,Object> parseSolrResponse(QueryResponse queryResponse) {

        Map<String,Object> returnMap = new HashMap<String, Object>();
        List<Idea> ideaList = new ArrayList<Idea>();

        SolrDocumentList solrDocumentList = queryResponse.getResults();

        for (SolrDocument entries : solrDocumentList) {
            entries.get(IdeaField.UID);

            Idea idea = new Idea();

            idea.setId((String) entries.getFieldValue(IdeaField.IDEA_ID));
            idea.setTitle((String) entries.getFieldValue(IdeaField.TITLE));
            idea.setUrlTitle((String) entries.getFieldValue(IdeaField.URL_TITLE));
            idea.setPhase((String) entries.getFieldValue(IdeaField.PHASE));

            String status = (String) entries.getFieldValue(IdeaField.STATUS);

            IdeaStatus ideaStatus;
            String commentCountStr;

            if (status.equals("PUBLIC_IDEA")) {
                ideaStatus = IdeaStatus.PUBLIC_IDEA;
                commentCountStr = (String) entries.getFieldValue(IdeaField.PUBLIC_COMMENT_COUNT);
            } else {
                ideaStatus = IdeaStatus.PRIVATE_IDEA;
                commentCountStr = (String) entries.getFieldValue(IdeaField.PRIVATE_COMMENT_COUNT);
            }

            if (commentCountStr != null && !commentCountStr.isEmpty()){
                idea.setCommentsCount(Integer.parseInt(commentCountStr));
            } else {
                idea.setCommentsCount(0);
            }

            idea.setStatus(ideaStatus);
            IdeaContent ideaContent = new IdeaContent();
            ideaContent.setIntro((String) entries.getFieldValue(IdeaField.PUBLIC_INTRO));
            ideaContent.setDescription((String) entries.getFieldValue(IdeaField.PUBLIC_DESCRIPTION));

            Set<IdeaUserLike> likes = idea.getLikes();

            String likeCountStr = (String) entries.getFieldValue(IdeaField.PUBLIC_LIKES_COUNT);
            int likeCount = Integer.parseInt(likeCountStr);

            for (int i = 0; i < likeCount; i++) {
                likes.add(new IdeaUserLike());
            }



            ideaContent.setType(IdeaContentType.IDEA_CONTENT_TYPE_PUBLIC);
            idea.getIdeaContents().add(ideaContent);

           // idea.setIdeaContentPublic();
            ideaList.add(idea);
        }

        returnMap.put("ideas", ideaList);
        returnMap.put("totalIdeasCount", queryResponse.getResults().getNumFound());

        return returnMap;
    }
}
