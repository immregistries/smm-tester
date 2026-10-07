package org.immregistries.smm.transform.procedure;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.immregistries.smm.transform.TestCaseMessage;
import org.immregistries.smm.transform.TransformRequest;
import org.immregistries.smm.transform.Transformer;

public class PopulateQueryFromUpdate implements ProcedureInterface {

  private Transformer transformer = null;

  public void setTransformer(Transformer transformer) {
    this.transformer = transformer;
  }

  private static Map<String, String> mapMap = new HashMap<String, String>();
  static {
    mapMap.put("QPD-3.1", "PID-3.1");
    mapMap.put("QPD-4.1", "PID-5.1");
    mapMap.put("QPD-4.2", "PID-5.2");
    mapMap.put("QPD-4.3", "PID-5.3");
    mapMap.put("QPD-5.1", "PID-6.1");
    mapMap.put("QPD-5.2", "PID-6.2");
    mapMap.put("QPD-5.3", "PID-6.3");
    mapMap.put("QPD-5.7", "PID-6.7");
    mapMap.put("QPD-6.1", "PID-7.1");
    mapMap.put("QPD-7.1", "PID-8.1");
    mapMap.put("QPD-8.1", "PID-11.1");
    mapMap.put("QPD-8.3", "PID-11.3");
    mapMap.put("QPD-8.4", "PID-11.4");
    mapMap.put("QPD-8.5", "PID-11.5");
    mapMap.put("QPD-8.7", "PID-11.7");
    mapMap.put("QPD-9.1", "PID-13.1");
    mapMap.put("QPD-9.2", "PID-13.2");
    mapMap.put("QPD-9.3", "PID-13.3");
    mapMap.put("QPD-9.4", "PID-13.4");
    mapMap.put("QPD-9.5", "PID-13.5");
    mapMap.put("QPD-9.6", "PID-13.6");
    mapMap.put("QPD-9.7", "PID-13.7");
  }

  // run procedure POPULATE_QUERY_FROM_UPDATE QA-1.0.1
  public void doProcedure(TransformRequest transformRequest, LinkedList<String> tokenList)
      throws IOException {
    TestCaseMessage testCaseMessage = null;
    if (!tokenList.isEmpty()) {
      String token = tokenList.removeFirst();
      if (StringUtils.isNotBlank(token)) {
        Map<String, TestCaseMessage> testCaseMessageMap = transformRequest.getTestCaseMessageMap();

        testCaseMessage = Transformer.getRepeatReferenceTestCaseMessage(transformRequest, token,
            testCaseMessageMap);
      }
    }
    if (testCaseMessage != null) {
      System.out.println("--> doing transforms");
      String updateMessageText = testCaseMessage.getMessageText();
      for (String queryRef : mapMap.keySet()) {
        String updateRef = mapMap.get(queryRef);
        String value = Transformer.getValueFromHL7(updateRef, updateMessageText, transformRequest);
        transformRequest.setLine(queryRef + "=" + value);
        transformer.doSetField(transformRequest);
      }
    }

  }
}
