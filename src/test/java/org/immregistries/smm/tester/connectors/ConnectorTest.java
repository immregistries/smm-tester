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
  
  @Test
  public void getTransformsFromScenarioMap_nullScenario() {
    Map<String, String> map = new HashMap<>();
    map.put(null, "t1");
    
    connector.setScenarioTransformationsMap(map);

    List<String> results = connector.getTransformsFromScenarioMap("GM-1.4");
    assertEquals(0, results.size());
  }
  
  @Test
  public void getTransformsFromScenarioMap_emptyScenario() {
    Map<String, String> map = new HashMap<>();
    map.put("", "t1");
    
    connector.setScenarioTransformationsMap(map);

    List<String> results = connector.getTransformsFromScenarioMap("GM-1.4");
    assertEquals(0, results.size());
  }
  
  @Test
  public void getTransformsFromScenarioMap_singleComma() {
    Map<String, String> map = new HashMap<>();
    map.put(",", "t1");
    
    connector.setScenarioTransformationsMap(map);

    List<String> results = connector.getTransformsFromScenarioMap("GM-1.4");
    assertEquals(0, results.size());
  }
  
  @Test
  public void getTransformsFromScenarioMap_multipleCommas() {
    Map<String, String> map = new HashMap<>();
    map.put(",,,,,,,", "t1");
    
    connector.setScenarioTransformationsMap(map);

    List<String> results = connector.getTransformsFromScenarioMap("GM-1.4");
    assertEquals(0, results.size());
  }
  
  @Test
  public void getTransformsFromScenarioMap_multiple_oneExact_first() {
    Map<String, String> map = new HashMap<>();
    map.put("GM-1.4,GM-1.5", "t1");
    map.put("GM-1.6,GM-1.7", "t2");
    
    connector.setScenarioTransformationsMap(map);

    List<String> results = connector.getTransformsFromScenarioMap("GM-1.4");
    assertEquals(1, results.size());
    assertTrue(results.contains("t1"));
  }
  
  @Test
  public void getTransformsFromScenarioMap_multiple_oneExact_second() {
    Map<String, String> map = new HashMap<>();
    map.put("GM-1.5,GM-1.4", "t1");
    map.put("GM-1.6,GM-1.7", "t2");
    
    connector.setScenarioTransformationsMap(map);

    List<String> results = connector.getTransformsFromScenarioMap("GM-1.4");
    assertEquals(1, results.size());
    assertTrue(results.contains("t1"));
  }
  
  @Test
  public void getTransformsFromScenarioMap_multiple_noMatch() {
    Map<String, String> map = new HashMap<>();
    map.put("GM-1.5,GM-1.3", "t1");
    map.put("GM-1.6,GM-1.7", "t2");
    
    connector.setScenarioTransformationsMap(map);

    List<String> results = connector.getTransformsFromScenarioMap("GM-1.4");
    assertEquals(0, results.size());
  }
  
  @Test
  public void getTransformsFromScenarioMap_multiple_wildcard_secondMatch() {
    Map<String, String> map = new HashMap<>();
    map.put("GM-1.*,GM-1.3,GM-2.1", "t1");
    map.put("GM-1.6,GM-1.7,GM-2.1", "t2");
    
    connector.setScenarioTransformationsMap(map);

    List<String> results = connector.getTransformsFromScenarioMap("GM-1.4");
    assertEquals(1, results.size());
    assertTrue(results.contains("t1"));
    
    results = connector.getTransformsFromScenarioMap("GM-1.3");
    assertEquals(1, results.size());
    assertTrue(results.contains("t1"));
    
    results = connector.getTransformsFromScenarioMap("GM-1.6");
    assertEquals(2, results.size());
    assertTrue(results.contains("t1"));
    assertTrue(results.contains("t2"));
    
    results = connector.getTransformsFromScenarioMap("GM-1.7");
    assertEquals(2, results.size());
    assertTrue(results.contains("t1"));
    assertTrue(results.contains("t2"));
    
    results = connector.getTransformsFromScenarioMap("GM-2.1");
    assertEquals(2, results.size());
    assertTrue(results.contains("t1"));
    assertTrue(results.contains("t2"));
  }
  
  @Test
  public void getTransformsFromScenarioMap_multiple_wildcard_firstMatch() {
    Map<String, String> map = new HashMap<>();
    map.put("GM-1.3,GM-1.*", "t1");
    map.put("GM-1.6,GM-1.7", "t2");
    
    connector.setScenarioTransformationsMap(map);

    List<String> results = connector.getTransformsFromScenarioMap("GM-1.4");
    assertEquals(1, results.size());
    assertTrue(results.contains("t1"));
    
    results = connector.getTransformsFromScenarioMap("GM-1.3");
    assertEquals(1, results.size());
    assertTrue(results.contains("t1"));
    
    results = connector.getTransformsFromScenarioMap("GM-1.6");
    assertEquals(2, results.size());
    assertTrue(results.contains("t1"));
    assertTrue(results.contains("t2"));
    
    results = connector.getTransformsFromScenarioMap("GM-1.7");
    assertEquals(2, results.size());
    assertTrue(results.contains("t1"));
    assertTrue(results.contains("t2"));
  }
  
  @Test
  public void getTransformsFromScenarioMap_multiple_wildcard_twoMatch() {
    Map<String, String> map = new HashMap<>();
    map.put("GM-*.4,GM-1.*", "t1");
    map.put("GM-1.6,GM-1.7", "t2");
    
    connector.setScenarioTransformationsMap(map);

    List<String> results = connector.getTransformsFromScenarioMap("GM-1.4");
    assertEquals(1, results.size());
    assertTrue(results.contains("t1"));
  }
  
  @Test
  public void getTransformsFromScenarioMap_multiple_wildcard_threeMatch() {
    Map<String, String> map = new HashMap<>();
    map.put("     GM-*.4    ,    GM-1.*    ,       *-1.4"     , "t1");
    map.put("GM-1.6,GM-1.7", "t2");
    
    connector.setScenarioTransformationsMap(map);

    List<String> results = connector.getTransformsFromScenarioMap("GM-1.4");
    assertEquals(1, results.size());
    assertTrue(results.contains("t1"));
  }
  
  @Test
  public void getTransformsFromScenarioMap_multiple_wildcard_thirdFails() {
    Map<String, String> map = new HashMap<>();
    map.put("GM-*.4,GM-1.*,B*-1.4", "t1");
    map.put("GM-1.6,GM-1.7", "t2");
    
    connector.setScenarioTransformationsMap(map);

    List<String> results = connector.getTransformsFromScenarioMap("GM-1.4");
    assertEquals(1, results.size());
    assertTrue(results.contains("t1"));
  }
  
  @Test
  public void getTransformsFromScenarioMap_single_not_match() {
    Map<String, String> map = new HashMap<>();
    map.put("!BM-1.*", "t1");
    map.put("GM-1.6,GM-1.7", "t2");
    
    connector.setScenarioTransformationsMap(map);

    List<String> results = connector.getTransformsFromScenarioMap("GM-1.4");
    assertEquals(1, results.size());
    assertTrue(results.contains("t1"));
  }
  
  @Test
  public void getTransformsFromScenarioMap_single_not_noMatch() {
    Map<String, String> map = new HashMap<>();
    map.put("!GM-1.*", "t1");
    map.put("GM-1.6,GM-1.7", "t2");
    
    connector.setScenarioTransformationsMap(map);

    List<String> results = connector.getTransformsFromScenarioMap("GM-1.4");
    assertEquals(0, results.size());
  }
  
  @Test
  public void getTransformsFromScenarioMap_multiple_allNot() {
    Map<String, String> map = new HashMap<>();
    map.put("!BM-1.*, !GM-2.*", "t1");
    map.put("GM-1.6,GM-1.7", "t2");
    
    connector.setScenarioTransformationsMap(map);

    List<String> results = connector.getTransformsFromScenarioMap("GM-1.4");
    assertEquals(1, results.size());
    assertTrue(results.contains("t1"));
  }
  
  @Test
  public void getTransformsFromScenarioMap_multiple_oneNot_noMatch() {
    Map<String, String> map = new HashMap<>();
    map.put("!BM-1.*, !GM-1.*", "t1");
    
    connector.setScenarioTransformationsMap(map);

    List<String> results = connector.getTransformsFromScenarioMap("GM-1.4");
    assertEquals(0, results.size());
  }
  
  @Test
  public void getTransformsFromScenarioMap_multiple_oneNot_match() {
    Map<String, String> map = new HashMap<>();
    map.put("!BM-1.*, !GM-2.*", "t1");
    
    connector.setScenarioTransformationsMap(map);

    List<String> results = connector.getTransformsFromScenarioMap("GM-1.4");
    assertEquals(1, results.size());
    assertTrue(results.contains("t1"));
  }
  
  @Test
  public void getTransformsFromScenarioMap_single_not_multipleResults() {
    Map<String, String> map = new HashMap<>();
    map.put("!BM-1.*", "t1");
    map.put("!GM-2.*", "t2");
    map.put("!GM-1.4.*", "t3");
    
    connector.setScenarioTransformationsMap(map);

    List<String> results = connector.getTransformsFromScenarioMap("GM-1.4");
    assertEquals(3, results.size());
    assertTrue(results.contains("t1"));
    assertTrue(results.contains("t2"));
    assertTrue(results.contains("t3"));
  }
  
  @Test
  public void getTransformsFromScenarioMap_multiple_multipleMatches() {
    Map<String, String> map = new HashMap<>();
    map.put("GM-1.*, GM-2.*", "t1");
    map.put("GM-3.1", "t2");
    
    connector.setScenarioTransformationsMap(map);

    List<String> results = connector.getTransformsFromScenarioMap("GM-1.4");
    assertEquals(1, results.size());
    assertTrue(results.contains("t1"));

    results = connector.getTransformsFromScenarioMap("GM-2.4");
    assertEquals(1, results.size());
    assertTrue(results.contains("t1"));
  }
  
  @Test
  public void getTransformsFromScenarioMap_multiple_multipleMatches_withNot() {
    Map<String, String> map = new HashMap<>();
    map.put("GM-1.*, GM-2.*, !GM-*", "t1");
    map.put("GM-3.1", "t2");
    
    connector.setScenarioTransformationsMap(map);

    List<String> results = connector.getTransformsFromScenarioMap("GM-1.4");
    assertEquals(0, results.size());

    results = connector.getTransformsFromScenarioMap("GM-2.4");
    assertEquals(0, results.size());
  }
  
  @Test
  public void getTransformsFromScenarioMap_multiple_allNots() {
    Map<String, String> map = new HashMap<>();
    map.put("!GM-1.*, !GM-2.*, !GM-*", "t1");
    map.put("GM-3.1", "t2");
    
    connector.setScenarioTransformationsMap(map);

    List<String> results = connector.getTransformsFromScenarioMap("GM-1.4");
    assertEquals(0, results.size());

    results = connector.getTransformsFromScenarioMap("BM-1.4");
    assertEquals(1, results.size());
    assertTrue(results.contains("t1"));

    results = connector.getTransformsFromScenarioMap("GM-3.1");
    assertEquals(1, results.size());
    assertTrue(results.contains("t2"));
  }
}
