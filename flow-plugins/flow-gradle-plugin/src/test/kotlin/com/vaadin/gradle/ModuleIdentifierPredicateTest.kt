/*
 * Copyright (C) 2000-2024 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.gradle

import org.gradle.api.internal.artifacts.DefaultModuleIdentifier
import org.junit.Test
import kotlin.test.expect

class ModuleIdentifierPredicateTest {
    @Test
    fun testAcceptAnything() {
        val m = ModuleIdentifierPredicate.fromGroupNameGlob("*:*")
        expect(true) { m.test(DefaultModuleIdentifier.newId("com.vaadin", "flow-server")) }
        expect(true) { m.test(DefaultModuleIdentifier.newId("com.vaadin", "checkbox")) }
        expect(true) { m.test(DefaultModuleIdentifier.newId("org.foo", "bar")) }
    }

    @Test
    fun testAcceptAnyModuleFromComVaadin() {
        val m = ModuleIdentifierPredicate.fromGroupNameGlob("com.vaadin:*")
        expect(true) { m.test(DefaultModuleIdentifier.newId("com.vaadin", "flow-server")) }
        expect(true) { m.test(DefaultModuleIdentifier.newId("com.vaadin", "checkbox")) }
        expect(false) { m.test(DefaultModuleIdentifier.newId("org.foo", "bar")) }
    }

    @Test
    fun testAcceptFlowModuleFromComVaadin() {
        val m = ModuleIdentifierPredicate.fromGroupNameGlob("com.vaadin:flow-*")
        expect(true) { m.test(DefaultModuleIdentifier.newId("com.vaadin", "flow-server")) }
        expect(false) { m.test(DefaultModuleIdentifier.newId("com.vaadin", "checkbox")) }
        expect(false) { m.test(DefaultModuleIdentifier.newId("org.foo", "bar")) }
    }
}