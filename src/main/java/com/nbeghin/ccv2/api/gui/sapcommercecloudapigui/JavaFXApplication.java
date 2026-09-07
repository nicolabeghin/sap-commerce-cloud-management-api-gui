package com.nbeghin.ccv2.api.gui.sapcommercecloudapigui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

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
        stage.setOnCloseRequest(event -> {
            if (controller.hasPendingMaintenance()) {
                Alert alert = new Alert(Alert.AlertType.WARNING,
                        "One or more scheduled maintenance windows are still pending. "
                                + "They are held in memory only and will be LOST if you quit now — "
                                + "the enable and/or disable calls will not be made.\n\nQuit anyway?",
                        ButtonType.YES, ButtonType.NO);
                alert.setTitle("Pending maintenance windows");
                alert.setHeaderText(null);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isEmpty() || result.get() != ButtonType.YES) {
                    event.consume();
                    return;
                }
            }
            controller.shutdownScheduler();
            Platform.exit();
            System.exit(0);
        });
        stage.setTitle("sap-commerce-cloud-api-gui");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }
}
