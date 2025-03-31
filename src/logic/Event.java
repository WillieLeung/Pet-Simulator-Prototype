package logic;

import java.util.List;

public class Event {
    String question;
    List<String> options;
    int correctAnswer;
    int plusScore;
    int minusScore;
    String itemType;
    String item;

    public Event(String question, List<String> options, int correctAnswer, int plusScore, int minusScore, String itemType, String item) {
        this.question = question;
        this.options = options;
        this.correctAnswer = correctAnswer;
        this.plusScore = plusScore;
        this.minusScore = minusScore;
        this.itemType = itemType;
        this.item = item;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getOptions() {
        return options;
    }

    public String getAnswer(){
        return options.get(correctAnswer);
    }

    public int getAnswerIndex(){
        return correctAnswer;
    }

    public int getPlusScore() {
        return plusScore;
    }

    public int getMinusScore() {
        return minusScore;
    }

    public String getItemType() {
        return itemType;
    }
    public String getItem(){
        return item;
    }
}
