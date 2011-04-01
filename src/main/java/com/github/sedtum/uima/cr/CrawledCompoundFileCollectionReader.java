package com.github.sedtum.uima.cr;

import org.apache.commons.io.IOUtils;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.collection.CollectionReader_ImplBase;
import org.apache.uima.examples.SourceDocumentInformation;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceConfigurationException;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Progress;
import org.apache.uima.util.ProgressImpl;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class CrawledCompoundFileCollectionReader extends CollectionReader_ImplBase {
  public static final String PARAM_INPUTFILE = "input.file";

  private List<String> lines;
  private int i;

  @Override
  public void initialize() throws ResourceInitializationException {
    Object configParameterValue = getConfigParameterValue(PARAM_INPUTFILE);
    File crawledFile = new File(((String) configParameterValue).trim());
    if (!crawledFile.exists() || crawledFile.isDirectory()) {
      throw new ResourceInitializationException(ResourceConfigurationException.STANDARD_MESSAGE_CATALOG,
              new Object[] { PARAM_INPUTFILE, this.getMetaData().getName(), crawledFile.getPath() });
    }
    try {
      lines = IOUtils.readLines(new FileReader(crawledFile));
    } catch (IOException e) {
      e.printStackTrace();
      throw new ResourceInitializationException(ResourceConfigurationException.RESOURCE_DATA_NOT_VALID,
              new Object[]{configParameterValue});
    }
    i = 0;
  }

  public void getNext(CAS aCAS) throws IOException, CollectionException {
    JCas jcas;
    try {
      jcas = aCAS.getJCas();
    } catch (CASException e) {
      throw new CollectionException(e);
    }

    String inlinePage = lines.get(i);

      // put document in CAS
    jcas.setDocumentText(inlinePage);

    // Also store location of source document in CAS. This information is critical
    // if CAS Consumers will need to know where the original document contents are located.
    // For example, the Semantic Search CAS Indexer writes this information into the
    // search index that it creates, which allows applications that use the search index to
    // locate the documents that satisfy their semantic queries.
    SourceDocumentInformation srcDocInfo = new SourceDocumentInformation(jcas);
    srcDocInfo.setOffsetInSource(i);
    srcDocInfo.setDocumentSize((int) inlinePage.length());
    srcDocInfo.setLastSegment(i+1==lines.size());
    srcDocInfo.addToIndexes();
    i++;
  }

  public void close() throws IOException {
  }

  public Progress[] getProgress() {
    return new Progress[] { new ProgressImpl(i, lines.size(), Progress.ENTITIES) };
  }

  public boolean hasNext() throws IOException, CollectionException {
    return i < lines.size();
  }

}
