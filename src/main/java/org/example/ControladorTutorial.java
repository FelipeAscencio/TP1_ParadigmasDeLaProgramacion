package org.example;

import java.io.IOException;
import javafx.fxml.FXML;

public class ControladorTutorial {

    @FXML
    private void switchToMenu() throws IOException {
        App.setRoot("menu");
    }
}