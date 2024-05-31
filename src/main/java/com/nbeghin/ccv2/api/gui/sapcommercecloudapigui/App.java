package com.nbeghin.ccv2.api.gui.sapcommercecloudapigui;

import com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.utils.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.prefs.Preferences;

/**
 * @link <a href="https://stackoverflow.com/a/58498686/2378095">...</a>
 */
public class App {
    public static Logger LOG = LogManager.getLogger("sap-commerce-cloud-api-gui");

    public static void main(String[] args) {
        JavaFXApplication.main(args);
    }

    public static void savePreference(String key, boolean value) {
        try {
            if (!key.equals(Constants.PREFS_SUBSCRIPTION)) {
                key = Constants.SUBSCRIPTION_CODE + "." + key;
            }
            Preferences prefs = Preferences.userNodeForPackage(App.class);
            LOG.info("Storing preference " + key + "=" + value);
            prefs.putBoolean(key, value);
        } catch (Exception ex) {
            System.out.println("Unable to save preference " + key);
        }
    }

    public static void savePreference(String key, String value) {
        try {
            if (!key.equals(Constants.PREFS_SUBSCRIPTION)) {
                key = Constants.SUBSCRIPTION_CODE + "." + key;
            }
            Preferences prefs = Preferences.userNodeForPackage(App.class);
            LOG.info("Storing preference " + key + "=" + value);
            prefs.put(key, value);
        } catch (Exception ex) {
            System.out.println("Unable to save preference " + key);
        }
    }

    public static boolean getBooleanPreference(String key) {
        if (!key.equals(Constants.PREFS_SUBSCRIPTION)) {
            key = Constants.SUBSCRIPTION_CODE + "." + key;
        }
        Preferences prefs = Preferences.userNodeForPackage(App.class);
        return prefs.getBoolean(key, false);
    }

    public static String getPreference(String key) {
        if (!key.equals(Constants.PREFS_SUBSCRIPTION)) {
            key = Constants.SUBSCRIPTION_CODE + "." + key;
        }
        Preferences prefs = Preferences.userNodeForPackage(App.class);
        return prefs.get(key, null);
    }

}