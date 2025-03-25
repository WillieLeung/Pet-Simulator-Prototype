import java.util.List;

public class Event {
    String question;
    List<String> options;
    int correctAnswer;
    int score;

    public Event(String question, List<String> options, int correctAnswer, int score) {
        this.question = question;
        this.options = options;
        this.correctAnswer = correctAnswer;
        this.score = score;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getOptions() {
        return options;
    }

    public int getAnswer(){
        return correctAnswer;
    }

    public int getScore() {
        return score;
    }
}