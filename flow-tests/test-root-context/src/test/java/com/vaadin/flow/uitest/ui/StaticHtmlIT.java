/*
 * Copyright (C) 2000-2024 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.flow.uitest.ui;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;

import com.vaadin.flow.testutil.ChromeBrowserTest;

public class StaticHtmlIT extends ChromeBrowserTest {

    @Test
    public void ensureStaticHtmlShown() {
        open();
        Assert.assertEquals("NetBeans plugin",
                findElement(By.xpath("//a[@href='/netbeans']")).getText());
    }
}
