package com.nbeghin.ccv2.api.gui.sapcommercecloudapigui;

import com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.utils.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import org.apache.commons.lang3.StringUtils;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController extends AbstractController implements Initializable {

    @FXML
    private TextField txtClientId;
    @FXML
    private PasswordField txtClientSecret;
    @FXML
    private TextField txtSubscriptionCode;
    @FXML
    private CheckBox checkboxDebugEnabled;
    @FXML
    private Button btnInfoSubscriptionCode;
    @FXML
    private Button btnInfoClientCredentials;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtClientId.setText(Constants.CLIENT_ID);
        txtClientSecret.setText(Constants.CLIENT_SECRET);
        txtSubscriptionCode.setText(Constants.SUBSCRIPTION_CODE);
        checkboxDebugEnabled.setSelected(Constants.DEBUG_ENABLED);
        btnInfoSubscriptionCode.setGraphic(fontAwesome.create(org.controlsfx.glyphfont.FontAwesome.Glyph.INFO));
        btnInfoClientCredentials.setGraphic(fontAwesome.create(org.controlsfx.glyphfont.FontAwesome.Glyph.INFO));
    }

    public void onSaveSettings(ActionEvent actionEvent) {
        if (StringUtils.isBlank(txtSubscriptionCode.getText())) {
            dialogError("Missing CCV2 subscription code");
            return;
        }
        if (StringUtils.isBlank(txtClientId.getText())) {
            dialogError("Missing OAuth2 client ID");
            return;
        }
        if (StringUtils.isBlank(txtClientSecret.getText())) {
            dialogError("Missing OAuth2 client secret");
            return;
        }
        Constants.SUBSCRIPTION_CODE = txtSubscriptionCode.getText();
        Constants.CLIENT_ID = txtClientId.getText();
        Constants.CLIENT_SECRET = txtClientSecret.getText();
        Constants.DEBUG_ENABLED = checkboxDebugEnabled.isSelected();
        App.savePreference(Constants.PREFS_SUBSCRIPTION, Constants.SUBSCRIPTION_CODE);
        App.savePreference(Constants.PREFS_CLIENT_ID, Constants.CLIENT_ID);
        App.savePreference(Constants.PREFS_CLIENT_SECRET, Constants.CLIENT_SECRET);
        App.savePreference(Constants.PREFS_DEBUG_ENABLED, Constants.DEBUG_ENABLED);
        Window window = txtClientId.getScene().getWindow();
        window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    public void onCancelSettings(ActionEvent actionEvent) {
        Window window = txtClientId.getScene().getWindow();
        window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    public void onHelpSubscriptionCode(ActionEvent actionEvent) {
        openWebpage(Constants.URL_CCV2_SUBSCRIPTION_CODE);
    }

    public void onHelpClientCredentials(ActionEvent actionEvent) {
        openWebpage(Constants.URL_CCV2_CLIENT_CREDENTIALS);
    }
}
