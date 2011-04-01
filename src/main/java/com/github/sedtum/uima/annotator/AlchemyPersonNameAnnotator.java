package com.github.sedtum.uima.annotator;

import com.github.sedtum.uima.ts.PersonAnnotation;
import org.apache.uima.alchemy.ts.entity.AlchemyAnnotation;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.util.Level;

/**
 * Gets {@link AlchemyAnnotation}s whit alchemyType = 'person' and create a
 * {@link PersonAnnotation}
 */
public class AlchemyPersonNameAnnotator extends JCasAnnotator_ImplBase {

  private static final String PERSON_TYPE = "org.apache.uima.alchemy.ts.entity.Person";

  @Override
  public void process(JCas jCas) throws AnalysisEngineProcessException {
    for (Annotation annotation : jCas.getAnnotationIndex(AlchemyAnnotation.type)) {
      AlchemyAnnotation alchemyAnnotation = (AlchemyAnnotation) annotation;
      if (alchemyAnnotation.getAlchemyType()!=null && PERSON_TYPE.equals(
              alchemyAnnotation.getAlchemyType())) {
        PersonAnnotation personAnnotation = new PersonAnnotation(jCas);
        personAnnotation.setBegin(annotation.getBegin());
        personAnnotation.setEnd(annotation.getEnd());
        personAnnotation.addToIndexes();
        getContext().getLogger().log(Level.INFO,"added a PersonAnnotation");
      }
    }
    
  }

}
