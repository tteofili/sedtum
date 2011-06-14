package com.github.sedtum.lucene;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.PayloadAttribute;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.index.Payload;

import java.io.IOException;

public class UIMABasePayloadsFilter extends TokenFilter {

  private TermAttribute termAttr;

  private PayloadAttribute payloadAttr;

  private Payload uimaPayload;

  private TypeAttribute typeAttr;

  protected UIMABasePayloadsFilter(TokenStream input) {
    super(input);
    payloadAttr = (PayloadAttribute) addAttribute(PayloadAttribute.class);
    termAttr = (TermAttribute) addAttribute(TermAttribute.class);
    typeAttr = (TypeAttribute) addAttribute(TypeAttribute.class);
  }


  public final boolean incrementToken() throws IOException {
    if (input.incrementToken()) {
      if (termAttr.term().equals("warning")) {
        payloadAttr.setPayload(null);
      } else {
        payloadAttr.setPayload(null);
      }
      return true;
    } else
      return false;
  }

}
