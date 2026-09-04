package com.nbeghin.ccv2.api.gui.sapcommercecloudapigui;

import com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.tasks.*;
import com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.utils.Constants;
import com.sap.cx.commercecloud.management.openapi.model.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.dialog.ProgressDialog;
import org.controlsfx.glyphfont.FontAwesome;

import javafx.scene.control.SpinnerValueFactory;
import javafx.util.StringConverter;
import org.threeten.bp.OffsetDateTime;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController extends AbstractController implements Initializable {
    private static final ObservableList<BuildDetailDTO> buildsList = FXCollections.observableArrayList();
    private static final ObservableList<DeploymentDetailDTO> deploymentsList = FXCollections.observableArrayList();
    private static final ObservableList<EnvironmentDetailDTO> environmentsList = FXCollections.observableArrayList(); // @TODO
    private static final ObservableList<EndpointDetailDTO> endpointsList = FXCollections.observableArrayList();
    private static final ObservableList<String> gitBranches = FXCollections.observableArrayList("develop", "master", "production");
    private static final ObservableList<CreateDeploymentRequestDTO.DatabaseUpdateModeEnum> deploymentDatabaseUpdateModes = FXCollections.observableArrayList(CreateDeploymentRequestDTO.DatabaseUpdateModeEnum.values());
    private static final ObservableList<CreateDeploymentRequestDTO.StrategyEnum> deploymentStrategies = FXCollections.observableArrayList(CreateDeploymentRequestDTO.StrategyEnum.values());
    private final ScheduledExecutorService maintenanceScheduler = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread t = new Thread(r);
        t.setDaemon(true);
        return t;
    });
    private ScheduledFuture<?> activeEnableFuture;
    private ScheduledFuture<?> activeDisableFuture;
    private static Stage primaryStage;
    @FXML
    public TabPane tabPane;
    @FXML
    public Button btnRefreshDeployments;
    @FXML
    public Button btnShowDeploymentDetails;
    @FXML
    private TableView tableDeployments;
    @FXML
    private CheckBox checkboxDeployAfterBuild;
    @FXML
    private Button btnRefreshBuilds;
    @FXML
    private TextArea txtAreaConsole;
    @FXML
    private Button btnStartBuild;
    @FXML
    private ProgressBar mainProgressBar;
    @FXML
    private Button btnShowBuildDetails;
    @FXML
    private TextField txtGitBranches;
    @FXML
    private Button btnStartDeploy;
    @FXML
    private Button btnProposeBuildName;
    @FXML
    private TableView tableBuilds;
    @FXML
    private ComboBox comboDeploymentStrategies;
    @FXML
    private ComboBox comboDeploymentDatabaseUpdateMode;
    @FXML
    private ComboBox comboEnvironments;
    @FXML
    private TextField txtBuildCode;
    @FXML
    private MenuBar menuBar;
    @FXML
    private TableView tableEndpoints;
    @FXML
    private DatePicker datePickerMaintenanceStart;
    @FXML
    private Spinner<Integer> spinnerMaintenanceHour;
    @FXML
    private Spinner<Integer> spinnerMaintenanceMinute;
    @FXML
    private DatePicker datePickerMaintenanceEnd;
    @FXML
    private Spinner<Integer> spinnerMaintenanceEndHour;
    @FXML
    private Spinner<Integer> spinnerMaintenanceEndMinute;
    @FXML
    private Button btnScheduleMaintenance;
    @FXML
    private Button btnRefreshEndpoints;
    @FXML
    private Button btnShowEndpointDetails;

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        MainController.primaryStage = primaryStage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TextFields.bindAutoCompletion(txtGitBranches, gitBranches);
        comboEnvironments.setItems(environmentsList);
        comboEnvironments.setConverter(new StringConverter<EnvironmentDetailDTO>() {
            @Override public String toString(EnvironmentDetailDTO e) { return e == null ? "" : e.getName(); }
            @Override public EnvironmentDetailDTO fromString(String s) { return null; }
        });
        tableDeployments.setItems(deploymentsList);
        comboDeploymentDatabaseUpdateMode.setItems(deploymentDatabaseUpdateModes);
        comboDeploymentStrategies.setItems(deploymentStrategies);
        comboDeploymentStrategies.getSelectionModel().select(CreateDeploymentRequestDTO.StrategyEnum.ROLLING_UPDATE);
        comboDeploymentDatabaseUpdateMode.getSelectionModel().select(CreateDeploymentRequestDTO.DatabaseUpdateModeEnum.NONE);
        comboDeploymentStrategies.setDisable(true);
        comboDeploymentDatabaseUpdateMode.setDisable(true);
        initializeBuildsTable();
        initializeDeploymentsTable();
        initializeEndpointsTable();
        java.time.LocalDate today = java.time.LocalDate.now();
        java.time.LocalDateTime startDefault = java.time.LocalDateTime.now().plusHours(1);
        spinnerMaintenanceHour.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, startDefault.getHour()));
        spinnerMaintenanceMinute.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, startDefault.getMinute()));
        spinnerMaintenanceEndHour.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, startDefault.getHour() + 1 > 23 ? 23 : startDefault.getHour() + 1));
        spinnerMaintenanceEndMinute.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, startDefault.getMinute()));
        datePickerMaintenanceStart.setValue(startDefault.toLocalDate());
        datePickerMaintenanceEnd.setValue(startDefault.toLocalDate());
        txtBuildCode.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !StringUtils.isEmpty(newValue)) {
                if (comboEnvironments.getSelectionModel().getSelectedIndex() != -1) {
                    checkboxDeployAfterBuild.setDisable(false);
                    btnStartBuild.setDisable(false);
                }
            }
        });
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue != oldValue && "tabDeployments".equals(newValue.getId()) && deploymentsList.isEmpty()) {
                onLoadLatestDeployments();
            }
            if (newValue != null && newValue != oldValue && "tabEndpoints".equals(newValue.getId()) && endpointsList.isEmpty()) {
                onLoadEndpoints();
            }
            if (newValue != null && newValue != oldValue) {
                String tabId = newValue.getId();
                boolean enabled = "tabNewBuild".equals(tabId)
                        || ("tabExistingBuild".equals(tabId) && tableBuilds.getSelectionModel().getSelectedIndex() != -1);
                comboDeploymentStrategies.setDisable(!enabled);
                comboDeploymentDatabaseUpdateMode.setDisable(!enabled);
            }
        });
        btnRefreshBuilds.setGraphic(fontAwesome.create(FontAwesome.Glyph.REFRESH));
        btnRefreshDeployments.setGraphic(fontAwesome.create(FontAwesome.Glyph.REFRESH));
        btnRefreshEndpoints.setGraphic(fontAwesome.create(FontAwesome.Glyph.REFRESH));
        btnShowBuildDetails.setGraphic(fontAwesome.create(FontAwesome.Glyph.INFO));
        btnShowDeploymentDetails.setGraphic(fontAwesome.create(FontAwesome.Glyph.INFO));
        btnShowEndpointDetails.setGraphic(fontAwesome.create(FontAwesome.Glyph.INFO));
        btnStartBuild.setGraphic(fontAwesome.create(FontAwesome.Glyph.BUILDING));
        btnStartDeploy.setGraphic(fontAwesome.create(FontAwesome.Glyph.CLOUD_UPLOAD));
        btnProposeBuildName.setGraphic(fontAwesome.create(FontAwesome.Glyph.LIGHTBULB_ALT));
        restorePreferences();
        bindPreferences();
        Platform.runLater(this::onLoadEnvironments);
        Platform.runLater(this::onLoadLatestBuilds);
    }

    private void bindPreferences() {
        txtBuildCode.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !StringUtils.isEmpty(newValue)) {
                if (comboEnvironments.getSelectionModel().getSelectedIndex() != -1) {
                    App.savePreference(Constants.PREFS_GIT_BRANCH, (String) newValue);
                }
            }
        });
        comboEnvironments.valueProperty().addListener((observable, oldValue, newValue) -> { // backup is selected
            if (newValue != null && newValue != oldValue) {
                String newEnvironmentCode = ((EnvironmentDetailDTO) newValue).getCode();
                String storedEnvironmentCode = App.getPreference(Constants.PREFS_ENVIRONMENT);
                if (newEnvironmentCode != null && !newEnvironmentCode.equals(storedEnvironmentCode)) {
                    App.savePreference(Constants.PREFS_ENVIRONMENT, ((EnvironmentDetailDTO) newValue).getCode());
                }
                if ("tabDeployments".equals(tabPane.getSelectionModel().getSelectedItem().getId())) {
                    onRefreshDeployments(null);
                }
                if ("tabEndpoints".equals(tabPane.getSelectionModel().getSelectedItem().getId())) {
                    onLoadEndpoints();
                }
            }
        });
        comboDeploymentStrategies.valueProperty().addListener((observable, oldValue, newValue) -> { // backup is selected
            if (newValue != null && newValue != oldValue) {
                App.savePreference(Constants.PREFS_DEPLOYMENT_STRATEGY, ((CreateDeploymentRequestDTO.StrategyEnum) newValue).getValue());
            }
        });
        comboDeploymentDatabaseUpdateMode.valueProperty().addListener((observable, oldValue, newValue) -> { // backup is selected
            if (newValue != null && newValue != oldValue) {
                App.savePreference(Constants.PREFS_DATABASE_UPDATE_MODE, ((CreateDeploymentRequestDTO.DatabaseUpdateModeEnum) newValue).getValue());
            }
        });
    }

    private void restorePreferences() {
        Constants.DEBUG_ENABLED = App.getBooleanPreference(Constants.PREFS_DEBUG_ENABLED);
        String subscriptionCode = App.getPreference(Constants.PREFS_SUBSCRIPTION);
        if (StringUtils.isNotBlank(subscriptionCode)) {
            Constants.SUBSCRIPTION_CODE = subscriptionCode;
        }
        String accessToken = App.getPreference(Constants.PREFS_CLIENT_ID);
        if (StringUtils.isNotBlank(accessToken)) {
            Constants.CLIENT_ID = accessToken;
        }
        String clientSecret = App.getPreference(Constants.PREFS_CLIENT_SECRET);
        if (StringUtils.isNotBlank(clientSecret)) {
            Constants.CLIENT_SECRET = clientSecret;
        }
        if (StringUtils.isBlank(Constants.SUBSCRIPTION_CODE) || StringUtils.isBlank(Constants.CLIENT_ID) || StringUtils.isBlank(Constants.CLIENT_SECRET)) {
            showSettingsDialog();
        }

        String gitBranch = App.getPreference(Constants.PREFS_GIT_BRANCH);
        if (gitBranch != null) {
            txtGitBranches.setText(gitBranch);
        }
        String deploymentStrategyCode = App.getPreference(Constants.PREFS_DEPLOYMENT_STRATEGY);
        if (deploymentStrategyCode != null) {
            CreateDeploymentRequestDTO.StrategyEnum strategyEnum = deploymentStrategies.stream().filter(e -> e.getValue().equals(deploymentStrategyCode)).findFirst().orElse(null);
            comboDeploymentStrategies.getSelectionModel().select(strategyEnum);
        }
        String databaseUpdateModeCode = App.getPreference(Constants.PREFS_DATABASE_UPDATE_MODE);
        if (databaseUpdateModeCode != null) {
            CreateDeploymentRequestDTO.DatabaseUpdateModeEnum databaseUpdateModeEnum = deploymentDatabaseUpdateModes.stream().filter(e -> e.getValue().equals(databaseUpdateModeCode)).findFirst().orElse(null);
            comboDeploymentDatabaseUpdateMode.getSelectionModel().select(databaseUpdateModeEnum);
        }
    }

    private void initializeBuildsTable() {
        TableColumn nameCol = new TableColumn("Code");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        nameCol.setPrefWidth(90);
        TableColumn lastNameCol = new TableColumn("Name");
        lastNameCol.setPrefWidth(105);
        lastNameCol.setMaxWidth(Double.MAX_VALUE);
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn statusCol = new TableColumn("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        TableColumn<BuildDetailDTO, OffsetDateTime> buildStartTimestampCol = new TableColumn<>("Start");
        buildStartTimestampCol.setCellValueFactory(new PropertyValueFactory<>("buildStartTimestamp"));
        buildStartTimestampCol.setCellFactory(col -> new TableCell<BuildDetailDTO, OffsetDateTime>() {
            @Override protected void updateItem(OffsetDateTime item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : fmtDt(item));
            }
        });
        buildStartTimestampCol.setPrefWidth(120);
        tableBuilds.getColumns().addAll(nameCol, lastNameCol, statusCol, buildStartTimestampCol);
        tableBuilds.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableBuilds.setItems(buildsList);
        tableBuilds.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> { // backup is selected
            if (newValue != null && newValue != oldValue) {
                btnShowBuildDetails.setDisable(false);
                btnStartDeploy.setDisable(false);
                comboDeploymentStrategies.setDisable(false);
                comboDeploymentDatabaseUpdateMode.setDisable(false);
            }
        });
    }

    private void initializeDeploymentsTable() {
        TableColumn buildCol = new TableColumn("Build");
        buildCol.setCellValueFactory(new PropertyValueFactory<>("buildCode"));
        buildCol.setPrefWidth(90);
        buildCol.setMaxWidth(Double.MAX_VALUE);
        TableColumn lastNameCol = new TableColumn("Platform Update");
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("databaseUpdateMode"));
        TableColumn strategyCol = new TableColumn("Strategy");
        strategyCol.setCellValueFactory(new PropertyValueFactory<>("strategy"));
        TableColumn statusCol = new TableColumn("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        TableColumn<DeploymentDetailDTO, OffsetDateTime> buildStartTimestampCol = new TableColumn<>("Start");
        buildStartTimestampCol.setCellValueFactory(new PropertyValueFactory<>("createdTimestamp"));
        buildStartTimestampCol.setCellFactory(col -> new TableCell<DeploymentDetailDTO, OffsetDateTime>() {
            @Override protected void updateItem(OffsetDateTime item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : fmtDt(item));
            }
        });
        buildStartTimestampCol.setPrefWidth(120);
        tableDeployments.getColumns().addAll(buildCol, lastNameCol, strategyCol, statusCol, buildStartTimestampCol);
        tableDeployments.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableDeployments.setItems(deploymentsList);
        tableDeployments.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> { // backup is selected
            if (newValue != null && newValue != oldValue) {
                btnShowDeploymentDetails.setDisable(false);
            }
        });
    }

    private void initializeEndpointsTable() {
        TableColumn nameCol = new TableColumn("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(120);
        TableColumn<EndpointDetailDTO, String> urlCol = new TableColumn<>("URL");
        urlCol.setCellValueFactory(new PropertyValueFactory<>("url"));
        urlCol.setPrefWidth(180);
        urlCol.setMaxWidth(Double.MAX_VALUE);
        urlCol.setCellFactory(col -> new TableCell<EndpointDetailDTO, String>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(null);
                if (empty || item == null) { setGraphic(null); return; }
                javafx.scene.control.Hyperlink link = new javafx.scene.control.Hyperlink(item);
                String url = item.startsWith("http") ? item : "https://" + item;
                link.setOnAction(e -> openWebpage(url));
                setGraphic(link);
            }
        });
        TableColumn<EndpointDetailDTO, Boolean> maintenanceCol = new TableColumn<>("Maintenance mode");
        maintenanceCol.setCellValueFactory(new PropertyValueFactory<>("maintenanceMode"));
        maintenanceCol.setPrefWidth(80);
        maintenanceCol.setCellFactory(col -> new TableCell<EndpointDetailDTO, Boolean>() {
            @Override protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                setAlignment(javafx.geometry.Pos.CENTER);
                setText(null);
                if (empty || item == null) { setGraphic(null); return; }
                if (item) {
                    javafx.scene.Node icon = fontAwesome.create(FontAwesome.Glyph.WARNING);
                    icon.setStyle("-fx-fill: #e74c3c;");
                    setGraphic(icon);
                } else {
                    javafx.scene.Node icon = fontAwesome.create(FontAwesome.Glyph.CHECK);
                    icon.setStyle("-fx-fill: #27ae60;");
                    setGraphic(icon);
                }
            }
        });
        maintenanceCol.setStyle("-fx-alignment: CENTER;");
        tableEndpoints.getColumns().addAll(nameCol, urlCol, maintenanceCol);
        tableEndpoints.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableEndpoints.setItems(endpointsList);
        tableEndpoints.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            boolean selected = newValue != null;
            btnShowEndpointDetails.setDisable(!selected);
            btnScheduleMaintenance.setDisable(!selected);
            datePickerMaintenanceStart.setDisable(!selected);
            spinnerMaintenanceHour.setDisable(!selected);
            spinnerMaintenanceMinute.setDisable(!selected);
            datePickerMaintenanceEnd.setDisable(!selected);
            spinnerMaintenanceEndHour.setDisable(!selected);
            spinnerMaintenanceEndMinute.setDisable(!selected);
        });
    }

    private void onLoadEndpoints() {
        if (comboEnvironments.getSelectionModel().getSelectedIndex() == -1) {
            return;
        }
        String environmentCode = ((EnvironmentDetailDTO) comboEnvironments.getSelectionModel().getSelectedItem()).getCode();
        EndpointListTask task = new EndpointListTask(environmentCode);
        task.setOnSucceeded(event -> {
            mainProgressBar.setProgress(100);
            tableEndpoints.setDisable(false);
            endpointsList.clear();
            if (task.getValue() != null && task.getValue().getValue() != null) {
                endpointsList.addAll(task.getValue().getValue());
            }
        });
        task.setOnFailed(event -> {
            mainProgressBar.setProgress(0);
            Platform.runLater(() -> dialogError(task.getException().getMessage()));
        });
        notificationInfo("Endpoints", "Retrieving endpoints");
        mainProgressBar.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
        AbstractTask.startDaemon(task);
    }

    public void onRefreshEndpoints(ActionEvent actionEvent) {
        endpointsList.clear();
        onLoadEndpoints();
    }

    public void onScheduleMaintenance(ActionEvent actionEvent) {
        if ("Cancel maintenance mode".equals(btnScheduleMaintenance.getText())) {
            if (activeEnableFuture != null) activeEnableFuture.cancel(false);
            if (activeDisableFuture != null) activeDisableFuture.cancel(false);
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String msg = "[" + LocalDateTime.now().format(fmt) + "] Maintenance mode cancelled";
            App.LOG.info(msg);
            txtAreaConsole.appendText(msg + "\n");
            btnScheduleMaintenance.setText("Schedule maintenance mode");
            return;
        }
        if (tableEndpoints.getSelectionModel().getSelectedIndex() == -1) {
            dialogError("No endpoint selected");
            return;
        }
        LocalDate startDate = datePickerMaintenanceStart.getValue();
        LocalDate endDate = datePickerMaintenanceEnd.getValue();
        if (startDate == null || endDate == null) {
            dialogError("Start and end dates are required");
            return;
        }
        LocalDateTime startLdt = startDate.atTime(spinnerMaintenanceHour.getValue(), spinnerMaintenanceMinute.getValue());
        LocalDateTime endLdt = endDate.atTime(spinnerMaintenanceEndHour.getValue(), spinnerMaintenanceEndMinute.getValue());
        long nowMs = System.currentTimeMillis();
        long startMs = startLdt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long endMs = endLdt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        if (startMs < nowMs) {
            dialogError("Start time is in the past");
            return;
        }
        if (endMs <= startMs) {
            dialogError("End time must be after start time");
            return;
        }
        EndpointDetailDTO endpoint = (EndpointDetailDTO) tableEndpoints.getSelectionModel().getSelectedItem();
        String endpointCode = endpoint.getCode();
        String endpointName = endpoint.getName();
        String environmentCode = ((EnvironmentDetailDTO) comboEnvironments.getSelectionModel().getSelectedItem()).getCode();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String logScheduled = "[" + LocalDateTime.now().format(fmt) + "] Maintenance mode scheduled for \"" + endpointName
                + "\" — start: " + startLdt.format(fmt) + ", end: " + endLdt.format(fmt);
        App.LOG.info(logScheduled);
        txtAreaConsole.appendText(logScheduled + "\n");
        activeEnableFuture = maintenanceScheduler.schedule(() -> {
            EndpointSetMaintenanceModeTask enableTask = new EndpointSetMaintenanceModeTask(environmentCode, endpointCode, true);
            enableTask.setOnSucceeded(e -> {
                String msg = "[" + LocalDateTime.now().format(fmt) + "] Maintenance mode ENABLED on \"" + endpointName + "\"";
                App.LOG.info(msg);
                Platform.runLater(() -> {
                    txtAreaConsole.appendText(msg + "\n");
                    notificationInfo("Maintenance started", "Maintenance mode enabled on " + endpointName);
                    onLoadEndpoints();
                });
            });
            enableTask.setOnFailed(e -> {
                String msg = "[" + LocalDateTime.now().format(fmt) + "] Failed to enable maintenance mode on \"" + endpointName + "\": " + enableTask.getException().getMessage();
                App.LOG.error(msg);
                Platform.runLater(() -> {
                    txtAreaConsole.appendText(msg + "\n");
                    dialogError("Failed to enable maintenance mode: " + enableTask.getException().getMessage());
                });
            });
            AbstractTask.startDaemon(enableTask);
        }, startMs - nowMs, TimeUnit.MILLISECONDS);
        activeDisableFuture = maintenanceScheduler.schedule(() -> {
            EndpointSetMaintenanceModeTask disableTask = new EndpointSetMaintenanceModeTask(environmentCode, endpointCode, false);
            disableTask.setOnSucceeded(e -> {
                String msg = "[" + LocalDateTime.now().format(fmt) + "] Maintenance mode DISABLED on \"" + endpointName + "\"";
                App.LOG.info(msg);
                Platform.runLater(() -> {
                    txtAreaConsole.appendText(msg + "\n");
                    notificationInfo("Maintenance ended", "Maintenance mode disabled on " + endpointName);
                    onLoadEndpoints();
                    btnScheduleMaintenance.setText("Schedule maintenance mode");
                });
            });
            disableTask.setOnFailed(e -> {
                String msg = "[" + LocalDateTime.now().format(fmt) + "] Failed to disable maintenance mode on \"" + endpointName + "\": " + disableTask.getException().getMessage();
                App.LOG.error(msg);
                Platform.runLater(() -> {
                    txtAreaConsole.appendText(msg + "\n");
                    dialogError("Failed to disable maintenance mode: " + disableTask.getException().getMessage());
                    btnScheduleMaintenance.setText("Schedule maintenance mode");
                });
            });
            AbstractTask.startDaemon(disableTask);
        }, endMs - nowMs, TimeUnit.MILLISECONDS);
        btnScheduleMaintenance.setText("Cancel maintenance mode");
        dialogMaintenanceConfirm(endpointName, startLdt.format(fmt), endLdt.format(fmt));
    }

    public void shutdownScheduler() {
        maintenanceScheduler.shutdownNow();
    }

    private static final org.threeten.bp.format.DateTimeFormatter DT_FMT =
            org.threeten.bp.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private static String fmtDt(OffsetDateTime dt) {
        return dt == null ? "" : dt.atZoneSameInstant(org.threeten.bp.ZoneId.systemDefault()).format(DT_FMT);
    }

    private void checksForDeploymentSettings() throws Exception {
        if (comboEnvironments.getSelectionModel().getSelectedIndex() == -1) {
            throw new Exception("No environment selected");
        }
        if (comboDeploymentStrategies.getSelectionModel().getSelectedIndex() == -1) {
            throw new Exception("No deployment strategy selected");
        }
        if (comboDeploymentDatabaseUpdateMode.getSelectionModel().getSelectedIndex() == -1) {
            throw new Exception("No database update model selected");
        }
    }

    private CreateDeploymentRequestDTO createDeploymentRequestDTO() throws Exception {
        checksForDeploymentSettings();
        EnvironmentDetailDTO environment = (EnvironmentDetailDTO) comboEnvironments.getSelectionModel().getSelectedItem();
        CreateDeploymentRequestDTO.StrategyEnum deploymentStrategy = (CreateDeploymentRequestDTO.StrategyEnum) comboDeploymentStrategies.getSelectionModel().getSelectedItem();
        CreateDeploymentRequestDTO.DatabaseUpdateModeEnum updateMode = (CreateDeploymentRequestDTO.DatabaseUpdateModeEnum) comboDeploymentDatabaseUpdateMode.getSelectionModel().getSelectedItem();
        CreateDeploymentRequestDTO createDeploymentRequestDTO = new CreateDeploymentRequestDTO();
        createDeploymentRequestDTO.setStrategy(deploymentStrategy);
        createDeploymentRequestDTO.setDatabaseUpdateMode(updateMode);
        createDeploymentRequestDTO.setEnvironmentCode(environment.getCode());
        return createDeploymentRequestDTO;
    }

    private void showBuildAndDeployDialog(CreateBuildRequestDTO createBuildRequestDTO) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("progress.fxml"));
            AnchorPane page = loader.load();
            BuildController controller = loader.getController();
            controller.setCreateBuildRequestDTO(createBuildRequestDTO);
            controller.setDeploy(checkboxDeployAfterBuild.isSelected());
            if (checkboxDeployAfterBuild.isSelected()) {
                controller.setCreateDeploymentRequestDTO(createDeploymentRequestDTO());
            }
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.showAndWait();
        } catch (Exception ex) {
            App.LOG.error("Unable to load dialog - " + ex.getMessage());
        }
    }

    private void showDeployDialog(CreateDeploymentRequestDTO createDeploymentRequestDTO) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("progress.fxml"));
            AnchorPane page = loader.load();
            BuildController controller = loader.getController();
            controller.setCreateBuildRequestDTO(null);
            controller.setCreateDeploymentRequestDTO(createDeploymentRequestDTO);
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.showAndWait();
        } catch (Exception ex) {
            App.LOG.error("Unable to load dialog - " + ex.getMessage());
        }
    }

    private void showSettingsDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("settings.fxml"));
            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setTitle("Settings");
            dialogStage.setScene(scene);
            dialogStage.showAndWait();
        } catch (Exception ex) {
            App.LOG.error("Unable to load dialog - " + ex.getMessage());
        }
    }

    @FXML
    protected void onStartBuild() {
        if (StringUtils.isEmpty(txtBuildCode.getText())) {
            dialogError("No build code provided");
            return;
        }
        if (StringUtils.isBlank(txtGitBranches.getText())) {
            dialogError("No Git branch selected");
            return;
        }
        if (checkboxDeployAfterBuild.isSelected()) {
            try {
                checksForDeploymentSettings();
            } catch (Exception e) {
                dialogError(e.getMessage());
            }
        }
        CreateBuildRequestDTO createBuildRequestDTO = new CreateBuildRequestDTO();
        createBuildRequestDTO.setName(txtBuildCode.getText());
        createBuildRequestDTO.setBranch(txtGitBranches.getText());
        showBuildAndDeployDialog(createBuildRequestDTO);
    }

    private void onLoadLatestBuilds() {
        BuildListTask task = new BuildListTask();
        task.setOnSucceeded(event -> {
            mainProgressBar.setProgress(100);
            tableBuilds.setDisable(false);
            buildsList.addAll(Objects.requireNonNull(task.getValue().getValue()));
            btnProposeBuildName.setDisable(false);
        });
        task.setOnFailed(event -> {
            dialogError(task.getException().getMessage());
        });
        AbstractTask.startDaemon(task);
        notificationInfo("Builds", "Retrieving latest builds");
        mainProgressBar.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
    }

    private void onLoadLatestDeployments() {
        if (comboEnvironments.getSelectionModel().getSelectedIndex() == -1) {
            dialogError("No environment selected");
            return;
        }
        DeploymentListTask task = new DeploymentListTask(((EnvironmentDetailDTO) comboEnvironments.getSelectionModel().getSelectedItem()).getCode());
        task.setOnSucceeded(event -> {
            mainProgressBar.setProgress(100);
            tableDeployments.setDisable(false);
            deploymentsList.addAll(task.getValue().getValue());
        });
        task.setOnFailed(event -> {
            dialogError(task.getException().getMessage());
        });
        AbstractTask.startDaemon(task);
        notificationInfo("Deployments", "Retrieving latest deployments");
        mainProgressBar.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
    }

    private void onLoadEnvironments() {
        EnvironmentListTask task = new EnvironmentListTask();
        task.setOnSucceeded(event -> {
            comboEnvironments.setDisable(false);
            environmentsList.addAll(Objects.requireNonNull(task.getValue().getValue()));
            String environmentCode = App.getPreference(Constants.PREFS_ENVIRONMENT);
            if (environmentCode != null) {
                EnvironmentDetailDTO environmentDetailDTO = environmentsList.stream().filter(e -> e.getCode().equals(environmentCode)).findFirst().orElse(null);
                comboEnvironments.getSelectionModel().select(environmentDetailDTO);
            } else {
                comboEnvironments.getSelectionModel().select(0);
            }
            if (tabPane.getSelectionModel().getSelectedItem() != null
                    && "tabEndpoints".equals(tabPane.getSelectionModel().getSelectedItem().getId())) {
                onLoadEndpoints();
            }
        });
        task.setOnFailed(event -> {
            dialogError(task.getException().getMessage());
        });
        notificationInfo("Environments", "Retrieving available environments");
        AbstractTask.startDaemon(task);
    }

    public void onShowBuildDetails(ActionEvent actionEvent) {
        if (tableBuilds.getSelectionModel().getSelectedIndex() == -1) {
            dialogError("No build selected");
            return;
        }
        btnShowBuildDetails.setDisable(true);
        BuildDetailTask task = new BuildDetailTask(((BuildDetailDTO) tableBuilds.getSelectionModel().getSelectedItem()).getCode());
        ProgressDialog progressDialog = new ProgressDialog(task);
        progressDialog.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        progressDialog.getDialogPane().setMaxHeight(Region.USE_PREF_SIZE);
        progressDialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        progressDialog.setGraphic(null);
        task.setOnSucceeded(event -> {
            btnShowBuildDetails.setDisable(false);
            progressDialog.close();
            BuildDetailDTO b = task.getValue();
            String details = "Code:          " + b.getCode() + "\n"
                    + "Name:          " + b.getName() + "\n"
                    + "Branch:        " + b.getBranch() + "\n"
                    + "Status:        " + b.getStatus() + "\n"
                    + "Build version: " + b.getBuildVersion() + "\n"
                    + "App def ver:   " + b.getApplicationDefinitionVersion() + "\n"
                    + "Created by:    " + b.getCreatedBy() + "\n"
                    + "Start:         " + fmtDt(b.getBuildStartTimestamp()) + "\n"
                    + "End:           " + fmtDt(b.getBuildEndTimestamp()) + "\n"
                    + "Deployed:      " + b.isDeployed() + "\n"
                    + "Preview:       " + b.isIsPreview();
            dialogDetails("Build details", details);
        });
        task.setOnFailed(event -> {
            btnShowBuildDetails.setDisable(false);
            dialogError(task.getException().getMessage());
        });
        task.setOnCancelled(event -> btnStartBuild.setDisable(false));
        progressDialog.setOnCloseRequest(event -> task.cancel());
        AbstractTask.startDaemon(task);
        progressDialog.showAndWait();
    }

    public void onActionMenuAbout(ActionEvent actionEvent) {
        String version = System.getProperty("app.version");
        if (version == null) version = App.class.getPackage().getImplementationVersion();
        dialogInfo("Version " + (version != null ? version : "unknown"));
    }

    public void onSuggestNewBuildName(ActionEvent actionEvent) {
        if (buildsList.isEmpty()) {
            dialogError("No previous build found, cannot generate");
            return;
        }
        if (!StringUtils.isEmpty(txtBuildCode.getText())) {
            dialogInfo("Build code provided already (" + txtBuildCode.getText() + ")");
            return;
        }
        String latestBuildCode = buildsList.get(0).getCode();
        String todayDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String suggestedBuildName;
        int latestNum = 1;
        if (latestBuildCode != null && latestBuildCode.contains(todayDate)) {
            latestNum = Integer.parseInt(latestBuildCode.replace(todayDate + ".", "")) + 1;
        }
        suggestedBuildName = todayDate + "-" + latestNum;
        txtBuildCode.setText(suggestedBuildName);
    }

    public void onStartDeploy(ActionEvent actionEvent) throws Exception {
        if (tableBuilds.getSelectionModel().getSelectedIndex() == -1) {
            dialogError("No build selected");
            return;
        }
        BuildDetailDTO build = (BuildDetailDTO) tableBuilds.getSelectionModel().getSelectedItem();
        if (!"SUCCESS".equals(build.getStatus())) {
            dialogError("Build cannot be deployed (status " + build.getStatus() + ")");
            return;
        }
        CreateDeploymentRequestDTO createDeploymentRequestDTO = createDeploymentRequestDTO();
        createDeploymentRequestDTO.setBuildCode(build.getCode());
        showDeployDialog(createDeploymentRequestDTO);
    }

    public void onRefreshBuilds(ActionEvent actionEvent) {
        buildsList.clear();
        onLoadLatestBuilds();
    }

    public void onActionMenuQuit(ActionEvent actionEvent) {
        Window window = menuBar.getScene().getWindow();
        window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    public void onShowDeploymentDetails(ActionEvent actionEvent) {
        if (tableDeployments.getSelectionModel().getSelectedIndex() == -1) {
            dialogError("No deployment selected");
            return;
        }
        btnShowDeploymentDetails.setDisable(true);
        DeploymentDetailTask task = new DeploymentDetailTask(((DeploymentDetailDTO) tableDeployments.getSelectionModel().getSelectedItem()).getCode());
        ProgressDialog progressDialog = new ProgressDialog(task);
        progressDialog.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        progressDialog.getDialogPane().setMaxHeight(Region.USE_PREF_SIZE);
        progressDialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        progressDialog.setGraphic(null);
        task.setOnSucceeded(event -> {
            btnShowDeploymentDetails.setDisable(false);
            progressDialog.close();
            DeploymentDetailDTO d = task.getValue();
            String details = "Code:           " + d.getCode() + "\n"
                    + "Build:          " + d.getBuildCode() + "\n"
                    + "Environment:    " + d.getEnvironmentCode() + "\n"
                    + "Status:         " + d.getStatus() + "\n"
                    + "Strategy:       " + d.getStrategy() + "\n"
                    + "DB update mode: " + d.getDatabaseUpdateMode() + "\n"
                    + "Created by:     " + d.getCreatedBy() + "\n"
                    + "Scheduled:      " + fmtDt(d.getScheduledTimestamp()) + "\n"
                    + "Deployed:       " + fmtDt(d.getDeployedTimestamp()) + "\n"
                    + "Failed:         " + fmtDt(d.getFailedTimestamp()) + "\n"
                    + "Canceled by:    " + d.getCanceledBy() + "\n"
                    + "Canceled:       " + fmtDt(d.getCanceledTimestamp());
            dialogDetails("Deployment details", details);
        });
        task.setOnFailed(event -> {
            btnShowDeploymentDetails.setDisable(false);
            dialogError(task.getException().getMessage());
        });
        task.setOnCancelled(event -> btnShowDeploymentDetails.setDisable(false));
        progressDialog.setOnCloseRequest(event -> task.cancel());
        AbstractTask.startDaemon(task);
        progressDialog.showAndWait();
    }

    public void onRefreshDeployments(ActionEvent actionEvent) {
        deploymentsList.clear();
        onLoadLatestDeployments();
    }

    public void onShowEndpointDetails(ActionEvent actionEvent) {
        if (tableEndpoints.getSelectionModel().getSelectedIndex() == -1) {
            dialogError("No endpoint selected");
            return;
        }
        EndpointDetailDTO e = (EndpointDetailDTO) tableEndpoints.getSelectionModel().getSelectedItem();
        String details = "Code:             " + e.getCode() + "\n"
                + "Name:             " + e.getName() + "\n"
                + "URL:              " + e.getUrl() + "\n"
                + "Web proxy:        " + e.getWebProxy() + "\n"
                + "Service:          " + e.getService() + "\n"
                + "Maintenance mode: " + e.isMaintenanceMode();
        dialogDetails("Endpoint details", details);
    }

    public void onActionMenuSettings(ActionEvent actionEvent) {
        showSettingsDialog();
    }
}