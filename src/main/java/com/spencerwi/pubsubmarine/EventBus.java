package com.spencerwi.pubsubmarine;


import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Named
public class EventBus {
    private ConcurrentHashMap<Class<? extends Event>, List<Consumer<Event>>> subscribersByEventType = new ConcurrentHashMap<>();

    protected EventBus(){}

     public void subscribe(Class<? extends Event> eventType, Consumer<Event> handler){
         List<Consumer<Event>> subscribers = subscribersByEventType.getOrDefault(eventType, new ArrayList<>());
         subscribers.add(handler);
         this.subscribersByEventType.put(eventType, subscribers);
     }

    public void publish(Event event){
        List<Consumer<Event>> subscribers = subscribersByEventType.getOrDefault(event.getClass(), new ArrayList<>());
        subscribers.forEach(subscriber -> subscriber.accept(event));
    }
}
