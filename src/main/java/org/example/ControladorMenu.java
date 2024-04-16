package org.example;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ControladorMenu {

    public Button Mapa1;
    public Button Mapa2;
    public Button Mapa3;
    public Button Mapa4;
    public Button Mapa5;

    @FXML
    private void switchToMapa1() throws IOException {
        App.setRoot("mapa1");
    }

    @FXML
    private void switchToMapa2() throws IOException {
        App.setRoot("mapa2");
    }

    @FXML
    private void switchToMapa3() throws IOException {
        App.setRoot("mapa3");
    }

    @FXML
    private void switchToMapa4() throws IOException {
        App.setRoot("mapa4");
    }

    @FXML
    private void switchToMapa5() throws IOException {
        App.setRoot("mapa5");
    }

    @FXML
    private void switchToTutorial() throws IOException {
        App.setRoot("tutorial");
    }

}
