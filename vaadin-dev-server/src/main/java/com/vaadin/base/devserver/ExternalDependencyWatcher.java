/*
 * Copyright 2000-2023 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.base.devserver;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.server.InitParameters;
import com.vaadin.flow.server.VaadinContext;
import com.vaadin.flow.server.startup.ApplicationConfiguration;

public class ExternalDependencyWatcher {

    private static Set<FileWatcher> watchers = new HashSet<>();

    public ExternalDependencyWatcher(VaadinContext context,
            File jarFrontendResourcesFolder) {
        ApplicationConfiguration config = ApplicationConfiguration.get(context);

        String hotdeployDependencies = config.getStringProperty(
                InitParameters.FRONTEND_HOTDEPLOY_DEPENDENCIES, null);

        List<String> hotdeployDependencyFolders = new ArrayList<>();
        if (hotdeployDependencies != null) {
            Collections.addAll(hotdeployDependencyFolders,
                    hotdeployDependencies.split(","));
        }

        for (String hotdeployDependencyFolder : hotdeployDependencyFolders) {
            Path moduleFolder = config.getProjectFolder().toPath()
                    .resolve(hotdeployDependencyFolder);
            Path metaInf = moduleFolder
                    .resolve(Path.of("src", "main", "resources", "META-INF"));
            watchDependencyFolder(metaInf.toFile(), jarFrontendResourcesFolder);
        }
    }

    private void watchDependencyFolder(File metaInfFolder,
            File jarFrontendResourcesFolder) {
        File metaInfFrontend = new File(metaInfFolder, "frontend");
        File metaInfResourcesFrontend = new File(
                new File(metaInfFolder, "resources"), "frontend");

        watchAndCopy(metaInfFrontend, jarFrontendResourcesFolder);
        watchAndCopy(metaInfResourcesFrontend, jarFrontendResourcesFolder);
    }

    private void watchAndCopy(File watchFolder, File targetFolder) {
        if (!watchFolder.exists()) {
            return;
        }

        try {
            FileWatcher watcher = new FileWatcher(updatedFile -> {
                Path pathInsideWatchFolder = watchFolder.toPath()
                        .relativize(updatedFile.toPath());
                Path target = targetFolder.toPath()
                        .resolve(pathInsideWatchFolder);
                try {
                    URL src = updatedFile.toURI().toURL();
                    IOUtils.copy(src, target.toFile());
                } catch (IOException e) {
                    getLogger().warn("Unable to copy modified file from "
                            + updatedFile + " to " + target);
                }
            }, watchFolder);
            watcher.start();
            watchers.add(watcher);
            getLogger().debug("Watching {} for frontend file changes",
                    watchFolder);
        } catch (Exception e) {
            getLogger().error("Unable to start file watcher for " + watchFolder,
                    e);
        }

    }

    public void destroy() {
        for (FileWatcher watcher : watchers) {
            try {
                watcher.stop();
            } catch (IOException e) {
                getLogger().error("Unable to stop file watcher", e);
            }
        }
        watchers.clear();
    }

    private Logger getLogger() {
        return LoggerFactory.getLogger(getClass());
    }
}
