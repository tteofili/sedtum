package com.github.sedtum.lucene;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;

import static org.junit.Assert.*;

public class UIMABaseAnalyzerTest {

  private Directory dir;

  private UIMABaseAnalyzer analyzer;

  private IndexWriter writer;

  @Before
  public void setUp() throws Exception {
    dir = new RAMDirectory();
    analyzer = new UIMABaseAnalyzer("src/main/resources/WhitespaceTokenizer.xml",
            "org.apache.uima.TokenAnnotation");
    writer = new IndexWriter(dir, analyzer, IndexWriter.MaxFieldLength.UNLIMITED);
  }

  @After
  public void tearDown() throws Exception {
    writer.close();
  }

  @Test
  public void baseUIMAAnalyzerStreamTest() {
    try {
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

  @Test
  public void baseUIMAAnalyzerIntegrationTest() {
    try {
      // add the first doc
      Document doc = new Document();
      doc.add(new Field("title", "this is a dummy title ", Field.Store.YES,
              Field.Index.ANALYZED));
      doc.add(new Field("contents", "there is some content written here",
              Field.Store.YES, Field.Index.ANALYZED));
      writer.addDocument(doc, analyzer);
      writer.commit();

      // try the search over the first doc
      IndexSearcher indexSearcher = new IndexSearcher(writer.getReader());
      TopDocs result = indexSearcher.search(
              new MatchAllDocsQuery("contents"), 10);
      assertTrue(result.totalHits > 0);
      Document d = indexSearcher.doc(result.scoreDocs[0].doc);
      assertNotNull(d);
      assertNotNull(d.getField("title"));
      assertNotNull(d.getField("contents"));

      // add a second doc
      doc = new Document();
      doc.add(new Field("title", "il mio titolo", Field.Store.YES,
              Field.Index.ANALYZED));
      doc.add(new Field("contents", "c' scritto qualcosa qui!",
              Field.Store.YES, Field.Index.ANALYZED));
      writer.addDocument(doc, analyzer);
      writer.commit();

      // do a matchalldocs query to retrieve both docs
      indexSearcher = new IndexSearcher(writer.getReader());
      result = indexSearcher.search(
              new MatchAllDocsQuery("contents"), 10);
      assertTrue(result.totalHits > 0);
      for (ScoreDoc di : result.scoreDocs) {
        d = indexSearcher.doc(di.doc);
        assertNotNull(d);
        assertNotNull(d.getField("title"));
        assertNotNull(d.getField("contents"));
      }
    } catch (Exception e) {
      fail(e.getLocalizedMessage());
    }
  }
}
