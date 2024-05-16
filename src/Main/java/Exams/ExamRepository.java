package Exams;

import DatabaseConfiguration.DBConnector;
import Questions.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
public class ExamRepository {
    List<Exam> exams;
    DBConnector connector = new DBConnector();

    public ExamRepository() {
        loadExam();
    }

    public void addExam() {
        Scanner sc = new Scanner(System.in);
        int id = exams.size() + 1;
        System.out.println("Enter number of questions for exam " + id + ": ");
        int TotalQuestions = sc.nextInt();
        Exam exam = new Exam(TotalQuestions);
        exam.addQuestion();
        exams.add(exam);
    }

    public void editExam() {} //not done

    public void deleteExam(int id) {
        for (Exam e : exams) {
            if (e.getId() == id) {
                exams.remove(e);
                break;
            }
        }
    }

    public boolean examExist(int id) {
        for (Exam e : exams) {
            if (e.getId() == id) return true;
        }
        return false;
    }
    public Exam getExam(int id) {
        for (Exam e : exams) {
            if (e.getId() == id) return e;
        }
        return null;
    }

    public void saveExam() {
        deleteDatabaseExam();
        for (Exam e : exams) {
            String str = "";
            int size = e.getQuestions().size();
            for (Question q : e.getQuestions()) {
                str += q.getId();
                if (size > 1) str += ",";
                size--;
            }
            String query = "INSERT INTO exams (questionIDs) VALUES (" + str + ")";
            connector.execution(query);
        }
    }

    private void deleteDatabaseExam() {
        String query = "DELETE FROM exams";
        connector.execution(query);
    }
    public void loadExam() {
        exams.clear();
        String query = "SELECT * FROM exams";

        try (PreparedStatement preparedStatement = connector.getConnection().prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String questionIDs = resultSet.getString("questionIDs");
                exams.add(new Exam(id, questionIDs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
