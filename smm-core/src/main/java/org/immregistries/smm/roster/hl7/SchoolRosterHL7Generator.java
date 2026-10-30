package org.immregistries.smm.roster.hl7;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.immregistries.smm.roster.model.Student;
import org.immregistries.smm.roster.model.StudentLoad;
import org.immregistries.smm.transform.TestCaseMessage;
import org.immregistries.smm.transform.Transformer;

public class SchoolRosterHL7Generator {
  
  private static final DateTimeFormatter MSH_10_DTF = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
  private static final DateTimeFormatter OBX_14_DTF = DateTimeFormatter.ofPattern("yyyyMMdd");
  private static int counter = 0;
  
  private static final String HL7_COMPATIBLE_BASELINE =
"""
MSH|^~\\&|SMM|DOE-DISTRICT-0123|CHIRP|ISDH|20261026100000-0600||VXU^V04^VXU_V04|SMM-ROSTER-20261026-000001|P|2.5.1|||ER|AL|||||Z22^CDCPHINVS|DOE-SCHOOL-0456|CHIRP
PID|1||SMM-000001^^^EXAMPLE-SCHOOL-HEALTH^MR~987654321^^^IN-DOE^PI||Richardson^Russell^Clint^^^^L|Billington^^^^^^M|20180415|M||1002-5^American Indian or Alaska Native^CDCREC|543 Blount Drive^^Bozeman^MT^59715^USA^P||^PRN^PH^^^406^5557690|||||||||2186-5^Not Hispanic or Latino^CDCREC
ORC|RE||9999^SMM
RXA|0|1|20261026||998^No vaccine administered^CVX|999||||||||||||||NA|A
""";
  
  public enum MessageProfile {
    COMPATIBLE_BASELINE("Current Guide Compatible Baseline", false, false),
    PROPOSED_WITH_GRADE("Proposed Roster Message with Grade", true, false),
    IDEAL_WITH_IDENTIFIER("Future Ideal with a Dedicated Student Identifier Type", true, true);
    
    private String friendly;
    private boolean includesOBX;
    private boolean includesStudentIdentifier;
    
    private MessageProfile(
        String friendly,
        boolean includesOBX,
        boolean includesStudentIdentifier) {
      
      this.friendly = friendly;
      this.includesOBX = includesOBX;
      this.includesStudentIdentifier = includesStudentIdentifier;
    }
    
    public String getFriendly() {
      return friendly;
    }
    
    public boolean isIncludesOBX() {
      return includesOBX;
    }
    
    public boolean isIncludesStudentIdentifier() {
      return includesStudentIdentifier;
    }
  }
  
  public static StudentLoad generate(Student student, MessageProfile profile) {
    String id = generateId();
    
    String hl7 = transform(student, profile, id);
    
    StudentLoad load = new StudentLoad();
    load.setStudent(student);
    load.setVxu(hl7);
    load.setCreated(LocalDateTime.now());
    load.setId(id);
    
    return load;
  }

  private static String transform(
      Student student,
      MessageProfile profile,
      final String id) {
    
    String transforms =
    """
      MSH-3={{MSH-3}}
      MSH-4={{MSH-4}}
      MSH-10={{MSH-10}}
      MSH-21={{MSH-21}}
      MSH-22={{MSH-22}}
      PID-3.1={{PID-3.1}}
      PID-3.4={{PID-3.4}}
      PID-3.1={{PID-3.1}}
      PID-5.1={{PID-5.1}}
      PID-5.2={{PID-5.2}}
      PID-5.3={{PID-5.3}}
    """
      //TODO hook these up to configurable properties
      .replace("{{MSH-3}}", "SMM")
      .replace("{{MSH-4}}", "DOE-DISTRICT-0123")
      .replace("{{MSH-10}}", id)
      .replace("{{MSH-21}}", "Z22^CDCPHINVS")
      .replace("{{PID-3.1}}", "SMM-000001")
      .replace("{{PID-3.4}}", "EXAMPLE-SCHOOL-HEALTH")
      .replace("{{PID-3.1}}", student.getId())
      .replace("{{PID-5.1}}", student.getLastName())
      .replace("{{PID-5.2}}", student.getFirstName())
      .replace("{{PID-5.3}}", student.getMiddleName());
    
    if (profile.isIncludesOBX()) {
      transforms +=
      """
        insert segment OBX last
        OBX-1=1
        OBX-2=CE
        OBX-3=64990-5^Grade in school^LN
        OBX-4=1
        OBX-5.1={{OBX-5-1}}
        OBX-5.2={{OBX-5-2}}
        OBX-5.3=L
        OBX-11=F
        OBX-14={{OBX-14}}
      """
        .replace("{{OBX-5-1}}", student.getGrade().getCode())
        .replace("{{OBX-5-2}}", student.getGrade().getLabel())
        .replace("{{OBX-14}}", LocalDate.now().format(OBX_14_DTF));
    }
    
    if (profile.isIncludesStudentIdentifier()) {
      transforms += "PID-3.5#2=STU";
    }
    
    Transformer transformer = new Transformer();
    TestCaseMessage tcm = new TestCaseMessage();
    tcm.setOriginalMessage(HL7_COMPATIBLE_BASELINE);
    tcm.setCustomTransformations(transforms);
    transformer.transform(tcm);
    return tcm.getMessageText();
  }
  
  private static String generateId() {
    String msh10Prefix = "SMM-ROSTER-"; //TODO configurable
    return msh10Prefix + LocalDateTime.now().format(MSH_10_DTF) + "-" + counter++;
  }
}