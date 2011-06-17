package com.github.sedtum.lucene;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.junit.Test;

import java.io.StringReader;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * @author tommaso
 * @version $Id$
 */
public class UIMATypeAwareAnalyzerTest {
  @Test
  public void testSimpleUsage() {
    try {
      UIMATypeAwareAnalyzer analyzer = new UIMATypeAwareAnalyzer("src/main/resources/HmmTaggerAggregate.xml",
              "org.apache.uima.TokenAnnotation", "posTag");
      TokenStream ts = analyzer.tokenStream("text", new StringReader("the big brown fox jumped on the wood"));
      TermAttribute termAtt = (TermAttribute) ts.addAttribute(TermAttribute.class);
      OffsetAttribute offsetAtt = (OffsetAttribute) ts.addAttribute(OffsetAttribute.class);
      PositionIncrementAttribute posAtt = (PositionIncrementAttribute) ts.addAttribute(
              PositionIncrementAttribute.class);
      while (ts.incrementToken()) {
        assertNotNull(offsetAtt);
        assertNotNull(termAtt);
        assertNotNull(posAtt);
      }
    } catch (Exception e) {
      e.printStackTrace();
      fail(e.getLocalizedMessage());
    }
  }
}
