package se.vgregion.service.search;

import java.io.IOException;

import java.net.MalformedURLException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.common.util.NamedList;
/**
 * Created with IntelliJ IDEA.
 * User: simongoransson
 * Date: 2013-12-04
 * Time: 16:17
 * To change this template use File | Settings | File Templates.
 */

public class BasicAuthSolrServer extends SolrServer {

    private CommonsHttpSolrServer solrServer;

    public BasicAuthSolrServer(String username, String password, String url)
            throws MalformedURLException {

        HttpClient httpClient = new HttpClient( new MultiThreadedHttpConnectionManager());

        if (username != null && password != null) {
            HttpState httpState = httpClient.getState();
            httpState.setCredentials( AuthScope.ANY, new UsernamePasswordCredentials(username, password));
            HttpClientParams httpClientParams = httpClient.getParams();
            httpClientParams.setAuthenticationPreemptive(true);
        }

        solrServer = new CommonsHttpSolrServer(url, httpClient);
    }

    @Override
    public NamedList<Object> request(SolrRequest request) throws SolrServerException, IOException {
        return solrServer.request(request);
    }
}