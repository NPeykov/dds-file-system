package model;

import java.util.ArrayList;
import java.util.List;

public class HighLevelFileSystem {
  LowLevelFileSystem lowLevelFileSystem;
  List<File> archivosAbiertos;
  List<File> historialArchivos;


  public HighLevelFileSystem(LowLevelFileSystem lowLevelFileSystem) {
    this.lowLevelFileSystem = lowLevelFileSystem;
    this.archivosAbiertos = new ArrayList<>();
    this.historialArchivos = new ArrayList<>();
  }

  public File open(String path){
    if(yaEstaAbierto(path))
      throw new RuntimeException("El archivo ya esta abierto!!");
    int fileID = lowLevelFileSystem.openFile(path);
    if(fileID == 0) //aca vendria el valor de error
      throw new RuntimeException("Error al abrir el archivo");
    File nuevoArchivo = new File(path, getDirectory(path), fileID, this.lowLevelFileSystem, this);
    archivosAbiertos.add(nuevoArchivo);
    historialArchivos.add(nuevoArchivo);
    return nuevoArchivo;
  }

  public void close(File file){
    archivosAbiertos.remove(file);
    lowLevelFileSystem.closeFile(file.getFileID());
  }

  public boolean existeArchivo(String path){
    return historialArchivos.stream().anyMatch(archivo -> archivo.getPath().equals(path));
  }

  public boolean existeDirectorio(String path){
    return historialArchivos.stream().anyMatch(archivo -> archivo.getDirectory().equals(path));
  }

  public boolean existePath(String path){
    return existeArchivo(path) || existeDirectorio(path);
  }

  public boolean yaEstaAbierto(String path){
    return archivosAbiertos.stream().anyMatch(archivo -> archivo.getPath().equals(path));
  }

  String getDirectory(String path){
    return path.substring(0, path.lastIndexOf("/"));
  }

  public List<File> getArchivosAbiertos(){
    return this.archivosAbiertos;
  }

  public List<File> getHistorialArchivos(){
    return this.historialArchivos;
  }
}
