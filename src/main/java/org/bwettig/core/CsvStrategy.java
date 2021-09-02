package org.bwettig.core;

import org.bwettig.model.Column;
import org.bwettig.model.Strategy;
import org.bwettig.services.FileManager;

import java.io.*;
import java.util.List;

/**
 * @author bradwettig
 */
abstract public class CsvStrategy implements Strategy {

  private Column[] schema;
  protected List<String> headers;
  protected final FileManager fm;
  protected File currentFile;

  public CsvStrategy(FileManager fm) {
    this.fm = fm;
  }

  public void process(File[] files) throws IOException {
    setHeader();
    for (File csvfile : files) {
      currentFile = csvfile;
      boolean isHeaderRow = true;
      BufferedReader br = new BufferedReader(new FileReader(csvfile));
      int rowCounter = 1;

      try {
        if (csvfile.length() == 0) {
          throw new RuntimeException("File is empty");
        }
        String line = "";
        while ((line = br.readLine()) != null) {
          String[] tokens = parseCsvLine(line);
          if (isHeaderRow) {
            validateHeaders(tokens);
            isHeaderRow = false;
          } else {
            List<String> rowData = validateRowData(tokens, rowCounter);
            if (rowData.size() > 0) setRowData(rowData);
          }
          rowCounter++;
        }
        br.close();
        fm.moveToSuccess(csvfile);

      } catch (RuntimeException e) {
        br.close();
        fm.moveToError(csvfile);
        System.err.print(csvfile + ": " + e.getMessage() + "\n");
        e.printStackTrace();

      }
    }
  }


  protected static String[] parseCsvLine(String line) {
    String splitRegex = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
    return line.split(splitRegex, -1);
  }

  public void setRowData(List<String> rowData) throws IOException {
    fm.writeToOutput("\n");
    fm.writeToOutput(String.join(",", rowData));
  }

  abstract protected List<Integer> validateHeaders(String[] tokens);

  abstract protected List<String> validateRowData(String[] tokens, int rowCounter);

  abstract protected void setHeader() throws IOException;

  public String getFileEncoding() {
    return "csv";
  }
}
