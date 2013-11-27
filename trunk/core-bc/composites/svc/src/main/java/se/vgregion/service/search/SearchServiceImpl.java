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
import se.vgregion.portal.innovationsslussen.domain.IdeaStatus;
import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaContent;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaUserLike;
import se.vgregion.service.search.indexer.util.Field;

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

        Map<String,Object> returnMap = new HashMap<String, Object>();
        List<Idea> ideaList = new ArrayList<Idea>();
        ideaSolrQuery.findAllPublicIdeasQuery(companyId, groupId, start, rows);

        switch (phase) {
            case 0: ideaSolrQuery.filterIdeas();
                break;
            case 2: ideaSolrQuery.filterIdeasOnTwoPhases(0,2);
                break;
            case 3: ideaSolrQuery.filterIdeasOnTwoPhases(3,4);
                break;
            case 5:  ideaSolrQuery.filterIdeasOnPhase(5);
                break;
        }

        switch (sort) {
            case 0: ideaSolrQuery.addSortField(Field.CREATE_DATE, SolrQuery.ORDER.desc);
                break;
            case 1: ideaSolrQuery.addSortField(Field.PUBLIC_LIKES_COUNT, SolrQuery.ORDER.desc);
                break;
            case 2: ideaSolrQuery.addSortField(Field.PUBLIC_COMMENT_COUNT, SolrQuery.ORDER.desc);
                break;
            case 3: ideaSolrQuery.addSortField(Field.PUBLIC_LAST_COMMENT_DATE, SolrQuery.ORDER.desc);
                break;
        }

        QueryResponse queryResponse = ideaSolrQuery.query();
        SolrDocumentList solrDocumentList = queryResponse.getResults();

        for (SolrDocument entries : solrDocumentList) {
            entries.get(Field.UID);

            Idea idea = new Idea();

            idea.setId((String) entries.getFieldValue(Field.IDEA_ID));
            idea.setTitle((String) entries.getFieldValue(Field.TITLE));
            idea.setUrlTitle((String) entries.getFieldValue(Field.URL_TITLE));
            idea.setPhase((String) entries.getFieldValue(Field.PHASE));

            String status = (String) entries.getFieldValue(Field.STATUS);
            IdeaStatus ideaStatus = IdeaStatus.PRIVATE_IDEA;

            if (status.equals("PUBLIC_IDEA")) {
                ideaStatus = IdeaStatus.PUBLIC_IDEA;
            }

            idea.setStatus(ideaStatus);
            IdeaContent ideaContent = new IdeaContent();
            ideaContent.setIntro((String) entries.getFieldValue(Field.PUBLIC_INTRO));

            Set<IdeaUserLike> likes = idea.getLikes();

            String likeCountStr = (String) entries.getFieldValue(Field.PUBLIC_LIKES_COUNT);
            int likeCount = Integer.parseInt(likeCountStr);

            for (int i = 0; i < likeCount; i++) {
                likes.add(new IdeaUserLike());
            }

            String commentCountStr = (String) entries.getFieldValue(Field.PUBLIC_COMMENT_COUNT);

            if (commentCountStr != null && !commentCountStr.isEmpty()){
                idea.setCommentsCount(Integer.parseInt(commentCountStr));
            } else {
                idea.setCommentsCount(0);
            }


            idea.setIdeaContentPublic(ideaContent);
            ideaList.add(idea);
        }

        returnMap.put("ideas", ideaList);
        returnMap.put("totalIdeasCount", queryResponse.getResults().getNumFound());

        return returnMap;

    }


}
