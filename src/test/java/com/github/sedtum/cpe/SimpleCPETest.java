package com.github.sedtum.cpe;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;
import org.apache.uima.UIMAFramework;
import org.apache.uima.collection.CollectionProcessingEngine;
import org.apache.uima.collection.metadata.CpeDescription;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.ProcessTrace;
import org.apache.uima.util.ProcessTraceEvent;
import org.apache.uima.util.XMLInputSource;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.fail;

public class SimpleCPETest {

  private static final String DOCUMENT = " Marshall Schor is the Apache UIMA VP, I really don't know who Tong Zhang is instead. ";
  private static final String TOKENS_MODEL_FILE = "target/Tokens.bin";
  private static final String SENTENCE_MODEL_FILE = "target/SentDetect.bin";
  private static final String PERSON_MODEL_FILE = "target/Person.bin";
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

  @Test
  public void testPersonNameTrainerCPE() {
    try {
      runCPE("src/main/resources/PersonNameTrainerCPE.xml");
    } catch (Exception e) {
      e.printStackTrace();
      fail(e.getLocalizedMessage());
    }
  }

  private void checkModels() throws IOException {
    InputStream tokenizerModelIn = new FileInputStream(TOKENS_MODEL_FILE);

    String[] tokens = null;
    try {
      TokenizerModel model = new TokenizerModel(tokenizerModelIn);
      Tokenizer tokenizer = new TokenizerME(model);
      tokens = tokenizer.tokenize(DOCUMENT);
      for (String token : tokens) {
        System.out.println("token : "+token);
      }

    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (tokenizerModelIn != null) {
        try {
          tokenizerModelIn.close();
        } catch (IOException e) {
        }
      }
    }

    String[] sentences = null;
    InputStream sentenceModelIn = new FileInputStream(SENTENCE_MODEL_FILE);
    try {
      SentenceModel model = new SentenceModel(sentenceModelIn);
      SentenceDetectorME sentenceDetector = new SentenceDetectorME(model);
      sentences = sentenceDetector.sentDetect(DOCUMENT);
      for (String sentence : sentences) {
        System.out.println("sentence : "+sentence);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (sentenceModelIn != null) {
        try {
          sentenceModelIn.close();
        } catch (IOException e) {
        }
      }
    }



    InputStream nfModelIn = new FileInputStream(PERSON_MODEL_FILE);
    Span[] persons = null;
    try {
      TokenNameFinderModel model = new TokenNameFinderModel(nfModelIn);
      NameFinderME nameFinder = new NameFinderME(model);
      persons = nameFinder.find(tokens);
      for (Span span : persons) {
        System.out.println("person : "+span.toString());
      }

    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (nfModelIn != null) {
        try {
          nfModelIn.close();
        } catch (IOException e) {
        }
      }
    }
  }

  @Test
  public void testAllOpenNLPTrainerCPE() {
    try {
      runCPE("src/main/resources/AllOpenNLPTrainersCPE.xml");
      checkModels();
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
