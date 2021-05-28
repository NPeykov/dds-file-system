package model;

public class Buffer {
  private byte[] bytes;
  private int start;
  private int end;
  private Integer posicionActual;

  public Buffer(int size){
    if(size < 0)
      throw new RuntimeException("Tamaño de buffer invalido!");
    this.start = 0;
    this.posicionActual = 0;
    this.end   = size - 1;
    this.bytes = new byte[size];
  }

  public byte[] getBytes() {
    return bytes;
  }

  public int getStart() {
    return start;
  }

  public int getEnd() {
    return end;
  }

  public int getPosicionActual(){
    return posicionActual;
  }

  public void moverPosicionActual(Integer offset) {
    if(posicionActual == 0)
      posicionActual += offset - 1;
    else //para ajustar el indice 0
      posicionActual += offset;
  }

  public int espacioDisponible(){
    return posicionActual - end + 1;
  }

}
