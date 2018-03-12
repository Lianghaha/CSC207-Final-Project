package warehousesystem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class CustomFormatter extends Formatter {
  private static final DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:SSS");
  private static long initialTime = System.nanoTime();

  @Override
  public String format(LogRecord record) {
    StringBuilder output = new StringBuilder();
    if (record.getSequenceNumber() == 0) {
      output.append("\n\t\t[Warehouse Log Records\t");
      output.append(df.format(new Date(record.getMillis())));
      output.append("]\n\n[Runtime: ");
      output.append(System.nanoTime() - initialTime + "ns");
      output.append("   Log Number: " + (record.getSequenceNumber() + 1));
      output.append("  Type: " + record.getLevel() + "]\t");
    } else {
      output.append("[Runtime: ");
      output.append(System.nanoTime() - initialTime + "ns");
      if ((System.nanoTime() - initialTime) < 10000000) {
        output.append(" ");
      }
      output.append("  Log Number: " + (record.getSequenceNumber() + 1));
      output.append("  Type: " + record.getLevel() + "]");
      if (record.getSequenceNumber() < 10 && record.getLevel().toString().equals("INFO")) {
        output.append("  ");
      }
      output.append("\t");
    }
    output.append(record.getMessage() + "\n");
    return output.toString();
  }
}
