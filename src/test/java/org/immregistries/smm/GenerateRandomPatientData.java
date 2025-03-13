package org.immregistries.hart;

import org.immregistries.hart.tester.transform.Patient;
import org.immregistries.hart.transform.PatientType;
import org.immregistries.hart.transform.Transformer;
import org.junit.Test;


public class GenerateRandomPatientData {

  @Test
  public void generateRandomPatientData() throws Exception {
    Transformer transformer = new Transformer();
    Patient patient = transformer.setupPatient(PatientType.BABY);
    System.out.println("First name (boy): " + patient.getBoyName());
    System.out.println("First name (girl): " + patient.getGirlName());

  }
}
