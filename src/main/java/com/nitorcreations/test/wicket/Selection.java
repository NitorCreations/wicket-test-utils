package com.nitorcreations.test.wicket;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hamcrest.Matcher;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.util.iterator.GenericComponentHierarchyIterator;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;

/**
 * A utility that can be used to find specific components in Wicket's component hierarchy.
 * Example use:
 *
 * <pre>
 * Label label = select(Label.class).withId("name").that(is(visibleInHierarchy())).firstFrom(wmc);
 * </pre>
 *
 *
 * @param <T>
 *         the type of the component to find
 */
public final class Selection<T extends Component> {

    private final Class<T> clazz;

    private final Set<Matcher<? super T>> matchers;

    private Selection(Class<T> clazz) {
        this.clazz = clazz;
        this.matchers = new HashSet<Matcher<? super T>>();
    }

    public static <T extends Component> Selection<T> select(Class<T> clazz) {
        return new Selection<T>(clazz);
    }

    public static <T extends Component> Selection<T> select(Class<T> clazz, String markupId) {
        return new Selection<T>(clazz).withId(markupId);
    }

    public static Selection<Component> selectWithId(String markupId) {
        return select(Component.class).withId(markupId);
    }

    /**
     * Add the given matcher to the set of matchers to use when searching for the components.
     * @param matcher the matcher to add
     * @return {@code this} to allow chaining
     * @see #from(org.apache.wicket.MarkupContainer)
     */
    public Selection<T> that(Matcher<? super T> matcher) {
        matchers.add(matcher);
        return this;
    }

    public Selection<T> withId(String markupId) {
        return this.that(hasProperty("id", is(markupId)));
    }

    /**
     * List the components matching the given set of matchers that reside under {@code container} in the hierarchy.
     *
     * @param container
     *         the container to search for
     * @param <X>
     *         the type of the returned components
     * @return the list of found components or empty if none found
     * @see
     */
    @SuppressWarnings("unchecked")
    public <X extends T> List<X> from(MarkupContainer container) {
        final List<X> list = new ArrayList<X>();
        final Matcher<? super T> matcher = allOf(matchers);
        for (T c : new GenericComponentHierarchyIterator<T>(container, clazz)) {
            if (matcher.matches(c)) {
                list.add((X) c);
            }
        }
        return list;
    }

    /**
     * Get the first element matching the given set of matchers. Will throw an {@link IllegalArgumentException}
     * if no components matching the given set of matchers were found in the given container.
     *
     * @param container
     *         the container to search for
     * @param <X>
     *         the type of the returned components
     * @return the first matching component
     *
     * @see #from(org.apache.wicket.MarkupContainer)
     * @see #firstOrNullFrom(org.apache.wicket.MarkupContainer)
     */
    @SuppressWarnings("unchecked")
    public <X extends T> X firstFrom(MarkupContainer container) {
        final X x = firstOrNullFrom(container);
        if (x == null) {
            throw new IllegalArgumentException(String.format(
                    "No components of type '%s' matching '%s' found in '%s'",
                    clazz.getSimpleName(),
                    allOf(matchers),
                    container
            ));
        }
        return x;
    }

    /**
     * Get the first element matching the given set of matchers from the given container.
     * Will return {@code null} if none found.
     *
     * @param container
     *         the container to search for
     * @param <X>
     *         the type of the returned components
     * @return the first matching component or {@code null} if none found.
     *
     * @see #from(org.apache.wicket.MarkupContainer)
     * @see #firstFrom(org.apache.wicket.MarkupContainer)
     */
    public <X extends T> X firstOrNullFrom(MarkupContainer container) {
        List<X> from = from(container);
        if (from.size() == 0) {
            return null;
        }
        return from.get(0);
    }
}