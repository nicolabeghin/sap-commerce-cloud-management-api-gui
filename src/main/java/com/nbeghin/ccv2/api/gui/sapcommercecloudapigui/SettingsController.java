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
import org.controlsfx.glyphfont.FontAwesome;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController extends AbstractController implements Initializable {

    @FXML
    private Button btnInfoAccessToken;
    @FXML
    private Button btnInfoSubscriptionCode;
    @FXML
    private PasswordField txtAccessToken;
    @FXML
    private TextField txtSubscriptionCode;
    @FXML
    private CheckBox checkboxDebugEnabled;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtAccessToken.setText(Constants.ACCESS_TOKEN);
        txtSubscriptionCode.setText(Constants.SUBSCRIPTION_CODE);
        checkboxDebugEnabled.setSelected(Constants.DEBUG_ENABLED);
        btnInfoSubscriptionCode.setGraphic(fontAwesome.create(FontAwesome.Glyph.INFO));
        btnInfoAccessToken.setGraphic(fontAwesome.create(FontAwesome.Glyph.INFO));
    }

    public void onSaveSettings(ActionEvent actionEvent) {
        if (StringUtils.isBlank(txtSubscriptionCode.getText())) {
            dialogError("Missing CCV2 subscription code");
            return;
        }
        if (StringUtils.isBlank(txtAccessToken.getText())) {
            dialogError("Missing CCV2 API token");
            return;
        }
        Constants.SUBSCRIPTION_CODE = txtSubscriptionCode.getText();
        Constants.ACCESS_TOKEN = txtAccessToken.getText();
        Constants.DEBUG_ENABLED = checkboxDebugEnabled.isSelected();
        App.savePreference(Constants.PREFS_SUBSCRIPTION, Constants.SUBSCRIPTION_CODE);
        App.savePreference(Constants.PREFS_ACCESS_TOKEN, Constants.ACCESS_TOKEN);
        App.savePreference(Constants.PREFS_DEBUG_ENABLED, Constants.DEBUG_ENABLED);
        Window window = txtAccessToken.getScene().getWindow();
        window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    public void onHelpAccessToken(ActionEvent actionEvent) {
        openWebpage(Constants.URL_CCV2_ACCESS_TOKEN);
    }

    public void onHelpSubscriptionCode(ActionEvent actionEvent) {
        openWebpage(Constants.URL_CCV2_SUBSCRIPTION_CODE);
    }
}

