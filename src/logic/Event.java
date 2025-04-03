package logic;

import java.util.List;

/**
 * This class represents events, that will be stored in the Events class and can be called upon to get the appropriate data for each event
 *
 * @author Logan Ouellette-Tran
 * @version 1.0
 */

public class Event {
    private String question;
    private List<String> options;
    private int correctAnswer;
    private int plusScore;
    private int minusScore;
    private String itemType;
    private String item;


    /**
     * Constructor for Event
     * @param question event question
     * @param options List of options for possible answer to question
     * @param correctAnswer Index of correct answer in options
     * @param plusScore Score to be rewarded for correct answer
     * @param minusScore Score to be deducted for incorrect answer
     * @param itemType Item type to be rewarded for correct answer
     * @param item Item rewarded for correct answer
     */
    public Event(String question, List<String> options, int correctAnswer, int plusScore, int minusScore, String itemType, String item) {
        this.question = question;
        this.options = options;
        this.correctAnswer = correctAnswer;
        this.plusScore = plusScore;
        this.minusScore = minusScore;
        this.itemType = itemType;
        this.item = item;
    }

    /**
     * Getter for question
     * @return question
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Getter for options
     * @return List of options
     */
    public List<String> getOptions() {
        return options;
    }

    /**
     * Getter for answer
     * @return String of correct answer
     */
    public String getAnswer(){
        return options.get(correctAnswer);
    }

    /**
     * Getter for index of correct answer
     * @return int index
     */
    public int getAnswerIndex(){
        return correctAnswer;
    }

    /**
     * Getter for plus score
     * @return int to be added
     */
    public int getPlusScore() {
        return plusScore;
    }

    /**
     * Getter for minus score
     * @return int to be deducted
     */
    public int getMinusScore() {
        return minusScore;
    }

    /**
     * Getter for item type
     * @return String of item type
     */
    public String getItemType() {
        return itemType;
    }

    /**
     * Getter for item
     * @return String of item
     */
    public String getItem(){
        return item;
    }
}

