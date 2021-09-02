package org.bwettig.services;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertThrows;

/**
 * @author bradwettig
 */
public class FileManagerTest {

  @Test
  public void output_directory_follows_expected_format() {
    Map<String, Path> mockPaths = new HashMap<>();
    Path p = Paths.get("test");
    mockPaths.put("Test", p);
    Date date = new Date();
    FileManager fm = new FileManager(mockPaths);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss");

    String expected = "test\\" + sdf.format(date) + "-output.csv";
    String result = fm.getOutputFilePath(p, "csv", date);
    Assert.assertEquals(result, expected);
  }

  @Test
  public void check_that_nonexistant_dir_throws() {
    Map<String, Path> mockPaths = new HashMap<>();
    Path p = Paths.get("testtest");
    mockPaths.put("Test", p);

    FileManager fm = new FileManager(mockPaths);
    assertThrows(RuntimeException.class, fm::validateDirStructure);
  }
}