package com.github.sedtum.uima.cr;

import org.apache.uima.cas.CAS;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.collection.CollectionReader_ImplBase;
import org.apache.uima.util.Progress;

import java.io.IOException;

public class DummyCollectionReader extends CollectionReader_ImplBase {

  public void getNext(CAS aCAS) throws IOException, CollectionException {
    // TODO Auto-generated method stub
    
  }

  public void close() throws IOException {
    // TODO Auto-generated method stub
    
  }

  public Progress[] getProgress() {
    // TODO Auto-generated method stub
    return null;
  }

  public boolean hasNext() throws IOException, CollectionException {
    // TODO Auto-generated method stub
    return false;
  }

}
