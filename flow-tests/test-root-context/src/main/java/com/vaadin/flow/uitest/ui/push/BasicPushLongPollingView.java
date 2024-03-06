/*
 * Copyright (C) 2000-2024 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.flow.uitest.ui.push;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.internal.nodefeature.PushConfigurationMap;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.ui.Transport;
import com.vaadin.flow.uitest.servlet.ViewTestLayout;

@CustomPush(transport = Transport.LONG_POLLING)
@Route(value = "com.vaadin.flow.uitest.ui.push.BasicPushLongPollingView", layout = ViewTestLayout.class)
public class BasicPushLongPollingView extends BasicPushView {

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        attachEvent.getUI().getPushConfiguration().setParameter(
                PushConfigurationMap.FALLBACK_TRANSPORT_KEY, "none");
    }

}
