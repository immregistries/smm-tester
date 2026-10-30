package org.immregistries.smm.roster.loader;

import java.io.IOException;
import java.io.InputStream;
import org.immregistries.smm.roster.model.LoadDetails;

public interface SchoolRosterLoader {
  
  static final String COLUMN_ID = "ID";
  static final String COLUMN_FIRST_NAME = "First";
  static final String COLUMN_MIDDLE_NAME = "Middle";
  static final String COLUMN_LAST_NAME = "Last";
  static final String COLUMN_GRADE = "Grade";
  
  public LoadDetails load(InputStream inputStream) throws IOException;
}
