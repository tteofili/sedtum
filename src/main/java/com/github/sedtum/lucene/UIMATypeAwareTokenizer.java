package com.github.sedtum.lucene;

import org.apache.commons.io.IOUtils;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.*;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceSpecifier;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.XMLInputSource;

import java.io.IOException;
import java.io.Reader;

public class UIMATypeAwareTokenizer extends Tokenizer {

  private TypeAttribute typeAttr;

  private TermAttribute termAttr;

  private OffsetAttribute offsetAttr;

  private FSIterator<AnnotationFS> iterator;

  private String tokenTypeString;

  private String descriptorPath;

  private String typeAttributeFeaturePath;

  private FeaturePath featurePath;

  public UIMATypeAwareTokenizer(String descriptorPath, String tokenType, String typeAttributeFeaturePath, Reader input) {
    super(input);
    this.tokenTypeString = tokenType;
    this.termAttr = (TermAttribute) addAttribute(TermAttribute.class);
    this.typeAttr = (TypeAttribute) addAttribute(TypeAttribute.class);
    this.offsetAttr = (OffsetAttribute) addAttribute(OffsetAttribute.class);
    this.typeAttributeFeaturePath = typeAttributeFeaturePath;
    this.descriptorPath = descriptorPath;
  }

  private void analyzeText(Reader input, String descriptorPath) throws InvalidXMLException,
          IOException, ResourceInitializationException, AnalysisEngineProcessException, CASException {
    ResourceSpecifier specifier = UIMAFramework.getXMLParser().parseResourceSpecifier(
            new XMLInputSource(descriptorPath));
    AnalysisEngine ae = UIMAFramework.produceAnalysisEngine(specifier);
    CAS cas = ae.newCAS();
    cas.setDocumentText(IOUtils.toString(input));
    ae.process(cas);
    Type tokenType = cas.getTypeSystem().getType(this.tokenTypeString);
    iterator = cas.getAnnotationIndex(tokenType).iterator();
    featurePath = cas.createFeaturePath();
    featurePath.initialize(this.typeAttributeFeaturePath);
  }

  @Override
  public boolean incrementToken() throws IOException {
    if (iterator == null) {
      try {
        analyzeText(input, descriptorPath);
      } catch (Exception e) {
        throw new IOException(e);
      }
    }
    if (iterator.hasNext()) {
      AnnotationFS next = iterator.next();
      termAttr.setTermBuffer(next.getCoveredText());
      termAttr.setTermLength(next.getCoveredText().length());
      offsetAttr.setOffset(next.getBegin(), next.getEnd());
      typeAttr.setType(featurePath.getValueAsString(next));
      return true;
    } else {
      iterator = null;
      return false;
    }
  }

}
