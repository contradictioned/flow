/*
 * Copyright (C) 2000-2024 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
import {html, LitElement} from 'lit';
import './lit-template-inner.js';

class TemplateOuter extends LitElement {

  render() {
    return html`
            <div>Hello template outer</div>
            <lit-template-inner id="inner" style="display: block"></lit-template-inner>
    `;
  }
}

customElements.define('lit-template-outer', TemplateOuter);
