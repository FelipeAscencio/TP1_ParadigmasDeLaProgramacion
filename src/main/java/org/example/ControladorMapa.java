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
    public GridPane TableroGridpane;
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
    private static final int IMG_PLAYER_1 = 0;
    private static final int IMG_PLAYER_2 = 1;
    private static final int IMG_PLAYER_3 = 2;
    private static final int IMG_PLAYER_4 = 3;
    private static final int IMG_PLAYER_5 = 4;
    private static final int IMG_ROBOT1_1 = 5;
    private static final int IMG_ROBOT1_2 = 6;
    private static final int IMG_ROBOT1_3 = 7;
    private static final int IMG_ROBOT1_4 = 8;
    private static final int IMG_ROBOT2_1 = 9;
    private static final int IMG_ROBOT2_2 = 10;
    private static final int IMG_ROBOT2_3 = 11;
    private static final int IMG_ROBOT2_4 = 12;
    private static final int PRIMERA_POSICION = 0;
    private static final int SEGUNDA_POSICION = 1;
    private static final int TERCERA_POSICION = 2;
    private static final int CUARTA_POSICION = 3;
    private static final int GAMEOVER_POSICION = 4;
    private static final int IMG_EXPLOSION = 13;
    private static final double CORRECCION_X = 2; //Se usa para corregir la relaciòn de coordenadas X entre el mouse y el jugador.
    private static final double CORRECION_Y = 1.05; //Se usa para corregir la relaciòn de coordenadas Y entre el mouse y el jugador.
    private static final int TAMANIO_CURSOR = 300;
    private static final int USOS_MINIMOS = 1;
    private static int filas;
    private static int columnas;
    private int estado_sprites;
    private WritableImage[] sprites;
    private Juego juego;
    private boolean game_over;

    public ControladorMapa() {
        filas = CASILLAS_DEFAULT;
        columnas = CASILLAS_DEFAULT;
        estado_sprites = 0;
    }

    private void inicializarKeys(){
        TableroGridpane.setOnKeyReleased(event -> {
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

    private WritableImage[] separarSprites(ImageView spriteStrip) {
        WritableImage[] sprites = new WritableImage[CANTIDAD_SPRITES];
        PixelReader pixelReader = spriteStrip.getImage().getPixelReader();

        for (int i = 0; i < CANTIDAD_SPRITES; i++) {
            int startX = i * TAMANIO_SPRITE;
            int startY = 0;
            WritableImage spriteImage = new WritableImage(pixelReader, startX, startY, TAMANIO_SPRITE, TAMANIO_SPRITE);
            sprites[i] = spriteImage;
        }
        return sprites;
    }

    private void inicializarSprites() {
        ImageView spriteStrip = new ImageView("file:doc/modelos.png");
        sprites = separarSprites(spriteStrip);
    }

    private Image obtenerPrimerSprite(Object elemento){
        if (elemento instanceof Jugador){
            return sprites[IMG_PLAYER_1];
        } else if (elemento instanceof Robot1){
            return sprites[IMG_ROBOT1_1];
        } else if (elemento instanceof Robot2){
            return sprites[IMG_ROBOT2_1];
        }
        return null;
    }

    private Image obtenerSegundoSprite(Object elemento){
        if (elemento instanceof Jugador){
            return sprites[IMG_PLAYER_2];
        } else if (elemento instanceof Robot1){
            return sprites[IMG_ROBOT1_2];
        } else if (elemento instanceof Robot2){
            return sprites[IMG_ROBOT2_2];
        }
        return null;
    }

    private Image obtenerTercerSprite(Object elemento){
        if (elemento instanceof Jugador){
            return sprites[IMG_PLAYER_3];
        } else if (elemento instanceof Robot1){
            return sprites[IMG_ROBOT1_3];
        } else if (elemento instanceof Robot2){
            return sprites[IMG_ROBOT2_3];
        }
        return null;
    }

    private Image obtenerCuartoSprite(Object elemento){
        if (elemento instanceof Jugador){
            return sprites[IMG_PLAYER_4];
        } else if (elemento instanceof Robot1){
            return sprites[IMG_ROBOT1_4];
        } else if (elemento instanceof Robot2){
            return sprites[IMG_ROBOT2_4];
        }
        return null;
    }

    private Image obtenerGameOverSprite(Object elemento){
        if (elemento instanceof Jugador){
            return sprites[IMG_PLAYER_5];
        }
        return null;
    }

    private Image obtenerImagenElemento(Object elemento) {
        if (elemento instanceof Explosion){
            return sprites[IMG_EXPLOSION];
        } else if (estado_sprites == PRIMERA_POSICION){
            return obtenerPrimerSprite(elemento);
        } else if (estado_sprites == SEGUNDA_POSICION){
            return obtenerSegundoSprite(elemento);
        } else if (estado_sprites == TERCERA_POSICION){
            return obtenerTercerSprite(elemento);
        } else if (estado_sprites == CUARTA_POSICION){
            return obtenerCuartoSprite(elemento);
        } else if (estado_sprites == GAMEOVER_POSICION){
            return obtenerGameOverSprite(elemento);
        }

        return null;
    }

    private void inicializarGrid() {
        TableroGridpane.getChildren().clear();

        for (int fila = 0; fila < filas; fila++) {
            for (int columna = 0; columna < columnas; columna++) {
                StackPane celda = new StackPane();
                celda.setPrefSize(TAMANIO_CELDA, TAMANIO_CELDA);
                celda.setStyle("-fx-background-color: " + ((fila + columna) % 2 == 0 ? "white" : "lightblue"));
                TableroGridpane.add(celda, columna, fila);

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
        if (estado_sprites == CUARTA_POSICION){
            estado_sprites = PRIMERA_POSICION;
        } else {
            estado_sprites++;
        }
    }

    private void indicarFinJuego(){
        TextPuntaje.setText("Pulse 'Reiniciar'");
        TextNivelActual.setText("GAME OVER");
        estado_sprites = GAMEOVER_POSICION;
    }

    private void actualizarEstado(){
        int puntos_actuales = juego.getPuntos();
        int nivel_actual = juego.getNivel();
        int seguros_restantes = juego.getUsosTeletransportacion();

        TextPuntaje.setText("Puntos: " + puntos_actuales);
        TextNivelActual.setText("Nivel: " + nivel_actual);
        TextTPRestantes.setText("Restantes: " + seguros_restantes);

        game_over = juego.getEstado();
        if (game_over){
            indicarFinJuego();
        }
    }

    private void actualizarTurno(){
        actualizarEstado();
        inicializarGrid();
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
        double tamanioCursor = TAMANIO_CURSOR;
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
                TableroGridpane.setCursor(new ImageCursor(cursorArriba));
                break;
            case ABAJO:
                TableroGridpane.setCursor(new ImageCursor(cursorAbajo));
                break;
            case IZQUIERDA:
                TableroGridpane.setCursor(new ImageCursor(cursorIzq));
                break;
            case DERECHA:
                TableroGridpane.setCursor(new ImageCursor(cursorDer));
                break;
            case DIAGONAL_ARRIBA_IZQUIERDA:
                TableroGridpane.setCursor(new ImageCursor(cursorArrIzq));
                break;
            case DIAGONAL_ARRIBA_DERECHA:
                TableroGridpane.setCursor(new ImageCursor(cursorArrDer));
                break;
            case DIAGONAL_ABAJO_IZQUIERDA:
                TableroGridpane.setCursor(new ImageCursor(cursorAbaIzq));
                break;
            case DIAGONAL_ABAJO_DERECHA:
                TableroGridpane.setCursor(new ImageCursor(cursorAbaDer));
                break;
        }
    }

    private void cargarMapa(){
        juego = new Juego(filas,columnas);
        game_over = false;

        inicializarSprites();
        actualizarTurno();
        inicializarKeys();
        TableroGridpane.setOnMouseMoved(this::detectarDireccionMouse);
        TableroGridpane.setOnMouseClicked(this::detectarClicMouse);

        Platform.runLater(() -> {
            TableroGridpane.requestFocus();
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarMapa();
    }

    @FXML
    private void switchToMenu() throws IOException {
        App.setRoot("menu");
    }

    @FXML
    private void reloadMapa(){
        cargarMapa();
    }

    @FXML
    private void cambiarMapa(){
        if (!game_over){
            int nuevas_filas = Integer.parseInt(FieldFilas.getText());
            int nuevas_columnas = Integer.parseInt(FieldColumnas.getText());
            if (nuevas_filas >= CASILLAS_MIN && nuevas_filas <= CASILLAS_MAX) {
                if (nuevas_columnas >= CASILLAS_MIN && nuevas_columnas <= CASILLAS_MAX){
                    filas = nuevas_filas;
                    columnas = nuevas_columnas;
                    cargarMapa();
                }
            }
            TableroGridpane.requestFocus();
        }
    }

    @FXML
    private void teletransporteSeguro(){
        if (!game_over){
            comandoG();
            TableroGridpane.requestFocus();
        }
    }

    @FXML
    private void teletransporteAleatorio(){
        if (!game_over){
            comandoT();
            TableroGridpane.requestFocus();
        }
    }

    @FXML
    private void noMoverse(){
        if (!game_over){
            comandoX();
            TableroGridpane.requestFocus();
        }
    }

    private void comandoA() {
        if (!game_over){
            juego.moverJugadorenTablero(Direccion.IZQUIERDA);
            actualizarTurno();
        }
    }

    private void comandoS() {
        if (!game_over){
            juego.moverJugadorenTablero(Direccion.ABAJO);
            actualizarTurno();
        }
    }

    private void comandoD() {
        if (!game_over){
            juego.moverJugadorenTablero(Direccion.DERECHA);
            actualizarTurno();
        }
    }

    private void comandoW() {
        if (!game_over){
            juego.moverJugadorenTablero(Direccion.ARRIBA);
            actualizarTurno();
        }
    }

    private void comandoQ() {
        if (!game_over){
            juego.moverJugadorenTablero(Direccion.DIAGONAL_ARRIBA_IZQUIERDA);
            actualizarTurno();
        }
    }

    private void comandoE() {
        if (!game_over){
            juego.moverJugadorenTablero(Direccion.DIAGONAL_ARRIBA_DERECHA);
            actualizarTurno();
        }
    }

    private void comandoZ() {
        if (!game_over){
            juego.moverJugadorenTablero(Direccion.DIAGONAL_ABAJO_IZQUIERDA);
            actualizarTurno();
        }
    }

    private void comandoC() {
        if (!game_over){
            juego.moverJugadorenTablero(Direccion.DIAGONAL_ABAJO_DERECHA);
            actualizarTurno();
        }
    }

    private void comandoX() {
        if (!game_over){
            juego.moverEnemigos();
            actualizarTurno();
        }
    }

    private void comandoG() {
        if (!game_over){
            int turnos = juego.getUsosTeletransportacion();
            if (turnos >= USOS_MINIMOS){
                int nueva_fila = Integer.parseInt(FieldTPFila.getText());
                int nueva_columna = Integer.parseInt(FieldTPColumna.getText());
                if (nueva_fila >= 0 && nueva_fila < filas) {
                    if (nueva_columna >= 0 && nueva_columna < columnas){
                        juego.jugadorteletransportacion(nueva_fila, nueva_columna, true);
                        juego.moverEnemigos();
                        actualizarTurno();
                    }
                }
            }
        }
    }

    private void comandoT() {
        if (!game_over){
            Random random = new Random();
            int nueva_fila = random.nextInt(filas);
            int nueva_columna = random.nextInt(columnas);
            juego.jugadorteletransportacion(nueva_fila, nueva_columna, false);
            juego.moverEnemigos();
            actualizarTurno();
        }
    }
}