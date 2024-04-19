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
    private static final int TAMANIO_CELDA_FIL = 40;
    private static final int TAMANIO_CELDA_COL = 130;
    private static final int TAMANIO_SPRITE = 32;
    private static final int CANTIDAD_SPRITES = 14;
    private static int filas;
    private static int columnas;
    private WritableImage[] sprites;
    public Juego juego;

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

    private void imprimirSpriteEnCelda(int fila, int columna, Image sprite) {
        StackPane celda = (StackPane) tableroGridPane.getChildren().get(calcularIndiceNodo(tableroGridPane, fila, columna));

        celda.getChildren().clear();

        ImageView spriteView = new ImageView(sprite);
        celda.getChildren().add(spriteView);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        juego=new Juego(filas,columnas);

        inicializarSprites();
        inicializarGrid();
        inicializarKeys();

        //imprimirSpriteEnCelda(2, 2, sprites[1]);

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
        TextNivelActual.setText("TP Safe");
        tableroGridPane.requestFocus();
    }

    @FXML
    private void teletransporteAleatorio() throws IOException {
        TextNivelActual.setText("TP Rand");
        tableroGridPane.requestFocus();
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
                inicializarGrid();
            }
        }
        tableroGridPane.requestFocus();
    }

    @FXML
    private void comandoA() {
        TextNivelActual.setText("A pulsada");
    }

    @FXML
    private void comandoS() {
        TextNivelActual.setText("S pulsada");
    }

    @FXML
    private void comandoD() {
        TextNivelActual.setText("D pulsada");
    }

    @FXML
    private void comandoW() {
        TextNivelActual.setText("W pulsada");
    }

    @FXML
    private void comandoQ() {
        TextNivelActual.setText("Q pulsada");
    }

    @FXML
    private void comandoE() {
        TextNivelActual.setText("E pulsada");
    }

    @FXML
    private void comandoZ() {
        TextNivelActual.setText("Z pulsada");
    }

    @FXML
    private void comandoC() {
        TextNivelActual.setText("C pulsada");
    }

    @FXML
    private void comandoX() {
        TextNivelActual.setText("X pulsada");
    }

    @FXML
    private void comandoT() {
        TextNivelActual.setText("T pulsada");
    }

    @FXML
    private void comandoG() {
        TextNivelActual.setText("G pulsada");
    }
}