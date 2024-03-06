/*
 * Copyright (C) 2000-2024 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.flow.shared;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class VaadinUriResolverTest {

    private final class NullContextVaadinUriResolver extends VaadinUriResolver {
        public String resolveVaadinUri(String uri) {
            return super.resolveVaadinUri(uri, "http://someplace/");
        }
    }

    @Test
    public void testContextProtocol() {
        NullContextVaadinUriResolver resolver = new NullContextVaadinUriResolver();
        assertEquals("http://someplace/my-component.html",
                resolver.resolveVaadinUri("context://my-component.html"));
    }

}
