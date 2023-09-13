/*
 * Copyright 2000-2023 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.flow.uitest.ui.testbench;

import java.util.Collections;

import com.vaadin.testbench.HasStringValueProperty;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("vaadin-dev-tools-theme-text-input")
public class DevToolsThemeTextInputElement extends TestBenchElement
        implements HasStringValueProperty {
    @Override
    public void setValue(String value) {
        TestBenchElement input = $("input").first();
        input.setProperty("value", value);
        input.dispatchEvent("input", Collections.singletonMap("bubbles", true));
        input.dispatchEvent("change",
                Collections.singletonMap("bubbles", true));
    }
}
