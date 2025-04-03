package testing;

import logic.Event;
import logic.Events;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for logic.Eventsclass
 *
 * @author Logan Ouellette
 * @version 1.0
 */

class TestEvents {

    @Test
    public void getEventQuestion(){
        Events events = new Events("testEvents.csv");
        String answer = events.getEventQuestion();
        assertEquals(answer, "check");
    }

    @Test
    public void getEventAnswer(){
        Events events = new Events("testEvents.csv");
        String answer = events.getEventAnswer();
        assertEquals(answer, "B");
    }

    @Test
    public void getEventPlus(){
        Events events = new Events("testEvents.csv");
        int score = events.getEventPlus();
        assertEquals(score, 10);
    }

    @Test
    public void getEventMinus() {
        Events events = new Events("testEvents.csv");
        int score = events.getEventMinus();
        assertEquals(score, 5);
    }

    @Test
    public void getEventItemType() {
        Events events = new Events("testEvents.csv");
        String type = events.getEventType();
        assertEquals(type, "Food");
    }

    @Test
    public void getEventItem() {
        Events events = new Events("testEvents.csv");
        String item = events.getEventItem();
        assertEquals(item, "Pizza");
    }
}