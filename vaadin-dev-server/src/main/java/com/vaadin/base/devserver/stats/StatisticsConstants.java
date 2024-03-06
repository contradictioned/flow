/*
 * Copyright (C) 2000-2024 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.base.devserver.stats;

/**
 * Constants for development mode statistics.
 *
 * For internal use only. May be renamed or removed in a future release.
 *
 * @author Vaadin Ltd
 */
public class StatisticsConstants {

    /*
     * Event tracking identifiers.
     */
    public static final String EVENT_LIVE_RELOAD = "liveReload";
    public static final String EVENT_DEV_SERVER_START_PREFIX = "startDevserver";
    public static final String EVENT_PACKAGEMANAGER_INSTALL_TIME_PREFIX = "packageManagerInstall";
    public static final String EVENT_PACKAGEMANAGER_CLEANUP_TIME_PREFIX = "packageManagerCleanup";

    /*
     * Name of the default JSON file containing all the statistics.
     */
    static final String STATISTICS_FILE_NAME = "usage-statistics.json";

    /*
     * Meta fields for reporting and scheduling.
     */
    static final String FIELD_LAST_SENT = "lastSent";
    static final String FIELD_LAST_STATUS = "lastSendStatus";
    static final String FIELD_SEND_INTERVAL = "reportInterval";
    static final String FIELD_SERVER_MESSAGE = "serverMessage";

    /*
     * Data fields.
     */
    static final String FIELD_PROJECT_ID = "id";
    static final String FIELD_PROJECT_DEVMODE_STARTS = "devModeStarts";
    static final String FIELD_OPERATING_SYSTEM = "os";
    static final String FIELD_JVM = "jvm";
    static final String FIELD_FLOW_VERSION = "flowVersion";
    static final String FIELD_SOURCE_ID = "sourceId";
    static final String FIELD_PROKEY = "proKey";
    static final String FIELD_USER_KEY = "userKey";
    static final String FIELD_PROJECTS = "projects";
    static final String VAADIN_PROJECT_SOURCE_TEXT = "Vaadin project from";
    static final String PROJECT_SOURCE_TEXT = "Project from";

    /*
     * Default data values and limits.
     */
    static final String MISSING_DATA = "[NA]";
    static final String DEFAULT_PROJECT_ID = "default-project-id";
    static final String GENERATED_USERNAME = "GENERATED";
    static final long TIME_SEC_12H = 43200L;
    static final long TIME_SEC_24H = 86400L;
    static final long TIME_SEC_30D = 2592000L;
    static final int MAX_TELEMETRY_LENGTH = 1024 * 1000; // 1MB maximum data
    static final String INVALID_SERVER_RESPONSE = "Invalid server response.";

    /*
     * Default remote URL.
     */
    static final String USAGE_REPORT_URL = "https://tools.vaadin.com/usage-stats/v2/submit";

    /*
     * External parameters and file names.
     */
    static final String PROPERTY_USER_HOME = "user.home";
    static final String VAADIN_FOLDER_NAME = ".vaadin";
    static final String PRO_KEY_FILE_NAME = "proKey"; // NOSONAR
    static final String USER_KEY_FILE_NAME = "userKey"; // NOSONAR

    /*
     * Avoid instantiation.
     */
    private StatisticsConstants() {
        // Utility class only
    }
}
