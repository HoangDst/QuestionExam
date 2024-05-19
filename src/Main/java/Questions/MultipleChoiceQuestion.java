package Questions;

import DatabaseConfiguration.DBConnector;


import java.util.*;

public class MultipleChoiceQuestion extends Question {
    private List<Answer> answers;

    public MultipleChoiceQuestion() {
        answers = new ArrayList<>();
    }

    public MultipleChoiceQuestion(int id, int grade, String subject, String chapter, int difficulty,
                                  String question, List<Answer> answers, String suggestion, double score) {
        super(id, "MC", grade, subject, chapter, difficulty, question, suggestion, score);
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
        boolean status = false;
        for (int i = 0; i < numAnswers; i++) {
            System.out.println("Enter content for choice " + (char) (i + 65) + ": ");
            content = sc.next();
            boolean valid = false;
            while (!valid) {
                try {
                    System.out.println("Enter status for choice " + (char) (i + 65) + " (true/false): ");
                    status = sc.nextBoolean();
                    valid = true;
                } catch (InputMismatchException e) {
                    System.out.println("Must enter a boolean value! Try again");
                    sc.nextLine();
                }
            }
            answers.add(new Answer(content, status));
        }
        //sc.close();
    }

    public void saveAnswer() {
        DBConnector connector = new DBConnector();
        for (Answer a : answers) {
            String query = "INSERT INTO answers (Content, Status, questionID) VALUES (?, ?, ?)";
            List<Object> parameters = Arrays.asList(
                    a.getContent(),
                    a.getStatus(),
                    getId()
            );
            connector.execution(query, parameters);
        }
    }

    public void shuffleAnswer() {
        Collections.shuffle(answers);
    }

    public String toString() {
        String str = "";
        str += "id = " + getId() +
                "\ntype = " + "Multiple Choice" +
                "\ngrade = " + getGrade() +
                "\nsubject = '" + getSubject() + '\'' +
                "\nchapter = '" + getChapter() + '\'' +
                "\ndifficulty = " + getDifficulty() +
                "\nquestion = '" + getQuestion() + '\'' +
                "\nchoices:";
        for (int i = 0; i < answers.size(); i++) {
            str += "\n";
            str += (char) (i + 65) + ". ";
            str += answers.get(i).toString();
        }
        str += "\nsuggestion = '" + getSuggestion() + '\'' +
                "\nscore = " + getScore();
        return str;
    }
}
