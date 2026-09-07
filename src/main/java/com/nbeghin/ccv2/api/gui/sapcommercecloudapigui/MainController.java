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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.Optional;
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
    private ScheduledFuture<?>[] pendingFor(String endpointCode) {
        return scheduledMaintenance.computeIfAbsent(endpointCode, k -> new ScheduledFuture<?>[2]);
    }

    // Pending maintenance windows keyed by endpoint code. Each entry holds the
    // [enableFuture, disableFuture] pair so a window can be cancelled per endpoint
    // even after the user selects a different endpoint.
    private final java.util.Map<String, ScheduledFuture<?>[]> scheduledMaintenance = new java.util.concurrent.ConcurrentHashMap<>();

    private boolean hasPendingSchedule(String endpointCode) {
        ScheduledFuture<?>[] futures = scheduledMaintenance.get(endpointCode);
        if (futures == null) return false;
        boolean pending = (futures[0] != null && !futures[0].isDone()) || (futures[1] != null && !futures[1].isDone());
        if (!pending) {
            scheduledMaintenance.remove(endpointCode);
        }
        return pending;
    }
    private static Stage primaryStage;
    @FXML
    public TabPane tabPane;
    @FXML
    private TableView tableDeployments;
    @FXML
    private CheckBox checkboxDeployAfterBuild;
    @FXML
    private Button btnRefresh;
    @FXML
    private TextArea txtAreaConsole;
    @FXML
    private Button btnStartBuild;
    @FXML
    private ProgressBar mainProgressBar;
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
            } else {
                checkboxDeployAfterBuild.setDisable(true);
                btnStartBuild.setDisable(true);
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
                boolean refreshable = "tabExistingBuild".equals(tabId) || "tabDeployments".equals(tabId) || "tabEndpoints".equals(tabId);
                btnRefresh.setDisable(!refreshable);
            }
        });
        btnRefresh.setDisable(false);
        btnRefresh.setGraphic(fontAwesome.create(FontAwesome.Glyph.REFRESH));
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
                btnStartDeploy.setDisable(false);
                comboDeploymentStrategies.setDisable(false);
                comboDeploymentDatabaseUpdateMode.setDisable(false);
            }
        });
        tableBuilds.setRowFactory(tv -> {
            TableRow<BuildDetailDTO> row = new TableRow<>();
            row.setTooltip(new Tooltip("Double-click to show build details"));
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    onShowBuildDetails(null);
                }
            });
            return row;
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
        tableDeployments.setRowFactory(tv -> {
            TableRow<DeploymentDetailDTO> row = new TableRow<>();
            row.setTooltip(new Tooltip("Double-click to show deployment details"));
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    onShowDeploymentDetails(null);
                }
            });
            return row;
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
                Runnable updateColor = () -> link.setStyle(isSelected() ? "-fx-text-fill: white;" : "-fx-text-fill: #1a73e8;");
                updateColor.run();
                selectedProperty().addListener((obs, wasSelected, nowSelected) -> updateColor.run());
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
            btnScheduleMaintenance.setDisable(!selected);
            if (selected) {
                EndpointDetailDTO ep = (EndpointDetailDTO) newValue;
                boolean inMaintenance = Boolean.TRUE.equals(ep.isMaintenanceMode());
                boolean pending = hasPendingSchedule(ep.getCode());
                boolean lockInputs = inMaintenance || pending;
                datePickerMaintenanceStart.setDisable(lockInputs);
                spinnerMaintenanceHour.setDisable(lockInputs);
                spinnerMaintenanceMinute.setDisable(lockInputs);
                datePickerMaintenanceEnd.setDisable(lockInputs);
                spinnerMaintenanceEndHour.setDisable(lockInputs);
                spinnerMaintenanceEndMinute.setDisable(lockInputs);
                if (pending) {
                    setMaintenanceButtonState("Cancel maintenance mode");
                } else {
                    setMaintenanceButtonState(inMaintenance ? "Disable maintenance mode" : "Schedule maintenance mode");
                }
            } else {
                datePickerMaintenanceStart.setDisable(true);
                spinnerMaintenanceHour.setDisable(true);
                spinnerMaintenanceMinute.setDisable(true);
                datePickerMaintenanceEnd.setDisable(true);
                spinnerMaintenanceEndHour.setDisable(true);
                spinnerMaintenanceEndMinute.setDisable(true);
                setMaintenanceButtonState("Schedule maintenance mode");
            }
        });
        tableEndpoints.setRowFactory(tv -> {
            TableRow<EndpointDetailDTO> row = new TableRow<>();
            row.setTooltip(new Tooltip("Double-click to show endpoint details"));
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    onShowEndpointDetails(null);
                }
            });
            return row;
        });
    }

    private void onLoadEndpoints() {
        if (comboEnvironments.getSelectionModel().getSelectedIndex() == -1) {
            return;
        }
        String environmentCode = ((EnvironmentDetailDTO) comboEnvironments.getSelectionModel().getSelectedItem()).getCode();
        EndpointListTask task = new EndpointListTask(environmentCode);
        task.setOnSucceeded(event -> {
            mainProgressBar.setProgress(1.0);
            tableEndpoints.setDisable(false);
            endpointsList.clear();
            if (task.getValue() != null && task.getValue().getValue() != null) {
                endpointsList.addAll(task.getValue().getValue());
            }
            Platform.runLater(() -> txtAreaConsole.appendText(logMsg("Endpoints loaded (" + endpointsList.size() + ")") + "\n"));
        });
        task.setOnFailed(event -> {
            mainProgressBar.setProgress(0);
            Platform.runLater(() -> dialogError(task.getException().getMessage()));
        });
        txtAreaConsole.appendText(logMsg("Loading endpoints...") + "\n");
        mainProgressBar.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
        AbstractTask.startDaemon(task);
    }

    public void onRefreshEndpoints(ActionEvent actionEvent) {
        endpointsList.clear();
        onLoadEndpoints();
    }

    public void onScheduleMaintenance(ActionEvent actionEvent) {
        if ("Cancel maintenance mode".equals(btnScheduleMaintenance.getText())) {
            EndpointDetailDTO selectedEndpoint = (EndpointDetailDTO) tableEndpoints.getSelectionModel().getSelectedItem();
            if (selectedEndpoint == null) {
                return;
            }
            ScheduledFuture<?>[] futures = scheduledMaintenance.remove(selectedEndpoint.getCode());
            if (futures != null) {
                if (futures[0] != null) futures[0].cancel(false);
                if (futures[1] != null) futures[1].cancel(false);
            }
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String msg = "[" + LocalDateTime.now().format(fmt) + "] Maintenance mode cancelled for \"" + selectedEndpoint.getName() + "\"";
            App.LOG.info(msg);
            txtAreaConsole.appendText(msg + "\n");
            datePickerMaintenanceStart.setDisable(false);
            spinnerMaintenanceHour.setDisable(false);
            spinnerMaintenanceMinute.setDisable(false);
            datePickerMaintenanceEnd.setDisable(false);
            spinnerMaintenanceEndHour.setDisable(false);
            spinnerMaintenanceEndMinute.setDisable(false);
            setMaintenanceButtonState("Schedule maintenance mode");
            return;
        }
        if ("Disable maintenance mode".equals(btnScheduleMaintenance.getText())) {
            EndpointDetailDTO endpoint = (EndpointDetailDTO) tableEndpoints.getSelectionModel().getSelectedItem();
            String endpointCode = endpoint.getCode();
            String endpointName = endpoint.getName();
            String environmentCode = ((EnvironmentDetailDTO) comboEnvironments.getSelectionModel().getSelectedItem()).getCode();
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String startMsg = "[" + LocalDateTime.now().format(fmt) + "] Calling API to disable maintenance mode on \"" + endpointName + "\"...";
            App.LOG.info(startMsg);
            txtAreaConsole.appendText(startMsg + "\n");
            EndpointSetMaintenanceModeTask disableTask = new EndpointSetMaintenanceModeTask(environmentCode, endpointCode, false);
            disableTask.setOnSucceeded(e -> {
                String msg = "[" + LocalDateTime.now().format(fmt) + "] Maintenance mode DISABLED on \"" + endpointName + "\"";
                App.LOG.info(msg);
                Platform.runLater(() -> {
                    txtAreaConsole.appendText(msg + "\n");
                    notificationInfo("Maintenance ended", "Maintenance mode disabled on " + endpointName);
                    onLoadEndpoints();
                });
            });
            disableTask.setOnFailed(e -> {
                String msg = "[" + LocalDateTime.now().format(fmt) + "] Failed to disable maintenance mode on \"" + endpointName + "\": " + disableTask.getException().getMessage();
                App.LOG.error(msg);
                Platform.runLater(() -> {
                    txtAreaConsole.appendText(msg + "\n");
                    dialogError("Failed to disable maintenance mode: " + disableTask.getException().getMessage());
                });
            });
            AbstractTask.startDaemon(disableTask);
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
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        Optional<ButtonType> confirm = dialogMaintenanceConfirm(endpointName, startLdt.format(fmt), endLdt.format(fmt));
        if (!confirm.isPresent() || confirm.get() != ButtonType.OK) {
            return;
        }

        String logScheduled = "[" + LocalDateTime.now().format(fmt) + "] Maintenance mode scheduled for \"" + endpointName
                + "\" — start: " + startLdt.format(fmt) + ", end: " + endLdt.format(fmt);
        App.LOG.info(logScheduled);
        txtAreaConsole.appendText(logScheduled + "\n");
        ScheduledFuture<?>[] futures = pendingFor(endpointCode);
        futures[0] = maintenanceScheduler.schedule(() -> {
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
                // Enable never happened, so the paired disable is pointless — cancel it and clear the window.
                ScheduledFuture<?>[] pending = scheduledMaintenance.remove(endpointCode);
                if (pending != null && pending[1] != null) pending[1].cancel(false);
                Platform.runLater(() -> {
                    txtAreaConsole.appendText(msg + "\n");
                    dialogError("Failed to enable maintenance mode: " + enableTask.getException().getMessage());
                    onLoadEndpoints();
                });
            });
            String startMsg = "[" + LocalDateTime.now().format(fmt) + "] Calling API to enable maintenance mode on \"" + endpointName + "\"...";
            App.LOG.info(startMsg);
            Platform.runLater(() -> txtAreaConsole.appendText(startMsg + "\n"));
            AbstractTask.startDaemon(enableTask);
        }, startMs - nowMs, TimeUnit.MILLISECONDS);
        futures[1] = maintenanceScheduler.schedule(() -> {
            EndpointSetMaintenanceModeTask disableTask = new EndpointSetMaintenanceModeTask(environmentCode, endpointCode, false);
            disableTask.setOnSucceeded(e -> {
                String msg = "[" + LocalDateTime.now().format(fmt) + "] Maintenance mode DISABLED on \"" + endpointName + "\"";
                App.LOG.info(msg);
                scheduledMaintenance.remove(endpointCode);
                Platform.runLater(() -> {
                    txtAreaConsole.appendText(msg + "\n");
                    notificationInfo("Maintenance ended", "Maintenance mode disabled on " + endpointName);
                    onLoadEndpoints();
                });
            });
            disableTask.setOnFailed(e -> {
                String msg = "[" + LocalDateTime.now().format(fmt) + "] Failed to disable maintenance mode on \"" + endpointName + "\": " + disableTask.getException().getMessage();
                App.LOG.error(msg);
                // Enable succeeded but disable failed: the endpoint is stuck in maintenance.
                // Clear the window and refresh so the user can retry via the "Disable" button.
                scheduledMaintenance.remove(endpointCode);
                Platform.runLater(() -> {
                    txtAreaConsole.appendText(msg + "\n");
                    dialogError("Failed to disable maintenance mode — endpoint \"" + endpointName + "\" is still in maintenance. "
                            + "Select it and use \"Disable maintenance mode\" to retry.\n\n" + disableTask.getException().getMessage());
                    onLoadEndpoints();
                });
            });
            String endMsg = "[" + LocalDateTime.now().format(fmt) + "] Calling API to disable maintenance mode on \"" + endpointName + "\"...";
            App.LOG.info(endMsg);
            Platform.runLater(() -> txtAreaConsole.appendText(endMsg + "\n"));
            AbstractTask.startDaemon(disableTask);
        }, endMs - nowMs, TimeUnit.MILLISECONDS);
        datePickerMaintenanceStart.setDisable(true);
        spinnerMaintenanceHour.setDisable(true);
        spinnerMaintenanceMinute.setDisable(true);
        datePickerMaintenanceEnd.setDisable(true);
        spinnerMaintenanceEndHour.setDisable(true);
        spinnerMaintenanceEndMinute.setDisable(true);
        setMaintenanceButtonState("Cancel maintenance mode");
    }

    public void shutdownScheduler() {
        maintenanceScheduler.shutdownNow();
    }

    /**
     * Returns true if there is at least one maintenance window still pending
     * (an enable or disable call not yet made). These are held in-memory only and
     * are lost when the app exits.
     */
    public boolean hasPendingMaintenance() {
        return scheduledMaintenance.keySet().stream().anyMatch(this::hasPendingSchedule);
    }

    private static final org.threeten.bp.format.DateTimeFormatter DT_FMT =
            org.threeten.bp.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private static String fmtDt(OffsetDateTime dt) {
        return dt == null ? "" : dt.atZoneSameInstant(org.threeten.bp.ZoneId.systemDefault()).format(DT_FMT);
    }

    private static String logMsg(String msg) {
        return "[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "] " + msg;
    }

    private void setMaintenanceButtonState(String text) {
        btnScheduleMaintenance.setText(text);
        if ("Schedule maintenance mode".equals(text)) {
            btnScheduleMaintenance.setStyle("-fx-base: #f0ad4e; -fx-text-fill: #000;");
        } else {
            btnScheduleMaintenance.setStyle("");
        }
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
                return;
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
            mainProgressBar.setProgress(1.0);
            tableBuilds.setDisable(false);
            buildsList.clear();
            if (task.getValue() != null && task.getValue().getValue() != null) {
                buildsList.addAll(task.getValue().getValue());
            }
            btnProposeBuildName.setDisable(false);
            Platform.runLater(() -> txtAreaConsole.appendText(logMsg("Builds loaded (" + buildsList.size() + ")") + "\n"));
        });
        task.setOnFailed(event -> {
            dialogError(task.getException().getMessage());
        });
        AbstractTask.startDaemon(task);
        txtAreaConsole.appendText(logMsg("Loading builds...") + "\n");
        mainProgressBar.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
    }

    private void onLoadLatestDeployments() {
        if (comboEnvironments.getSelectionModel().getSelectedIndex() == -1) {
            dialogError("No environment selected");
            return;
        }
        DeploymentListTask task = new DeploymentListTask(((EnvironmentDetailDTO) comboEnvironments.getSelectionModel().getSelectedItem()).getCode());
        task.setOnSucceeded(event -> {
            mainProgressBar.setProgress(1.0);
            tableDeployments.setDisable(false);
            deploymentsList.clear();
            if (task.getValue() != null && task.getValue().getValue() != null) {
                deploymentsList.addAll(task.getValue().getValue());
            }
            Platform.runLater(() -> txtAreaConsole.appendText(logMsg("Deployments loaded (" + deploymentsList.size() + ")") + "\n"));
        });
        task.setOnFailed(event -> {
            dialogError(task.getException().getMessage());
        });
        AbstractTask.startDaemon(task);
        txtAreaConsole.appendText(logMsg("Loading deployments...") + "\n");
        mainProgressBar.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
    }

    private void onLoadEnvironments() {
        EnvironmentListTask task = new EnvironmentListTask();
        task.setOnSucceeded(event -> {
            comboEnvironments.setDisable(false);
            environmentsList.clear();
            if (task.getValue() != null && task.getValue().getValue() != null) {
                environmentsList.addAll(task.getValue().getValue());
            }
            if (environmentsList.isEmpty()) {
                Platform.runLater(() -> txtAreaConsole.appendText(logMsg("No environments found") + "\n"));
                return;
            }
            String environmentCode = App.getPreference(Constants.PREFS_ENVIRONMENT);
            if (environmentCode != null) {
                EnvironmentDetailDTO environmentDetailDTO = environmentsList.stream().filter(e -> e.getCode().equals(environmentCode)).findFirst().orElse(null);
                comboEnvironments.getSelectionModel().select(environmentDetailDTO);
            } else {
                comboEnvironments.getSelectionModel().select(0);
            }
            Platform.runLater(() -> txtAreaConsole.appendText(logMsg("Environments loaded (" + environmentsList.size() + ")") + "\n"));
            if (tabPane.getSelectionModel().getSelectedItem() != null
                    && "tabEndpoints".equals(tabPane.getSelectionModel().getSelectedItem().getId())) {
                onLoadEndpoints();
            }
        });
        task.setOnFailed(event -> {
            dialogError(task.getException().getMessage());
        });
        txtAreaConsole.appendText(logMsg("Loading environments...") + "\n");
        AbstractTask.startDaemon(task);
    }

    public void onShowBuildDetails(ActionEvent actionEvent) {
        if (tableBuilds.getSelectionModel().getSelectedIndex() == -1) {
            dialogError("No build selected");
            return;
        }
        BuildDetailTask task = new BuildDetailTask(((BuildDetailDTO) tableBuilds.getSelectionModel().getSelectedItem()).getCode());
        ProgressDialog progressDialog = new ProgressDialog(task);
        progressDialog.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        progressDialog.getDialogPane().setMaxHeight(Region.USE_PREF_SIZE);
        progressDialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        progressDialog.setGraphic(null);
        task.setOnSucceeded(event -> {
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
            dialogError(task.getException().getMessage());
        });
        progressDialog.setOnCloseRequest(event -> task.cancel());
        AbstractTask.startDaemon(task);
        progressDialog.showAndWait();
    }

    public void onActionMenuAbout(ActionEvent actionEvent) {
        String version = System.getProperty("app.version");
        if (version == null) version = App.class.getPackage().getImplementationVersion();
        String disclaimer =
            "Version " + (version != null ? version : "unknown") + "\n\n" +
            "This is an unofficial, community-driven project and is not affiliated with, " +
            "endorsed by, or supported by SAP SE or its affiliates.\n\n" +
            "This software is provided \"as is\", without warranty of any kind, express or " +
            "implied, including but not limited to the warranties of merchantability, fitness " +
            "for a particular purpose and noninfringement. In no event shall the authors or " +
            "copyright holders be liable for any claim, damages or other liability, whether " +
            "in an action of contract, tort or otherwise, arising from, out of or in " +
            "connection with the software or the use or other dealings in the software.\n\n" +
            "Use this tool at your own risk. The authors assume no responsibility for any " +
            "issues that may arise from its use.";
        dialogDetails("About", disclaimer);
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
        // Only bump the counter when the latest build code matches our own
        // "yyyyMMdd.N" scheme; other naming schemes (that merely contain today's
        // date) are left alone rather than risking a parse failure.
        if (latestBuildCode != null && latestBuildCode.startsWith(todayDate + ".")) {
            String suffix = latestBuildCode.substring((todayDate + ".").length());
            try {
                latestNum = Integer.parseInt(suffix) + 1;
            } catch (NumberFormatException ex) {
                latestNum = 1;
            }
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

    @FXML
    public void onRefresh(ActionEvent actionEvent) {
        Tab selected = tabPane.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        switch (selected.getId()) {
            case "tabExistingBuild": onRefreshBuilds(actionEvent); break;
            case "tabDeployments":   onRefreshDeployments(actionEvent); break;
            case "tabEndpoints":     onRefreshEndpoints(actionEvent); break;
        }
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
        DeploymentDetailTask task = new DeploymentDetailTask(((DeploymentDetailDTO) tableDeployments.getSelectionModel().getSelectedItem()).getCode());
        ProgressDialog progressDialog = new ProgressDialog(task);
        progressDialog.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        progressDialog.getDialogPane().setMaxHeight(Region.USE_PREF_SIZE);
        progressDialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        progressDialog.setGraphic(null);
        task.setOnSucceeded(event -> {
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
            dialogError(task.getException().getMessage());
        });
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

    @FXML
    public void onActionMenuClearConsole(ActionEvent actionEvent) {
        txtAreaConsole.clear();
    }
}