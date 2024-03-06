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

class ExceptionsDuringPropertyUpdates extends PolymerElement {
  static get is() { return 'exceptions-property-update' }

  static get template() {
    return html`
       <button id="set-properties" on-click='_handleClick'>Set properties</button>
       <slot></slot>
    `;
  }

  static get properties() {
    return {
       text :{
         type: String,
         notify: true,
       },
       name :{
         type: String,
         notify: true
       },
       title :{
         type: String,
         notify: true
       }
     };
   }

  _handleClick() {
    this.title ="foo";
    this.name="bar";
    this.text="baz";
  }
}
customElements.define(ExceptionsDuringPropertyUpdates.is, ExceptionsDuringPropertyUpdates);
