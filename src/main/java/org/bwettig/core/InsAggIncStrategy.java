package org.bwettig.core;

import org.bwettig.model.Column;
import org.bwettig.model.DataType;
import org.bwettig.services.FileManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author bradwettig
 */
public class InsAggIncStrategy extends CsvStrategy {
  private Column[] schema = {
      new Column("Provider Name", DataType.STRING),
      new Column("Zipcode", DataType.STRING),
      new Column("Cost Per Ad Click", DataType.STRING)
  };
  private final List<Integer> headerIndexing = new ArrayList<>();

  public InsAggIncStrategy(FileManager fm) {
    super(fm);
  }

  @Override
  public void setHeader() throws IOException {
    headers = Arrays.stream(schema).map(Column::getTitle).collect(Collectors.toList());
    fm.writeToOutput(String.join(",", headers));
  }

  protected void validateHeaders(String[] tokens) {
    for (String h : headers) {
      int headerIndex = Arrays.asList(tokens).indexOf(h);
      if (headerIndex < 0) return;
      headerIndexing.add(headerIndex);
    }
  }

  protected List<String> validateRowData(String[] tokens) {
    List<String> rowData = new ArrayList<>();
      for(int i :headerIndexing) {
        rowData.add(tokens[i]);
      }
      return rowData;
    }
}
