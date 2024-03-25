/*
 * Copyright 2000-2024 Vaadin Ltd.
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

package com.vaadin.flow.server.frontend;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A helper for tasks to handle generated files.
 * <p>
 * </p>
 * Allow to write file on disk only if the content has not changed. Generated
 * files are tracked and can be fetched by post-processing tasks.
 * <p>
 * </p>
 * For internal use only. May be renamed or removed in a future release.
 */
public final class GeneratedFilesSupport {

    private final Set<Path> fileList = new HashSet<>();

    /**
     * Writes the given content into the given file unless the file already
     * contains that content.
     *
     * @param file
     *            the file to write to
     * @param content
     *            the lines to write
     * @return true if the content was written to the file, false otherwise
     * @throws IOException
     *             if something went wrong
     */
    public boolean writeIfChanged(File file, List<String> content)
            throws IOException {
        boolean written = FileIOUtils.writeIfChanged(file,
                String.join("\n", content));
        track(file);
        return written;
    }

    /**
     * Writes the given content into the given file unless the file already
     * contains that content.
     *
     * @param file
     *            the file to write to
     * @param content
     *            the lines to write
     * @return true if the content was written to the file, false otherwise
     * @throws IOException
     *             if something went wrong
     */
    public boolean writeIfChanged(Path file, List<String> content)
            throws IOException {
        return writeIfChanged(file.toFile(), content);
    }

    /**
     * Writes the given content into the given file unless the file already
     * contains that content.
     *
     * @param file
     *            the file to write to
     * @param content
     *            the content to write
     * @return true if the content was written to the file, false otherwise
     * @throws IOException
     *             if something went wrong
     */
    public boolean writeIfChanged(File file, String content)
            throws IOException {
        boolean written = FileIOUtils.writeIfChanged(file, content);
        track(file);
        return written;
    }

    /**
     * Writes the given content into the given file unless the file already
     * contains that content.
     *
     * @param file
     *            the file to write to
     * @param content
     *            the content to write
     * @return true if the content was written to the file, false otherwise
     * @throws IOException
     *             if something went wrong
     */
    public boolean writeIfChanged(Path file, String content)
            throws IOException {
        return writeIfChanged(file.toFile(), content);
    }

    /**
     * Marks the give file as generated by the task.
     *
     * @param file
     *            the file to be marked as generated.
     */
    public void track(File file) {
        fileList.add(file.toPath().normalize().toAbsolutePath());
    }

    /**
     * Marks the give file as generated by the task.
     *
     * @param file
     *            the file to be marked as generated.
     */
    public void track(Path file) {
        fileList.add(file.normalize().toAbsolutePath());
    }

    /**
     * Gets paths of all generated files, whether they have been written to disk
     * or not.
     *
     * @return paths of files generated under the given {@code root} folder,
     *         never {@literal null}.
     */
    public Set<Path> getFiles() {
        return new HashSet<>(fileList);
    }

    /**
     * Gets paths of files generated under the given {@code root} folder.
     *
     * @param root
     *            root folder to get generated files.
     * @return paths of files generated under the given {@code root} folder,
     *         never {@literal null}.
     */
    public Set<Path> getFiles(Path root) {
        Objects.requireNonNull(root, "root path is mandatory");
        Path absolute = root.normalize().toAbsolutePath();
        return fileList.stream().filter(p -> p.startsWith(absolute))
                .collect(Collectors.toCollection(HashSet::new));
    }

}
