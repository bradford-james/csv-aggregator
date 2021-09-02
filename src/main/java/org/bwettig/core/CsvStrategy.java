package org.bwettig.core;

import org.bwettig.model.Column;
import org.bwettig.services.FileManager;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author bradwettig
 */
abstract public class CsvStrategy implements Strategy {

  private Column[] schema;
  protected List<String> headers;
  protected final FileManager fm;

  private final List<Integer> headerIndexing = new ArrayList<>();

  public CsvStrategy(FileManager fm) {
    this.fm = fm;
  }

  public void process(File[] files) throws IOException {
    setHeader();
    for (File csvfile : files) {
      BufferedReader br = new BufferedReader(new FileReader(csvfile));

      try {
        if (csvfile.length() == 0) {
          throw Exception;
        }
        String line = "";
        boolean isHeaderRow = true;
        while ((line = br.readLine()) != null) {
          String[] tokens = parseCsvLine(line);
          if (isHeaderRow) {
            validateHeaders(tokens);
            isHeaderRow = false;
          } else {
            List<String> rowData = validateRowData(tokens);
            setRowData(rowData);
          }
        }
        br.close();
        fm.moveToSuccess(csvfile);

      } catch(Exception e) {
        br.close();
        fm.moveToError(csvfile);
        break;
      }
    }
  }

  private String[] parseCsvLine(String line) {
    String splitRegex = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
    return line.split(splitRegex, -1);
  }

  private boolean validateHeaders(String[] tokens) {
    for (String h : headers) {
      int headerIndex = Arrays.asList(tokens).indexOf(h);
      if (headerIndex < 0) return false;
      headerIndexing.add(headerIndex);
    }
    return true;
  }

  private List<String> validateRowData(String[] tokens) {
    List<String> rowData = new ArrayList<>();
    for (int i : headerIndexing) {
      rowData.add(tokens[i]);
    }
    return rowData;
  }

  public void setRowData(List<String> rowData) throws IOException {
    fm.writeToOutput("\n");
    fm.writeToOutput(String.join(",", rowData));
  }

  abstract public void setHeader() throws IOException;

  public String getFileEncoding() {
    String fileEncoding = "csv";
    return fileEncoding;
  }
}
