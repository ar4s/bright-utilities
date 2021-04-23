package pieselki.bright_utilities.utils;

import java.text.DecimalFormat;

public class NumericUtils {
  private static String[] ABBREVATIONS = { "", "k", "M", "G", "T", "P", "E", "Z", "Y" };

  public static String getShortNotation(float num) {
    int abbrevationIndex = 0;
    while (num > 1000.0f) {
      num /= 1000.0f;
      abbrevationIndex++;
    }
    String formatted = new DecimalFormat("###.##").format(num);
    formatted = formatted.substring(0, Math.min(formatted.length(), 4));
    if (formatted.charAt(formatted.length() - 1) == ',') {
      formatted = formatted.substring(0, formatted.length() - 1);
    }
    return formatted + ABBREVATIONS[abbrevationIndex];
  }
}
