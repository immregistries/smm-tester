package org.immregistries.hart.mover;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class TenIISConverter {

  private static final char CR = 13;
  private static final char LF = 10;

  // returns the number of messages processed.
  public static int convert(BufferedReader input, OutputStreamWriter output) throws IOException {
    String line;
    boolean mshFoundOnce = false;
    int count = 0;
    while ((line = input.readLine()) != null) {
      if (line.startsWith("BTS") || (mshFoundOnce && line.startsWith("MSH"))) {
        output.write(LF);
      }
      output.write(line);
      output.write(CR);
      if (line.startsWith("FHS") || line.startsWith("BHS") || line.startsWith("BTS") || line.startsWith("FTS")) {
        output.write(LF);
      }
      if (line.startsWith("MSH")) {
        mshFoundOnce = true;
        count++;
      }
    }
    input.close();
    output.close();
    return count;
  }
}
