package org.bwettig.core;

import org.bwettig.services.FileManager;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;

/**
 * @author bradwettig
 */
public class InsAggIncStrategyTest {
  private final PrintStream stderr = System.err;
  private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

  @Test
  public void all_headers_for_this_schema_should_render() throws IOException {
    Map<String, Path> mockPaths = new HashMap<>();
    Path p = Paths.get("test");
    mockPaths.put("Test", p);

    FileManager fm = new FileManager(mockPaths);
    fm.setOutputFW(new FileWriter(p.toString()));
    FileManager spyFM = Mockito.spy(fm);
    InsAggIncStrategy iaiStrategy = new InsAggIncStrategy(spyFM);

    iaiStrategy.setHeader();
    Mockito.verify(spyFM).writeToOutput("Provider Name,CampaignID,Cost Per Ad Click,Redirect Link,Phone Number,Address,Zipcode");
  }

  @Test
  public void should_throw_when_header_is_missing() throws IOException {
    Map<String, Path> mockPaths = new HashMap<>();
    Path p = Paths.get("test");
    mockPaths.put("Test", p);

    FileManager fm = new FileManager(mockPaths);
    fm.setOutputFW(new FileWriter(p.toString()));

    InsAggIncStrategy iaiStrategy = new InsAggIncStrategy(fm);
    iaiStrategy.setHeader();
    String[] tokens = {"Provider Name", "CampaignID", "Cost Per Ad Click", "Redirect Link", "", "Address", "Zipcode"};

    assertThrows(RuntimeException.class, () -> iaiStrategy.validateHeaders(tokens));

  }

  @Test
  public void should_not_throw_when_header_matches_up() throws IOException {
    Map<String, Path> mockPaths = new HashMap<>();
    Path p = Paths.get("test");
    mockPaths.put("Test", p);

    FileManager fm = new FileManager(mockPaths);
    fm.setOutputFW(new FileWriter(p.toString()));

    InsAggIncStrategy iaiStrategy = new InsAggIncStrategy(fm);
    iaiStrategy.setHeader();
    String[] tokens = {"Provider Name", "CampaignID", "Cost Per Ad Click", "Redirect Link", "Phone Number", "Address", "Zipcode", "Extra"};

    List<Integer> expected = new ArrayList<Integer>();
        expected.add(0);
        expected.add(1);
        expected.add(2);
        expected.add(3);
        expected.add(4);
        expected.add(5);
        expected.add(6);

    List<Integer> result = iaiStrategy.validateHeaders(tokens);
    Assert.assertEquals(expected, result);
  }

  @Test
  public void check_that_invalid_data_types_are_caught_float() throws IOException {
        System.setErr(new PrintStream(outputStream));

    Map<String, Path> mockPaths = new HashMap<>();
    Path p = Paths.get("test");
    mockPaths.put("Test", p);

    FileManager fm = new FileManager(mockPaths);
    fm.setOutputFW(new FileWriter(p.toString()));

    InsAggIncStrategy iaiStrategy = new InsAggIncStrategy(fm);
    iaiStrategy.setHeader();
    String[] headerTokens = {"Provider Name", "CampaignID", "Cost Per Ad Click", "Redirect Link", "Phone Number", "Address", "Zipcode",};
    iaiStrategy.validateHeaders(headerTokens);

    String[] rowTokens = {"val", "val", "thisShouldBeAFloat", "val", "val", "val", "val"};
    List<String> result = iaiStrategy.validateRowData(rowTokens, 0);
    // 'null' included here to represent the file that doesn't exist
    Assert.assertEquals("null: Invalid Data Type - Cost Per Ad Click - Row #0", outputStream.toString().trim());

    System.setErr(stderr);
  }

  @Test
  public void check_that_invalid_data_types_are_caught_zipcode() throws IOException {
    System.setErr(new PrintStream(outputStream));

    Map<String, Path> mockPaths = new HashMap<>();
    Path p = Paths.get("test");
    mockPaths.put("Test", p);

    FileManager fm = new FileManager(mockPaths);
    fm.setOutputFW(new FileWriter(p.toString()));

    InsAggIncStrategy iaiStrategy = new InsAggIncStrategy(fm);
    iaiStrategy.setHeader();
    String[] headerTokens = {"Provider Name", "CampaignID", "Cost Per Ad Click", "Redirect Link", "Phone Number", "Address", "Zipcode",};
    iaiStrategy.validateHeaders(headerTokens);

    String[] rowTokens = {"val", "val", "6", "val", "val", "val", "ThisShouldBeAZipCodeType"};
    iaiStrategy.validateRowData(rowTokens, 0);
    // 'null' included here to represent the file that doesn't exist
    Assert.assertEquals("null: Invalid Data Type - Zipcode - Row #0", outputStream.toString().trim());

    System.setErr(stderr);
  }

  @Test
  public void check_that_missing_data_is_caught() throws IOException {
    System.setErr(new PrintStream(outputStream));

    Map<String, Path> mockPaths = new HashMap<>();
    Path p = Paths.get("test");
    mockPaths.put("Test", p);

    FileManager fm = new FileManager(mockPaths);
    fm.setOutputFW(new FileWriter(p.toString()));

    InsAggIncStrategy iaiStrategy = new InsAggIncStrategy(fm);
    iaiStrategy.setHeader();
    String[] headerTokens = {"Provider Name", "CampaignID", "Cost Per Ad Click", "Redirect Link", "Phone Number", "Address", "Zipcode",};
    iaiStrategy.validateHeaders(headerTokens);

    String[] rowTokens = {"val", "val", ".6", "val", "", "val", "444"};
    iaiStrategy.validateRowData(rowTokens, 0);
    // 'null' included here to represent the file that doesn't exist
    Assert.assertEquals("null: Missing Data - Row #0", outputStream.toString().trim());

    System.setErr(stderr);
  }

  @Test
  public void check_that_well_formed_data_passes() throws IOException {
    Map<String, Path> mockPaths = new HashMap<>();
    Path p = Paths.get("test");
    mockPaths.put("Test", p);

    FileManager fm = new FileManager(mockPaths);
    fm.setOutputFW(new FileWriter(p.toString()));

    InsAggIncStrategy iaiStrategy = new InsAggIncStrategy(fm);
    iaiStrategy.setHeader();
    String[] headerTokens = {"Provider Name", "CampaignID", "Cost Per Ad Click", "Redirect Link", "Phone Number", "Address", "Zipcode",};
    iaiStrategy.validateHeaders(headerTokens);

    String[] rowTokens = {"val", "val", "6.64", "val", "val", "val", "77008"};
    List<String> result = iaiStrategy.validateRowData(rowTokens, 0);
    List<String> expected = new ArrayList<>();
      expected.add("val");
      expected.add("val");
      expected.add("6.64");
      expected.add("val");
      expected.add("val");
      expected.add("val");
      expected.add("77008");
    Assert.assertEquals(result, expected);
  }

}