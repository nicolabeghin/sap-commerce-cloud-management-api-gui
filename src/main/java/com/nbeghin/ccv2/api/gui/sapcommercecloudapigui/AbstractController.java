package com.nbeghin.ccv2.api.gui.sapcommercecloudapigui;

import com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.utils.SystemCommons;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
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
        String script = "display notification \"" + escapeAppleScript(content)
                + "\" with title \"" + escapeAppleScript(title) + "\" sound name \"Frog\"";
        Runtime.getRuntime().exec(new String[]{"osascript", "-e", script});
    }

    private static String escapeAppleScript(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private void dialogInfo(String header, String content) {
        App.LOG.warn("INFO - " + header + " - " + content);
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, content);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.setTitle("Info");
            alert.setHeaderText(header);
            alert.showAndWait();
        });
    }

    private void dialogError(String header, String content) {
        App.LOG.error("ERROR - " + header + " - " + content);
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

    protected void dialogDetails(String title, String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(null);
            TextArea textArea = new TextArea(content);
            textArea.setEditable(false);
            textArea.setWrapText(true);
            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            GridPane.setVgrow(textArea, Priority.ALWAYS);
            GridPane.setHgrow(textArea, Priority.ALWAYS);
            GridPane grid = new GridPane();
            grid.setMaxWidth(Double.MAX_VALUE);
            grid.add(textArea, 0, 0);
            alert.getDialogPane().setContent(grid);
            alert.getDialogPane().setPrefSize(520, 400);
            alert.showAndWait();
        });
    }

    protected Optional<ButtonType> dialogMaintenanceConfirm(String endpointName, String start, String end) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Confirm Maintenance Mode");
        alert.setHeaderText(null);
        alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        Label nameLabel = new Label("Schedule maintenance window for \"" + endpointName + "\"?");
        nameLabel.setFont(Font.font(null, FontWeight.BOLD, 13));

        Label startLabel = new Label("Start:   " + start);
        Label endLabel   = new Label("End:     " + end);

        Label warningTitle = new Label("⚠  Application must remain running");
        warningTitle.setFont(Font.font(null, FontWeight.BOLD, 12));
        warningTitle.setTextFill(Color.web("#7d4e00"));

        Label warningBody = new Label(
            "This application drives the enable and disable API calls at the\n" +
            "scheduled times. It must stay open and running for the entire\n" +
            "duration of the maintenance window. Closing it early will prevent\n" +
            "one or both calls from being made.");
        warningBody.setTextFill(Color.web("#7d4e00"));
        warningBody.setWrapText(true);

        Label powerTitle = new Label("⚡  Disable sleep / power management");
        powerTitle.setFont(Font.font(null, FontWeight.BOLD, 12));
        powerTitle.setTextFill(Color.web("#7d4e00"));

        Label powerBody = new Label(
            "Make sure your machine will not go to sleep or hibernate during\n" +
            "the window. On macOS go to System Settings → Battery → prevent\n" +
            "sleep when charging. On Windows open Power Options and set\n" +
            "\"Put the computer to sleep\" to Never.");
        powerBody.setTextFill(Color.web("#7d4e00"));
        powerBody.setWrapText(true);

        VBox warningBox = new VBox(6, warningTitle, warningBody, powerTitle, powerBody);
        warningBox.setPadding(new Insets(10));
        warningBox.setStyle("-fx-background-color: #fff3cd; -fx-border-color: #f0ad4e; -fx-border-radius: 4; -fx-background-radius: 4;");

        VBox content = new VBox(10, nameLabel, startLabel, endLabel, warningBox);
        content.setPadding(new Insets(4, 12, 4, 12));

        alert.getDialogPane().setContent(content);
        alert.getDialogPane().setPrefWidth(460);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        return alert.showAndWait();
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
