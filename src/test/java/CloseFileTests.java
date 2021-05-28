import model.File;
import model.HighLevelFileSystem;
import model.LowLevelFileSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class CloseFileTests {
  LowLevelFileSystem lowLevelFSMock = Mockito.mock(LowLevelFileSystem.class);
  HighLevelFileSystem highLevelFS = new HighLevelFileSystem(lowLevelFSMock);

  @BeforeEach
  void init(){
    when(lowLevelFSMock.openFile("mi/ruta/11")).thenReturn(1);
    when(lowLevelFSMock.openFile("mi/ruta/22")).thenReturn(2);
    when(lowLevelFSMock.openFile("mi/ruta/FALLA")).thenReturn(0);
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
