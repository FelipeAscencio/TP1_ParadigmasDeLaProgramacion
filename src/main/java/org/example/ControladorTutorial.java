package org.example;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ControladorTutorial {
    public Button BotonMenu;

    @FXML
    private void switchToMenu() throws IOException {
        App.setRoot("menu");
    }
}