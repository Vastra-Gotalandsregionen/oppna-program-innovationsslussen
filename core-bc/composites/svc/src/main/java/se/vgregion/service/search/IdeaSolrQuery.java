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

package se.vgregion.service.search;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;

import com.liferay.portal.kernel.search.Field;

/**
 * The Class IdeaSolrQuery is a extension of SolrQuery, so it is a query. IdeaSolrQuery holds an connection to
 * the solr server and therefore it can preform a search in it self and return the search result.
 * 
 * @author Simon Göransson vgrid=simgo3
 */
public class IdeaSolrQuery extends SolrQuery {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The Constant LOG. */
    private static final Log LOG = LogFactory.getLog(IdeaSolrQuery.class);

    private SolrServer solrServer;

    /**
     * Instantiates a new IdeaSolrQuery.
     * 
     * @param solrServer the solr server
     */
    public IdeaSolrQuery(SolrServer solrServer) {
        this.solrServer = solrServer;
    }

    /**
     * Instantiates a new IdeaSolrQuery.
     */
    public IdeaSolrQuery() {
        super();
    }

    /**
     * Instantiates a new IdeaSolrQuery.
     * 
     * @param s
     *            a search query.
     */
    public IdeaSolrQuery(String s) {
        super(s);
    }

    /**
     * This method finds all the journal articles.
     * 
     * @return it self.
     */
    public IdeaSolrQuery findAllPublicIdeasQuery(long companyId, long groupId, int start, int rows) {
        this.setQuery("groupId:" + groupId + " AND companyId:" + companyId + " AND status:PUBLIC_IDEA");
        this.setStart(start); //0
        this.setRows(rows);
        return this;
    }


    /**
     * Filters on entryClassName = com.liferay.portlet.journal.model.JournalArticle.
     *
     * @return the actro solr query
     */
    public IdeaSolrQuery filterIdeas() {
        String filterEntryClassName = "entryClassName:se.vgregion.portal.innovationsslussen.domain.jpa.Idea";
        this.addFilterQuery(filterEntryClassName);
        return this;
    }

    /**
     * Filters on entryClassName = com.liferay.portlet.journal.model.JournalArticle and phase.
     *
     * @return the actro solr query
     */
    public IdeaSolrQuery filterIdeasOnPhase(int phase) {
        String filterEntryClassName = "entryClassName:se.vgregion.portal.innovationsslussen.domain.jpa.Idea";
        String filterPhase = "and phase:" + phase;
        this.addFilterQuery(filterEntryClassName + filterPhase);
        return this;
    }

    /**
     * Filters on entryClassName = com.liferay.portlet.journal.model.JournalArticle and phase.
     *
     * @return the actro solr query
     */
    public IdeaSolrQuery filterIdeasOnTwoPhases(int phase1, int phase2) {
        String filterEntryClassName = "entryClassName:se.vgregion.portal.innovationsslussen.domain.jpa.Idea";
        String filterPhase = "and phase:" + phase1 + " or phase:" + phase2;
        this.addFilterQuery(filterEntryClassName + filterPhase);
        return this;
    }

    //************************ OLD ******************************************

    /**
     * Use this method to perform a weighted search for an idea.
     * 
     * @param q
     *            the search term.
     * @return it self.
     */
    public IdeaSolrQuery actorQuery(String q) {
        this.setQuery(q);
        this.setQueryType("dismax");
        this.set("qf", "assetTagNames^1000 title^100 actor-name^100 org-name^100 intro^10 description^1 ");
        this.set("mm", "50%");
        this.set("fl", "*");
        setHighlightingParameters();
        return this;
    }

    private void setHighlightingParameters() {
        this.set("hl", true);
        this.set("hl.fl", "assetTagNames title intro description");
        this.set("hl.snippets", 3);
        this.set("hl.mergeContiguous", true);
        this.set("hl.simple.pre", "<span class=\"hit\">");
        this.set("hl.simple.post", "</span>");

    }

    /**
     * Use this method to perform a More like this search. Finds documents that are similar to an already indexed
     * document or a posted text.
     * 
     * @param text
     *            the text
     * @return the actro solr query
     */
    public IdeaSolrQuery moreLikeThis(String text) {
        this.setQuery("entryClassPK:" + text);
        this.moreLikeThis();
        return this;
    }

    /**
     * Use this method to perform a More like this search. Finds documents that are similar to an already indexed
     * document or a posted text.
     * 
     * @return the actro solr query
     */
    public IdeaSolrQuery moreLikeThis() {
        this.setQueryType("mlt");
        this.set("mlt.fl", "assetTagNames content");
        this.set("mlt.bf", "assetTagNames^10 content^1");
        this.set("mlt.interestingTerms", "none");
        this.set("mlt.mintf", 0);
        this.set("fl", "assetTagNames content");
        this.set("rows", "5");
        return this;
    }

    /**
     * Filters on a category.
     * 
     * @param category
     *            the category
     * 
     * @return the actro solr query
     */
    public IdeaSolrQuery filterActorsOnCategory(String category) {

        String filter = "Category" + ":" + category;
        this.addFilterQuery(filter);

        return this;
    }

    /**
     * This method performs the search using the class itself as the search query. It also clear it self after
     * completing the search.
     * 
     * @return the query response as a result of the performed search.
     */
    public QueryResponse query() {

        QueryResponse response = null;

        try {
            response = solrServer.query(this);
        } catch (SolrServerException e) {
            LOG.error("Server error: {}", e.getCause());
        }

        // Clear query for next query
        clear();

        return response;

    }

}
