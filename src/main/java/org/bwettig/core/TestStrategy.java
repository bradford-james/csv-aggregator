package org.bwettig.core;

import org.bwettig.model.Column;
import org.bwettig.model.DataType;
import org.bwettig.services.FileManager;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author bradwettig
 */
public class TestStrategy extends CsvStrategy {
  protected static Column[] schema = {
      new Column("h1", DataType.STRING),
      new Column("h2", DataType.STRING)
  };

  public TestStrategy(FileManager fm) {
    super(fm);
  }

  @Override
  public void setHeader() throws IOException {
    headers = Arrays.stream(schema).map(Column::getTitle).collect(Collectors.toList());
    fm.writeToOutput(String.join(",", headers));
  }
}
