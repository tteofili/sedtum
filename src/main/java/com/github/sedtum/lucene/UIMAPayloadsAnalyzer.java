package com.github.sedtum.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;

import java.io.Reader;

public class UIMAPayloadsAnalyzer extends Analyzer {

  private String descriptorPath;

  public UIMAPayloadsAnalyzer(String descriptorPath) {
    this.descriptorPath = descriptorPath;
  }

  @Override
  public TokenStream tokenStream(String fieldName, Reader reader) {
    return new UIMABasePayloadsFilter(new UIMABaseTokenizer(descriptorPath, 
            "org.apache.uima.TokenAnnotation", reader));
  }

}
