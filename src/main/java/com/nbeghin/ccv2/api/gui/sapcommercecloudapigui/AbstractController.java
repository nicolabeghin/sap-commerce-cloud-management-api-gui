package com.nbeghin.ccv2.api.gui.sapcommercecloudapigui;

import com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.utils.SystemCommons;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.controlsfx.control.Notifications;
import org.controlsfx.glyphfont.FontAwesome;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;

public abstract class AbstractController {
    public static Stage notificationStage;
    protected static FontAwesome fontAwesome;

    static {
        fontAwesome = new FontAwesome(MainController.class.getClassLoader().getResourceAsStream("fontawesome-free-652.otf"));
    }

    protected void notificationInfo(String title, String content) {
        Platform.runLater(() -> {
            try {
                if (SystemCommons.isMac()) osxNotification(title, content);
                else notification(title, content).showInformation();
            } catch (Exception ex) {
                App.LOG.error("Unable to show notification - " + ex.getMessage());
            }
        });
    }

    protected void notificationError(String title, String content) {
        Platform.runLater(() -> {
            try {
                if (SystemCommons.isMac()) osxNotification(title, content);
                else notification(title, content).showError();
            } catch (Exception ex) {
                App.LOG.error("Unable to show notification - " + ex.getMessage());
            }
        });
    }

    private Notifications notification(String title, String content) {
        getOwnerStageForNotification();
        return Notifications.create().title(title).position(Pos.TOP_RIGHT).text(content);
    }


    /**
     * @url https://stackoverflow.com/a/26876019/2378095
     */
    private void getOwnerStageForNotification() {
        try {
            if (notificationStage != null) return;
            App.LOG.info("Creating hidden stage for notification");
            notificationStage = new Stage(StageStyle.TRANSPARENT);
            StackPane root = new StackPane();
            root.setStyle("-fx-background-color: TRANSPARENT");
            Scene scene = new Scene(root, 1, 1);
            scene.setFill(Color.TRANSPARENT);
            notificationStage.setScene(scene);
            notificationStage.setWidth(1);
            notificationStage.setHeight(1);
            notificationStage.toBack();
            notificationStage.show();
        } catch (Exception ex) {
            App.LOG.error("Unable to create owner stage for notification - " + ex.getMessage());
        }
    }

    private void osxNotification(String title, String content) throws IOException {
        Runtime.getRuntime().exec(new String[]{"osascript", "-e", "display notification \"" + content + "\" with title \"" + title + "\" sound name \"Frog\""});
    }

    protected void dialogInfo(String header, String content) {
        App.LOG.info(content);
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, content);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.setTitle("Info");
            alert.setHeaderText(header);
            alert.showAndWait();
        });
    }

    private void dialogError(String header, String content) {
        App.LOG.error(content);
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR, content);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.setTitle("Error");
            alert.setHeaderText(header);
            Optional<ButtonType> result = alert.showAndWait();
        });
    }

    protected void dialogError(String content) {
        dialogError("ERROR", content);
    }

    protected void dialogInfo(String content) {
        dialogInfo("INFO", content);
    }

    protected void openWebpage(String site) {
        try {
            Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
            if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                desktop.browse(new URL(site).toURI());
            }
        } catch (Exception ex) {
            App.LOG.warn("Unable to open webpage - " + ex.getMessage());
        }
    }

}
