package org.example.model;

public class Tablero {
    private int filas;
    private int columnas;

    private Object[][] matriz;

    public Tablero(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        this.matriz = new Object[filas][columnas];
    }

    public int getFilas() {
        return filas;
    }

    public int getColumnas() {
        return columnas;
    }

    public Object getElemento(int fila, int columna) {
        return matriz[fila][columna];
    }

    public void setElemento(int fila, int columna, Object elemento) {
        matriz[fila][columna] = elemento;
    }
}
