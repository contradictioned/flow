/*
 * Copyright (C) 2000-2024 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.flow.data.provider.hierarchy;

import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import com.vaadin.flow.data.provider.InMemoryDataProvider;
import com.vaadin.flow.function.SerializableComparator;
import com.vaadin.flow.function.SerializablePredicate;

/**
 * An in-memory data provider for listing components that display hierarchical
 * data. Uses an instance of {@link TreeData} as its source of data.
 *
 * @author Vaadin Ltd
 * @since 1.2
 *
 * @param <T>
 *            data type
 */
public class TreeDataProvider<T>
        extends AbstractHierarchicalDataProvider<T, SerializablePredicate<T>>
        implements InMemoryDataProvider<T> {

    private final TreeData<T> treeData;

    private SerializablePredicate<T> filter = null;

    private SerializableComparator<T> sortOrder = null;

    /**
     * Constructs a new TreeDataProvider.
     * <p>
     * This data provider should be refreshed after making changes to the
     * underlying {@link TreeData} instance.
     *
     * @param treeData
     *            the backing {@link TreeData} for this provider, not
     *            {@code null}
     */
    public TreeDataProvider(TreeData<T> treeData) {
        Objects.requireNonNull(treeData, "treeData cannot be null");
        this.treeData = treeData;
    }

    /**
     * Return the underlying hierarchical data of this provider.
     *
     * @return the underlying data of this provider
     */
    public TreeData<T> getTreeData() {
        return treeData;
    }

    @Override
    public boolean hasChildren(T item) {
        if (!treeData.contains(item)) {
            // The item might be dropped from the tree already
            return false;
        }
        return !treeData.getChildren(item).isEmpty();
    }

    @Override
    public int getChildCount(
            HierarchicalQuery<T, SerializablePredicate<T>> query) {
        Stream<T> items;

        if (query.getParent() != null) {
            items = treeData.getChildren(query.getParent()).stream();
        } else {
            items = treeData.getRootItems().stream();
        }

        return (int) getFilteredStream(items, query.getFilter())
                .skip(query.getOffset()).limit(query.getLimit()).count();
    }

    @Override
    public Stream<T> fetchChildren(
            HierarchicalQuery<T, SerializablePredicate<T>> query) {
        if (!treeData.contains(query.getParent())) {
            throw new IllegalArgumentException("The queried item "
                    + query.getParent()
                    + " could not be found in the backing TreeData. "
                    + "Did you forget to refresh this data provider after item removal?");
        }

        Stream<T> childStream = getFilteredStream(
                treeData.getChildren(query.getParent()).stream(),
                query.getFilter());

        Optional<Comparator<T>> comparing = Stream
                .of(query.getInMemorySorting(), sortOrder)
                .filter(Objects::nonNull)
                .reduce((c1, c2) -> c1.thenComparing(c2));

        if (comparing.isPresent()) {
            childStream = childStream.sorted(comparing.get());
        }

        return childStream.skip(query.getOffset()).limit(query.getLimit());
    }

    @Override
    public SerializablePredicate<T> getFilter() {
        return filter;
    }

    @Override
    public void setFilter(SerializablePredicate<T> filter) {
        this.filter = filter;
        refreshAll();
    }

    @Override
    public SerializableComparator<T> getSortComparator() {
        return sortOrder;
    }

    @Override
    public void setSortComparator(SerializableComparator<T> comparator) {
        sortOrder = comparator;
        refreshAll();
    }

    private Stream<T> getFilteredStream(Stream<T> stream,
            Optional<SerializablePredicate<T>> queryFilter) {
        final Optional<SerializablePredicate<T>> combinedFilter = filter != null
                ? Optional.of(queryFilter.map(filter::and).orElse(filter))
                : queryFilter;
        return combinedFilter.map(
                f -> stream.filter(element -> flatten(element).anyMatch(f)))
                .orElse(stream);
    }

    private Stream<T> flatten(T element) {
        return Stream.concat(Stream.of(element), getTreeData()
                .getChildren(element).stream().flatMap(this::flatten));
    }
}
