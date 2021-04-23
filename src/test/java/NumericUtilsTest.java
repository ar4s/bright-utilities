import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import pieselki.bright_utilities.utils.NumericUtils;

public class NumericUtilsTest {
  @Test
  public void testFormatting() {
    assertEquals("3k", NumericUtils.getShortNotation(3000));
    assertEquals("3,2k", NumericUtils.getShortNotation(3200));
    assertEquals("300k", NumericUtils.getShortNotation(300000));
    assertEquals("300k", NumericUtils.getShortNotation(300111));
    assertEquals("312k", NumericUtils.getShortNotation(312312));
    assertEquals("31,2k", NumericUtils.getShortNotation(31232));
  }

  @Test
  public void testAbbrevations() {
    assertEquals("3", NumericUtils.getShortNotation(3));
    assertEquals("3k", NumericUtils.getShortNotation((float) 3e3));
    assertEquals("3M", NumericUtils.getShortNotation((float) 3e6));
    assertEquals("3G", NumericUtils.getShortNotation((float) 3e9));
    assertEquals("3T", NumericUtils.getShortNotation((float) 3e12));
    assertEquals("3P", NumericUtils.getShortNotation((float) 3e15));
  }
}
