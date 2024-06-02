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
import org.controlsfx.dialog.ProgressDialog;
import org.controlsfx.glyphfont.FontAwesome;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainController extends AbstractController implements Initializable {
    private static final ObservableList<BuildDetailDTO> buildsList = FXCollections.observableArrayList();
    private static final ObservableList<DeploymentDetailDTO> deploymentsList = FXCollections.observableArrayList();
    private static final ObservableList<EnvironmentDetailDTO> environmentsList = FXCollections.observableArrayList(); // @TODO
    private static final ObservableList<String> gitBranches = FXCollections.observableArrayList("develop", "master", "production");
    private static final ObservableList<CreateDeploymentRequestDTO.DatabaseUpdateModeEnum> deploymentDatabaseUpdateModes = FXCollections.observableArrayList(CreateDeploymentRequestDTO.DatabaseUpdateModeEnum.values());
    private static final ObservableList<CreateDeploymentRequestDTO.StrategyEnum> deploymentStrategies = FXCollections.observableArrayList(CreateDeploymentRequestDTO.StrategyEnum.values());
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
    private ComboBox comboGitBranches;
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

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        MainController.primaryStage = primaryStage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        comboGitBranches.setItems(gitBranches);
        comboEnvironments.setItems(environmentsList);
        tableDeployments.setItems(deploymentsList);
        comboDeploymentDatabaseUpdateMode.setItems(deploymentDatabaseUpdateModes);
        comboDeploymentStrategies.setItems(deploymentStrategies);
        comboDeploymentStrategies.getSelectionModel().select(CreateDeploymentRequestDTO.StrategyEnum.ROLLING_UPDATE);
        comboDeploymentDatabaseUpdateMode.getSelectionModel().select(CreateDeploymentRequestDTO.DatabaseUpdateModeEnum.NONE);
        initializeBuildsTable();
        initializeDeploymentsTable();
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
        });
        btnRefreshBuilds.setGraphic(fontAwesome.create(FontAwesome.Glyph.REFRESH));
        btnRefreshDeployments.setGraphic(fontAwesome.create(FontAwesome.Glyph.REFRESH));
        btnShowBuildDetails.setGraphic(fontAwesome.create(FontAwesome.Glyph.INFO));
        btnShowDeploymentDetails.setGraphic(fontAwesome.create(FontAwesome.Glyph.INFO));
        btnStartBuild.setGraphic(fontAwesome.create(FontAwesome.Glyph.BUILDING));
        btnStartDeploy.setGraphic(fontAwesome.create(FontAwesome.Glyph.CLOUD_UPLOAD));
        btnProposeBuildName.setGraphic(fontAwesome.create(FontAwesome.Glyph.LIGHTBULB_ALT));
        restorePreferences();
        bindPreferences();
        Platform.runLater(this::onLoadEnvironments);
        Platform.runLater(this::onLoadLatestBuilds);
    }

    private void bindPreferences() {
        comboGitBranches.valueProperty().addListener((observable, oldValue, newValue) -> { // backup is selected
            if (newValue != null && newValue != oldValue) {
                App.savePreference(Constants.PREFS_GIT_BRANCH, (String) newValue);
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
        String accessToken = App.getPreference(Constants.PREFS_ACCESS_TOKEN);
        if (StringUtils.isNotBlank(accessToken)) {
            Constants.ACCESS_TOKEN = accessToken;
        }
        if (StringUtils.isBlank(Constants.SUBSCRIPTION_CODE) || StringUtils.isBlank(Constants.ACCESS_TOKEN)) {
            showSettingsDialog();
        }

        String gitBranch = App.getPreference(Constants.PREFS_GIT_BRANCH);
        if (gitBranch != null) {
            comboGitBranches.getSelectionModel().select(gitBranch);
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
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn statusCol = new TableColumn("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        TableColumn buildStartTimestampCol = new TableColumn("Start");
        buildStartTimestampCol.setCellValueFactory(new PropertyValueFactory<>("buildStartTimestamp"));
        buildStartTimestampCol.setPrefWidth(150);
        tableBuilds.getColumns().addAll(nameCol, lastNameCol, statusCol, buildStartTimestampCol);
        tableBuilds.setItems(buildsList);
        tableBuilds.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> { // backup is selected
            if (newValue != null && newValue != oldValue) {
                btnShowBuildDetails.setDisable(false);
                btnStartDeploy.setDisable(false);
            }
        });
    }

    private void initializeDeploymentsTable() {
        TableColumn nameCol = new TableColumn("Env");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("environmentCode"));
        nameCol.setPrefWidth(20);
        TableColumn buildCol = new TableColumn("Build");
        buildCol.setCellValueFactory(new PropertyValueFactory<>("buildCode"));
        buildCol.setPrefWidth(90);
        TableColumn lastNameCol = new TableColumn("DB");
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("databaseUpdateMode"));
        TableColumn strategyCol = new TableColumn("Strategy");
        strategyCol.setCellValueFactory(new PropertyValueFactory<>("strategy"));
        TableColumn statusCol = new TableColumn("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        TableColumn buildStartTimestampCol = new TableColumn("Start");
        buildStartTimestampCol.setCellValueFactory(new PropertyValueFactory<>("createdTimestamp"));
        buildStartTimestampCol.setPrefWidth(150);
        tableDeployments.getColumns().addAll(nameCol, buildCol, lastNameCol, strategyCol, statusCol, buildStartTimestampCol);
        tableDeployments.setItems(deploymentsList);
        tableDeployments.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> { // backup is selected
            if (newValue != null && newValue != oldValue) {
                btnShowDeploymentDetails.setDisable(false);
            }
        });
    }

    private void checksForDeploymentSettings() throws Exception{
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
        if (comboGitBranches.getSelectionModel().getSelectedIndex() == -1) {
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
        createBuildRequestDTO.setBranch(comboGitBranches.getSelectionModel().getSelectedItem().toString());
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
        new Thread(task).start();
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
        new Thread(task).start();
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
        });
        task.setOnFailed(event -> {
            dialogError(task.getException().getMessage());
        });
        notificationInfo("Environments", "Retrieving available environments");
        new Thread(task).start();
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
            BuildDetailDTO extractionResult = task.getValue();
            txtAreaConsole.setText(extractionResult.toString());
        });
        task.setOnFailed(event -> {
            btnShowBuildDetails.setDisable(false);
            dialogError(task.getException().getMessage());
        });
        task.setOnCancelled(event -> btnStartBuild.setDisable(false));
        progressDialog.setOnCloseRequest(event -> task.cancel());
        new Thread(task).start();
        progressDialog.showAndWait();
    }

    public void onActionMenuAbout(ActionEvent actionEvent) {
        dialogInfo("Version 1.0.0");
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
            DeploymentDetailDTO deploymentDetailDTO = task.getValue();
            txtAreaConsole.setText(deploymentDetailDTO.toString());
        });
        task.setOnFailed(event -> {
            btnShowDeploymentDetails.setDisable(false);
            dialogError(task.getException().getMessage());
        });
        task.setOnCancelled(event -> btnShowDeploymentDetails.setDisable(false));
        progressDialog.setOnCloseRequest(event -> task.cancel());
        new Thread(task).start();
        progressDialog.showAndWait();
    }

    public void onRefreshDeployments(ActionEvent actionEvent) {
        deploymentsList.clear();
        onLoadLatestDeployments();
    }

    public void onActionMenuSettings(ActionEvent actionEvent) {
        showSettingsDialog();
    }
}