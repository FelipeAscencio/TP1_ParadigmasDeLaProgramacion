package org.example;

public class Robot1 extends Enemigo{
    public Robot1(int filaInicial, int columnaInicial) {
        super(filaInicial, columnaInicial);
    }

    @Override
    public void mover(int filaJugador, int columnaJugador) {
        int dirFilaMover = calcularDireccion(getFilaActual(), filaJugador);
        int dirColumnaMover = calcularDireccion(getColumnaActual(), columnaJugador);

        int nuevaFila=getFilaActual()+dirFilaMover;
        int nuevaColumna=getColumnaActual()+dirColumnaMover;

        setColumnaActual(nuevaColumna);
        setFilaActual(nuevaFila);
    }

    private int calcularDireccion(int actual, int objetivo) {
        int diferencia = objetivo - actual;
        if (Math.abs(diferencia) > 1) {
            return diferencia > 0 ? 1 : -1;
        }
        return 0;
    }

    public boolean colisionCeldaIncendiada(boolean[][] celdasIncendiadas) {
        return celdasIncendiadas[getFilaActual()][getColumnaActual()];
    }

    // Otros métodos específicos de Robot1, si es necesario
}

