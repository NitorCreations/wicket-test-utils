package com.nitorcreations.test.mockito;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MockitoUtilTest {

    @Mock
    TestInterface testInterface;

    @Test
    public void testArgThat() throws Exception {
        testInterface.invoke("Foobarbaz");

        verify(testInterface).invoke(MockitoUtil.argThat(
                equalTo("Foobarbaz")
        ));
    }

    @Test
    public void testArgThatDoesNotMatch() throws Exception {
        testInterface.invoke("OneTwoThree");

        verify(testInterface, never()).invoke(MockitoUtil.argThat(
                equalTo("Foobarbaz")
        ));
    }

    @Test
    public void argumentCaptorCanCaptureGenericClasses() {
        testInterface.invokeOther(String.class);

        ArgumentCaptor<Class<String>> classArgumentCaptor = MockitoUtil.captorFor(Class.class);
        verify(testInterface).invokeOther(classArgumentCaptor.capture());

        assertThat(classArgumentCaptor.getValue(), equalTo(String.class));
    }

    private static interface TestInterface {
        void invoke(String value);

        <T> void invokeOther(Class<T> clazz);
    }
}
