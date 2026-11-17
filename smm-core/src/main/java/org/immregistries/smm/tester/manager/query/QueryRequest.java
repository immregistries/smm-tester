package org.immregistries.smm.tester.manager.query;

public class QueryRequest extends Patient {
  private QueryType queryType = QueryType.QBP_Z34;
  
  /** Default Z45 conditions: pregnant, onset 30 weeks before today. */
  private String conditions = "007=Y,170=" + java.time.LocalDate.now().minusDays(210).format(java.time.format.DateTimeFormatter.BASIC_ISO_DATE);

  /** Contextual conditions for a Z45 query, for example 007=Y,170=20260701 */
  public String getConditions() {
    return conditions;
  }

  public void setConditions(String conditions) {
    this.conditions = conditions == null ? "" : conditions;
  }

  public QueryType getQueryType() {
    return queryType;
  }
  public void setQueryType(QueryType query) {
    this.queryType = query;
  }


}
