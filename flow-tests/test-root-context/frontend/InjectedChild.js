/*
 * Copyright (C) 2000-2024 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
import {PolymerElement} from '@polymer/polymer/polymer-element.js';
import {html} from '@polymer/polymer/lib/utils/html-tag.js';

class InjectedChild extends PolymerElement {
  static get is() {
    return 'injected-child'
  }

  static get template() {
    return html`
       <div id="text">[[text]]</div>
    `;
  }
}

customElements.define(InjectedChild.is, InjectedChild);
