package org.example;

public class Explosion extends Enemigo {
    private int fila;
    private int columna;

    public Explosion(int fila, int columna) {
        super(fila,columna);
    }

    @Override
    public void mover(int filajugador, int columnajugador) {
        return;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }
}
