package model;
public class Questions {
    private int id;
    private String type;
    private String grade;
    private String subject;
    private String chapter;
    private int difficulty;
    private String question;
    private String suggestion;
    private int score;

    public Questions(int id, String type, String grade, String subject, String chapter, int difficulty,
                     String question, String suggestion, int score) {
        this.id = id;
        this.type = type;
        this.grade = grade;
        this.subject = subject;
        this.chapter = chapter;
        this.difficulty = difficulty;
        this.question = question;
        this.suggestion = suggestion;
        this.score = score;
    }

    public Questions() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}


