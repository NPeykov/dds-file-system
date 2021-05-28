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
    this.posicionActual = -1;
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
    posicionActual += offset;
  }

  public void limit(int offset){
    end = start + offset - 1;
  }

  public int espacioDisponible(){
    return bytes.length;
  }

}
