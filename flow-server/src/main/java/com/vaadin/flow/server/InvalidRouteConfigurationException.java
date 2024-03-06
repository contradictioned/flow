/*
 * Copyright (C) 2000-2024 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.flow.server;

/**
 * Exception indicating that the application's routes have been configured
 * incorrectly.
 *
 * @since 1.0
 */
public class InvalidRouteConfigurationException extends RuntimeException {

    /**
     * Constructs a new invalid route configuration runtime exception with the
     * specified detail message.
     *
     * @param message
     *            the detail message. The detail message is saved for later
     *            retrieval by the {@link #getMessage()} method.
     */
    public InvalidRouteConfigurationException(String message) {
        super(message);
    }
}
