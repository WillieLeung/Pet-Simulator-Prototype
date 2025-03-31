package testing;

import logic.Event;
import logic.Events;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for logic.Actions class
 *
 * @author Logan Ouellette
 * @version 1.0
 */

class TestEvents {

    @Test
    public void newEvent() {
        List<String> options = new ArrayList<String>();
        options.add("A");
        options.add("B");
        options.add("C");
        Event event = new Event("Hello", options, 1, 5, 10, "Food", "Pizza");
        String question = event.getQuestion();
        assertEquals(question, "Hello");
    }

    @Test
    public void getEventOptions() {
        List<String> options = new ArrayList<String>();
        options.add("A");
        options.add("B");
        options.add("C");
        Event event = new Event("Hello", options, 1, 5, 10, "Food", "Pizza");
        List<String> options2 = event.getOptions();
        assertEquals(options, options2);
    }

    @Test
    public void getEventAnswer(){
        List<String> options = new ArrayList<String>();
        options.add("A");
        options.add("B");
        options.add("C");
        Event event = new Event("Hello", options, 1, 5, 10, "Food", "Pizza");
        String answer = event.getAnswer();
        assertEquals(answer, "B");
    }

    @Test
    public void getEventPlus(){
        List<String> options = new ArrayList<String>();
        options.add("A");
        options.add("B");
        options.add("C");
        Event event = new Event("Hello", options, 1, 5, 10, "Food", "Pizza");
        int index = event.getAnswerIndex();
        assertEquals(index, 1);
    }

    @Test
    public void getEventMinus() {
        List<String> options = new ArrayList<String>();
        options.add("A");
        options.add("B");
        options.add("C");
        Event event = new Event("Hello", options, 1, 5, 10, "Food", "Pizza");
        int plus = event.getPlusScore();
        assertEquals(plus, 5);
    }

    @Test
    public void getEventItem() {
        List<String> options = new ArrayList<String>();
        options.add("A");
        options.add("B");
        options.add("C");
        Event event = new Event("Hello", options, 1, 5, 10, "Food", "Pizza");
        int minus = event.getMinusScore();
        assertEquals(minus, 10);
    }

    @Test
    public void addEvent(){
        List<String> options = new ArrayList<String>();
        options.add("A");
        options.add("B");
        options.add("C");
        Event event = new Event("Hello", options, 1, 5, 10, "Food", "Pizza");
        String itemType = event.getItemType();
        assertEquals(itemType, "Food");
    }
}

