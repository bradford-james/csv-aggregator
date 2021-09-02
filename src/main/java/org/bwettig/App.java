package org.bwettig;

import org.bwettig.core.InsAggIncStrategy;
import org.bwettig.services.FileManager;

import java.io.*;

/**
 * @author bradwettig
 */
public class App {

  public static void main(String[] args) {
    FileManager fm = new FileManager();
    InsAggIncStrategy strategy = new InsAggIncStrategy(fm);

    try {
      fm.initialize(strategy.getFileEncoding());

      File[] files = fm.getStagedFiles();
      strategy.process(files);

      fm.close();

    } catch(Exception e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
    }
  }
}

