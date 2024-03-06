/*
 * Copyright (C) 2000-2024 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.flow.uitest.ui;

import java.util.function.Consumer;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HtmlComponent;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.dom.DomEvent;
import com.vaadin.flow.dom.DomEventListener;
import com.vaadin.flow.dom.DomListenerRegistration;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.uitest.servlet.ViewTestLayout;

@Route(value = "com.vaadin.flow.uitest.ui.DebounceSynchronizePropertyView", layout = ViewTestLayout.class)
public class DebounceSynchronizePropertyView
        extends AbstractDebounceSynchronizeView {
    private final HtmlComponent input = new HtmlComponent("input");
    private final Element inputElement = input.getElement();

    public DebounceSynchronizePropertyView() {
        input.getElement().setAttribute("id", "input");

        Component eagerToggle = createModeToggle("Eager (every keypress)",
                "eager");
        Component filteredToggle = createModeToggle("Filtered (even length)",
                "filtered", registration -> registration
                        .setFilter("element.value.length % 2 === 0"));
        Component debounceToggle = createModeToggle(
                "Debounce (when typing pauses)", "debounce",
                registration -> registration.debounce(CHANGE_TIMEOUT));
        Component throttleToggle = createModeToggle("Throttle (while typing)",
                "throttle",
                registration -> registration.throttle(CHANGE_TIMEOUT));

        add(eagerToggle, filteredToggle, debounceToggle, throttleToggle, input);
        addChangeMessagesDiv();
    }

    private Component createModeToggle(String caption, String id,
            Consumer<DomListenerRegistration> configurator) {
        Element checkbox = new Element("input");
        checkbox.setAttribute("type", "checkbox");
        checkbox.setAttribute("id", id);

        checkbox.addEventListener("change", new DomEventListener() {
            private DomListenerRegistration registration = null;

            @Override
            public void handleEvent(DomEvent event) {
                if (event.getEventData().getBoolean("element.checked")) {
                    assert registration == null;

                    registration = inputElement.addPropertyChangeListener(
                            "value", "input",
                            propertyChange -> addChangeMessage(
                                    propertyChange.getValue()));

                    configurator.accept(registration);
                } else {
                    registration.remove();
                    registration = null;
                }
            }
        }).addEventData("element.checked");

        Label label = new Label(caption);
        label.getElement().insertChild(0, checkbox);
        label.getElement().getStyle().set("display", "block");
        return label;
    }

    // Shorthand without configuration to keep UI building code clean
    private Component createModeToggle(String caption, String id) {
        return createModeToggle(caption, id, ignore -> {
        });
    }
}
