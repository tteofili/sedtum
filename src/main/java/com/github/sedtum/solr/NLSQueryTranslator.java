package com.github.sedtum.solr;

import java.util.Collection;
import java.util.Map;

/**
 * @author tommaso
 * @version $Id$
 */
public class NLSQueryTranslator {

  public String createNLSExplicitQueryString(String qstr, NLSQueryAnalyzer nlsQueryAnalyzer) {
    StringBuilder nlsQueryBuilder = new StringBuilder();

    // TODO give higher boosts based on PoS to qstr terms
    qstr = nlsQueryAnalyzer.expandBoosts();

    // add the normal qstr
    // TODO : this should be translated as standard DisMax
    nlsQueryBuilder.append("(").append("text_uima").append(":").append("(").append(qstr).append(")").append(")").append(" ");

    // add in-sentence qstr matching
//        nlsQueryBuilder.append("(").append(localParams.get("sentencefield")).append(":").append(qstr).append(")").append(" ");
    nlsQueryBuilder.append("(").append("sentence").append(":").append("(").append(qstr).append(")").append(")^40").append(" ");

    // check for 'place queries'
    String placeQuery = nlsQueryAnalyzer.extractPlaceQuery();
    if (placeQuery != null) {
      // add the place query as a boost query

    }

    // extract entities and query per-entity field
    Map<String, Collection<String>> entitiesMap = nlsQueryAnalyzer.extractEntities();
    StringBuilder entitiesQueryBuilder = new StringBuilder();
    entitiesQueryBuilder.append("(");
    for (String type : entitiesMap.keySet()) {
      for (String entityValue : entitiesMap.get(type)) {
        entitiesQueryBuilder.append(type.substring(type.lastIndexOf(".") + 1)).append("_sm").append(":").append(entityValue).append("^30 ");
      }
    }
    entitiesQueryBuilder.append(")");
    if (entitiesQueryBuilder.length() > 2)
      nlsQueryBuilder.append(entitiesQueryBuilder.toString());

    // extract concept(s) from the cas/query
    String[] concepts = nlsQueryAnalyzer.extractConcepts();

    if (concepts != null && concepts.length > 0) {
      // eventually match concept(s) with concept field(s) - high boost
      StringBuilder conceptQueryStringBuilder = new StringBuilder();
      conceptQueryStringBuilder.append("(");
      for (String concept : concepts) {
//          conceptQueryStringBuilder.append(localParams.get("conceptfield")).append(":").append(concept);
        conceptQueryStringBuilder.append("concept").append(":").append(concept).append("^1000 ");
      }
      conceptQueryStringBuilder.append(") ");
      nlsQueryBuilder.append(" ").append(conceptQueryStringBuilder.toString());
    }

    return nlsQueryBuilder.toString();
  }

}
