package com.github.sedtum.solr;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author tommaso
 * @version $Id$
 */
public class NLSQueryTranslatorTest {

  @Test
  public void testTranslation() {
    String[] testQueries = new String[]{"articles about science", "people working at Google", "movies with Kevin Spacey"};
    try {
      NLSQueryTranslator nlsQueryTranslator = new NLSQueryTranslator();
      NLSQueryAnalyzer nlsQueryAnalyzer = mock(NLSQueryAnalyzer.class);
      when(nlsQueryAnalyzer.isNLSQuery()).thenReturn(true);
      for (String nlsQuery : testQueries) {
        String explicitNLSQuery = nlsQueryTranslator.createNLSExplicitQueryString(nlsQuery, nlsQueryAnalyzer);
        System.out.println(explicitNLSQuery);
        assertNotNull("returning a null query is wrong", explicitNLSQuery);
      }
    } catch (Exception e) {
      fail(e.getLocalizedMessage());
    }
  }
}
