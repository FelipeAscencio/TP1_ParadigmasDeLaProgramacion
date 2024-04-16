package org.example.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Juego {
    private Jugador jugador;
    private Tablero tablero;
    private List<Enemigo> enemigos;

    public Juego(int filas, int columnas){
        tablero=new Tablero(filas,columnas);
        jugador=new Jugador(filas/2,columnas/2,1);
        enemigos=new ArrayList<>();
    }

    private void generarEnemigos(int cantidadEnemigos) {
        Random random = new Random();

        for (int i = 0; i < cantidadEnemigos; i++) {
            int filaAleatoria, columnaAleatoria;

            do {
                filaAleatoria = random.nextInt(tablero.getFilas());
                columnaAleatoria = random.nextInt(tablero.getColumnas());
            } while (esPosicionOcupada(filaAleatoria, columnaAleatoria));

            Enemigo nuevoEnemigo;
            if (random.nextBoolean()){
                nuevoEnemigo=new Robot1(filaAleatoria,columnaAleatoria);
            }
            else{
                nuevoEnemigo =new Robot2(filaAleatoria,columnaAleatoria);
            }
            enemigos.add(nuevoEnemigo);
        }
    }

    private boolean esPosicionOcupada(int fila, int columna) {
        return tablero.getElemento(fila,columna)!=null;
    }

    private boolean esMovimientoValido(int fil, int col) {
        return (fil<0 || fil>=tablero.getFilas() || col<0 || col>=tablero.getColumnas());
    }

    private void moverEnemigos() {
        for (Enemigo enemigo : enemigos) {
            enemigo.mover(jugador.getFilaActual(), jugador.getColumnaActual());
        }
    }

}
