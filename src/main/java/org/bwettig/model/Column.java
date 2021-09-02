package org.bwettig.model;

import org.bwettig.model.DataType;

/**
 * @author bradwettig
 */
public class Column {
  private String title;
  private DataType type;

  public Column(String title, DataType type) {
    this.title = title;
    this.type = type;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public DataType getType() {
    return type;
  }

  public void setType(DataType type) {
    this.type = type;
  }
}
