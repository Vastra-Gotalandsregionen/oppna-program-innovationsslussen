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
     * @param s a search query.
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
        this.setQuery("groupId:" + groupId + " AND companyId:" + companyId + " AND entryClassName:se.vgregion.portal.innovationsslussen.domain.jpa.Idea" + " AND status:PUBLIC_IDEA");
        this.setStart(start); //0
        this.setRows(rows);
        return this;
    }

    public IdeaSolrQuery findAllIdeasQuery(long companyId, long groupId, int start, int rows) {
        this.setQuery("groupId:" + groupId + " AND companyId:" + companyId + " AND entryClassName:se.vgregion.portal.innovationsslussen.domain.jpa.Idea");
        this.setStart(start); //0
        this.setRows(rows);
        this.setFacet(true);
        this.addFacetField("privateIdeaTransporter");
        this.setFacetMinCount(1);
        return this;
    }

    public IdeaSolrQuery filterIdeasOnOpen() {
        String filterEntryClassName = "status:PUBLIC_IDEA";
        this.addFilterQuery(filterEntryClassName);
        return this;
    }

    public IdeaSolrQuery filterIdeasOnClosed() {
        String filterEntryClassName = "status:PRIVATE_IDEA";
        this.addFilterQuery(filterEntryClassName);
        return this;
    }

    /**
     * Filters on entryClassName = com.liferay.portlet.journal.model.JournalArticle and phase.
     *
     * @return the actro solr query
     */
    public IdeaSolrQuery filterIdeasOnPhase(int phase) {
        String filterPhase = "phase:" + phase;
        this.addFilterQuery(filterPhase);
        return this;
    }

    /**
     * Filters on entryClassName = com.liferay.portlet.journal.model.JournalArticle and phase.
     *
     * @return the actro solr query
     */
    public IdeaSolrQuery filterIdeasOnTwoPhases(int phase1, int phase2) {
        String filterPhase = "phase:" + phase1 + " OR phase:" + phase2;
        this.addFilterQuery( filterPhase);
        return this;
    }

    /**
     * Filters on entryClassName = com.liferay.portlet.journal.model.JournalArticle and phase.
     *
     * @return the actro solr query
     */
    public IdeaSolrQuery filterIdeasOnThreePhases(int phase1, int phase2, int phase3) {
        String filterPhase = "phase:" + phase1 + " OR phase:" + phase2 + " OR phase:" + phase3;
        this.addFilterQuery(filterPhase);
        return this;
    }

    /**
     * Filters on entryClassName = com.liferay.portlet.journal.model.JournalArticle and phase.
     *
     * @return the actro solr query
     */
    public IdeaSolrQuery filterIdeasOnFourPhases(int phase1, int phase2, int phase3, int phase4) {
        String filterPhase = "phase:" + phase1 + " OR phase:" + phase2 + " OR phase:" + phase3 + " OR phase:" + phase4;
        this.addFilterQuery(filterPhase);
        return this;
    }

    /**
     * Filters on transporter
     *
     * @return the actro solr query
     */
    public IdeaSolrQuery filterIdeasOnTransporter(String transporter) {
        this.addFilterQuery("privateIdeaTransporter:\"" + transporter + "\"");
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
