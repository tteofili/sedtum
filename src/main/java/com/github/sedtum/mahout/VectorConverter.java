package com.github.sedtum.mahout;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.mahout.utils.vectors.TermInfo;
import org.apache.mahout.utils.vectors.lucene.CachedTermInfo;
import org.apache.mahout.utils.vectors.lucene.LuceneIterable;
import org.apache.mahout.utils.vectors.lucene.TFDFMapper;
import org.apache.mahout.utils.vectors.lucene.VectorMapper;
import org.apache.mahout.vectorizer.TFIDF;
import org.apache.mahout.vectorizer.Weight;

public class VectorConverter {

  private String uniqueKey;

  private String field;

  public VectorConverter(String uniqueKey, String field) {
    this.uniqueKey = uniqueKey;
    this.field = field;
  }

  public LuceneIterable convert(String directoryPath) throws IOException {
    Directory directory = FSDirectory.open(new File(directoryPath));
    IndexReader reader = IndexReader.open(directory, true);
    Weight weight = new TFIDF();
    TermInfo termInfo = new CachedTermInfo(reader, field, 1, 100);
    VectorMapper mapper = new TFDFMapper(reader, weight, termInfo);
    return new LuceneIterable(reader, uniqueKey, field, mapper);
  }
}
