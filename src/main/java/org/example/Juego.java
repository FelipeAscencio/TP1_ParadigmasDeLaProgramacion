package org.example;

import java.util.*;


public class Juego {
    private Jugador jugador;
    private Tablero tablero;
    private List<Enemigo> enemigos;
    private int CANTIDAD_INICIAL_ENEMIGOS=5;
    private int nivel;
    private int puntos;
    private boolean game_over;

    public Juego(int filas, int columnas){
        tablero=new Tablero(filas,columnas);
        jugador=new Jugador(filas/2,columnas/2,1);
        tablero.agregarElemento(jugador,filas/2,columnas/2);
        enemigos=new ArrayList<>();
        generarEnemigos(CANTIDAD_INICIAL_ENEMIGOS);
        nivel=1;
        puntos=0;
        game_over = false;
    }


    public void jugadorteletransportacion(int fila, int columna){
        tablero.eliminarElemento(jugador.getFilaActual(), jugador.getColumnaActual());
        jugador.setFila(fila);
        jugador.setColumna(columna);
        tablero.agregarElemento(jugador,jugador.getFilaActual(), jugador.getColumnaActual());
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
        return fil>=0 && fil<tablero.getFilas() && col>=0 && col<tablero.getColumnas();
    }

    public void moverJugadorenTablero(Direccion direccion) {
        if (game_over){
            return;
        }
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
                game_over=true;
                return;
            }
        }
        moverEnemigos();
    }

    public int movimientointermediofila(Enemigo enemigo){
        return enemigo.getFilaActual()+enemigo.calcularDireccion(enemigo.getFilaActual(), jugador.getFilaActual());
    }
    public int movimientointermediocol(Enemigo enemigo){
        return enemigo.getColumnaActual()+enemigo.calcularDireccion(enemigo.getColumnaActual(),jugador.getColumnaActual());
    }
/*
    public void moverEnemigos2() {
        List<Enemigo> nuevosEnemigos = new ArrayList<>();
        List<Enemigo> enemigosaeliminar=new ArrayList<>();
        for (Enemigo enemigo : enemigos) {
            tablero.eliminarElemento(enemigo.getFilaActual(), enemigo.getColumnaActual());
            if (enemigo instanceof Robot2) {
                int filaprimermov = movimientointermediofila(enemigo);
                int colprimermov = movimientointermediocol(enemigo);
                System.out.println(filaprimermov);
                System.out.println(colprimermov);
                if (tablero.hayExplosion(filaprimermov, colprimermov)) {
                    Explosion nuevaexplosion = new Explosion(filaprimermov, colprimermov);
                    nuevosEnemigos.add(nuevaexplosion);
                    enemigosaeliminar.add(enemigo);
                } else if (tablero.hayEnemigo(filaprimermov,colprimermov)){
                    Explosion explosion1=new Explosion(filaprimermov,colprimermov);
                    Explosion explosion2=new Explosion(filaprimermov,colprimermov);
                    nuevosEnemigos.add(explosion1);
                    nuevosEnemigos.add(explosion2);
                    enemigosaeliminar.add((Enemigo)tablero.getElemento(filaprimermov,colprimermov));
                    enemigosaeliminar.add(enemigo);
                }
            }
            enemigo.mover(jugador.getFilaActual(), jugador.getColumnaActual());
            if (enemigo.colisionJugador(jugador.getFilaActual(), jugador.getColumnaActual())){
                game_over=true;
                return;
            }
            nuevosEnemigos.add(enemigo);
        }
        nuevosEnemigos.removeAll(enemigosaeliminar);
        enemigos = verificarColisionEnemigos2(nuevosEnemigos);
        for (Enemigo enemigo: enemigos){
            tablero.agregarElemento(enemigo, enemigo.getFilaActual(), enemigo.getColumnaActual());
        }
        if (enemigossonexplosiones()){
            reiniciarJuego(nivel+1);
        }
    }

    public boolean enemigossonexplosiones(){
        for (Enemigo enemigo:enemigos){
            if (!(enemigo instanceof Explosion)){
                return false;
            }
        }
        return true;
    }

    private List<Enemigo> verificarColisionEnemigos2(List<Enemigo> nuevosEnemigos) {
        for (int i = 0; i < nuevosEnemigos.size(); i++) {
            Enemigo enemigo1 = nuevosEnemigos.get(i);
            for (int j = i + 1; j < nuevosEnemigos.size(); j++) {
                Enemigo enemigo2 = nuevosEnemigos.get(j);

                if (enemigo1.getFilaActual() == enemigo2.getFilaActual() &&
                        enemigo1.getColumnaActual() == enemigo2.getColumnaActual()) {
                    Explosion explosion1 =new Explosion((enemigo1.getFilaActual()),enemigo1.getColumnaActual());
                    Explosion explosion2 = new Explosion(enemigo2.getFilaActual(), enemigo2.getColumnaActual());
                    nuevosEnemigos.set(i,explosion1);
                    nuevosEnemigos.set(j,explosion2);
                    tablero.agregarElemento(explosion2, enemigo2.getFilaActual(), enemigo2.getColumnaActual());
                }
            }
        }
        return nuevosEnemigos;
    }*/

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
    private void agregarelementostablero(){
        for (Enemigo enemigo: enemigos){
            if (enemigo.colisionJugador(jugador.getFilaActual(),jugador.getColumnaActual())){
                game_over=true;
                return;
            }
            tablero.agregarElemento(enemigo, enemigo.getFilaActual(), enemigo.getColumnaActual());
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
            game_over=true;
            return true;
        }
        return false;
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
                    // Crear una explosión en la posición de la colisión
                    Explosion explosion = new Explosion(enemigo1.getFilaActual(), enemigo1.getColumnaActual());
                    tablero.agregarElemento(explosion, enemigo1.getFilaActual(), enemigo1.getColumnaActual());
                }
            }
        }
        enemigos.removeAll(enemigosAEliminar);
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
    public int getPuntos(){
        return puntos;
    }
    public int getNivel(){
        return nivel;
    }

    public boolean getEstado(){
        return game_over;
    }
}
