public class Event {
    String question;
    String[] options;
    int correctAnswer;
    int score;

    public Event(String question, String[] options, int correctAnswer, int score) {
        this.question = question;
        this.options = options;
        this.correctAnswer = correctAnswer;
        this.score = score;
    }

    public String getQuestion() {
        return question;
    }

    public String[] getOptions() {
        return options;
    }

    public int getAnswer(){
        return correctAnswer;
    }

    public int getScore() {
        return score;
    }
}