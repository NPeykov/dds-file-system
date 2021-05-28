package model;

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

  public void read(Buffer buffer){
    int readBytes = lowLevelFileSystem.syncReadFile(fileID,
        buffer.getBytes(),
        buffer.getStart(),
        buffer.getEnd());

    if(readBytes < 0 || readBytes > buffer.capacidadDisponible())
      throw new RuntimeException("Error al leer el archivo!");
    buffer.limit(readBytes);
  }

  public void write(Buffer buffer){
    if(buffer.posicionActual() == -1)
      throw new RuntimeException("Buffer vacio");
    lowLevelFileSystem.syncWriteFile(fileID, buffer.getBytes(), buffer.getStart(), buffer.posicionActual());
    buffer.moverPosicionActual(1);
    //le paso posicion actual en el buffer ya que luego de eso no habria nada para escribir
    //muevo uno porque quedaria en el siguiente caracter luego de leer
  }

  public void read(Buffer buffer){
    int leido = lowLevelFileSystem.syncReadFile(fileID, buffer.getBytes(), buffer.getStart(), buffer.getEnd());
    buffer.moverPosicionActual(leido);
  }

  public void asyncRead(Buffer buffer, Consumer<Buffer> callback){
    lowLevelFileSystem.asyncRead
    (
      fileID, 
      buffer.getBytes(), 
      buffer.getStart(), 
      buffer.getEnd(), 
      (leido) -> {
              buffer.moverPosicionActual(leido);
              callback.accept(buffer);
            };
        
    );
  }

  public void asyncWrite(Buffer buffer, Runnable callback){
    if(buffer.posicionActual() == -1)
      throw new RuntimeException("Buffer vacio");
    lowLevelFileSystem.asyncWrite
    (
      fileID, 
      buffer.getBytes(), 
      buffer.getStart(), 
      buffer.getPosicionActual(), 
      () -> {
              buffer.moverPosicionActual(1);
              callback.accept(buffer);
            };
        
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
