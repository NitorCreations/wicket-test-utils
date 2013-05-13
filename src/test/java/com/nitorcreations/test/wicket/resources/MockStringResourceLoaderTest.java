package com.nitorcreations.test.wicket.resources;

import java.util.Locale;

import org.junit.Test;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.util.tester.WicketTester;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class MockStringResourceLoaderTest {

    WicketTester wicketTester = new WicketTester();
    MockStringResourceLoader loader = new MockStringResourceLoader();

    @Test
    public void returnsKeyByDefault() {
        assertThat(loader.loadStringResource(Label.class, "key", Locale.ENGLISH, "foo", "bar"), is("key"));
        assertThat(loader.loadStringResource(new Label("id"), "key2", Locale.ENGLISH, "foo", "bar"), is("key2"));
    }

    @Test
    public void expectMessageWithKeyReturnsByKey() {
        loader.expectStringMessage("foo", "Barbar");
        assertThat(loader.loadStringResource((Component)null, "foo", null, null, null), is("Barbar"));
        assertThat(loader.loadStringResource((Class<?>)null, "foo", null, null, null), is("Barbar"));
    }

    @Test
    public void expectMessageWithLocaleAndKeyReturnsOnlyByLocale() {
        loader.expectStringMessage(Locale.GERMAN, "foo", "Ach, ja!");
        assertThat(loader.loadStringResource((Class<?>)null, "foo", Locale.GERMAN, null, null),
                is("Ach, ja!"));
        assertThat(loader.loadStringResource((Class<?>)null, "foo", Locale.ENGLISH, null, null),
                is("foo"));
    }

    @Test
    public void expectMessageWithComponentClassReturnsOnlyForComponentClass() {
        loader.expectStringMessage(Label.class, "foo", "It's correct!");
        assertThat(loader.loadStringResource(Label.class, "foo", null, null, null), is("It's correct!"));
        assertThat(loader.loadStringResource(new Label("id"), "foo", null, null, null), is("It's correct!"));
        assertThat(loader.loadStringResource(TextField.class, "foo", null, null, null), is("foo"));
    }
}
