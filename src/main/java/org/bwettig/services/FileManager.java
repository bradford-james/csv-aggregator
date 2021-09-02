package org.bwettig.services;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * @author bradwettig
 */
public class FileManager {
  private final Path RESOURCES = Paths.get("resources");
  private final Path STAGING = Paths.get(RESOURCES.toString(), "staging");
  private final Path ERROR = Paths.get(RESOURCES.toString(), "error");
  private final Path SUCCESS = Paths.get(RESOURCES.toString(), "processed");
  private final Path COMPILED = Paths.get(RESOURCES.toString(), "compiled");

  private FileWriter out;
  private String fileEncoding;

  public void initialize(String fe) throws IOException {
    this.fileEncoding = fe;
    validateDirStructure();
    this.out = initOutputFileWriter();
  }

  private FileWriter initOutputFileWriter() throws IOException {
    Date date = new Date();
    Timestamp timestamp = new Timestamp(date.getTime());
    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss");
    String outputFileName = sdf1.format(timestamp) + "-output." + fileEncoding;
    Path outputFilePath = Paths.get(COMPILED.toString(), outputFileName);
    return new FileWriter(outputFilePath.toString());
  }

  private void validateDirStructure() {
    for (Path p : Arrays.asList(STAGING, ERROR, SUCCESS, COMPILED)) {
      File f = new File(p.toString());
      if (!(Files.exists(p) && f.canWrite())) return;
    }
  }

  public File[] getStagedFiles() {
    File stagingFiles2 = new File(STAGING.toString());
    return stagingFiles2.listFiles((d, name) -> name.endsWith("." + fileEncoding));
  }

  public void writeToOutput(String output) throws IOException {
    out.append(output);
  }

  public void moveToError(File f) throws IOException {
    Path source = Paths.get(f.getPath());
    Path target = Paths.get(ERROR.toString(), source.getFileName().toString());
    Files.move(source,target);
  }

  public void moveToSuccess(File f) throws IOException {
    Path source = Paths.get(f.getPath());
    Path target = Paths.get(SUCCESS.toString(), source.getFileName().toString());
    Files.move(source,target);
  }

  public void close() throws IOException {
    out.flush();
    out.close();
  }
}
