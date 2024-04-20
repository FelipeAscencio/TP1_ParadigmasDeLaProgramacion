package org.example;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

public class ControladorMapa implements Initializable {
    @FXML
    public GridPane tableroGridPane;
    public Button BotonMenu;
    public Button BotonReiniciar;
    public Button BotonTeletransporteSeguro;
    public Button BotonTeletransporteAleatorio;
    public Button BotonEsperarRobots;
    public Button BotonCambiarMapa;
    public Label TextTPRestantes;
    public Label TextNivelActual;
    public Label TextPuntaje;
    public TextField FieldFilas;
    public TextField FieldColumnas;
    private static final int CASILLAS_DEFAULT = 15;
    private static final int CASILLAS_MIN = 15;
    private static final int CASILLAS_MAX = 30;
    private static final int TAMANIO_CELDA_FIL = 50;
    private static final int TAMANIO_CELDA_COL = 130;
    private static final int TAMANIO_SPRITE = 32;
    private static final int CANTIDAD_SPRITES = 14;
    private static int filas;
    private static int columnas;
    private WritableImage[] sprites;
    private Juego juego;

    public ControladorMapa() {
        this.filas = CASILLAS_DEFAULT;
        this.columnas = CASILLAS_DEFAULT;
    }

    private void inicializarGrid (){
        tableroGridPane.getChildren().clear();
        for (int fila = 0; fila < filas; fila++) {
            for (int columna = 0; columna < columnas; columna++) {
                StackPane celda = new StackPane();
                celda.setPrefSize(TAMANIO_CELDA_FIL, TAMANIO_CELDA_COL);
                celda.setStyle("-fx-background-color: " + ((fila + columna) % 2 == 0 ? "white" : "lightblue"));
                tableroGridPane.add(celda, columna, fila);

                Object elemento = juego.getTablero().getElemento(fila, columna);
                if (elemento != null) {
                    Image nuevaImagen = obtenerImagenElemento(elemento);
                    ImageView spriteView = new ImageView(nuevaImagen);
                    celda.getChildren().add(spriteView);
                }
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

    private WritableImage[] separarSprites(ImageView spriteStrip, int spriteWidth, int spriteHeight, int numSprites) {
        WritableImage[] sprites = new WritableImage[numSprites];

        PixelReader pixelReader = spriteStrip.getImage().getPixelReader();

        for (int i = 0; i < numSprites; i++) {
            int startX = i * spriteWidth;
            int startY = 0;
            WritableImage spriteImage = new WritableImage(pixelReader, startX, startY, spriteWidth, spriteHeight);
            sprites[i] = spriteImage;
        }
        return sprites;
    }

    private void inicializarSprites() {
        ImageView spriteStrip = new ImageView("file:doc/modelos.png");
        int spriteWidth = TAMANIO_SPRITE;
        int spriteHeight = TAMANIO_SPRITE;
        int numSprites = CANTIDAD_SPRITES;

        sprites = separarSprites(spriteStrip, spriteWidth, spriteHeight, numSprites);
    }

    private Image obtenerImagenElemento(Object elemento) {
        if (elemento instanceof Jugador) {
            return sprites[0];
        } else if (elemento instanceof Robot1) {
            return sprites[6];
        } else if (elemento instanceof Robot2) {
            return sprites[10];
        } else if (elemento instanceof Explosion){
            return sprites[13];
        }

        return null;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        juego=new Juego(filas,columnas);

        inicializarSprites();
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

    public void detectarDireccion(MouseEvent event){
        int filaJugador = juego.getJugador().getFilaActual();
        int colJugador = juego.getJugador().getColumnaActual();
        double diffX = event.getX() - colJugador;
        double diffY = event.getY() - filaJugador;
        Direccion direccion = Direccion.calcular(diffX, diffY);

        double tamanioCursor = 300;
        Image cursorArriba = new Image("file:doc/up.png", tamanioCursor, tamanioCursor, true, true);
        Image cursorAbajo = new Image("file:doc/down.png", tamanioCursor, tamanioCursor, true, true);
        Image cursorIzq = new Image("file:doc/left.png", tamanioCursor, tamanioCursor, true, true);
        Image cursorDer = new Image("file:doc/right.png", tamanioCursor, tamanioCursor, true, true);
        Image cursorArrIzq = new Image("file:doc/top-left.png", tamanioCursor, tamanioCursor, true, true);
        Image cursorArrDer = new Image("file:doc/top-right.png", tamanioCursor, tamanioCursor, true, true);
        Image cursorAbaIzq = new Image("file:doc/down-left.png", tamanioCursor, tamanioCursor, true, true);
        Image cursorAbaDer = new Image("file:doc/down-right.png", tamanioCursor, tamanioCursor, true, true);
        Image cursorPunto = new Image("file:doc/point.png", tamanioCursor, tamanioCursor, true, true);

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
    private void switchToMenu() throws IOException {
        App.setRoot("menu");
    }

    @FXML
    private void reloadMapa() throws IOException {
        App.setRoot("mapa");
    }

    @FXML
    private void teletransporteSeguro() throws IOException {
        TextNivelActual.setText("TP Safe");
        tableroGridPane.requestFocus();
        juego.jugadorteletransporteseguro();
        inicializarGrid();
    }

    @FXML
    private void teletransporteAleatorio() throws IOException {
        TextNivelActual.setText("TP Rand");
        tableroGridPane.requestFocus();
        juego.jugadorteletransportacionrandom();
        inicializarGrid();
    }

    @FXML
    private void noMoverse() throws IOException {
        TextNivelActual.setText("No Move");
        tableroGridPane.requestFocus();
    }

    @FXML
    private void cambiarMapa() throws IOException {
        int nuevas_filas = Integer.parseInt(FieldFilas.getText());
        int nuevas_columnas = Integer.parseInt(FieldColumnas.getText());
        if (nuevas_filas >= CASILLAS_MIN && nuevas_filas <= CASILLAS_MAX) {
            if (nuevas_columnas >= CASILLAS_MIN && nuevas_columnas <= CASILLAS_MAX){
                this.filas = nuevas_filas;
                this.columnas = nuevas_columnas;
                juego.reinicioCambioMapa(filas,columnas);
                inicializarGrid();
            }
        }
        tableroGridPane.requestFocus();
    }

    @FXML
    private void comandoA() {
        TextNivelActual.setText("A pulsada");
        juego.moverJugadorenTablero(Direccion.IZQUIERDA);
        inicializarGrid();
    }

    @FXML
    private void comandoS() {
        TextNivelActual.setText("S pulsada");
        juego.moverJugadorenTablero(Direccion.ABAJO);
        inicializarGrid();

    }

    @FXML
    private void comandoD() {
        TextNivelActual.setText("D pulsada");
        juego.moverJugadorenTablero(Direccion.DERECHA);
        inicializarGrid();
    }

    @FXML
    private void comandoW() {
        TextNivelActual.setText("W pulsada");
        juego.moverJugadorenTablero(Direccion.ARRIBA);
        inicializarGrid();
    }

    @FXML
    private void comandoQ() {
        TextNivelActual.setText("Q pulsada");
        juego.moverJugadorenTablero(Direccion.DIAGONAL_ARRIBA_IZQUIERDA);
        inicializarGrid();
    }

    @FXML
    private void comandoE() {
        TextNivelActual.setText("E pulsada");
        juego.moverJugadorenTablero(Direccion.DIAGONAL_ARRIBA_DERECHA);
        inicializarGrid();
    }

    @FXML
    private void comandoZ() {
        TextNivelActual.setText("Z pulsada");
        juego.moverJugadorenTablero(Direccion.DIAGONAL_ABAJO_IZQUIERDA);
        inicializarGrid();
    }

    @FXML
    private void comandoC() {
        TextNivelActual.setText("C pulsada");
        juego.moverJugadorenTablero(Direccion.DIAGONAL_ABAJO_DERECHA);
        inicializarGrid();
    }

    @FXML
    private void comandoX() {
        TextNivelActual.setText("X pulsada");
        juego.moverEnemigos();
        inicializarGrid();
    }

    @FXML
    private void comandoT() {
        TextNivelActual.setText("T pulsada");
        juego.jugadorteletransportacionrandom();
        inicializarGrid();
    }

    @FXML
    private void comandoG() {
        TextNivelActual.setText("G pulsada");
        juego.jugadorteletransporteseguro();
        inicializarGrid();
    }
}