/*
 * Copyright (C) 2000-2024 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */

import { css, registerStyles } from '@vaadin/vaadin-themable-mixin';

registerStyles("themed-component", css`
    [part="content"] {
        color: rgba(255, 0, 0, 1);
    }
 `);
