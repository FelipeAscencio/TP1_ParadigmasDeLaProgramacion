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
    private static final int CASILLAS_MIN = 5;
    private static final int CASILLAS_MAX = 19;
    private static final int TAMANIO_CELDA = 32;
    private static final int TAMANIO_SPRITE = 32;
    private static final int CANTIDAD_SPRITES = 14;
    private static final int IMG_PLAYER = 0;
    private static final int IMG_ROBOT1 = 6;
    private static final int IMG_ROBOT2 = 10;
    private static final int IMG_EXPLOSION = 13;
    private static final double CORRECCION_X = 2;
    private static final double CORRECION_Y = 1.05;
    private static int filas;
    private static int columnas;
    private WritableImage[] sprites;
    private Juego juego;

    public ControladorMapa() {
        this.filas = CASILLAS_DEFAULT;
        this.columnas = CASILLAS_DEFAULT;
    }

    private void inicializarGrid() {
        tableroGridPane.getChildren().clear();

        for (int fila = 0; fila < filas; fila++) {
            for (int columna = 0; columna < columnas; columna++) {
                StackPane celda = new StackPane();
                celda.setPrefSize(TAMANIO_CELDA, TAMANIO_CELDA);
                celda.setStyle("-fx-background-color: " + ((fila + columna) % 2 == 0 ? "white" : "lightblue"));
                tableroGridPane.add(celda, columna, fila);

                // Crear ImageView para la imagen del elemento en la celda
                ImageView imageView = new ImageView();
                imageView.setPreserveRatio(true); // Mantener la relación de aspecto
                imageView.setFitWidth(TAMANIO_CELDA); // Ajustar ancho al tamaño de la celda
                imageView.setFitHeight(TAMANIO_CELDA); // Ajustar alto al tamaño de la celda

                // Obtener el elemento en la posición actual y establecer la imagen correspondiente
                Object elemento = juego.getTablero().getElemento(fila, columna);
                if (elemento != null) {
                    Image nuevaImagen = obtenerImagenElemento(elemento);
                    imageView.setImage(nuevaImagen);
                }

                // Agregar el ImageView a la celda
                celda.getChildren().add(imageView);
            }
        }
    }

    private void actualizarEstado(){
        int puntos_actuales = juego.getPuntos();
        int nivel_actual = juego.getNivel();
        int seguros_restantes = juego.getJugador().getUsosTeletransportacion();

        TextPuntaje.setText("Puntos: " + puntos_actuales);
        TextNivelActual.setText("Nivel: " + nivel_actual);
        TextTPRestantes.setText("Restantes: " + seguros_restantes);
    }

    private void actualizarTurno(){
        actualizarEstado();
        inicializarGrid();
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
            return sprites[IMG_PLAYER];
        } else if (elemento instanceof Robot1) {
            return sprites[IMG_ROBOT1];
        } else if (elemento instanceof Robot2) {
            return sprites[IMG_ROBOT2];
        } else if (elemento instanceof Explosion){
            return sprites[IMG_EXPLOSION];
        }

        return null;
    }

    public Direccion hallarDireccion(MouseEvent event){
        double filaJugador = (juego.getJugador().getFilaActual()) * TAMANIO_CELDA;
        double colJugador = (juego.getJugador().getColumnaActual()) * TAMANIO_CELDA;
        double mouseX = event.getX();
        double mouseY = event.getY();
        double diffX = mouseX - (colJugador * CORRECCION_X);
        double diffY = mouseY - (filaJugador * CORRECION_Y);
        return Direccion.calcular(diffX, diffY);
    }

    public void detectarClicMouse(MouseEvent event){
        Direccion direccion = hallarDireccion(event);
        switch (direccion) {
            case ARRIBA:
                comandoW();
                break;
            case ABAJO:
                comandoS();
                break;
            case IZQUIERDA:
                comandoA();
                break;
            case DERECHA:
                comandoD();
                break;
            case DIAGONAL_ARRIBA_IZQUIERDA:
                comandoQ();
                break;
            case DIAGONAL_ARRIBA_DERECHA:
                comandoE();
                break;
            case DIAGONAL_ABAJO_IZQUIERDA:
                comandoZ();
                break;
            case DIAGONAL_ABAJO_DERECHA:
                comandoC();
                break;
        }
    }

    public void detectarDireccionMouse(MouseEvent event){
        Direccion direccion = hallarDireccion(event);
        double tamanioCursor = 300;
        Image cursorArriba = new Image("file:doc/up.png", tamanioCursor, tamanioCursor, true, true);
        Image cursorAbajo = new Image("file:doc/down.png", tamanioCursor, tamanioCursor, true, true);
        Image cursorIzq = new Image("file:doc/left.png", tamanioCursor, tamanioCursor, true, true);
        Image cursorDer = new Image("file:doc/right.png", tamanioCursor, tamanioCursor, true, true);
        Image cursorArrIzq = new Image("file:doc/top-left.png", tamanioCursor, tamanioCursor, true, true);
        Image cursorArrDer = new Image("file:doc/top-right.png", tamanioCursor, tamanioCursor, true, true);
        Image cursorAbaIzq = new Image("file:doc/down-left.png", tamanioCursor, tamanioCursor, true, true);
        Image cursorAbaDer = new Image("file:doc/down-right.png", tamanioCursor, tamanioCursor, true, true);

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
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        juego=new Juego(filas,columnas);

        inicializarSprites();
        actualizarTurno();
        inicializarKeys();
        tableroGridPane.setOnMouseMoved(this::detectarDireccionMouse);
        tableroGridPane.setOnMouseClicked(this::detectarClicMouse);

        Platform.runLater(() -> {
            tableroGridPane.requestFocus();
        });
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
    private void cambiarMapa() throws IOException {
        int nuevas_filas = Integer.parseInt(FieldFilas.getText());
        int nuevas_columnas = Integer.parseInt(FieldColumnas.getText());
        if (nuevas_filas >= CASILLAS_MIN && nuevas_filas <= CASILLAS_MAX) {
            if (nuevas_columnas >= CASILLAS_MIN && nuevas_columnas <= CASILLAS_MAX){
                this.filas = nuevas_filas;
                this.columnas = nuevas_columnas;
                juego.reinicioCambioMapa(filas,columnas);
                actualizarTurno();
            }
        }
        tableroGridPane.requestFocus();
    }

    @FXML
    private void teletransporteSeguro() throws IOException {
        comandoG();
        tableroGridPane.requestFocus();
    }

    @FXML
    private void teletransporteAleatorio() throws IOException {
        comandoT();
        tableroGridPane.requestFocus();
    }

    @FXML
    private void noMoverse() throws IOException {
        comandoX();
        tableroGridPane.requestFocus();
    }

    @FXML
    private void comandoA() {
        juego.moverJugadorenTablero(Direccion.IZQUIERDA);
        actualizarTurno();
    }

    @FXML
    private void comandoS() {
        juego.moverJugadorenTablero(Direccion.ABAJO);
        actualizarTurno();
    }

    @FXML
    private void comandoD() {
        juego.moverJugadorenTablero(Direccion.DERECHA);
        actualizarTurno();
    }

    @FXML
    private void comandoW() {
        juego.moverJugadorenTablero(Direccion.ARRIBA);
        actualizarTurno();
    }

    @FXML
    private void comandoQ() {
        juego.moverJugadorenTablero(Direccion.DIAGONAL_ARRIBA_IZQUIERDA);
        actualizarTurno();
    }

    @FXML
    private void comandoE() {
        juego.moverJugadorenTablero(Direccion.DIAGONAL_ARRIBA_DERECHA);
        actualizarTurno();
    }

    @FXML
    private void comandoZ() {
        juego.moverJugadorenTablero(Direccion.DIAGONAL_ABAJO_IZQUIERDA);
        actualizarTurno();
    }

    @FXML
    private void comandoC() {
        juego.moverJugadorenTablero(Direccion.DIAGONAL_ABAJO_DERECHA);
        actualizarTurno();
    }

    @FXML
    private void comandoX() {
        juego.moverEnemigos();
        actualizarTurno();
    }

    @FXML
    private void comandoG() {
        juego.jugadorteletransporteseguro();
        actualizarTurno();
    }

    @FXML
    private void comandoT() {
        juego.jugadorteletransportacionrandom();
        actualizarTurno();
    }
}