package org.example;

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.ImageCursor;
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
    public TextField FieldTPFila;
    public TextField FieldTPColumna;
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
    private boolean game_over;

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

                ImageView imageView = new ImageView();
                imageView.setPreserveRatio(true);
                imageView.setFitWidth(TAMANIO_CELDA);
                imageView.setFitHeight(TAMANIO_CELDA);

                Object elemento = juego.getTablero().getElemento(fila, columna);
                if (elemento != null) {
                    Image nuevaImagen = obtenerImagenElemento(elemento);
                    imageView.setImage(nuevaImagen);
                }

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

    private void indicarFinJuego(){
        TextPuntaje.setText("GAME OVER");
        TextNivelActual.setText("Pulse reiniciar");
    }

    private void actualizarTurno(){
        actualizarEstado();
        inicializarGrid();
        game_over = juego.getEstado();
        if (game_over){
            indicarFinJuego();
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
        game_over = false;

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
        if (!game_over){
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
    }

    @FXML
    private void teletransporteSeguro() throws IOException {
        if (!game_over){
            comandoG();
            tableroGridPane.requestFocus();
        }
    }

    @FXML
    private void teletransporteAleatorio() throws IOException {
        if (!game_over){
            comandoT();
            tableroGridPane.requestFocus();
        }
    }

    @FXML
    private void noMoverse() throws IOException {
        if (!game_over){
            comandoX();
            tableroGridPane.requestFocus();
        }
    }

    @FXML
    private void comandoA() {
        if (!game_over){
            juego.moverJugadorenTablero(Direccion.IZQUIERDA);
            actualizarTurno();
        }
    }

    @FXML
    private void comandoS() {
        if (!game_over){
            juego.moverJugadorenTablero(Direccion.ABAJO);
            actualizarTurno();
        }
    }

    @FXML
    private void comandoD() {
        if (!game_over){
            juego.moverJugadorenTablero(Direccion.DERECHA);
            actualizarTurno();
        }
    }

    @FXML
    private void comandoW() {
        if (!game_over){
            juego.moverJugadorenTablero(Direccion.ARRIBA);
            actualizarTurno();
        }
    }

    @FXML
    private void comandoQ() {
        if (!game_over){
            juego.moverJugadorenTablero(Direccion.DIAGONAL_ARRIBA_IZQUIERDA);
            actualizarTurno();
        }
    }

    @FXML
    private void comandoE() {
        if (!game_over){
            juego.moverJugadorenTablero(Direccion.DIAGONAL_ARRIBA_DERECHA);
            actualizarTurno();
        }
    }

    @FXML
    private void comandoZ() {
        if (!game_over){
            juego.moverJugadorenTablero(Direccion.DIAGONAL_ABAJO_IZQUIERDA);
            actualizarTurno();
        }
    }

    @FXML
    private void comandoC() {
        if (!game_over){
            juego.moverJugadorenTablero(Direccion.DIAGONAL_ABAJO_DERECHA);
            actualizarTurno();
        }
    }

    @FXML
    private void comandoX() {
        if (!game_over){
            juego.moverEnemigos();
            actualizarTurno();
        }
    }

    @FXML
    private void comandoG() {
        if (!game_over){
            int nueva_fila = Integer.parseInt(FieldTPFila.getText());
            int nueva_columna = Integer.parseInt(FieldTPColumna.getText());
            if (nueva_fila >= 0 && nueva_fila < this.filas) {
                if (nueva_columna >= 0 && nueva_columna < this.columnas){
                    //juego.jugadorTeletransportacion(nueva_fila, nueva_columna);
                    actualizarTurno();
                }
            }
        }
    }

    @FXML
    private void comandoT() {
        if (!game_over){
            Random random = new Random();
            int nueva_fila = random.nextInt(this.filas);
            int nueva_columna = random.nextInt(this.columnas);
            //juego.jugadorTeletransportacion(nueva_fila, nueva_columna);
            actualizarTurno();
        }
    }
}