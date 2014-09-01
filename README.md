PubSubmarine
============

A dead-simple injectable (JSR-330-compliant) pub-sub event bus. 

Usage
-----

Create a subtype of com.spencerwi.pubsubmarine.Event:

```java
import com.spencerwi.pubsubmarine.Event;

public class SomeTypeOfEvent extends Event {
    public final Integer value; 
}
```

Inject the event bus and subscribe to your event type:

```java
import com.spencerwi.pubsubmarine.*;

public class YourClass {
    @Inject /* or @Autowired, if you use Spring */
    private EventBus eventBus;
    
    public void subscribe(){
        eventBus.register(SomeTypeOfEvent.class, (Event event) -> {
            SomeTypeOfEvent specificEventType = (SomeTypeOfEvent)event
            System.out.println("Got: " + specificEventType.value);
        });
    }
}
```
