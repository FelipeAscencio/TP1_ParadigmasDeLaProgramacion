package org.example.model;

public class Jugador {
    private int filaActual;
    private int columnaActual;
    private int usosTeletransportacion;

    public Jugador(int filaActual,int columnaActual,int usosTeletransportacion){
        this.columnaActual=columnaActual;
        this.filaActual=filaActual;
        this.usosTeletransportacion=usosTeletransportacion;
    }
    public int getFilaActual() {
        return filaActual;
    }

    public int getColumnaActual() {
        return columnaActual;
    }

    public int getUsosTeletransportacion() {
        return usosTeletransportacion;
    }

    public void mover(int filaNueva, int columnaNueva){
        this.filaActual=filaNueva;
        this.columnaActual=columnaNueva;
    }

    public void mover(Direccion direccion){
        filaActual+=direccion.getCambioFila();
        columnaActual+=direccion.getCambioColumna();
    }

    public void teletransportarse(int nuevaFila, int nuevaColumna) {
        this.filaActual = nuevaFila;
        this.columnaActual = nuevaColumna;
        this.usosTeletransportacion--; // Se reduce un uso de teletransportación
    }

    public void incrementarUsosTeletransportacion() {
        this.usosTeletransportacion++;
    }
}
