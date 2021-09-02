package org.bwettig.core;

import org.bwettig.model.Column;
import org.bwettig.services.FileManager;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author bradwettig
 */
abstract public class CsvStrategy implements Strategy {

  private Column[] schema;
  protected List<String> headers;
  protected final FileManager fm;

  public CsvStrategy(FileManager fm) {
    this.fm = fm;
  }

  public void process(File[] files) throws IOException {
    setHeader();
    for (File csvfile : files) {
      boolean errorFlag = false;
      if (csvfile.length() == 0) {
        fm.moveToError(csvfile);
        break;
      }

      String line = "";
      boolean isHeaderLine = true;
      List<Integer> headerIndexing = new ArrayList<>();

      BufferedReader br = new BufferedReader(new FileReader(csvfile));
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
          fm.writeToOutput("\n");
          fm.writeToOutput(String.join(",", rowData));
        }
        if (errorFlag) {
          br.close();
          fm.moveToError(csvfile);
          break;
        }
      }

      if (!errorFlag) {
        br.close();
        fm.moveToSuccess(csvfile);
      }
    }
  }

  abstract public void setHeader() throws IOException;

  public String getFileEncoding() {
    String fileEncoding = "csv";
    return fileEncoding;
  }
}
