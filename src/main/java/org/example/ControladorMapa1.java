package org.example;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ControladorMapa1 implements Initializable {
    @FXML
    public GridPane tableroGridPane;
    private int filas = 15;
    private int columnas = 15;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        double tamanioCelda = 33;

        for (int fila = 0; fila < filas; fila++) {
            for (int columna = 0; columna < columnas; columna++) {
                Rectangle celda = new Rectangle(tamanioCelda, tamanioCelda);
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