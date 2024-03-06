/*
 * Copyright (C) 2000-2024 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.flow.server.webcomponent;

import java.io.Serializable;
import java.util.Objects;

/**
 * Value object containing information of a web component's property field.
 *
 * @param <P>
 *            type of the property's value
 * @author Vaadin Ltd.
 * @since 2.0
 */
public final class PropertyData<P extends Serializable>
        implements Serializable {
    private final String name;
    private final Class<P> type;
    private final P defaultValue;
    private final boolean readOnly;

    /**
     * Constructs a new {@code PropertyData} instance tied to the type of the
     * property's value given by {@code type}.
     *
     * @param name
     *            name of the property
     * @param type
     *            type of the property value
     * @param readOnly
     *            is the property read-only (on the client-side)
     * @param defaultValue
     *            default value for the property
     */
    public PropertyData(String name, Class<P> type, boolean readOnly,
            P defaultValue) {
        Objects.requireNonNull(name, "Parameter 'name' must not be null!");
        Objects.requireNonNull(type, "Parameter 'type' must not be null!");
        this.name = name;
        this.type = type;
        this.readOnly = readOnly;
        this.defaultValue = defaultValue;
    }

    /**
     * Getter for the property name.
     *
     * @return property name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the property value class type.
     *
     * @return value class type
     */
    public Class<P> getType() {
        return type;
    }

    /**
     * Getter for the initial value if given.
     *
     * @return initial value or {@code null} if none given
     */
    public P getDefaultValue() {
        return defaultValue;
    }

    /**
     * Checks if the property is a read-only value.
     *
     * @return is read-only
     */
    public boolean isReadOnly() {
        return readOnly;
    }

    /**
     * Creates a copy of {@code this} with the new {@code readOnly} value.
     *
     * @param readOnly
     *            new {@code readOnly} value
     * @return copy of {@code this}
     */
    public PropertyData<P> updateReadOnly(boolean readOnly) {
        return new PropertyData<>(this.name, this.type, readOnly,
                this.defaultValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PropertyData) {
            PropertyData other = (PropertyData) obj;
            return name.equals(other.name) && type.equals(other.type);
        }
        return false;
    }
}
