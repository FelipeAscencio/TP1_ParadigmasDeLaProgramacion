package org.example;

import java.util.*;


public class Juego {
    private Jugador jugador;
    private Tablero tablero;
    private List<Enemigo> enemigos;
    private int CANTIDAD_INICIAL_ENEMIGOS=5;
    private int nivel;
    private int puntos;

    public Juego(int filas, int columnas){
        tablero=new Tablero(filas,columnas);
        jugador=new Jugador(filas/2,columnas/2,1);
        tablero.agregarElemento(jugador,filas/2,columnas/2);
        enemigos=new ArrayList<>();
        generarEnemigos(CANTIDAD_INICIAL_ENEMIGOS);
    }
    public void jugadorteletransportacionrandom(){
        Random random = new Random();
        int filaAleatoria = random.nextInt(tablero.getFilas());
        int columnaAleatoria = random.nextInt(tablero.getColumnas());
        jugador.setFila(filaAleatoria);
        jugador.setColumna(columnaAleatoria);
        verificarColisionJugador();

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

    public void iniciarNivel(int cantidadEnemigos, int nivel) {
        generarEnemigos(cantidadEnemigos+(nivel-1)*2);
    }

    private boolean esPosicionOcupada(int fila, int columna) {
        return tablero.getElemento(fila,columna)!=null;
    }

    private boolean esMovimientoValido(int fil, int col) {
        return fil>0 || fil<tablero.getFilas() || col>0 || col<tablero.getColumnas();
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
        int colmoverse= jugador.getColumnaActual();
        List<Enemigo> enemigosAEliminar = new ArrayList<>();
        for (Enemigo enemigo : enemigos) {
            tablero.eliminarElemento(enemigo.getFilaActual(),enemigo.getColumnaActual());
            enemigo.mover(filamoverse, colmoverse);
            if (tablero.hayExplosion(enemigo.getFilaActual(),enemigo.getColumnaActual())){
                enemigosAEliminar.add(enemigo);
                continue;
            }
            tablero.agregarElemento(enemigo,filamoverse, colmoverse);
        }
        enemigos.removeAll(verificarColisionEnemigos(enemigosAEliminar));
        if (enemigos.isEmpty()) {
            reiniciarJuego(nivel++);
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
            reiniciarJuego(1);
        }

    }

    public List<Enemigo> verificarColisionEnemigos(List<Enemigo> enemigosAEliminar) {
        for (int i = 0; i < enemigos.size(); i++) {
            Enemigo enemigo1 = enemigos.get(i);
            if (enemigosAEliminar.contains(enemigo1)) {
                continue;
            }
            for (int j = i + 1; j < enemigos.size(); j++) {
                Enemigo enemigo2 = enemigos.get(j);

                if (enemigo1.getFilaActual() == enemigo2.getFilaActual() &&
                        enemigo1.getColumnaActual() == enemigo2.getColumnaActual()) {
                    enemigosAEliminar.add(enemigo1);
                    enemigosAEliminar.add(enemigo2);

                    // Crear una explosión en la posición de la colisión
                    Explosion explosion = new Explosion(enemigo1.getFilaActual(), enemigo1.getColumnaActual());
                    tablero.agregarElemento(explosion, explosion.getFila(), explosion.getColumna());
                }
            }
        }
        return enemigosAEliminar;
    }


    public void reiniciarJuego(int nivel) {
        // Vaciar el tablero
        tablero.vaciar();

        // Generar nuevos enemigos (si es necesario)
        if (enemigos.isEmpty()) {
            iniciarNivel(CANTIDAD_INICIAL_ENEMIGOS,nivel);
        } else {
            enemigos.clear();
            // Restablecer la posición del jugador
            jugador.setFila(tablero.getFilas() / 2);
            jugador.setColumna(tablero.getColumnas() / 2);
            iniciarNivel(CANTIDAD_INICIAL_ENEMIGOS,1);
            puntos=0;
        }
    }

    public Tablero getTablero(){return tablero;}
    public Jugador getJugador(){
        return jugador;
    }

}
