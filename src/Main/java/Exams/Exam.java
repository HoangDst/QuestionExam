package Exams;

import Questions.*;
import Lib.Table;

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
                try {
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
                catch (InputMismatchException ex) {
                    System.out.println("Must enter a integer value! Try again");
                    sc.nextLine();
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
        if (questions != null) {
            Collections.shuffle(questions);
            for (Question q : questions) {
                if (q instanceof MultipleChoiceQuestion) {
                    MultipleChoiceQuestion mcq = (MultipleChoiceQuestion) q;
                    mcq.shuffleAnswer();
                }
            }
            System.out.println("Shuffle successfully!");
        } else System.out.println("Questions list is null, cannot shuffle.");
    }

    public void display() {
        String exam = "Exam ID#" + id;
        Table table = new Table("", exam);
        table.setColumnAlignment(1, false);
        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            String row1 = Integer.toString(i + 1);
            String row2 = "ID#" + q.getId() + " - " +
                    q.getQuestion() + " (" +
                    q.getScore() + ")";
            if (q instanceof MultipleChoiceQuestion) {
                MultipleChoiceQuestion mcq = (MultipleChoiceQuestion) q;
                List<Answer> a = mcq.getAnswers();
                for (int j = 0; j < a.size(); j++) {
                    row2 += "\n" + (char)(j + 65) + ". " + a.get(j).toString();
                }
            }
            table.addRow(row1, row2);
        }
        System.out.println(table);
    }

}
