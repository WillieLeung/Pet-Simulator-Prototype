package logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * This class represents our extra specification: pop-up events
 *
 * @author Logan Ouellette-Tran
 * @version 1.0
 */

public class Events{
    //Stored array list of all event classes and current event option chosen
    private ArrayList<Event> events = new ArrayList<Event>();
    private Event currentEvent = null;

    /**
     * Constructor of Events class
     *
     * @param filename The filename of events file
     */
    public Events(String filename){
        ReadWriteFile reader = new ReadWriteFile();
        Map<String, List<String>> mappedEvents = reader.readEventCSV(filename);
        for(String key : mappedEvents.keySet()){
            addEvent(key, mappedEvents.get(key), 1, 50, -75,"Food", "Pizza");
        }
        newEvent();
    }

    /**
     * Function sets newEvent as currentEvent
     */
    public void newEvent(){
        Random rand = new Random();
        int index = rand.nextInt(events.size() - 1);
        currentEvent = events.get(index);
    }

    /**
     * Function returns current Event question
     * @return String of current question
     */
    public String getEventQuestion(){
        return currentEvent.getQuestion();
    }

    /**
     * Function gets event options
     * @return currentEvent.getOptions, string list of event options
     */
    public List<String> getEventOptions(){
        return currentEvent.getOptions();
    }

    /**
     * Function to get events correct answer
     *
     * @return currentEvent.getAnswer(), String of answer
     */
    public String getEventAnswer(){
        return currentEvent.getAnswer();
    }

    /**
     * Function to get score based on option chosen
     * @param index
     * @return int score based on index
     */
    public int getEventPlus(){
        return currentEvent.getPlusScore();
    }

    public int getEventMinus(){
        return currentEvent.getMinusScore();
    }

    /**
     * Function to get current Event item reward
     * @return item String
     */
    public String getEventItem(){
        return currentEvent.getItem();
    }

    /**
     * Function to add new Event to Array List
     * @param question question of event
     * @param options list of options
     * @param correctAnswer index of correct answer
     * @param plus points to award to score if correct answer chosen
     * @param minus points to deduct from score if incorrect
     * @param item item to reward if correct
     */
    private void addEvent(String question, List<String> options, int correctAnswer, int plus, int minus,String itemType, String item){
        Event newEvent = new Event(question, options, correctAnswer, plus, minus,itemType, item);
        events.add(newEvent);
    }
}
