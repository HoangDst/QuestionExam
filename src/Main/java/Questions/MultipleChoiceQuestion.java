package Questions;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MultipleChoiceQuestion extends Question {
    private List<Answer> answers;
    public MultipleChoiceQuestion() {}
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
        sc.close();
    }
    public String toString() {
        String str = getQuestion();
        for (int i = 0; i < answers.size(); i++) {
            str += "\n";
            str += (char)(i + 65) + ". ";
            str += answers.get(i).toString();
        }
        return str;
    }
}
