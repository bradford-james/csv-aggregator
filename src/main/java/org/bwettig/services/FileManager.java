package org.bwettig.services;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author bradwettig
 */
public class FileManager {

  private Map<String, Path> dirPaths;
  private FileWriter outputFW;

  public FileManager(Map<String, Path> dirPaths) {
    this.dirPaths = dirPaths;
  }

  public void setOutputFW(FileWriter fw) throws IOException {
    this.outputFW = fw;
  }

  public String getOutputFilePath(Path path, String fileEncoding, Date date) {
    Timestamp timestamp = new Timestamp(date.getTime());
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss");
    String outputFileName = sdf.format(timestamp) + "-output." + fileEncoding;
    Path outputFilePath = Paths.get(path.toString(), outputFileName);
    return outputFilePath.toString();
  }

  public void validateDirStructure() {
    for (Path p : dirPaths.values()) {
      File f = new File(p.toString());
      if (!(Files.exists(p) && f.canWrite())) throw new RuntimeException("Directory structure has been corrupted, ensure that there is a \"resources\" directory in the root with subdirectories \"compiled\", \"error\", \"staging\", and \"processed\"");
    }
  }

  public File[] getStagedFiles(Path p, String fileEncoding) {
    File stagingFiles2 = new File(p.toString());
    return stagingFiles2.listFiles((d, name) -> name.endsWith("." + fileEncoding));
  }

  public void writeToOutput(String output) throws IOException {
    outputFW.append(output);
  }

  public void moveToError(File f) throws IOException {
    Path source = Paths.get(f.getPath());
    Path target = Paths.get(dirPaths.get("Error").toString(), source.getFileName().toString());
    Files.move(source,target);
  }

  public void moveToSuccess(File f) throws IOException {
    Path source = Paths.get(f.getPath());
    Path target = Paths.get(dirPaths.get("Success").toString(), source.getFileName().toString());
    Files.move(source,target);
  }

  public void close() throws IOException {
    outputFW.flush();
    outputFW.close();
  }
}
