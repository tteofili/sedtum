package com.github.sedtum.lucene.payloads;

import com.github.sedtum.lucene.TypeScoreMap;
import org.apache.lucene.search.DefaultSimilarity;


public class UIMATypeBasedSimilarity extends DefaultSimilarity {

  private TypeScoreMap scoreMap;

  public UIMATypeBasedSimilarity() {
    this.scoreMap = new TypeScoreMap();
  }

  @Override
  public float scorePayload(int docId, String fieldName, int start, int end, byte[] payload, int offset, int length) {
     return scoreMap.getScore(new String(payload));
  }
}
