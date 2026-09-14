package com.nbeghin.ccv2.api.gui.sapcommercecloudapigui;

import com.sap.cx.commercecloud.management.openapi.model.CreateScheduledActivityRequestDTO;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class ScheduledActivityCreateController extends AbstractController implements Initializable {

    @FXML private ComboBox<String> comboActivityType;
    @FXML private DatePicker datePicker;
    @FXML private Spinner<Integer> spinnerHour;
    @FXML private Spinner<Integer> spinnerMinute;

    private CreateScheduledActivityRequestDTO result;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        comboActivityType.setItems(FXCollections.observableArrayList(
            "HIBERNATE_COMMERCE_ENVIRONMENT",
            "WAKE_UP_COMMERCE_ENVIRONMENT"
        ));
        comboActivityType.getSelectionModel().selectFirst();

        LocalDateTime defaultTime = LocalDateTime.now().plusHours(1);
        spinnerHour.setValueFactory(
            new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, defaultTime.getHour()));
        spinnerMinute.setValueFactory(
            new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, defaultTime.getMinute()));
        datePicker.setValue(defaultTime.toLocalDate());
    }

    @FXML
    private void onCreate(ActionEvent event) {
        String activityType = comboActivityType.getValue();
        if (activityType == null) {
            dialogError("Activity Type is required");
            return;
        }
        java.time.LocalDate date = datePicker.getValue();
        if (date == null) {
            dialogError("Scheduled date is required");
            return;
        }
        // Bridge java.time (JavaFX DatePicker) to org.threeten.bp (API model layer)
        org.threeten.bp.LocalDate tbpDate =
            org.threeten.bp.LocalDate.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
        org.threeten.bp.LocalTime tbpTime =
            org.threeten.bp.LocalTime.of(spinnerHour.getValue(), spinnerMinute.getValue());
        org.threeten.bp.OffsetDateTime scheduledTs =
            org.threeten.bp.LocalDateTime.of(tbpDate, tbpTime)
                .atOffset(org.threeten.bp.ZoneOffset.UTC);

        result = new CreateScheduledActivityRequestDTO();
        result.setActivityType(activityType);
        result.setScheduledTimestamp(scheduledTs);

        closeStage();
    }

    @FXML
    private void onCancel(ActionEvent event) {
        result = null;
        closeStage();
    }

    public CreateScheduledActivityRequestDTO getResult() {
        return result;
    }

    private void closeStage() {
        Stage stage = (Stage) comboActivityType.getScene().getWindow();
        stage.close();
    }
}
