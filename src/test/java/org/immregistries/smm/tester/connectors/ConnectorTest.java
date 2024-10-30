package org.immregistries.smm.tester.connectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

public class ConnectorTest {

  private Connector connector;
  
  @Before
  public void before() {
    connector = new Connector("label", "type") {
      @Override
      protected void setupFields(List<String> fields) {}

      @Override
      public String submitMessage(String message, boolean debug) throws Exception {
        return null;
      }

      @Override
      public String connectivityTest(String message) throws Exception {
        return null;
      }

      @Override
      public boolean connectivityTestSupported() {
        return false;
      }

      @Override
      protected void makeScriptAdditions(StringBuilder sb) {}
    };
  }
  
  @Test
  public void getTransformsFromScenarioMap_nullMap() {
    connector.setScenarioTransformationsMap(null);
    
    List<String> results = connector.getTransformsFromScenarioMap("GM-1.2");
    assertEquals(0, results.size());
  }
  
  @Test
  public void getTransformsFromScenarioMap_emptyMap() {
    connector.setScenarioTransformationsMap(new HashMap<>());
    
    List<String> results = connector.getTransformsFromScenarioMap("GM-1.2");
    assertEquals(0, results.size());
  }
  
  @Test
  public void getTransformsFromScenarioMap_exactMatch() {
    Map<String, String> map = new HashMap<>();
    map.put("GM-1.1", "t1");
    map.put("GM-1.2", "t2");
    map.put("GM-1.3", "t3");
    
    connector.setScenarioTransformationsMap(map);

    List<String> results = connector.getTransformsFromScenarioMap("GM-1.2");
    assertEquals(1, results.size());
    assertEquals("t2", results.get(0));
  }
  
  @Test
  public void getTransformsFromScenarioMap_noExactMatch_noWildcards() {
    Map<String, String> map = new HashMap<>();
    map.put("GM-1.1", "t1");
    map.put("GM-1.2", "t2");
    map.put("GM-1.3", "t3");
    
    connector.setScenarioTransformationsMap(map);

    List<String> results = connector.getTransformsFromScenarioMap("GM-1.4");
    assertEquals(0, results.size());
  }
  
  @Test
  public void getTransformsFromScenarioMap_noExactMatch_hasWildcards_match() {
    Map<String, String> map = new HashMap<>();
    map.put("GM-1.1", "t1");
    map.put("GM-1.*", "t2");
    map.put("GM-1.3", "t3");
    
    connector.setScenarioTransformationsMap(map);

    List<String> results = connector.getTransformsFromScenarioMap("GM-1.4");
    assertEquals(1, results.size());
    assertEquals("t2", results.get(0));
  }
  
  @Test
  public void getTransformsFromScenarioMap_noExactMatch_hasWildcards_noMatch() {
    Map<String, String> map = new HashMap<>();
    map.put("GM-1.1", "t1");
    map.put("GM-1.2*", "t2");
    map.put("GM-1.3", "t3");
    
    connector.setScenarioTransformationsMap(map);

    List<String> results = connector.getTransformsFromScenarioMap("GM-1.4");
    assertEquals(0, results.size());
  }
  
  @Test
  public void getTransformsFromScenarioMap_noExactMatch_hasWildcards_multipleMatch() {
    Map<String, String> map = new HashMap<>();
    map.put("GM-1.1", "t1");
    map.put("GM-1.*", "t2");
    map.put("GM-1.3", "t3");
    map.put("GM-*.4", "t4");
    
    connector.setScenarioTransformationsMap(map);

    List<String> results = connector.getTransformsFromScenarioMap("GM-1.4");
    assertEquals(2, results.size());
    assertTrue(results.contains("t2"));
    assertTrue(results.contains("t4"));
  }
  
  @Test
  public void getTransformsFromScenarioMap_hasExactMatch_hasWildcards_multipleMatch() {
    Map<String, String> map = new HashMap<>();
    map.put("GM-1.1", "t1");
    map.put("GM-1.*", "t2");
    map.put("GM-1.3", "t3");
    map.put("GM-1.4", "t4");
    map.put("GM-*.4", "t5");
    
    connector.setScenarioTransformationsMap(map);

    List<String> results = connector.getTransformsFromScenarioMap("GM-1.4");
    assertEquals(3, results.size());
    assertTrue(results.contains("t2"));
    assertTrue(results.contains("t4"));
    assertTrue(results.contains("t5"));
  }
}
