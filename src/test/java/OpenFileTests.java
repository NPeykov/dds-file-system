import model.File;
import model.HighLevelFileSystem;
import model.LowLevelFileSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

public class OpenFileTests {
  LowLevelFileSystem lowLevelFSMock = Mockito.mock(LowLevelFileSystem.class);
  HighLevelFileSystem highLevelFS = new HighLevelFileSystem(lowLevelFSMock);

  @BeforeEach
  void init(){
    when(lowLevelFSMock.openFile("mi/ruta/11")).thenReturn(1);
    when(lowLevelFSMock.openFile("mi/ruta/22")).thenReturn(2);
    when(lowLevelFSMock.openFile("mi/ruta/FALLA")).thenReturn(0);
  }

  @Test
  void siHayErrorAlAbrirArchivoTiraError(){
    assertThrows(RuntimeException.class,
        () -> highLevelFS.open("mi/ruta/FALLA"));
  }

  @Test
  void siAbroUnArchivoYaAbiertoTiraError(){
    highLevelFS.open("mi/ruta/11");
    assertThrows(RuntimeException.class,
        () -> highLevelFS.open("mi/ruta/11"));
  }

  @Test
  void alCrearDosArchivoSeGuardanEnHistorialArchivos(){
    highLevelFS.open("mi/ruta/11");
    highLevelFS.open("mi/ruta/22");
    assertEquals(2, highLevelFS.getHistorialArchivos().size());
  }

  @Test
  void sePuedeSaberSiUnDirectorioExiste(){
    highLevelFS.open("mi/ruta/11");
    highLevelFS.open("mi/ruta/22");
    assertTrue(highLevelFS.existeDirectorio("mi/ruta"));
  }

  @Test
  void noExisteUnDirectorio(){
    highLevelFS.open("mi/ruta/11");
    highLevelFS.open("mi/ruta/22");
    assertFalse(highLevelFS.existeDirectorio("mi/rucula"));
  }

  @Test
  void elIDDeMiArchivoCreadoEs2(){
    File archivo = highLevelFS.open("mi/ruta/22");
    assertEquals(2, archivo.getFileID());
  }

  @Test
  void elPathDeMiArchivoCreadoEsElDeseado(){
    File archivo = highLevelFS.open("mi/ruta/22");
    assertEquals("mi/ruta/22", archivo.getPath());
  }

  @Test
  void alCrearUnArchivoSeAsignaElDirectorio(){
    File archivo = highLevelFS.open("mi/ruta/22");
    assertEquals("mi/ruta", archivo.getDirectory());
  }

  @Test
  void alCerrarUnArchivoSeBorraDeLosArchivosAbiertos(){
    File archivo = highLevelFS.open("mi/ruta/22");
    archivo.close();
    assertFalse(highLevelFS.getArchivosAbiertos().contains(archivo));
  }

  @Test
  void alCerrarUnArchivoNOseBorraDelHistorial(){
    File archivo = highLevelFS.open("mi/ruta/22");
    archivo.close();
    assertTrue(highLevelFS.getHistorialArchivos().contains(archivo));
  }

}
