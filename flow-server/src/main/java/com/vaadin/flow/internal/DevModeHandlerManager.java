/*
 * Copyright (C) 2000-2024 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.flow.internal;

import java.util.Optional;
import java.util.Set;

import com.vaadin.flow.di.Lookup;
import com.vaadin.flow.server.VaadinContext;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.startup.VaadinInitializerException;

/**
 * Provides API to access to the {@link DevModeHandler} instance by a
 * {@link VaadinService}.
 * <p>
 * For internal use only. May be renamed or removed in a future release.
 *
 * @author Vaadin Ltd
 * @since
 *
 */
public interface DevModeHandlerManager {

    /**
     * The annotations the dev mode handler is interested in having scanned from
     * the class path.
     *
     * @return an array of types the dev mode handler is interested in
     */
    Class<?>[] getHandlesTypes();

    /**
     * Starts up a new {@link DevModeHandler}.
     *
     * @param classes
     *            classes to check for npm- and js modules
     * @param context
     *            servlet context we are running in
     *
     * @throws VaadinInitializerException
     *             if dev mode can't be initialized
     */
    void initDevModeHandler(Set<Class<?>> classes, VaadinContext context)
            throws VaadinInitializerException;

    /**
     * Stops a running {@link DevModeHandler}.
     *
     */
    void stopDevModeHandler();

    /**
     * Defines the handler to use with this manager.
     *
     * @param devModeHandler
     *            the dev mode handler to use
     */
    void setDevModeHandler(DevModeHandler devModeHandler);

    /**
     * Returns a {@link DevModeHandler} instance for the given {@code service}.
     *
     * @return a {@link DevModeHandler} instance
     */
    DevModeHandler getDevModeHandler();

    /**
     * Opens the given application URL in a browser if the application is
     * running in development mode.
     *
     * @param url
     *            the url to open
     */
    void launchBrowserInDevelopmentMode(String url);

    /**
     * Gets the {@link DevModeHandler}.
     *
     * @param service
     *            a Vaadin service
     * @return an {@link Optional} containing a {@link DevModeHandler} instance
     *         or <code>EMPTY</code> if disabled
     */
    static Optional<DevModeHandler> getDevModeHandler(VaadinService service) {
        return getDevModeHandler(service.getContext());
    }

    /**
     * Gets the {@link DevModeHandler}.
     *
     * @param context
     *            the Vaadin context
     * @return an {@link Optional} containing a {@link DevModeHandler} instance
     *         or <code>EMPTY</code> if disabled
     */
    static Optional<DevModeHandler> getDevModeHandler(VaadinContext context) {
        return Optional.ofNullable(context)
                .map(ctx -> ctx.getAttribute(Lookup.class))
                .map(lu -> lu.lookup(DevModeHandlerManager.class))
                .flatMap(dmha -> Optional.ofNullable(dmha.getDevModeHandler()));
    }
}
