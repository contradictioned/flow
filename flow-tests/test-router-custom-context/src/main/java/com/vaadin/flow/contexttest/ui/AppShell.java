/*
 * Copyright (C) 2000-2024 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.flow.contexttest.ui;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.NoTheme;

@NoTheme
@PWA(name = "test-router-custom-context", shortName = "custom-context", offlinePath = "offline.html")
public class AppShell implements AppShellConfigurator {
}
