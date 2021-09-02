package org.bwettig;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * @author bradwettig
 */
public class App {

  protected static final Path RESOURCES = Paths.get("resources");
  protected static final Path STAGING = Paths.get(RESOURCES.toString(), "staging");
  protected static final Path ERROR = Paths.get(RESOURCES.toString(), "error");
  protected static final Path SUCCESS = Paths.get(RESOURCES.toString(), "processed");
  protected static final Path COMPILED = Paths.get(RESOURCES.toString(), "compiled");

  protected static final String[] HEADERS = {"h1", "h2", "h3"};

  private static String appendToPathAsString(Path path, String fileName) {
    Path appended = Paths.get(path.toString(), fileName);
    return appended.toString();
  }

  public static void main(String[] args) throws IOException {
    // TODO check that the needed folders exist and can be accessed
    for (Path p : Arrays.asList(STAGING, ERROR, SUCCESS, COMPILED)) {
      File f = new File(p.toString());
      if (!(Files.exists(p) && f.canWrite())) {
        System.exit(0);
        break;
      }
    }

    String compiledFilePath = appendToPathAsString(COMPILED, "output.csv");
    FileWriter out = new FileWriter(compiledFilePath);

    File stagingFiles2 = new File(STAGING.toString());
    File[] files = stagingFiles2.listFiles((d, name) -> name.endsWith(".csv"));

  }
}

