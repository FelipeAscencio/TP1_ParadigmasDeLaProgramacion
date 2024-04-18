package org.example;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.example.Juego;


public class ControladorMapa1 implements Initializable {
    @FXML
    public GridPane tableroGridPane;
    private static final int filas = 15;
    private static final int columnas = 15;
    private static final int tamaniocelda = 40;
    public Button Menu;
    public Button Reiniciar;
    public Button TeletransporteSeguro;
    public Button TeletransporteAleatorio;
    public Button EsperarRobots;
    public Label TPRestantes;
    public Label NivelActual;
    public Label Puntaje;

    public Juego juego;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        juego=new Juego(filas,columnas);
        for (int fila = 0; fila < filas; fila++) {
            for (int columna = 0; columna < columnas; columna++) {
                StackPane celda = new StackPane();
                celda.setPrefSize(tamaniocelda, tamaniocelda);
                celda.setStyle("-fx-background-color: " + ((fila + columna) % 2 == 0 ? "white" : "lightblue"));
                tableroGridPane.add(celda, columna, fila);
            }
        }
    }

    private int calcularIndiceNodo(GridPane gridPane, int fila, int columna) {
        int numColumnas = gridPane.getColumnConstraints().size();
        return fila * numColumnas + columna;
    }

    private StackPane obtenerCeldaEnPosicion(GridPane gridPane, int fila, int columna) {
        int indiceNodo = calcularIndiceNodo(gridPane, fila, columna);
        Node node = gridPane.getChildren().get(indiceNodo);
        if (node instanceof StackPane) {
            return (StackPane) node;
        }
        return null;
    }


    @FXML
    public void detectarDireccion(MouseEvent event){
        int filaJugador=juego.getJugador().getFilaActual();
        int colJugador=juego.getJugador().getColumnaActual();

        double diffX = event.getX() - colJugador;
        double diffY = event.getY() - filaJugador;

        Direccion direccion = Direccion.calcular(diffX, diffY);

        // Cambia el cursor del mouse según la dirección calculada
        switch (direccion) {
            case ARRIBA:
                tableroGridPane.setCursor(Cursor.N_RESIZE);
                break;
            case ABAJO:
                tableroGridPane.setCursor(Cursor.S_RESIZE);
                break;
            case IZQUIERDA:
                tableroGridPane.setCursor(Cursor.W_RESIZE);
                break;
            case DERECHA:
                tableroGridPane.setCursor(Cursor.E_RESIZE);
                break;
            case DIAGONAL_ARRIBA_IZQUIERDA:
                tableroGridPane.setCursor(Cursor.NW_RESIZE);
                break;
            case DIAGONAL_ARRIBA_DERECHA:
                tableroGridPane.setCursor(Cursor.NE_RESIZE);
                break;
            case DIAGONAL_ABAJO_IZQUIERDA:
                tableroGridPane.setCursor(Cursor.SW_RESIZE);
                break;
            case DIAGONAL_ABAJO_DERECHA:
                tableroGridPane.setCursor(Cursor.SE_RESIZE);
                break;
            default:
                tableroGridPane.setCursor(Cursor.DEFAULT);
                break;
        }
    }

    @FXML
    public void moverJugador(MouseEvent event) {
        double x = event.getX() - juego.getJugador().getColumnaActual();
        double y = event.getY() - juego.getJugador().getFilaActual();

        // Determina la dirección de movimiento en función de la posición del mouse
        Direccion direccion = Direccion.calcular(x,y);

        // Mueve al jugador en la dirección calculada
        juego.getJugador().mover(direccion);
        juego.moverEnemigos();
    }

    private void actualizarIconoEnPosicion(int fila, int columna, String rutaIcono) {
        // Obtener el StackPane en la posición especificada
        StackPane stackPane = (StackPane) obtenerCeldaEnPosicion(tableroGridPane,fila, columna);
        if (stackPane != null) {

            // Crear el ImageView con el nuevo icono
            ImageView nuevoIcono = new ImageView(rutaIcono);

            // Agregar el nuevo icono al StackPane
            stackPane.getChildren().add(nuevoIcono);
        }
    }

    private void actualizarGrid() {
        Object[][] matrizTablero = juego.getTablero().getMatriz(); // Obtener la matriz del tablero

        for (int fila = 0; fila < filas; fila++) {
            for (int columna = 0; columna < columnas; columna++) {
                Object elemento = matrizTablero[fila][columna];
                if (elemento != null) {
                    actualizarIconoEnPosicion(fila,columna,obtenerImagenElemento(elemento));
                }
            }
        }
    }

    private String obtenerImagenElemento(Object elemento) {
        if (elemento instanceof Jugador) {
            return "rutaIconoJugador.png";
        } else if (elemento instanceof Robot1) {
            return "rutaIconoRobot1";
        } else if (elemento instanceof Robot2) {
            return "rutaIconoRobot2";
        }else if (elemento instanceof Explosion) {
            return "rutaIconoExplosion";
        }
        return null;
    }

    @FXML
    private void switchToMenu() throws IOException {
        App.setRoot("menu");
    }
}