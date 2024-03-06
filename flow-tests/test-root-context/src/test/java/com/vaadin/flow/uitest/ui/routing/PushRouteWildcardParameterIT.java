/*
 * Copyright (C) 2000-2024 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.flow.uitest.ui.routing;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.vaadin.flow.testutil.ChromeBrowserTest;

public class PushRouteWildcardParameterIT extends ChromeBrowserTest {

    // #8968: Push connection with parameter containing slashes
    @Test
    public void wildcardParameter_parameterContainsSlash() {
        getDriver().get(getRootURL() + getTestPath() + "/a/b/c");
        waitForDevServer();

        waitForElementPresent(By.id(PushRouteWildcardParameterView.LABEL_ID));
        WebElement label = findElement(
                By.id(PushRouteWildcardParameterView.LABEL_ID));
        Assert.assertEquals("a/b/c", label.getText());
    }
}
