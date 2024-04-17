package org.example;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ControladorMapa1 implements Initializable {
    @FXML
    public GridPane tableroGridPane;
    private static final int filas = 15;
    private static final int columnas = 15;
    private static final int tamaniocelda = 33;
    public Button Menu;
    public Button Reiniciar;
    public Button TeletransporteSeguro;
    public Button TeletransporteAleatorio;
    public Button EsperarRobots;
    public Label TPRestantes;
    public Label NivelActual;
    public Label Puntaje;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for (int fila = 0; fila < filas; fila++) {
            for (int columna = 0; columna < columnas; columna++) {
                Rectangle celda = new Rectangle(tamaniocelda, tamaniocelda);
                celda.setFill((fila + columna) % 2 == 0 ? Color.WHITE : Color.LIGHTBLUE);
                tableroGridPane.add(celda, columna, fila);
            }
        }
    }

    @FXML
    private void switchToMenu() throws IOException {
        App.setRoot("menu");
    }
}