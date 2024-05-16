package Questions;

import DatabaseConfiguration.DBConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class QuestionRepository {
    private List<Question> questions;
    private DBConnector connector = new DBConnector();

    public QuestionRepository() {
        loadQuestions();
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<Question> filter() {
        List<Question> filteredQuestions = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter question type (E/MC): ");
        String type = scanner.nextLine();
        System.out.println("Enter grade: ");
        int grade = scanner.nextInt();
        System.out.println("Enter subject: ");
        String subject = scanner.nextLine();
        System.out.println("Enter chapter: ");
        String chapter = scanner.nextLine();
        System.out.println("Enter difficulty: ");
        int difficulty = scanner.nextInt();
        scanner.nextLine();
        String query = "SELECT * FROM questions WHERE type = " + type + " AND grade = " + grade
                + " AND subject = " + subject + " AND chapter = " + chapter + " AND difficulty = " + difficulty;
        try (PreparedStatement preparedStatement = connector.getConnection().prepareStatement(query)){
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
//                String type1 = resultSet.getString("Type");
//                int grade1 = resultSet.getInt("Grade");
//                String subject1 = resultSet.getString("Subject");
//                String chapter1 = resultSet.getString("Chapter");
//                int difficulty1 = resultSet.getInt("Difficulty");

            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return filteredQuestions;
    }

    public boolean questionExist(int id) {
        for (Question q : questions) {
            if (q.getId() == id) return true;
        }
        return false;
    }

    public Question getQuestion(int id) {
        for (Question q : questions) {
            if (q.getId() == id) return q;
        }
        return null;
    }

    public void addQuestion() {
        Scanner scanner = new Scanner(System.in);
        List<Answer> answers = new ArrayList<>();
        System.out.println("Enter question type (E/MC): ");
        String type = scanner.nextLine();
        System.out.println("Enter grade: ");
        int grade = scanner.nextInt();
        System.out.println("Enter subject: ");
        String subject = scanner.nextLine();
        System.out.println("Enter chapter: ");
        String chapter = scanner.nextLine();
        System.out.println("Enter difficulty: ");
        int difficulty = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter question: ");
        String question = scanner.nextLine();
        if (type.equalsIgnoreCase("MC")) {
            int numChoice = 0;
            while (true) {
                System.out.println("Enter number of choices: ");
                numChoice = scanner.nextInt();
                if (numChoice > 0 && numChoice <= 10) break;
                else System.out.println("Invalid number! Please try again");
            }
            MultipleChoiceQuestion mcq = new MultipleChoiceQuestion();
            mcq.addAnswer(numChoice);
            answers = mcq.getAnswers();
            System.out.println("Enter suggestion: ");
            String suggestion = scanner.next();
            System.out.println("Enter score: ");
            double score = scanner.nextDouble();
            mcq = new MultipleChoiceQuestion(questions.size() + 1, grade, subject, chapter, difficulty,
                    question, answers, suggestion, score);
            questions.add(mcq);
        } else {
            System.out.println("Enter suggestion: ");
            String suggestion = scanner.next();
            System.out.println("Enter score: ");
            double score = scanner.nextDouble();
            EssayQuestion eq = new EssayQuestion(questions.size() + 1, grade, subject, chapter, difficulty,
                    question, suggestion, score);
            questions.add(eq);
        }

        scanner.nextLine();
        scanner.close();
    }

    public void updateQuestion(int id) {
        for (Question q : questions) {
            if (q.getId() == id) {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Enter new question: ");
                String question = scanner.nextLine();
                if (q instanceof MultipleChoiceQuestion) {
                    MultipleChoiceQuestion mcq = (MultipleChoiceQuestion) q;
                    int numChoice = 0;
                    while (true) {
                        System.out.println("Enter number of choices: ");
                        numChoice = scanner.nextInt();
                        if (numChoice > 0 && numChoice <= 10) break;
                        else System.out.println("Invalid number! Please try again");
                    }
                    mcq.addAnswer(numChoice);
                }
                q.setQuestion(question);
                break;
            }
        }
    }


    public void deleteQuestion(int id) {
        for (Question q : questions) {
            if (q.getId() == id) questions.remove(q);
            break;
        }
    }

    private void deleteDatabaseQuestions() {
        String query = "DELETE FROM questions";
        connector.execution(query);
        query = "DELETE FROM answers";
        connector.execution(query);
    }

    public void saveQuestions() {
        deleteDatabaseQuestions();
        for (Question q : questions) {
            String query = "INSERT INTO Questions (Type, Grade, Subject, Chapter, Difficulty, Question, " +
                    "Suggestion, Score) VALUES (" + q.getType() + ", " + q.getGrade() + ", " + q.getSubject() + ", " +
                    q.getChapter() + ", " + q.getDifficulty() + ", " + q.getQuestion() + ", " + q.getSuggestion() + ", "
                    + q.getScore() + ")";
            connector.execution(query);
            if (q.getType().equalsIgnoreCase("MC")) {
                MultipleChoiceQuestion mcq = (MultipleChoiceQuestion) q;
                mcq.saveAnswer();
            }
        }
    }

    public void loadQuestions() {
        questions.clear();
        String query = "SELECT * FROM questions";
        try (PreparedStatement preparedStatement = connector.getConnection().prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String type = resultSet.getString("Type");
                int grade = resultSet.getInt("Grade");
                String subject = resultSet.getString("Subject");
                String chapter = resultSet.getString("Chapter");
                int difficulty = resultSet.getInt("Difficulty");
                String question = resultSet.getString("Question");
                String suggestion = resultSet.getString("Suggestion");
                double score = resultSet.getDouble("Score");
                if (type.equalsIgnoreCase("MC")) {
                    List<Answer> answers = new ArrayList<>();
                    String answerQuery = "SELECT * FROM answers WHERE questionID = " + id;
                    try (PreparedStatement answerStatement = connector.getConnection().prepareStatement(answerQuery)) {
                        ResultSet answerSet = answerStatement.executeQuery();
                        while (answerSet.next()) {
                            String content = answerSet.getString("Content");
                            boolean status = answerSet.getBoolean("Status");
                            answers.add(new Answer(content, status));
                        }
                    }
                    MultipleChoiceQuestion mcq = new MultipleChoiceQuestion(id, grade, subject, chapter, difficulty,
                            question, answers, suggestion, score);
                    questions.add(mcq);
                } else {
                    EssayQuestion eq = new EssayQuestion(id, grade, subject, chapter, difficulty, question, suggestion, score);
                    questions.add(eq);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

