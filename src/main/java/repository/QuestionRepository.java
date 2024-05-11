package repository;
import config.DBConnection;
import model.Questions;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class QuestionRepository {
    private Connection connection;
    Scanner scanner;

    public QuestionRepository() throws SQLException, ClassNotFoundException {
        this.connection = DBConnection.getConnection();
        this.scanner=new Scanner(System.in);
    }

    public void addQuestion() {
        System.out.println("Enter question type:");
        String type = scanner.nextLine();
        System.out.println("Enter grade:");
        String grade = scanner.nextLine();
        System.out.println("Enter subject:");
        String subject = scanner.nextLine();
        System.out.println("Enter chapter:");
        String chapter = scanner.nextLine();
        System.out.println("Enter difficulty:");
        int difficulty = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter question:");
        String content = scanner.nextLine();
        System.out.println("Enter score:");
        int score = scanner.nextInt();
        scanner.nextLine();
        String query = "INSERT INTO Questions (type, grade, subject, chapter, difficulty, question, score) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, type);
            preparedStatement.setString(2, grade);
            preparedStatement.setString(3, subject);
            preparedStatement.setString(4, chapter);
            preparedStatement.setInt(5, difficulty);
            preparedStatement.setString(6, content);
            preparedStatement.setInt(7, score);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateQuestion( int id) {
        System.out.println("Enter question type:");
        String type = scanner.nextLine();
        System.out.println("Enter grade:");
        String grade = scanner.nextLine();
        System.out.println("Enter subject:");
        String subject = scanner.nextLine();
        System.out.println("Enter chapter:");
        String chapter = scanner.nextLine();
        System.out.println("Enter difficulty:");
        int difficulty = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter question:");
        String content = scanner.nextLine();
        System.out.println("Enter score:");
        int score = scanner.nextInt();
        scanner.nextLine();
        String query = "UPDATE Questions SET type = ?, grade = ?, subject = ?, chapter = ?, difficulty = ?, question = ?, score = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, type);
            preparedStatement.setString(2, grade);
            preparedStatement.setString(3, subject);
            preparedStatement.setString(4, chapter);
            preparedStatement.setInt(5, difficulty);
            preparedStatement.setString(6, content);
            preparedStatement.setInt(7, score);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteQuestion(int id) {
        String query = "DELETE FROM Questions WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<Questions> getAllQuestions() {
        List<Questions> questions = new ArrayList<>();
        String query = "SELECT * FROM Questions";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Questions question = new Questions();
                question.setType(resultSet.getString("type"));
                question.setGrade(resultSet.getString("grade"));
                question.setSubject(resultSet.getString("subject"));
                question.setChapter(resultSet.getString("chapter"));
                question.setDifficulty(resultSet.getInt("difficulty"));
                question.setQuestion(resultSet.getString("question"));
                question.setScore(resultSet.getInt("score"));
                questions.add(question);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questions;
    }
}
