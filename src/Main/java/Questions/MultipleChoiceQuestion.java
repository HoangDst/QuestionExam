package Questions;

import DatabaseConfiguration.DBConnector;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Collections;

public class MultipleChoiceQuestion extends Question {
    private List<Answer> answers;

    public MultipleChoiceQuestion() {
        answers = new ArrayList<>();
    }
    public MultipleChoiceQuestion(int id, int grade, String subject, String chapter, int difficulty,
                         String question, List<Answer> answers, String suggestion, double score) {
        super(id, "Multiple choice", grade, subject, chapter, difficulty, question, suggestion, score);
        this.answers = answers;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public void addAnswer(int numAnswers) {
        answers.clear();
        Scanner sc = new Scanner(System.in);
        String content;
        boolean status;
        for (int i = 0; i < numAnswers; i++) {
            System.out.println("Enter content for choice " + (char)(i + 65) + ": ");
            content = sc.next();
            System.out.println("Enter status for choice " + (char)(i + 65) + " (true/false): ");
            status = sc.nextBoolean();
            answers.add(new Answer(content, status));
        }
        //sc.close();
    }

    public void saveAnswer() {
        DBConnector connector = new DBConnector();
        for (Answer answer : answers) {
            String query = "INSERT INTO answers (Content, Status, questionID) VALUES ("
                    + answer.getContent() + ", " + answer.getStatus() + ", " + getId() + ")";
            connector.execution(query);
        }
    }

    public void shuffleAnswer() {
        Collections.shuffle(answers);
    }

    public String toString() {
        String str = "";
        str += "id = " + getId() +
                "\ntype = " + "Essay" +
                "\ngrade = " + getGrade() +
                "\nsubject = '" + getSubject() + '\'' +
                "\nchapter = '" + getChapter() + '\'' +
                "\ndifficulty = " + getDifficulty() +
                "\nquestion = '" + getQuestion() + '\'' +
                "\nchoices:";
        for (int i = 0; i < answers.size(); i++) {
            str += "\n";
            str += (char)(i + 65) + ". ";
            str += answers.get(i).toString();
        }
        str += "\nsuggestion = '" + getSuggestion() + '\'' +
                "\nscore = " + getScore() +
                "\n}";
        return str;
    }
}
