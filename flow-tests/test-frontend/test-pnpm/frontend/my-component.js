/*
 * Copyright (C) 2000-2024 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
import { PolymerElement } from '@polymer/polymer/polymer-element.js';
import { html } from '@polymer/polymer/lib/utils/html-tag.js';

class MyComponentElement extends PolymerElement {
  static get template() {
    return html`
        <button id="button">Click</button>
        <div id="content"></div>
    `;
  }

  static get is() {
    return 'my-component'
  }
}
customElements.define(MyComponentElement.is, MyComponentElement);
