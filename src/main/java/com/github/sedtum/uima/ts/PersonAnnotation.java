

/* First created by JCasGen Fri Apr 01 12:18:29 CEST 2011 */
package com.github.sedtum.uima.ts;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;
import org.apache.uima.jcas.tcas.Annotation;


/** a person annotation
 * Updated by JCasGen Fri Apr 01 12:18:29 CEST 2011
 * XML source: /Users/tommasoteofili/Documents/workspaces/uima_workspace/sedtum/src/main/resources/AlchemyPersonNameAnnotatorDescriptor.xml
 * @generated */
public class PersonAnnotation extends Annotation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(PersonAnnotation.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected PersonAnnotation() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public PersonAnnotation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public PersonAnnotation(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public PersonAnnotation(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** <!-- begin-user-doc -->
    * Write your own initialization here
    * <!-- end-user-doc -->
  @generated modifiable */
  private void readObject() {}
     
}

    