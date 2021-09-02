package org.bwettig;

import org.bwettig.core.InsAggIncStrategy;
import org.bwettig.services.FileManager;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author bradwettig
 */
public class App {

  private static final Path RESOURCES = Paths.get("resources");
  private static final Path STAGING = Paths.get(RESOURCES.toString(), "staging");
  private static final Path ERROR = Paths.get(RESOURCES.toString(), "error");
  private static final Path SUCCESS = Paths.get(RESOURCES.toString(), "processed");
  private static final Path COMPILED = Paths.get(RESOURCES.toString(), "compiled");

  public static void main(String[] args) {
    Map<String,Path> dirPaths = new HashMap<>();
        dirPaths.put("Staging",STAGING);
        dirPaths.put("Error", ERROR);
        dirPaths.put("Success", SUCCESS);
        dirPaths.put("Compiled", COMPILED);

    FileManager fm = new FileManager(dirPaths);
    InsAggIncStrategy strategy = new InsAggIncStrategy(fm);

    try {
      fm.validateDirStructure();
      String outPath = fm.getOutputFilePath(COMPILED, strategy.getFileEncoding(), new Date());
      fm.setOutputFW(new FileWriter(outPath));

      File[] files = fm.getStagedFiles(STAGING, strategy.getFileEncoding());
      strategy.process(files);

      fm.close();

    } catch(Exception e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
    }
  }


}

