package com.github.sedtum.lucene.payloads;

import com.github.sedtum.lucene.UIMATypeAwareTokenizer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.payloads.TypeAsPayloadTokenFilter;

import java.io.Reader;

public class UIMAPayloadsAnalyzer extends Analyzer {


  private String descriptorPath;

  public UIMAPayloadsAnalyzer(String descriptorPath) {
    this.descriptorPath = descriptorPath;
  }

  @Override
  public TokenStream tokenStream(String fieldName, Reader reader) {
    return new TypeAsPayloadTokenFilter(new UIMATypeAwareTokenizer(descriptorPath,
            "org.apache.uima.TokenAnnotation", "posTag", reader));
  }

}
