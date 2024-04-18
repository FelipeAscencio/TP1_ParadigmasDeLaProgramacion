package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Juego {
    private Jugador jugador;
    private Tablero tablero;
    private List<Enemigo> enemigos;
    private int CANTIDAD_INICIAL_ENEMIGOS=10;
    private int nivel;
    private int puntos;

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
            tablero.agregarElemento(nuevoEnemigo,filaAleatoria,columnaAleatoria);
            enemigos.add(nuevoEnemigo);
        }
    }

    public void iniciarNivel(int cantidadEnemigos) {
        this.generarEnemigos(cantidadEnemigos);
    }

    private boolean esPosicionOcupada(int fila, int columna) {
        return tablero.getElemento(fila,columna)!=null;
    }

    private boolean esMovimientoValido(int fil, int col) {
        return !(fil<0 || fil>=tablero.getFilas() || col<0 || col>=tablero.getColumnas());
    }

    public void moverJugadorenTablero(Direccion direccion) {
        int nuevaFila = jugador.getFilaActual() + direccion.getCambioFila();
        int nuevaColumna = jugador.getColumnaActual() + direccion.getCambioColumna();

        if (esMovimientoValido(nuevaFila, nuevaColumna)) {
            // Eliminar al jugador de su posición actual en el tablero
            tablero.eliminarElemento(jugador.getFilaActual(), jugador.getColumnaActual());

            // Mover al jugador a la nueva posición
            jugador.mover(direccion);

            // Agregar al jugador en la nueva posición en el tablero
            tablero.agregarElemento(jugador, nuevaFila, nuevaColumna);
            verificarColisionJugador();
        }
    }

    public void moverEnemigos() {
        int filamoverse=jugador.getFilaActual();
        int colmoverse= jugador.getColumnaActual();;
        for (Enemigo enemigo : enemigos) {
            tablero.eliminarElemento(enemigo.getFilaActual(),enemigo.getColumnaActual());
            enemigo.mover(filamoverse, colmoverse);
            if (tablero.hayExplosion(enemigo.getFilaActual(),enemigo.getColumnaActual())){
                enemigos.remove(enemigo);
                return;
            }
            tablero.agregarElemento(enemigo,filamoverse, colmoverse);
            verificarColisionEnemigos();
        }
    }

    private void verificarColisionJugador() {
        boolean reiniciar = false;

        for (Enemigo enemigo : enemigos) {
            if (enemigo.getFilaActual() == jugador.getFilaActual() &&
                    enemigo.getColumnaActual() == jugador.getColumnaActual()) {
                reiniciar = true;
                break;
            }
        }
        if (reiniciar || tablero.hayExplosion(jugador.getFilaActual(), jugador.getColumnaActual())) {
            reiniciar = true;
            reiniciarJuego(CANTIDAD_INICIAL_ENEMIGOS, 1);
        }

    }

    public void verificarColisionEnemigos() {
        for (int i = 0; i < enemigos.size(); i++) {
            for (int j = i + 1; j < enemigos.size(); j++) {
                Enemigo enemigo1 = enemigos.get(i);
                Enemigo enemigo2 = enemigos.get(j);

                if (enemigo1.getFilaActual() == enemigo2.getFilaActual() &&
                        enemigo1.getColumnaActual() == enemigo2.getColumnaActual()) {

                    // Eliminar los enemigos involucrados en la colisión
                    enemigos.remove(enemigo1);
                    enemigos.remove(enemigo2);

                    // Crear una explosión en la posición de la colisión
                    Explosion explosion = new Explosion(enemigo1.getFilaActual(), enemigo1.getColumnaActual());
                    tablero.agregarElemento(explosion, explosion.getFila(), explosion.getColumna());
                }
            }
        }
    }


    public void reiniciarJuego(int cantidadenemigos, int nivel) {
        // Vaciar el tablero
        tablero.vaciar();

        // Generar nuevos enemigos (si es necesario)
        if (enemigos.isEmpty()) {
            generarEnemigos(cantidadenemigos+(nivel-1)*2);
        } else {
            enemigos.clear();
            // Restablecer la posición del jugador
            jugador.setFila(tablero.getFilas() / 2);
            jugador.setColumna(tablero.getColumnas() / 2);
            generarEnemigos(cantidadenemigos);
            puntos=0;
        }
    }

    public Tablero getTablero(){return tablero;}
    public Jugador getJugador(){
        return jugador;
    }

}
