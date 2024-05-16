package Exams;

import Questions.*;

import java.util.*;
import java.util.stream.Collectors;

public class Exam {
    private int id;
    private int TotalQuestions;
    private List<Question> questions;

    public Exam() {
        TotalQuestions = 25;
    }

    public Exam(int TotalQuestion) {
        this.TotalQuestions = TotalQuestion;
    }

    public Exam(int id, String questionIDs) {
        this.id = id;
        this.questions = addQuestion(questionIDs);
    }

    public int getId() {
        return id;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public void setId(int id) { this.id = id; }

    public void addQuestion() {
        QuestionRepository qr = new QuestionRepository();
        Scanner sc = new Scanner(System.in);
        for (int i = 0; i < TotalQuestions; i++) {
            boolean found = false;
            while (!found) {
                System.out.println("Enter the ID for question " + (i+1) + ": ");
                int questionID = sc.nextInt();
                found = qr.questionExist(id);
                if (!found) {
                    System.out.println("Question is not exist! Try again.");
                    continue;
                }
                else {
                    questions.add(qr.getQuestion(id));
                }
            }
        }
    }

    public List<Question> addQuestion(String questionIDs) {
        QuestionRepository qr = new QuestionRepository();
        List<Question> questionList = new ArrayList<>();
        List<Integer> qIDs = Arrays.stream(questionIDs.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        for (int i : qIDs) {
            if (qr.questionExist(i)) {
                questionList.add(qr.getQuestion(id));
            }
        }
        return questionList;
    }

    public void removeQuestion(int id) {
        for (Question q : questions) {
            if (q.getId() == id) {
                questions.remove(q);
                break;
            }
        }
    }

    public void shuffleQuestions() {
        Collections.shuffle(questions);
        for (Question q : questions) {
            if (q instanceof MultipleChoiceQuestion) {
                MultipleChoiceQuestion mcq = (MultipleChoiceQuestion) q;
                mcq.shuffleAnswer();
            }
        }
    }

}
