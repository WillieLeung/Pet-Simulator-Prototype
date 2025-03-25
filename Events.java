import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Events{
    private ArrayList<Event> events = new ArrayList<Event>();
    private Event currentEvent = null;

    public Events(String filename){
        WriteToFile reader = new WriteToFile();
        Map<String, List<String>> mappedEvents = reader.readEventCSV();
        for (String key : mappedEvents.keySet()) {
            events.add(new Event(key, mappedEvents.get(key), 1, 10));
        }
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

    public int getEventAnswer(){
        return currentEvent.getAnswer();
    }

    public int getEventScore(){
        return currentEvent.getScore();
    }
}