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
        nivel=1;
        puntos=0;
    }


    public void jugadorteletransportacionrandom(){
        Random random = new Random();
        int filaAleatoria = random.nextInt(tablero.getFilas());
        int columnaAleatoria = random.nextInt(tablero.getColumnas());
        tablero.eliminarElemento(jugador.getFilaActual(), jugador.getColumnaActual());
        jugador.setFila(filaAleatoria);
        jugador.setColumna(columnaAleatoria);
        tablero.agregarElemento(jugador,jugador.getFilaActual(), jugador.getColumnaActual());
        moverEnemigos();
        verificarColisionJugador();
    }
    public void jugadorteletransporteseguro(){
        if (jugador.getUsosTeletransportacion()==0){
            return;
        }
        Random random = new Random();
        int filaAleatoria, columnaAleatoria;
        do {
            filaAleatoria = random.nextInt(tablero.getFilas());
            columnaAleatoria = random.nextInt(tablero.getColumnas());
        } while (esPosicionOcupada(filaAleatoria, columnaAleatoria));
        tablero.eliminarElemento(jugador.getFilaActual(), jugador.getColumnaActual());
        jugador.setFila(filaAleatoria);
        jugador.setColumna(columnaAleatoria);
        jugador.setUsosTeletransportacion(jugador.getUsosTeletransportacion()-1);
        tablero.agregarElemento(jugador, jugador.getFilaActual(),jugador.getColumnaActual());
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
        return fil>=0 && fil<tablero.getFilas() && col>=0 && col<tablero.getColumnas();
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
            if (verificarColisionJugador()){
                return;
            }
        }
        moverEnemigos();
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
            if (enemigo.colisionJugador(jugador.getFilaActual(),jugador.getColumnaActual())){
                reiniciarJuego(1);
                return;
            }
            tablero.agregarElemento(enemigo,enemigo.getFilaActual(),enemigo.getColumnaActual());

        }
        enemigos.removeAll(verificarColisionEnemigos(enemigosAEliminar));
        if (enemigos.isEmpty()) {
            reiniciarJuego(nivel+1);
        }
    }

    private boolean verificarColisionJugador() {
        boolean reiniciar = false;

        for (Enemigo enemigo : enemigos) {
            if (enemigo.colisionJugador(jugador.getFilaActual(),jugador.getColumnaActual())){
                reiniciar = true;
                break;
            }
        }
        if (reiniciar || tablero.hayExplosion(jugador.getFilaActual(), jugador.getColumnaActual())) {
            reiniciarJuego(1);
            return true;
        }
        return false;
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
                    tablero.agregarElemento(explosion, enemigo1.getFilaActual(), enemigo1.getColumnaActual());

                }
            }
        }

        return enemigosAEliminar;
    }

    public void reiniciarJuego(int nivel) {
        tablero.vaciar();
        // Generar nuevos enemigos (si es necesario)
        if (enemigos.isEmpty()) {
            tablero.agregarElemento(jugador, jugador.getFilaActual(), jugador.getColumnaActual());
            iniciarNivel(CANTIDAD_INICIAL_ENEMIGOS,nivel);
        } else {
            enemigos.clear();
            // Restablecer la posición del jugador
            jugador.setFila(tablero.getFilas() / 2);
            jugador.setColumna(tablero.getColumnas() / 2);
            tablero.agregarElemento(jugador,jugador.getFilaActual(), jugador.getColumnaActual());
            iniciarNivel(CANTIDAD_INICIAL_ENEMIGOS,1);
            puntos=0;
        }
    }

    public void reinicioCambioMapa(int nuevasFilas, int nuevasColumnas) {
        tablero.vaciar();
        tablero = new Tablero(nuevasFilas,nuevasColumnas);
        jugador.setFila(tablero.getFilas() / 2);
        jugador.setColumna(tablero.getColumnas() / 2);
        tablero.agregarElemento(jugador, jugador.getFilaActual(), jugador.getColumnaActual());
        iniciarNivel(CANTIDAD_INICIAL_ENEMIGOS, 1);
        puntos = 0;
    }

    public Tablero getTablero(){return tablero;}
    public Jugador getJugador(){
        return jugador;
    }

}
