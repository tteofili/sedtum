package com.github.sedtum.cpe;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.apache.uima.UIMAFramework;
import org.apache.uima.collection.CollectionProcessingEngine;
import org.apache.uima.collection.metadata.CpeDescription;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.ProcessTrace;
import org.apache.uima.util.ProcessTraceEvent;
import org.apache.uima.util.XMLInputSource;
import org.junit.Test;

public class SimpleCPETest {

  private CollectionProcessingEngine mCPE;

  @Test
  public void testSimpleCPEExec() {

    try {
      runCPE("src/main/resources/SimpleFirstStageCPE.xml");
    } catch (Exception e) {
      e.printStackTrace();
      fail(e.getLocalizedMessage());
    }

  }

  private void runCPE(String descriptorPath) throws InvalidXMLException, IOException,
          ResourceInitializationException, InterruptedException {

    // parse CPE descriptor
    System.out.println("Parsing CPE Descriptor");
    CpeDescription cpeDesc = UIMAFramework.getXMLParser().parseCpeDescription(
            new XMLInputSource(descriptorPath));
    // instantiate CPE
    System.out.println("Instantiating CPE");
    mCPE = UIMAFramework.produceCollectionProcessingEngine(cpeDesc);

    TestStatusCallbackListener listener = new TestStatusCallbackListener();
    mCPE.addStatusCallbackListener(listener);

    // Start Processing
    System.out.println("Running CPE");
    mCPE.process();

    while (!listener.isFinished()) {
      Thread.sleep(1000);
    }
    ProcessTrace pt = mCPE.getPerformanceReport();
    for (ProcessTraceEvent pte : pt.getEvents()) {
      System.out.println("[" + pte.getType() + "]  " + pte.getComponentName() + " in "
              + pte.getDuration() + "ms");
    }

  }

}
