package org.example;

import java.io.IOException;
import javafx.fxml.FXML;

public class Mapa1 {

    @FXML
    private void switchToMenu() throws IOException {
        App.setRoot("menu");
    }
}