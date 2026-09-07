package com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.utils;

/**
 * Application-wide constants and runtime configuration.
 *
 * <p>The first three fields ({@link #SUBSCRIPTION_CODE}, {@link #CLIENT_ID},
 * {@link #CLIENT_SECRET}) and {@link #DEBUG_ENABLED} are mutable: they are populated
 * at startup from stored {@link java.util.prefs.Preferences} and updated from the
 * settings dialog. The {@code PREFS_*} strings are preference keys; the {@code URL_*}
 * strings point to the relevant SAP help pages linked from the settings dialog.
 */
public class Constants {
    public static String SUBSCRIPTION_CODE;
    public static String CLIENT_ID;
    public static String CLIENT_SECRET;
    public static boolean DEBUG_ENABLED = false;
    /** Base URL of the SAP Commerce Cloud Management API (v2). */
    public static final String BASE_PATH = "https://portalapi.commerce.ondemand.com/v2/";
    /** Upper bound on how many builds the build list fetch requests. */
    public static final int MAX_NUM_BUILDS = 8;
    public static final String PREFS_GIT_BRANCH = "gitBranch";
    public static final String PREFS_ENVIRONMENT = "targetEnvironment";
    public static final String PREFS_DEPLOYMENT_STRATEGY = "deploymentStrategy";
    public static final String PREFS_DATABASE_UPDATE_MODE = "databaseUpdateMode";
    public static final String PREFS_SUBSCRIPTION = "subscription";
    public static final String PREFS_CLIENT_ID = "clientId";
    public static final String PREFS_CLIENT_SECRET = "clientSecret";
    public static final String PREFS_DEBUG_ENABLED = "debugEnabled";
    public static final String URL_CCV2_SUBSCRIPTION_CODE = "https://help.sap.com/docs/SAP_COMMERCE_CLOUD_PUBLIC_CLOUD/452dcbb0e00f47e88a69cdaeb87a925d/04a3ab885fa84717800d302b05f61391.html?locale=en-US";
    public static final String URL_CCV2_ACCESS_TOKEN = "https://help.sap.com/docs/SAP_COMMERCE_CLOUD_PUBLIC_CLOUD/0fa6bcf4736c46f78c248512391eb467/b5d4d851cbd54469906a089bb8dd58d8.html?locale=en-US";
    public static final String URL_CCV2_CLIENT_CREDENTIALS = "https://help.sap.com/docs/SAP_COMMERCE_CLOUD_PUBLIC_CLOUD/0c2050f6d31f49ddb6eba18509060ae5/57bef96f18034193af93d2cc36f6d526.html?locale=en-US&version=LATEST";

}
