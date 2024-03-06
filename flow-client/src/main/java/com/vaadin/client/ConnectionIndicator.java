/*
 * Copyright (C) 2000-2024 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.client;

/**
 * GWT interface to ConnectionIndicator.ts
 *
 * @author Vaadin Ltd
 * @since 1.0
 */
public class ConnectionIndicator {

    /*
     * Constants shared with ConnectionState.ts
     */

    /**
     * Application is connected to server: last transaction over the wire (XHR /
     * heartbeat / endpoint call) was successful.
     */
    public static final String CONNECTED = "connected";

    /**
     * Application is connected and Flow is loading application state from the
     * server, or Fusion is waiting for an endpoint call to return.
     */
    public static final String LOADING = "loading";

    /**
     * Application has been temporarily disconnected from the server because the
     * last transaction over the wire (XHR / heartbeat / endpoint call) resulted
     * in a network error, or the browser has received the 'online' event and
     * needs to verify reconnection with the server. Flow is attempting to
     * reconnect a configurable number of times before giving up.
     */
    public static final String RECONNECTING = "reconnecting";

    /**
     * Application has been permanently disconnected due to browser receiving
     * the 'offline' event, or the server not being reached after a number of
     * reconnect attempts.
     */
    public static final String CONNECTION_LOST = "connection-lost";

    private ConnectionIndicator() {
        // No instance should ever be created
    }

    /**
     * Set the connection state to be displayed by the loading indicator.
     *
     * @param state
     *            the connection state
     */
    public static native void setState(String state)
    /*-{
        if ($wnd.Vaadin.connectionState) {
            $wnd.Vaadin.connectionState.state = state;
        }
    }-*/;

    /**
     * Get the connection state.
     *
     * @return the connection state
     */
    public static native String getState()
    /*-{
        if ($wnd.Vaadin.connectionState) {
            return $wnd.Vaadin.connectionState.state;
        } else {
            return null;
        }
    }-*/;

    /**
     * Set a property of the connection indicator component.
     *
     * @param property
     *            the property to set
     * @param value
     *            the value to set
     */
    public static native void setProperty(String property, Object value)
    /*-{
        if ($wnd.Vaadin.connectionIndicator) {
            $wnd.Vaadin.connectionIndicator[property] = value;
        }
    }-*/;
}
