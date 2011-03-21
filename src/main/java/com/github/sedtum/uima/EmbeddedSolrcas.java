package com.github.sedtum.uima;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.core.CoreContainer;
import org.apache.uima.solrcas.SolrCASConsumer;

import java.net.URL;

/**
 * A {@link SolrCASConsumer} which runs on top of the {@link EmbeddedSolrServer}
 */

public class EmbeddedSolrcas extends SolrCASConsumer {

  @Override
  protected SolrServer createServer() throws SolrServerException {
    SolrServer solrServer = null;
    try {
      String solrInstanceTypeParam = String.valueOf(getContext().
              getConfigParameterValue("solrInstanceType"));

      String solrPathParam = String.valueOf(getContext().
              getConfigParameterValue("solrPath"));

      if (solrInstanceTypeParam.equals("embedded")) {
        URL solrURL = getURL(solrPathParam);
        System.setProperty("solr.solr.home", solrURL.getFile());
        CoreContainer.Initializer initializer = new CoreContainer.Initializer();
        CoreContainer coreContainer = initializer.initialize();
        solrServer = new EmbeddedSolrServer(coreContainer, "");
      }
    } catch (Exception e){
      throw new SolrServerException(e);
    }
    return solrServer;
  }
}
