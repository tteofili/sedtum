package com.github.sedtum.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;

import java.io.Reader;

/**
 * @author tommaso
 * @version $Id$
 */
public class UIMATypeAwareAnalyzer extends Analyzer {
  private String descriptorPath;
  private String tokenType;
  private String featurePath;

  public UIMATypeAwareAnalyzer(String descriptorPath, String tokenType, String featurePath) {
    this.descriptorPath = descriptorPath;
    this.tokenType = tokenType;
    this.featurePath = featurePath;
  }

  @Override
  public TokenStream tokenStream(String fieldName, Reader reader) {
    return new UIMATypeAwareTokenizer(descriptorPath, tokenType, featurePath, reader);
  }
}
