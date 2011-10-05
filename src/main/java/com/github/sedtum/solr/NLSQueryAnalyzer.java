package com.github.sedtum.solr;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.Type;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * @author tommaso
 * @version $Id$
 */
public class NLSQueryAnalyzer {

  private Float threshold;
  private CAS cas;
  private String qstring;

  NLSQueryAnalyzer(CAS cas, String qstring) {
    this.cas = cas;
    this.qstring = qstring;
    this.threshold = 0f;
  }

  NLSQueryAnalyzer(CAS cas, String qstring, Float threshold) {
    this.cas = cas;
    this.qstring = qstring;
    this.threshold = threshold;
  }


  Boolean isNLSQuery() {
    // TODO implement this
    return true;
  }

  String extractPlaceQuery() {
    // TODO implement this
    return null;
  }

  public String[] extractConcepts() {
    Collection<String> concepts = new HashSet<String>();
    Type conceptsType = cas.getTypeSystem().getType("org.apache.uima.alchemy.ts.concept.ConceptFS");
    FSIterator<FeatureStructure> conceptsIterator = cas.getIndexRepository().getAllIndexedFS(conceptsType);
    while (conceptsIterator.hasNext()) {
      FeatureStructure fs = conceptsIterator.next();
      concepts.add(fs.getStringValue(conceptsType.getFeatureByBaseName("text")));
    }
    Type keywordsType = cas.getTypeSystem().getType("org.apache.uima.alchemy.ts.concept.KeywordFS");
    FSIterator<FeatureStructure> keywordsIterator = cas.getIndexRepository().getAllIndexedFS(keywordsType);
    while (keywordsIterator.hasNext()) {
      FeatureStructure fs = keywordsIterator.next();
      concepts.add(fs.getStringValue(keywordsType.getFeatureByBaseName("text")));
    }

    String[] a = new String[concepts.size()];
    return concepts.toArray(a);
  }

  public Map<String, Collection<String>> extractEntities() {
    Map<String, Collection<String>> entitiesMap = new HashMap<String, Collection<String>>();
    Type entitiesType = cas.getTypeSystem().getType("org.apache.uima.alchemy.ts.entity.BaseEntity");
    FSIterator<FeatureStructure> featureStructureFSIterator = cas.getIndexRepository().getAllIndexedFS(entitiesType);
    while (featureStructureFSIterator.hasNext()) {
      FeatureStructure fs = featureStructureFSIterator.next();
      String entityTypeName = fs.getType().getName();
      Collection<String> existingTypedEntitiesValues = entitiesMap.get(entityTypeName);
      String value = fs.getStringValue(entitiesType.getFeatureByBaseName("text"));
      if (existingTypedEntitiesValues != null) {
        existingTypedEntitiesValues.add(value);
      } else {
        existingTypedEntitiesValues = new HashSet<String>();
        existingTypedEntitiesValues.add(value);
        entitiesMap.put(entityTypeName, existingTypedEntitiesValues);
      }
    }
    return entitiesMap;
  }
}
