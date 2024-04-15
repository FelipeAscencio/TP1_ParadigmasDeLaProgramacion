package org.example;

import java.io.IOException;
import javafx.fxml.FXML;

public class ControladorMenu {

    @FXML
    private void switchToMapa() throws IOException {
        App.setRoot("mapa");
    }

    @FXML
    private void switchToTutorial() throws IOException {
        App.setRoot("tutorial");
    }

}
