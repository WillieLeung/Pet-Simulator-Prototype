package logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Events{
    private ArrayList<Event> events = new ArrayList<Event>();
    private Event currentEvent = null;

    public Events(String filename){
        ReadWriteFile reader = new ReadWriteFile();
        Map<String, List<String>> mappedEvents = reader.readEventCSV(filename);
        currentEvent = events.getFirst();
    }

    public void newEvent(){
        Random rand = new Random();
        int index = rand.nextInt(events.size() - 1);
        currentEvent = events.get(index);
    }

    public String getEventQuestion(){
        return currentEvent.getQuestion();
    }

    public List<String> getEventOptions(){
        return currentEvent.getOptions();
    }

    public String getEventAnswer(){
        return currentEvent.getAnswer();
    }

    public int getEventScore(int index){
        return currentEvent.getScore(index);
    }

    public void addEvent(String question, List<String> options, int correctAnswer, int plus, int minus, String item){
        Event newEvent = new Event(question, options, correctAnswer, plus, minus, item);
        events.add(newEvent);
    }
}