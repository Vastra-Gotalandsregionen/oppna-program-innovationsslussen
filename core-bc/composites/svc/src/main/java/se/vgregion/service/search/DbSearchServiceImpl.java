package se.vgregion.service.search;

import org.apache.solr.client.solrj.response.FacetField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;
import se.vgregion.service.innovationsslussen.repository.idea.IdeaRepository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DbSearchServiceImpl implements SearchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DbSearchServiceImpl.class);

    @Autowired
    private IdeaRepository ideaRepository;

    public Map<String, Object> getPublicVisibleIdeas(long companyId, long groupId, int start, int rows, int sort, int phase) {
        return getVisibleIdeasForIdeaTransporters(companyId, groupId, start, rows, sort, phase, 2, "0");
    }


    public Map<String, Object> getVisibleIdeasForIdeaTransporters(long companyId, long groupId, int start, int rows, int sort, int phase, int visible, String transporter) {

        List<Idea> ideas = ideaRepository.findVisibleIdeasForIdeaTransporters(companyId, groupId, start, rows, sort, phase, visible, transporter);
        List<Idea> forCountAndFacets = ideaRepository.findVisibleIdeasForIdeaTransporters(companyId, groupId, 0, Integer.MAX_VALUE, sort, phase, visible, transporter);

        Map<String, Long> collect = new HashMap<>();
        for (Idea forCountAndFacet : forCountAndFacets) {
            String ideTansportor = forCountAndFacet.getIdeaContentPrivate().getIdeTansportor();
            if (ideTansportor != null && !"".equals(ideTansportor)) {
                if (collect.containsKey(ideTansportor)) {
                    collect.put(ideTansportor, collect.get(ideTansportor) + 1);
                } else {
                    collect.put(ideTansportor, 1L);
                }
            }
        }

        FacetField facetField = new FacetField("privateIdeaTransporter");
        for (Map.Entry<String, Long> stringLongEntry : collect.entrySet()) {
            facetField.add(stringLongEntry.getKey(), stringLongEntry.getValue());
        }

        Map<String, Object> returnMap = new HashMap<>();// parseSolrResponse(queryResponse, false);

        returnMap.put("totalIdeasCount", (long) forCountAndFacets.size());
        returnMap.put("ideas", ideas);
        returnMap.put("ideaTranspoterFacets", Arrays.asList(facetField));

        // Map with:
        // "ideaTranspoterFacets" mapped to List<FacetField> with facet name "privateIdeaTransporter"
        // "totalIdeasCount" mapped to Long
        // "ideas" mapped to List<Idea>
        return returnMap;
    }

}
