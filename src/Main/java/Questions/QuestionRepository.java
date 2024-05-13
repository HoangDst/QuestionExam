package Questions;
import DatabaseConfiguration.DBConnection;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class QuestionRepository {
    private Connection connection;
    public QuestionRepository() throws SQLException, ClassNotFoundException {
        this.connection = DBConnection.getConnection();
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
        String content = scanner.nextLine();
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
        }
        System.out.println("Enter suggestion: ");
        String suggestion = scanner.next();
        System.out.println("Enter score: ");
        double score = scanner.nextDouble();
        scanner.nextLine();
        scanner.close();



        String query = "INSERT INTO Questions (Type, Grade, Subject, Chapter, Difficulty, Question, Suggestion, Score) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, type.toUpperCase());
            preparedStatement.setInt(2, grade);
            preparedStatement.setString(3, subject);
            preparedStatement.setString(4, chapter);
            preparedStatement.setInt(5, difficulty);
            preparedStatement.setString(6, content);
            preparedStatement.setString(7, suggestion);
            preparedStatement.setDouble(8, score);
            preparedStatement.executeUpdate();

            if (type.equalsIgnoreCase("MC")) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                int questionId = -1;
                if (generatedKeys.next()) {
                    questionId = generatedKeys.getInt(1);
                }
                query = "INSERT INTO Answers (Content, Status, questionID) VALUES (?, ?, ?)";
                for (Answer answer : answers) {
                    try (PreparedStatement answerStatement = connection.prepareStatement(query)) {
                        answerStatement.setString(1, answer.getContent());
                        answerStatement.setBoolean(2, answer.getStatus());
                        answerStatement.setInt(3, questionId);
                        answerStatement.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    /*
    public void updateQuestion(int id) {

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
        String content = scanner.nextLine();
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
        }
        System.out.println("Enter score:");
        double score = scanner.nextDouble();
        scanner.nextLine();

        String query = "UPDATE Questions SET Type = ?, Grade = ?, Subject = ?, Chapter = ?, Sifficulty = ?, Question = ?, Score = ? WHERE ID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, type.toUpperCase());
            preparedStatement.setInt(2, grade);
            preparedStatement.setString(3, subject);
            preparedStatement.setString(4, chapter);
            preparedStatement.setInt(5, difficulty);
            preparedStatement.setString(6, content);
            preparedStatement.setDouble(7, score);
            preparedStatement.setInt(8, id);
            preparedStatement.executeUpdate();

            if (type.equalsIgnoreCase("MC")) {
                query = "UPDATE Answers SET Content = ?, Status = ? WHERE questionID = ?";
                for (Answer answer : answers) {
                    try (PreparedStatement answerStatement = connection.prepareStatement(query)) {
                        answerStatement.setString(1, answer.getContent());
                        answerStatement.setBoolean(2, answer.getStatus());
                        answerStatement.setInt(3, id);
                        answerStatement.executeUpdate();
                    }
                }
            }
            else {
                query = "DELETE FROM Answers WHERE idQuestion = ?";
                try (PreparedStatement answerStatement = connection.prepareStatement(query)) {
                    answerStatement.setInt(1, id);
                    answerStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
*/

    public void deleteQuestion(int id) {
        String query = "DELETE FROM Questions WHERE ID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            query = "DELETE FROM Answers WHERE questionID = ?";
            try (PreparedStatement answerStatement = connection.prepareStatement(query)) {
                answerStatement.setInt(1, id);
                answerStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<Question> getAllQuestions() {
        List<Question> questions = new ArrayList<>();
        String query = "SELECT * FROM Questions";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
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
                    String answerQuery = "SELECT * FROM Answers WHERE questionID = " + id;
                    try (PreparedStatement answerStatement = connection.prepareStatement(answerQuery)) {
                        ResultSet answerSet = preparedStatement.executeQuery();
                        while (answerSet.next()) {
                            String content = answerSet.getString("Content");
                            boolean status = answerSet.getBoolean("Status");
                            answers.add(new Answer(content, status));
                        }
                    }
                    MultipleChoiceQuestion mcq = new MultipleChoiceQuestion(id, grade, subject, chapter, difficulty,
                    question, answers, suggestion, score);
                    questions.add(mcq);
                }
                else {
                    EssayQuestion eq = new EssayQuestion(id, grade, subject, chapter, difficulty, question,suggestion, score);
                    questions.add(eq);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questions;
    }
}
