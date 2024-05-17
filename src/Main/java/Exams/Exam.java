package Exams;

import Questions.*;

import java.util.*;
import java.util.stream.Collectors;

public class Exam {
    private int id;
    private int TotalQuestions;
    private List<Question> questions;

    public Exam(int id) {
        this.id = id;
        questions = new ArrayList<>();
        TotalQuestions = 25;
    }

    public Exam(int id, int TotalQuestion) {
        this.id = id;
        questions = new ArrayList<>();
        this.TotalQuestions = TotalQuestion;
    }

    public Exam(int id, String questionIDs) {
        questions = new ArrayList<>();
        this.id = id;
        this.questions = addQuestions(questionIDs);
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

    public void addQuestions() {
        QuestionRepository qr = new QuestionRepository();
        Scanner sc = new Scanner(System.in);
        for (int i = 0; i < TotalQuestions; i++) {
            boolean found = false;
            boolean exist = true;
            while (!found && exist) {
                System.out.println("Enter the ID for question " + (i+1) + ": ");
                int questionID = sc.nextInt();
                found = qr.questionExist(questionID);
                if (!found) {
                    System.out.println("Question is not exist! Try again.");
                    continue;
                }
                else {
                    exist = checkExist(questionID);
                    if (exist) {
                        System.out.println("Question is already in the exam! Try again.");
                        continue;
                    }
                    questions.add(qr.getQuestion(questionID));
                }
            }
        }
    }

    public boolean checkExist(int id) {
        for (Question q : questions) {
            if (q.getId() == id) return true;
        }
        return false;
    }
    public void addQuestion(int id) {
        QuestionRepository qr = new QuestionRepository();
        questions.add(qr.getQuestion(id));
    }

    public List<Question> addQuestions(String questionIDs) {
        QuestionRepository qr = new QuestionRepository();
        List<Question> questionList = new ArrayList<>();

        List<Integer> qIDs = new ArrayList<>();
        String[] qidString = questionIDs.split(",");
        for (String qs : qidString) {
            int qID = Integer.parseInt(qs.trim());
            qIDs.add(qID);
        }
        for (int i : qIDs) {
            if (qr.questionExist(i) && !checkExist(i)) {
                Question q = qr.getQuestion(i);
                questionList.add(q);
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

    public String toString() {
        String str = "ID: " + id;
        for (Question q : questions) {
            str += "\n" + q.toString();
        }
        return str;
    }

}
