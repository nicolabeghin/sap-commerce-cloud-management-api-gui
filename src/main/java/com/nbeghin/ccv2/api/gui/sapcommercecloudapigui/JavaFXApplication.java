package com.nbeghin.ccv2.api.gui.sapcommercecloudapigui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class JavaFXApplication extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("main.fxml"));
        Parent root = loader.load();
        MainController controller = loader.getController();
        controller.setPrimaryStage(stage);
        stage.setTitle("sap-commerce-cloud-api-gui");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }
}
