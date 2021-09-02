package org.bwettig;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author bradwettig
 */
public class App {

  protected static final Path RESOURCES = Paths.get("resources");
  protected static final Path STAGING = Paths.get(RESOURCES.toString(), "staging");
  protected static final Path ERROR = Paths.get(RESOURCES.toString(), "error");
  protected static final Path SUCCESS = Paths.get(RESOURCES.toString(), "processed");
  protected static final Path COMPILED = Paths.get(RESOURCES.toString(), "compiled");

  protected static Column[] schema = {
      new Column("h1", 0, DataType.STRING),
      new Column("h2", 1, DataType.STRING)
  };

  protected static Column[] schema2 = {
      new Column("Provider Name", 0, DataType.STRING),
      new Column("Zipcode", 0, DataType.STRING),
      new Column("Cost Per Ad Click", 0, DataType.STRING)
  };

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
    List<String> headers = Arrays.stream(schema2).map(Column::getTitle).collect(Collectors.toList());
    out.append(String.join(",", headers));

    File stagingFiles2 = new File(STAGING.toString());
    File[] files = stagingFiles2.listFiles((d, name) -> name.endsWith(".csv"));
    for (File csvfile : files) {
      boolean errorFlag = false;
      if (csvfile.length() == 0) {
        handleError(csvfile);
        break;
      }
      String line = "";
      boolean isHeaderLine = true;
      BufferedReader br = new BufferedReader(new FileReader(csvfile));
      List<Integer> headerIndexing = new ArrayList<>();
      while ((line = br.readLine()) != null) {
        String[] tokens = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        if (isHeaderLine) {
          for (String h : headers) {
            int headerIndex = Arrays.asList(tokens).indexOf(h);
            if (headerIndex < 0) errorFlag = true;
            headerIndexing.add(headerIndex);
          }
          isHeaderLine = false;
        } else {
          List<String> rowData = new ArrayList<>();
          for (int i : headerIndexing) {
            rowData.add(tokens[i]);
          }
          out.append("\n");
          out.append(String.join(",", rowData));
        }
        if (errorFlag) {
          br.close();
          handleError(csvfile);
          break;
        }
      }

      if (!errorFlag) {
        br.close();
        handleSuccess(csvfile);
      }
    }
    out.flush();
    out.close();
  }

  protected static void handleError(File f) throws IOException {
    Path source = Paths.get(f.getPath());
    Path target = Paths.get(ERROR.toString(), source.getFileName().toString());
    Files.move(source,target);
  }

  protected static void handleSuccess(File f) throws IOException {
    Path source = Paths.get(f.getPath());
    Path target = Paths.get(SUCCESS.toString(), source.getFileName().toString());
    Files.move(source,target);
  }
}

