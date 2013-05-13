package com.nitorcreations.test.wicket;

import org.hamcrest.Matcher;
import org.junit.BeforeClass;
import org.junit.Test;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.util.tester.WicketTester;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;

public class SelectionTest {
    /** Needed to instantiate components */
    static WicketTester wicketTester = new WicketTester();

    static MarkupContainer parent;

    static Label labelOne;

    static MarkupContainer container;

    static Label deepLabel;

    @BeforeClass
    public static void setupHierarchy() {
        parent = new WebMarkupContainer("parent");
        parent.add(labelOne = new Label("labelOne", "Label One"));
        parent.add(container = new WebMarkupContainer("container"));
        container.add(deepLabel = new Label("labelTwo", "Label Deep"));
    }

    @Test
    public void findsBothLabels() {
        assertThat(Selection.select(Label.class).from(parent), contains(labelOne, deepLabel));
    }

    @Test
    public void labelInstantiationWithIdFindsCorrectLabel() {
        assertThat(Selection.select(Label.class, "labelOne").from(parent), contains(labelOne));
    }

    @Test
    public void labelWithIdFindsCorrectLabel() {
        assertThat(Selection.select(Label.class).withId("labelTwo").from(parent), contains(deepLabel));
    }

    @Test
    public void firstFromReturnsFirst() {
        assertThat(Selection.select(Label.class).firstFrom(parent), is(labelOne));
    }

    @Test
    public void selectWithIdSelectsComponents() {
        assertThat(Selection.selectWithId("labelTwo").from(parent), contains((Component) deepLabel));
    }

    @Test
    public void chainingMatchersWorks() {
        Selection<Label> selection = Selection.select(Label.class);
        selection.that(label(startsWith("Label")));
        assertThat(selection.from(parent), contains(labelOne, deepLabel));
        selection.that(label(endsWith("One")));
        assertThat(selection.from(parent), contains(labelOne));
    }

    private static Matcher<? super Component> label(Matcher<String> content) {
        return allOf(instanceOf(Label.class), hasProperty("defaultModelObjectAsString", content));
    }

    @Test
    public void noLinksSelected() {
        assertThat(Selection.select(Link.class).from(parent), empty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void firstFromNotFound() {
        Selection.selectWithId("non-existent").firstFrom(parent);
    }

    @Test
    public void firstFromWhenNullsAllowedAndNotFoundReturnsNull() {
        assertThat(Selection.selectWithId("non-existent").firstOrNullFrom(parent), nullValue());
    }

}
