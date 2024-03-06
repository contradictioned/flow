/*
 * Copyright (C) 2000-2024 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.flow.component.html;

import com.vaadin.flow.component.ClickNotifier;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasAriaLabel;
import com.vaadin.flow.component.HasOrderedComponents;
import com.vaadin.flow.component.HtmlContainer;
import com.vaadin.flow.component.Tag;

/**
 * Component representing a <code>&lt;main&gt;</code> element.
 *
 * @author Vaadin Ltd
 * @since 1.0
 */
@Tag(Tag.MAIN)
public class Main extends HtmlContainer
        implements ClickNotifier<Main>, HasOrderedComponents, HasAriaLabel {

    /**
     * Creates a new empty main.
     */
    public Main() {
        super();
    }

    /**
     * Creates a new main with the given child components.
     *
     * @param components
     *            the child components
     */
    public Main(Component... components) {
        super(components);
    }
}
