package com.nitorcreations.test.wicket.resources;

import java.util.Locale;

import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.resource.loader.IStringResourceLoader;

import static org.mockito.AdditionalAnswers.returnsArgAt;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * A string resource loader that returns the key.
 * 
 * @author Reko Jokelainen / Nitor Creations
 * 
 */
public final class MockStringResourceLoader implements IStringResourceLoader {
    private final IStringResourceLoader mockLoader;

    public MockStringResourceLoader() {
        this.mockLoader = mock(IStringResourceLoader.class);
        when(mockLoader.loadStringResource(any(Class.class), anyString(), any(Locale.class), anyString(), anyString()))
                .then(returnsArgAt(1));
        when(mockLoader.loadStringResource(any(Component.class), anyString(), any(Locale.class), anyString(),
                anyString())).then(returnsArgAt(1));
    }

    /**
     * Binds the resource loader to the application by removing all other resource loaders
     * and adding itself to the resource loader collection
     * @param application the application to bind to
     */
    public void bind(Application application) {
        application.getResourceSettings().getStringResourceLoaders().clear();
        application.getResourceSettings().getStringResourceLoaders().add(this);
    }

    @Override
    public String loadStringResource(Class<?> clazz, String key, Locale locale, String style, String variation) {
        return mockLoader.loadStringResource(clazz, key, locale, style, variation);
    }

    @Override
    public String loadStringResource(Component component, String key, Locale locale, String style, String variation) {
        return mockLoader.loadStringResource(component, key, locale, style, variation);
    }

    public void expectStringMessage(String key, String message) {
        when(mockLoader.loadStringResource(any(Class.class), eq(key), any(Locale.class), anyString(), anyString())).thenReturn(
                message);
        when(mockLoader.loadStringResource(any(Component.class), eq(key), any(Locale.class), anyString(), anyString())).thenReturn(message);
    }

    public void expectStringMessage(Class<? extends Component> clazz, String key, String message) {
        when(mockLoader.loadStringResource(eq(clazz), eq(key), any(Locale.class), anyString(), anyString())).thenReturn(message);
        when(mockLoader.loadStringResource(isA(clazz), eq(key), any(Locale.class), anyString(), anyString())).thenReturn(
                message);
    }

    public void expectStringMessage(Locale locale, String key, String message) {
        when(mockLoader.loadStringResource(any(Class.class), eq(key), eq(locale), anyString(), anyString())).thenReturn(
                message);
        when(mockLoader.loadStringResource(any(Component.class), eq(key), eq(locale), anyString(), anyString())).thenReturn(message);
    }

}