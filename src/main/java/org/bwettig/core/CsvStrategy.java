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

  public CsvStrategy(FileManager fm) {
    this.fm = fm;
  }

  public void process(File[] files) throws IOException {
    setHeader();
    for (File csvfile : files) {
      BufferedReader br = new BufferedReader(new FileReader(csvfile));

      try {
        if (csvfile.length() == 0) {
          throw new RuntimeException("File is empty: " + csvfile.getName());
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

      } catch(RuntimeException e) {
        br.close();
        fm.moveToError(csvfile);
        System.err.print(e.getMessage());
        break;
      }
    }
  }

  private String[] parseCsvLine(String line) {
    String splitRegex = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
    return line.split(splitRegex, -1);
  }

  public void setRowData(List<String> rowData) throws IOException {
    fm.writeToOutput("\n");
    fm.writeToOutput(String.join(",", rowData));
  }
  abstract protected void validateHeaders(String[] tokens);

  abstract protected List<String> validateRowData(String[] tokens);

  abstract protected void setHeader() throws IOException;

  public String getFileEncoding() {
    return "csv";
  }
}
