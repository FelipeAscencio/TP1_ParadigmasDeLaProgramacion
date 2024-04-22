package org.example;

public class Jugador {
    private int filaActual;
    private int columnaActual;

    public Jugador(int filaActual,int columnaActual){
        this.columnaActual=columnaActual;
        this.filaActual=filaActual;
    }

    public int getFilaActual() {
        return filaActual;
    }

    public int getColumnaActual() {
        return columnaActual;
    }

    public void setFila(int fila){
        filaActual=fila;
    }

    public void setColumna(int columna){
        columnaActual=columna;
    }

    public void mover(Direccion direccion){
        filaActual+=direccion.getCambioFila();
        columnaActual+=direccion.getCambioColumna();
    }
}
