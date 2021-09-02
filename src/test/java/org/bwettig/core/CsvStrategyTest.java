package org.bwettig.core;

import junit.framework.TestCase;
import org.bwettig.services.FileManager;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author bradwettig
 */
public class CsvStrategyTest {

  @Test
  public void returns_parsed_csv_line() {
    String testString = "val1,val2,val3";
    String[] expected = {"val1","val2","val3"};
    String[] result = CsvStrategy.parseCsvLine(testString);
    Assert.assertEquals(expected, result);
  }

  @Test
  public void returns_parsed_csv_line_2() {
    String testString = "val1,\"va,l2\",val3";
    String[] expected = {"val1","\"va,l2\"","val3"};
    String[] result = CsvStrategy.parseCsvLine(testString);
    Assert.assertEquals(expected, result);
  }

  @Test
  public void returns_parsed_csv_line_3() {
    String testString = ",val2,val3";
    String[] expected = {"","val2","val3"};
    String[] result = CsvStrategy.parseCsvLine(testString);
    Assert.assertEquals(expected, result);
  }

  @Test
  public void returns_parsed_csv_line_4() {
    String testString = "";
    String[] expected = {""};
    String[] result = CsvStrategy.parseCsvLine(testString);
    Assert.assertEquals(expected, result);
  }
}