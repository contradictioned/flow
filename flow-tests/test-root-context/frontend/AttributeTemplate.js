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


class AttributeTemplate extends PolymerElement {
  static get template() {
    return html`
       <div style="padding: 10px; border: 1px solid black">
         <div id="div" title="foo" foo="bar" baz></div>
         <div id="disabled" disabled></div>
       </div>
       <slot>
    `;
  }
  static get is() {
    return 'attribute-template'
  }
    
}
  
customElements.define(AttributeTemplate.is, AttributeTemplate);
