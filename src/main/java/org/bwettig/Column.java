package org.bwettig;

/**
 * @author bradwettig
 */
public class Column {
  private String title;
  private Integer index;
  private DataType type;

  public Column(String title, Integer index, DataType type) {
    this.title = title;
    this.index = index;
    this.type = type;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Integer getIndex() {
    return index;
  }

  public void setIndex(Integer index) {
    this.index = index;
  }

  public DataType getType() {
    return type;
  }

  public void setType(DataType type) {
    this.type = type;
  }
}
