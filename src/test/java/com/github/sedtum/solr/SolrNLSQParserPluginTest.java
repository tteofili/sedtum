package com.github.sedtum.solr;

import org.apache.solr.search.QParser;
import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * Testcase for {@link SolrNLSQParserPlugin}
 *
 * @author tommaso
 * @version $Id$
 */
public class SolrNLSQParserPluginTest {
  @Test
  public void testSimple() {
    try {
      SolrNLSQParserPlugin solrNLSQParserPlugin = new SolrNLSQParserPlugin();
      QParser nlsQParser = solrNLSQParserPlugin.createParser("people working at Google Amsterdam office", null, null, null);
      nlsQParser.parse();
    } catch (Exception e) {
      fail(e.getLocalizedMessage());
    }
  }
}
