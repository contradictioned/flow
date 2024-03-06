/*
 * Copyright (C) 2000-2024 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */

import { LitElement, html } from "lit";

export class SimpleLitTemplateShadowRoot extends LitElement {
  static get properties() {
    return {
      text: String
    };
  }
  render() {
    return html`
      <button id="clientButton" @click="${e => this.$server.sayHello()}">${this.text}</button>

      <button id="mappedButton"></button>
      <div id="label"></div>
      <div id="sort"></div>
    `;
  }
}
customElements.define("simple-lit-template-shadow-root", SimpleLitTemplateShadowRoot);
