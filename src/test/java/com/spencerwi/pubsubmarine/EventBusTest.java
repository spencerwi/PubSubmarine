package com.spencerwi.pubsubmarine;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.core.IsNot.not;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class EventBusTest {

    @InjectMocks private EventBus sut;

    @Mock private Consumer<Event> consumerMock;
    @Mock private Consumer<Event> anotherConsumerMock;

    @Test
    public void canBeInjected(){
        assertThat(sut, is(not(nullValue())));
    }

    @Test
    public void canRegisterAHandlerForEventSubtypes(){

        sut.subscribe(TestEventSubclass.class, consumerMock);

        ConcurrentHashMap<Class<? extends Event>, List<Consumer<Event>>> handlerMap = (ConcurrentHashMap<Class<? extends Event>, List<Consumer<Event>>>) ReflectionTestUtils.getField(sut, "subscribersByEventType");
        List<Consumer<Event>> testEventHandlers = handlerMap.get(TestEventSubclass.class);
        assertThat(testEventHandlers, is(not(nullValue())));
        assertThat(testEventHandlers, contains(consumerMock));
    }

    @Test
    public void canRegisterHandlersForMultipleEventSubtypes(){
        sut.subscribe(TestEventSubclass.class, consumerMock);
        sut.subscribe(AnotherEventSubclass.class, anotherConsumerMock);
    }

    @Test
    public void callsListenersOnPublicationOfSubscribedEvents(){
        sut.subscribe(TestEventSubclass.class, consumerMock);
        sut.subscribe(AnotherEventSubclass.class, anotherConsumerMock);
        final TestEventSubclass testEventSubclass = new TestEventSubclass();
        final AnotherEventSubclass anotherEventSubclass = new AnotherEventSubclass();

        sut.publish(testEventSubclass);

        verify(consumerMock).accept(testEventSubclass);
        verify(anotherConsumerMock, never()).accept(testEventSubclass);
        verifyNoMoreInteractions(consumerMock);


        sut.publish(anotherEventSubclass);

        verify(anotherConsumerMock).accept(anotherEventSubclass);

    }

    private static class TestEventSubclass extends Event {}

    private static class AnotherEventSubclass extends Event{}
}