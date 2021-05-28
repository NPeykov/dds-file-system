package model;

import java.util.function.*;

public class File {
  private String path;
  private String directory;
  private int fileID;
  private LowLevelFileSystem lowLevelFileSystem;
  private HighLevelFileSystem highLevelFileSystem;

  public File(String path, String directory, int fileID, LowLevelFileSystem lowLevelFileSystem, HighLevelFileSystem highLevelFileSystem) {
    this.path = path;
    this.directory = directory;
    this.fileID = fileID;
    this.lowLevelFileSystem = lowLevelFileSystem;
    this.highLevelFileSystem = highLevelFileSystem;
  }

  public void close(){
    highLevelFileSystem.close(this);
  }

  public void write(Buffer buffer){
    lowLevelFileSystem.syncWriteFile(fileID, buffer.getBytes(), buffer.getStart(), buffer.getPosicionActual());
    //le paso posicion actual en el buffer ya que luego de eso no habria nada para escribir
    //muevo uno porque quedaria en el siguiente caracter luego de leer
  }

  public void read(Buffer buffer){
    int leido = lowLevelFileSystem.syncReadFile(fileID, buffer.getBytes(), buffer.getStart(), buffer.getEnd());
    buffer.limit(leido);
  }

  public void asyncRead(Buffer buffer, Consumer<Buffer> callback) {
    lowLevelFileSystem.asyncReadFile
        (
            fileID,
            buffer.getBytes(),
            buffer.getStart(),
            buffer.getEnd(),
            (leido) -> {
              buffer.limit(leido);
              callback.accept(buffer);
            }
        );
  }

  public void asyncWrite(Buffer buffer, Runnable callback) {
    if (buffer.getPosicionActual() == -1)
      throw new RuntimeException("Buffer vacio");

    lowLevelFileSystem.asyncWriteFile
        (
            fileID,
            buffer.getBytes(),
            buffer.getStart(),
            buffer.getPosicionActual(),
            () -> {
              //callback.accept(buffer);
            }
        );
  }

  //---------------------

  public int getFileID(){
    return fileID;
  }

  public String getDirectory(){
    return directory;
  }

  public String getPath(){
    return path;
  }
}
