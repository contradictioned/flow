/*
 * Copyright (C) 2000-2024 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.flow.internal.nodefeature;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.vaadin.flow.internal.StateNode;
import com.vaadin.flow.internal.change.EmptyChange;
import com.vaadin.flow.internal.change.MapPutChange;
import com.vaadin.flow.internal.change.NodeChange;

public class NodeValueEmptyRequiredFeatureTest {

    private StateNode node;

    private NodeValue<Serializable> nodeValue;

    @Before
    public void setUp() {
        node = new StateNode(Arrays.asList(BasicTypeValue.class)) {
            @Override
            public boolean isAttached() {
                return true;
            }
        };
        nodeValue = node.getFeature(BasicTypeValue.class);
    }

    @Test
    public void generateChangesFromEmpty_featureHasChangesToCollect() {
        nodeValue.generateChangesFromEmpty();

        AtomicReference<NodeChange> change = new AtomicReference<>();
        node.collectChanges(change::set);

        Assert.assertTrue(change.get() instanceof EmptyChange);

        nodeValue.generateChangesFromEmpty();
        change.set(null);
        node.collectChanges(change::set);
        Assert.assertNull(change.get());
    }

    @Test
    public void generateChangesFromEmpty_noEmptyChange() {
        nodeValue.setValue("foo");
        node.clearChanges();
        nodeValue.generateChangesFromEmpty();

        List<NodeChange> changes = new ArrayList<>();
        node.collectChanges(changes::add);

        Assert.assertEquals(1, changes.size());
        Assert.assertTrue(changes.get(0) instanceof MapPutChange);
    }

    @Test
    public void collectChanges_featureHasEmptyChange() {
        nodeValue.generateChangesFromEmpty();

        AtomicReference<NodeChange> change = new AtomicReference<>();
        nodeValue.collectChanges(change::set);

        Assert.assertTrue(change.get() instanceof EmptyChange);

        change.set(null);
        nodeValue.collectChanges(change::set);
        Assert.assertNull(change.get());
    }

    @Test
    public void collectChanges_noEmptyChange() {
        nodeValue.setValue("foo");

        List<NodeChange> changes = new ArrayList<>();
        node.collectChanges(changes::add);

        Assert.assertEquals(1, changes.size());
        Assert.assertTrue(changes.get(0) instanceof MapPutChange);
    }
}
