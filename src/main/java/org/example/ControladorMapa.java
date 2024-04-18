package org.example;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

public class ControladorMapa implements Initializable {
    @FXML
    public GridPane tableroGridPane;
    private static final int CASILLAS_DEFAULT = 15;
    private static int filas;
    private static int columnas;
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

    public ControladorMapa() {
        this.filas = CASILLAS_DEFAULT;
        this.columnas = CASILLAS_DEFAULT;
    }

    private void inicializarGrid (){
        for (int fila = 0; fila < filas; fila++) {
            for (int columna = 0; columna < columnas; columna++) {
                StackPane celda = new StackPane();
                celda.setPrefSize(tamaniocelda, tamaniocelda);
                celda.setStyle("-fx-background-color: " + ((fila + columna) % 2 == 0 ? "white" : "lightblue"));
                tableroGridPane.add(celda, columna, fila);
            }
        }
    }

    private void inicializarKeys(){
        tableroGridPane.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case A:
                    comandoA();
                    break;
                case S:
                    comandoS();
                    break;
                case D:
                    comandoD();
                    break;
                case W:
                    comandoW();
                    break;
                case Q:
                    comandoQ();
                    break;
                case E:
                    comandoE();
                    break;
                case Z:
                    comandoZ();
                    break;
                case X:
                    comandoX();
                    break;
                case C:
                    comandoC();
                    break;
                case T:
                    comandoT();
                    break;
                case G:
                    comandoG();
                    break;
                default:
                    break;
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        juego=new Juego(filas,columnas);

        inicializarGrid();
        inicializarKeys();

        Platform.runLater(() -> {
            tableroGridPane.requestFocus();
        });
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
        double tamanioCursor = 200;

        Image cursorArriba = new Image("file:doc/up.png", tamanioCursor, tamanioCursor, true, true);
        Image cursorAbajo = new Image("file:doc/down.png", tamanioCursor, tamanioCursor, true, true);
        Image cursorIzq = new Image("file:doc/left.png", tamanioCursor, tamanioCursor, true, true);
        Image cursorDer = new Image("file:doc/right.png", tamanioCursor, tamanioCursor, true, true);
        Image cursorArrIzq = new Image("file:doc/top-left.png", tamanioCursor, tamanioCursor, true, true);
        Image cursorArrDer = new Image("file:doc/top-right.png", tamanioCursor, tamanioCursor, true, true);
        Image cursorAbaIzq = new Image("file:doc/down-left.png", tamanioCursor, tamanioCursor, true, true);
        Image cursorAbaDer = new Image("file:doc/down-right.png", tamanioCursor, tamanioCursor, true, true);
        Image cursorPunto = new Image("file:doc/point.png", tamanioCursor, tamanioCursor, true, true);

        Direccion direccion = Direccion.calcular(diffX, diffY);

        // Cambia el cursor del mouse según la dirección calculada
        switch (direccion) {
            case ARRIBA:
                tableroGridPane.setCursor(new ImageCursor(cursorArriba));
                break;
            case ABAJO:
                tableroGridPane.setCursor(new ImageCursor(cursorAbajo));
                break;
            case IZQUIERDA:
                tableroGridPane.setCursor(new ImageCursor(cursorIzq));
                break;
            case DERECHA:
                tableroGridPane.setCursor(new ImageCursor(cursorDer));
                break;
            case DIAGONAL_ARRIBA_IZQUIERDA:
                tableroGridPane.setCursor(new ImageCursor(cursorArrIzq));
                break;
            case DIAGONAL_ARRIBA_DERECHA:
                tableroGridPane.setCursor(new ImageCursor(cursorArrDer));
                break;
            case DIAGONAL_ABAJO_IZQUIERDA:
                tableroGridPane.setCursor(new ImageCursor(cursorAbaIzq));
                break;
            case DIAGONAL_ABAJO_DERECHA:
                tableroGridPane.setCursor(new ImageCursor(cursorAbaDer));
                break;
            default:
                tableroGridPane.setCursor(new ImageCursor(cursorPunto));
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
            return "rutaIconoRobot1.png";
        } else if (elemento instanceof Robot2) {
            return "rutaIconoRobot2.png";
        }else if (elemento instanceof Explosion) {
            return "rutaIconoExplosion.png";
        }
        return null;
    }

    @FXML
    private void switchToMenu() throws IOException {
        App.setRoot("menu");
    }

    @FXML
    private void reloadMapa() throws IOException {
        App.setRoot("mapa");
    }

    @FXML
    private void teletransporteSeguro() throws IOException {
        NivelActual.setText("TP Safe");
        tableroGridPane.requestFocus();
    }

    @FXML
    private void teletransporteAleatorio() throws IOException {
        NivelActual.setText("TP Rand");
        tableroGridPane.requestFocus();
    }

    @FXML
    private void noMoverse() throws IOException {
        NivelActual.setText("No Move");
        tableroGridPane.requestFocus();
    }

    @FXML
    private void comandoA() {
        NivelActual.setText("A pulsada");
    }

    @FXML
    private void comandoS() {
        NivelActual.setText("S pulsada");
    }

    @FXML
    private void comandoD() {
        NivelActual.setText("D pulsada");
    }

    @FXML
    private void comandoW() {
        NivelActual.setText("W pulsada");
    }

    @FXML
    private void comandoQ() {
        NivelActual.setText("Q pulsada");
    }

    @FXML
    private void comandoE() {
        NivelActual.setText("E pulsada");
    }

    @FXML
    private void comandoZ() {
        NivelActual.setText("Z pulsada");
    }

    @FXML
    private void comandoC() {
        NivelActual.setText("C pulsada");
    }

    @FXML
    private void comandoX() {
        NivelActual.setText("X pulsada");
    }

    @FXML
    private void comandoT() {
        NivelActual.setText("T pulsada");
    }

    @FXML
    private void comandoG() {
        NivelActual.setText("G pulsada");
    }
}