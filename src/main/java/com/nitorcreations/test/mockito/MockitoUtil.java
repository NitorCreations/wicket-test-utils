package com.nitorcreations.test.mockito;

import org.hamcrest.Matcher;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public final class MockitoUtil {

    private MockitoUtil() {}

    /**
     * Creates a Mockito argument similar to {@link Mockito#argThat(org.hamcrest.Matcher)} but
     * works with Hamcrest 1.3
     *
     * @param matcher
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <S,T extends S> T argThat(Matcher<S> matcher) {
        return (T) Mockito.argThat(matcher);
    }

    /**
     * Creates a Mockito argument captor that can capture parameterized classes.
     * @param clazz
     * @param <S>
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <S, T extends S> ArgumentCaptor<T> captorFor(Class<S> clazz) {
        return (ArgumentCaptor<T>) ArgumentCaptor.forClass(clazz);
    }
}
