/*
 * Copyright (C) 2000-2024 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.flow.spring.test.routescope;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.vaadin.flow.spring.annotation.RouteScope;
import com.vaadin.flow.spring.annotation.RouteScopeOwner;

@Component
@RouteScope
@RouteScopeOwner(PreserveOnRefreshRoute.class)
public class PreserveOnRefreshBean {

    private String value = UUID.randomUUID().toString();

    public String getValue() {
        return value;
    }

}
