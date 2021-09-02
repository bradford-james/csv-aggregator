package org.bwettig.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author bradwettig
 */
public interface Strategy {
  void process(File[] files) throws IOException;
}
