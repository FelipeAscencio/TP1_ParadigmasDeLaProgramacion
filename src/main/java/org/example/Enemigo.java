package org.example;

public abstract class Enemigo {
    private int filaActual;
    private int columnaActual;

    public Enemigo(int filaInicial, int columnaInicial) {
        this.filaActual = filaInicial;
        this.columnaActual = columnaInicial;
    }
    public int getFilaActual() {
        return filaActual;
    }

    public int getColumnaActual() {
        return columnaActual;
    }

    public void setFilaActual(int fil){
        filaActual=fil;
    }
    public void setColumnaActual(int col){
        columnaActual=col;
    }

    public abstract void mover(int filaJugador, int columnaJugador);

    public boolean colisionJugador(int filaJugador, int columnaJugador){
        return (filaJugador == getFilaActual() && columnaJugador == getColumnaActual());
    }

    //public abstract boolean colisionCeldaIncendiada(boolean[][] celdasIncendiadas);

}
