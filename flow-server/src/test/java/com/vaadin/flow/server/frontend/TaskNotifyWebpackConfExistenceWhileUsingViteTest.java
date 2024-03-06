/*
 * Copyright (C) 2000-2024 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.flow.server.frontend;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.vaadin.flow.server.ExecutionFailedException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.vaadin.flow.server.Constants.TARGET;
import static com.vaadin.flow.server.frontend.FrontendUtils.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;

public class TaskNotifyWebpackConfExistenceWhileUsingViteTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private String userDir;

    @Before
    public void setup() {
        userDir = temporaryFolder.getRoot().getAbsolutePath();
    }

    @Test
    public void should_logWarnMessageAboutWebpackConfigExistence_when_defaultWebpackConfigFileExist()
            throws IOException, ExecutionFailedException {
        try (MockedStatic<Paths> paths = Mockito.mockStatic(Paths.class);
                MockedStatic<LoggerFactory> loggerFactory = Mockito
                        .mockStatic(LoggerFactory.class)) {
            File webpackConfigFile = new File(userDir, WEBPACK_CONFIG);
            Path webpackConfigFilePath = webpackConfigFile.toPath();
            paths.when(() -> Paths.get(webpackConfigFile.getPath()))
                    .thenReturn(webpackConfigFilePath);
            Path targetPath = new File(userDir,
                    TARGET + "/" + DEFAULT_GENERATED_DIR).toPath();
            paths.when(() -> Paths.get(TARGET, DEFAULT_GENERATED_DIR))
                    .thenReturn(targetPath);
            paths.when(() -> Paths.get(new File(userDir).getPath(),
                    WEBPACK_CONFIG)).thenReturn(webpackConfigFilePath);
            Logger logger = Mockito.spy(Logger.class);
            loggerFactory
                    .when(() -> LoggerFactory.getLogger(
                            TaskNotifyWebpackConfExistenceWhileUsingVite.class))
                    .thenReturn(logger);
            //@formatter:off
            String content = "/**\n" +
                    " * This file contains project specific customizations for the webpack build.\n" +
                    " * It is autogenerated if it didn't exist or if it was made for an older\n" +
                    " * incompatible version.\n" +
                    " *\n" +
                    " * Defaults are provided in an autogenerated webpack.generated.js file and used by this file.\n" +
                    " * The webpack.generated.js file is overwritten on each build and no customization can be done there.\n" +
                    " */\n" +
                    "const merge = require('webpack-merge');\n" +
                    "/**random comment1*/" +
                    "/** random comment2 */" +
                    "/*random comment3*/" +
                    "/* random comment4 */" +
                    "//random comment5 */" +
                    "const flowDefaults = require('./webpack.generated.js');\n" +
                    "\n" +
                    "/**\n" +
                    " * To change the webpack config, add a new configuration object in\n" +
                    " * the merge arguments below:\n" +
                    " */\n" +
                    "module.exports = merge(flowDefaults, \n" +
                    "  // Override default configuration\n" +
                    "  // {\n" +
                    "  //   mode: 'development',\n" +
                    "  //   devtool: 'inline-source-map',\n" +
                    "  // },\n" +
                    "\n" +
                    "  // Add a custom plugin\n" +
                    "  // (install the plugin with `npm install --save-dev webpack-bundle-analyzer`)\n" +
                    "  // {\n" +
                    "  //   plugins: [\n" +
                    "  //     new require('webpack-bundle-analyzer').BundleAnalyzerPlugin({\n" +
                    "  //       analyzerMode: 'static'\n" +
                    "  //     })\n" +
                    "  //   ]\n" +
                    "  // },\n" +
                    ");\n";
            //@formatter:on
            Files.writeString(webpackConfigFilePath, content);

            TaskNotifyWebpackConfExistenceWhileUsingVite task = new TaskNotifyWebpackConfExistenceWhileUsingVite(
                    new File(userDir));
            task.execute();

            Mockito.verify(logger, times(1)).warn(anyString());
        }
    }

    @Test
    public void should_executeWithoutWarning_when_webpackConfigFileDoesNotExist()
            throws ExecutionFailedException {

        try (MockedStatic<Paths> paths = Mockito.mockStatic(Paths.class);
                MockedStatic<Files> files = Mockito.mockStatic(Files.class);
                MockedStatic<LoggerFactory> loggerFactory = Mockito
                        .mockStatic(LoggerFactory.class)) {
            File webpackConfigFile = new File(userDir, WEBPACK_CONFIG);
            Path webpackConfigFilePath = webpackConfigFile.toPath();
            paths.when(() -> Paths.get(webpackConfigFile.getPath()))
                    .thenReturn(webpackConfigFilePath);
            paths.when(() -> Paths.get(new File(userDir).getPath(),
                    WEBPACK_CONFIG)).thenReturn(webpackConfigFilePath);
            files.when(() -> Files.exists(webpackConfigFilePath))
                    .thenReturn(false);
            Logger logger = Mockito.spy(Logger.class);
            loggerFactory
                    .when(() -> LoggerFactory.getLogger(
                            TaskNotifyWebpackConfExistenceWhileUsingVite.class))
                    .thenReturn(logger);

            TaskNotifyWebpackConfExistenceWhileUsingVite task = new TaskNotifyWebpackConfExistenceWhileUsingVite(
                    new File(userDir));
            task.execute();

            Mockito.verify(logger, times(0)).warn(anyString());
        }
    }

}
