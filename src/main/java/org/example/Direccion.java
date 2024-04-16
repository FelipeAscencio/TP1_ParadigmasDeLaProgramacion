package org.example.model;

public enum Direccion {
    ARRIBA(-1, 0),
    ABAJO(1, 0),
    IZQUIERDA(0, -1),
    DERECHA(0, 1),
    DIAGONAL_ARRIBA_IZQUIERDA(-1,-1),
    DIAGONAL_ARRIBA_DERECHA(-1, 1),
    DIAGONAL_ABAJO_IZQUIERDA(1, -1),
    DIAGONAL_ABAJO_DERECHA(1,1);

    private final int cambioFila;
    private final int cambioColumna;

    Direccion(int cambioFila, int cambioColumna) {
        this.cambioFila = cambioFila;
        this.cambioColumna = cambioColumna;
    }

    public int getCambioFila() {
        return cambioFila;
    }

    public int getCambioColumna() {
        return cambioColumna;
    }
}

