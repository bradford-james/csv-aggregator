package org.bwettig.core;

import org.bwettig.model.Column;
import org.bwettig.model.DataType;
import org.bwettig.services.FileManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author bradwettig
 */
public class InsAggIncStrategy extends CsvStrategy {
  private final Column[] schema = {
      new Column("Provider Name", DataType.STRING),
      new Column("CampaignID", DataType.STRING),
      new Column("Cost Per Ad Click", DataType.FLOAT),
      new Column("Redirect Link", DataType.STRING),
      new Column("Phone Number", DataType.STRING),
      new Column("Address", DataType.STRING),
      new Column("Zipcode", DataType.ZIP_CODE)
  };
  private List<Integer> headerIndexing = new ArrayList<>();

  public InsAggIncStrategy(FileManager fm) {
    super(fm);
  }

  @Override
  public void setHeader() throws IOException {
    headers = Arrays.stream(schema).map(Column::getTitle).collect(Collectors.toList());
    fm.writeToOutput(String.join(",", headers));
  }

  protected void validateHeaders(String[] tokens) {
    headerIndexing = new ArrayList<>();
    for (String h : headers) {
      int headerIndex = Arrays.asList(tokens).indexOf(h);
      if (headerIndex < 0) throw new RuntimeException("Missing schema header - " + h);
      headerIndexing.add(headerIndex);
    }
  }

  protected List<String> validateRowData(String[] tokens, int rowCounter) {
    List<String> rowData = new ArrayList<>();
    int counter = -1;
    for(int i : headerIndexing) {
      counter++;
      String t = tokens[i];
      if (t.isEmpty() || t.isBlank()) {
        System.err.println(currentFile + ": Missing Data - Row #" + rowCounter);
         rowData = new ArrayList<>();
        break;
      }

      Pattern regex = null;
      switch (schema[counter].getType()) {
        case STRING:
          regex = Pattern.compile(".", Pattern.CASE_INSENSITIVE);
          break;
        case FLOAT:
          regex = Pattern.compile("^\\\"*[0-9]*\\.?[0-9]+\\\"*$", Pattern.CASE_INSENSITIVE);
          break;
        case ZIP_CODE:
          regex = Pattern.compile("^\\\"*[0-9]{5}\\\"*$", Pattern.CASE_INSENSITIVE);
          break;
        default:
          break;
      }
      Matcher matcher = regex.matcher(t);
      boolean matchFound = matcher.find();
      if (!matchFound) {
        System.err.print(currentFile + ": Invalid Data Type - " + schema[counter].getTitle() + " - Row #" + rowCounter + "\n");
        rowData = new ArrayList<>();
        break;
      }
      rowData.add(tokens[i]);
    }
    return rowData;
  }
}
