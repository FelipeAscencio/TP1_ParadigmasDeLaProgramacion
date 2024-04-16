package org.example;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ControladorMapa4 implements Initializable {
    @FXML
    public GridPane tableroGridPane;
    private int filas = 29;
    private int columnas = 29;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        double tamanioCelda = 18;

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