module sap.commerce.cloud.management.api.gui.main {
    requires com.google.gson;
    requires okhttp3;
    requires transitive org.apache.oltu.oauth2.client;
    requires retrofit2;
    requires retrofit2.converter.gson;
    requires retrofit2.converter.scalars;
    requires gson.fire;
    requires java.sql;
    requires com.fasterxml.jackson.databind;
    requires javafx.graphics;
    requires javafx.fxml;
    requires java.desktop;
    requires org.controlsfx.controls;
    requires java.datatransfer;
    requires java.prefs;
    requires org.apache.commons.lang3;
    requires org.slf4j;
    exports com.nbeghin.ccv2.api.gui.sapcommercecloudapigui;
    opens com.nbeghin.ccv2.api.gui.sapcommercecloudapigui to javafx.fxml;

}