/*
 * Copyright (C) 2000-2024 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.flow.internal;

import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.VaadinServletService;

public class UrlUtilTest {

    private String encodeURIShouldNotBeEscaped = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789;,/?:@&=+$-_.!~*'()#";
    private String encodeURIComponentShouldNotBeEscaped = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_.!~*'()";

    @Test
    public void isExternal_URLStartsWithTwoSlashes_returnsTrue() {
        Assert.assertTrue(UrlUtil.isExternal("//foo"));
    }

    @Test
    public void isExternal_URLContainsAnySchemaAsPrefix_returnsTrue() {
        Assert.assertTrue(UrlUtil.isExternal("http://foo"));
        Assert.assertTrue(UrlUtil.isExternal("https://foo"));
        Assert.assertTrue(UrlUtil.isExternal("context://foo"));
        Assert.assertTrue(UrlUtil.isExternal("base://foo"));
    }

    @Test
    public void isExternal_URLDoesnotContainSchema_returnsFalse() {
        Assert.assertFalse(UrlUtil.isExternal("foo"));
    }

    @Test
    public void plusAndSpaceHandledCorrectly() {
        Assert.assertEquals("Plus+Spa%20+%20ce",
                UrlUtil.encodeURI("Plus+Spa + ce"));
        Assert.assertEquals("Plus%2BSpa%20%2B%20ce",
                UrlUtil.encodeURIComponent("Plus+Spa + ce"));
    }

    @Test
    public void encodeURI_shouldNotBeEscaped() {
        Assert.assertEquals(encodeURIShouldNotBeEscaped,
                UrlUtil.encodeURI(encodeURIShouldNotBeEscaped));
    }

    @Test
    public void encodeURI_mustBeEscaped() {
        for (char c = 0; c < 255; c++) {
            String s = String.valueOf(c);
            if (encodeURIShouldNotBeEscaped.contains(s)) {
                continue;
            }
            Assert.assertNotEquals(UrlUtil.encodeURI(s), s);
        }
    }

    @Test
    public void encodeURIComponent_shouldNotBeEscaped() {
        Assert.assertEquals(encodeURIComponentShouldNotBeEscaped, UrlUtil
                .encodeURIComponent(encodeURIComponentShouldNotBeEscaped));
    }

    @Test
    public void encodeURIComponent_mustBeEscaped() {
        for (char c = 0; c < 255; c++) {
            String s = String.valueOf(c);
            if (encodeURIComponentShouldNotBeEscaped.contains(s)) {
                continue;
            }
            Assert.assertNotEquals(UrlUtil.encodeURIComponent(s), s);
        }
    }

    @Test
    public void getServletPathRelative() {
        Assert.assertEquals(".", UrlUtil.getServletPathRelative("/foo/bar/",
                createRequest("/foo", "/bar")));
        Assert.assertEquals(".", UrlUtil.getServletPathRelative("/foo/bar",
                createRequest("/foo", "/bar")));
        Assert.assertEquals("..", UrlUtil.getServletPathRelative("/foo/",
                createRequest("/foo", "/bar")));
        Assert.assertEquals("../..", UrlUtil.getServletPathRelative("/",
                createRequest("/foo", "/bar")));
        Assert.assertEquals("..", UrlUtil.getServletPathRelative("/foo",
                createRequest("/foo", "/bar")));
        Assert.assertEquals("../../login", UrlUtil.getServletPathRelative(
                "/login", createRequest("/foo", "/bar")));
        Assert.assertEquals("../login", UrlUtil.getServletPathRelative(
                "/foo/login", createRequest("/foo", "/bar")));
        Assert.assertEquals("login", UrlUtil.getServletPathRelative(
                "/foo/bar/login", createRequest("/foo", "/bar")));
        Assert.assertEquals("baz/login", UrlUtil.getServletPathRelative(
                "/foo/bar/baz/login", createRequest("/foo", "/bar")));
    }

    private VaadinServletRequest createRequest(String contextPath,
            String servletPath) {
        if (!servletPath.equals("") && !servletPath.startsWith("/")) {
            throw new IllegalArgumentException(
                    "A servlet path always starts with / except for the empty mapping \"\"");
        }
        if (!contextPath.equals("") && (!contextPath.startsWith("/")
                || contextPath.endsWith("/"))) {
            throw new IllegalArgumentException(
                    "A context path is either empty or starts, but not ends with, a slash");
        }
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getServletPath()).thenReturn(servletPath);
        Mockito.when(request.getContextPath()).thenReturn(contextPath);
        return new VaadinServletRequest(request,
                Mockito.mock(VaadinServletService.class));
    }
}
