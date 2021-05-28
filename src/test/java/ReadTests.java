import model.Buffer;
import model.File;
import model.HighLevelFileSystem;
import model.LowLevelFileSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class ReadTests {
  LowLevelFileSystem lowLevelFSMock = Mockito.mock(LowLevelFileSystem.class);
  HighLevelFileSystem highLevelFS = new HighLevelFileSystem(lowLevelFSMock);

  @BeforeEach
  void init() {
    when(lowLevelFSMock.openFile("mi/ruta/11")).thenReturn(1);

    //stub read
    when(lowLevelFSMock.syncReadFile(
        1, new byte[10], 0, 9))
        .thenReturn(4);
    when(lowLevelFSMock.syncReadFile(
        1, new byte[1], 0, 0))
        .thenReturn(1);
    when(lowLevelFSMock.syncReadFile(
        1, new byte[5], 0, 4))
        .thenReturn(5);
  }

  @Test
  void siLeo3BytesDelArchivoSeMueveElBufferAPosActual3(){
    Buffer bufferinoRead = new Buffer(10);
    File archivo = highLevelFS.open("mi/ruta/11");
    archivo.read(bufferinoRead);
    assertEquals(3, bufferinoRead.getEnd());
  }

  @Test
  void siLeo1BytesMasElBufferSeMueveAPosActual5(){
    Buffer bufferinoRead = new Buffer(1);
    File archivo = highLevelFS.open("mi/ruta/11");
    archivo.read(bufferinoRead);
    assertEquals(0, bufferinoRead.getEnd());
  }

  @Test
  void siLeo5BytesMasElBufferSeMueveAPosActual9(){
    Buffer bufferinoRead = new Buffer(5);
    File archivo = highLevelFS.open("mi/ruta/11");
    archivo.read(bufferinoRead);
    assertEquals(4, bufferinoRead.getEnd());
  }


}