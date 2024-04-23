package org.example;

import java.util.*;

public class Juego {
    private static final int USOS_MINIMOS = 1;
    private Jugador jugador;
    private Tablero tablero;
    private List<Enemigo> enemigos;
    private int CANTIDAD_INICIAL_ENEMIGOS = 5;
    private int nivel;
    private int puntos;
    private int teletransportes;
    private boolean game_over;

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

    private void agregarelementostablero(){
        for (Enemigo enemigo: enemigos){
            if (enemigo.colisionJugador(jugador.getFilaActual(),jugador.getColumnaActual())){
                game_over=true;
                return;
            }
            tablero.agregarElemento(enemigo, enemigo.getFilaActual(), enemigo.getColumnaActual());
        }
    }

    public Juego(int filas, int columnas){
        tablero=new Tablero(filas,columnas);
        jugador=new Jugador(filas/2,columnas/2);
        tablero.agregarElemento(jugador,filas/2,columnas/2);
        enemigos=new ArrayList<>();
        generarEnemigos(CANTIDAD_INICIAL_ENEMIGOS);
        nivel=1;
        puntos=0;
        teletransportes = 3;
        game_over = false;
    }

    private boolean esPosicionOcupada(int fila, int columna) {
        return tablero.getElemento(fila,columna)!=null;
    }

    private boolean esMovimientoValido(int fil, int col) {
        return fil>=0 && fil<tablero.getFilas() && col>=0 && col<tablero.getColumnas();
    }

    private boolean verificarColisionJugador(int fila, int columna) {
        for (Enemigo enemigo : enemigos) {
            if (enemigo.colisionJugador(fila,columna)){
                return true;
            }
        }
        if (tablero.hayExplosion(fila, columna)){
            return true;
        }
        return false;
    }

    public void moverJugadorenTablero(Direccion direccion) {
        if (game_over){
            return;
        }
        int nuevaFila = jugador.getFilaActual() + direccion.getCambioFila();
        int nuevaColumna = jugador.getColumnaActual() + direccion.getCambioColumna();
        if (verificarColisionJugador(nuevaFila, nuevaColumna)){
            game_over = true;
            return;
        }
        if (esMovimientoValido(nuevaFila, nuevaColumna)) {
            tablero.eliminarElemento(jugador.getFilaActual(), jugador.getColumnaActual());
            jugador.mover(direccion);
            tablero.agregarElemento(jugador, nuevaFila, nuevaColumna);
        }
        moverEnemigos();
    }

    public int movimientointermediofila(Enemigo enemigo){
        return enemigo.getFilaActual()+enemigo.calcularDireccion(enemigo.getFilaActual(), jugador.getFilaActual());
    }

    public int movimientointermediocol(Enemigo enemigo){
        return enemigo.getColumnaActual()+enemigo.calcularDireccion(enemigo.getColumnaActual(),jugador.getColumnaActual());
    }

    private void manejarMovimientoRobot2(Enemigo robot2, List<Enemigo> enemigosAEliminar) {
        int filaprimermov = movimientointermediofila(robot2);
        int colprimermov = movimientointermediocol(robot2);

        if (tablero.hayExplosion(filaprimermov, colprimermov)) {
            enemigosAEliminar.add(robot2);
            puntos++;
        } else if (tablero.hayEnemigo(filaprimermov, colprimermov)) {
            Explosion explosion = new Explosion(filaprimermov, colprimermov);
            enemigosAEliminar.add(robot2);
            enemigosAEliminar.add((Enemigo) tablero.getElemento(filaprimermov, colprimermov));
            tablero.agregarElemento(explosion, filaprimermov, colprimermov);
            puntos++;
        }
    }

    public void verificarColisionEnemigos(List<Enemigo> enemigosAEliminar) {
        for (int i = 0; i < enemigos.size(); i++) {
            Enemigo enemigo1 = enemigos.get(i);
            for (int j = i + 1; j < enemigos.size(); j++) {
                Enemigo enemigo2 = enemigos.get(j);
                if (enemigo1.getFilaActual() == enemigo2.getFilaActual() &&
                        enemigo1.getColumnaActual() == enemigo2.getColumnaActual()) {
                    enemigosAEliminar.add(enemigo1);
                    enemigosAEliminar.add(enemigo2);
                    puntos++;
                    if (verificarColisionJugador(jugador.getFilaActual(), jugador.getColumnaActual())){
                        game_over = true;
                        return;
                    }
                    Explosion explosion = new Explosion(enemigo1.getFilaActual(), enemigo1.getColumnaActual());
                    tablero.agregarElemento(explosion, enemigo1.getFilaActual(), enemigo1.getColumnaActual());
                }
            }
        }
        enemigos.removeAll(enemigosAEliminar);
    }

    public void reiniciarJuego(int nivel) {
        tablero.vaciar();
        tablero.agregarElemento(jugador, jugador.getFilaActual(), jugador.getColumnaActual());
        iniciarNivel(CANTIDAD_INICIAL_ENEMIGOS,nivel);
    }

    public void moverEnemigos() {
        int filamoverse=jugador.getFilaActual();
        int colmoverse= jugador.getColumnaActual();
        List<Enemigo> enemigosAEliminar = new ArrayList<>();
        for (Enemigo enemigo : enemigos) {
            if (!(tablero.getElemento(enemigo.getFilaActual(), enemigo.getColumnaActual()) instanceof Explosion)){
                tablero.eliminarElemento(enemigo.getFilaActual(),enemigo.getColumnaActual());
            }
            if (enemigo instanceof Robot2){
               manejarMovimientoRobot2(enemigo,enemigosAEliminar);
            }
            enemigo.mover(filamoverse, colmoverse);
            if (tablero.hayExplosion(enemigo.getFilaActual(),enemigo.getColumnaActual())){
                enemigosAEliminar.add(enemigo);
                puntos++;
            }
        }
        enemigos.removeAll(enemigosAEliminar);
        verificarColisionEnemigos(enemigosAEliminar);
        agregarelementostablero();
        if (enemigos.isEmpty()) {
            nivel+=1;
            reiniciarJuego(nivel);
        }
    }

    public Tablero getTablero(){return tablero;}

    public Jugador getJugador(){
        return jugador;
    }

    public int getPuntos(){
        return puntos;
    }

    public int getNivel(){
        return nivel;
    }

    public boolean getEstado(){
        return game_over;
    }

    public int getUsosTeletransportacion() {
        return teletransportes;
    }

    public void jugadorteletransportacion(int fila, int columna, boolean seguro){
        if (seguro){
            if (teletransportes < USOS_MINIMOS){
                return;
            }
            teletransportes--;
        }
        tablero.eliminarElemento(jugador.getFilaActual(), jugador.getColumnaActual());
        jugador.setFila(fila);
        jugador.setColumna(columna);
        moverEnemigos();
        if (verificarColisionJugador(jugador.getFilaActual(), jugador.getColumnaActual())){
            game_over = true;
        }
        tablero.agregarElemento(jugador,jugador.getFilaActual(), jugador.getColumnaActual());
    }
}
