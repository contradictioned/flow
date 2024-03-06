/*
 * Copyright (C) 2000-2024 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.flow.spring.security;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.internal.CurrentInstance;
import com.vaadin.flow.server.HandlerHelper;
import com.vaadin.flow.shared.ApplicationConstants;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UidlRedirectStrategyTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private HttpServletRequest request;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private HttpServletResponse response;

    private UidlRedirectStrategy strategy;

    @BeforeEach
    public void setup() {
        RequestUtil requestUtil = mock(RequestUtil.class);
        strategy = new UidlRedirectStrategy(requestUtil);
        doAnswer(i -> HandlerHelper.isFrameworkInternalRequest("/",
                i.getArgument(0))).when(requestUtil)
                .isFrameworkInternalRequest(any());
    }

    @AfterEach
    public void cleanup() {
        CurrentInstance.clearAll();
    }

    @Test
    public void isInternalRequest_setPageLocation()
            throws IOException, ServletException {
        when(request.getParameter(ApplicationConstants.REQUEST_TYPE_PARAMETER))
                .thenReturn(ApplicationConstants.REQUEST_TYPE_UIDL);

        var ui = mock(UI.class, Answers.RETURNS_DEEP_STUBS);
        CurrentInstance.set(UI.class, ui);

        var page = mock(Page.class);
        when(ui.getPage()).thenReturn(page);

        strategy.sendRedirect(request, response, "/foo");

        verify(page).setLocation("/foo");
    }

    @Test
    public void isExternalRequest_useDefaultRedirect()
            throws IOException, ServletException {
        when(request.getContextPath()).thenReturn("");
        when(response.encodeRedirectURL(anyString()))
                .thenAnswer(i -> i.getArguments()[0]);

        strategy.sendRedirect(request, response, "/foo");

        verify(response).sendRedirect("/foo");
    }
}
