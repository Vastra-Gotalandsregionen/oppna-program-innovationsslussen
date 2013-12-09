package se.vgregion.service.search;

import java.util.List;
import java.util.Map;
import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;

/**
 * Created with IntelliJ IDEA.
 * User: simongoransson
 * Date: 2013-11-15
 * Time: 10:49
 * To change this template use File | Settings | File Templates.
 */
public interface SearchService {

    public Map<String, Object> getPublicIdeas(long companyId, long groupId, int start, int rows, int sort, int phase);

    public Map<String, Object> getIdeasForIdeaTransporters(long companyId, long groupId, int start, int rows, int sort, int phase, String transporter);

}
