/*
 * Copyright (C) 2000-2024 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.client.flow.dom;

import elemental.dom.Node;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

/**
 * Implementation of {@link DomApiImpl} that uses the
 * <a href="https://www.polymer-project.org/1.0/docs/devguide/local-dom">Polymer
 * 's DOM API</a>.
 * <p>
 * Contains methods for checking whether polymer-micro.html has been loaded.
 *
 * @author Vaadin Ltd
 * @since 1.0
 */
public class PolymerDomApiImpl implements DomApiImpl {

    @Override
    public DomElement wrap(Node node) {
        return getPolymer().dom(node);
    }

    /**
     * Checks whether the polymer-micro.html is loaded or not.
     *
     * @return <code>true</code> if polymer micro has been loaded,
     *         <code>false</code> if not
     */
    public static boolean isPolymerMicroLoaded() {
        // Don't use the impl with Polymer 2
        return getPolymer() != null
                && getPolymer().getVersion().startsWith("1.");
    }

    /**
     * A reference to the native Polymer object in JavaScript.
     */
    @JsType(isNative = true)
    interface Polymer {

        /**
         * A <a href=
         * "https://www.polymer-project.org/1.0/docs/devguide/local-dom">Polymer
         * .dom</a> method java representation. Wraps a <code>Node</code> so
         * that it works with Polymer based web components.
         *
         * @param node
         *            the node to wrap to Polymer dom
         * @return the wrapped node
         */
        DomElement dom(Node node);

        /**
         * Returns polymer version.
         *
         * @return polymer version
         */
        @JsProperty
        String getVersion();

    }

    /**
     * Returns a reference to the native Polymer object in JavaScript.
     * <p>
     * The existence of this object can be used to check whether
     * polymer-micro.html has been loaded.
     *
     * @return the polymer object, or <code>null</code> if polymer micro has not
     *         been loaded
     */
    @JsProperty(namespace = JsPackage.GLOBAL, name = "Polymer")
    private static native Polymer getPolymer();

}
