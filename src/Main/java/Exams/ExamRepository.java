package Exams;

import DatabaseConfiguration.DBConnector;
import Questions.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class ExamRepository {
    List<Exam> exams;
    DBConnector connector = new DBConnector();

    public ExamRepository() {
        exams = new ArrayList<>();
        loadExam();
    }

    public void addExam() {
        Scanner sc = new Scanner(System.in);
        int id = exams.size() + 1;
        System.out.println("Enter number of questions for exam " + id + ": ");
        int TotalQuestions = sc.nextInt();
        Exam exam = new Exam(TotalQuestions);
        exam.addQuestions();
        exams.add(exam);
    }

    public void editExam(int id) {
        if (examExist(id)) {
            QuestionRepository qr = new QuestionRepository();
            Exam e = getExam(id);
            Scanner sc = new Scanner(System.in);

            //Display options: add more or remove
            boolean back = false;
            while (!back) {
                System.out.println("Options");
                System.out.println("1. Add new question");
                System.out.println("2. Remove question");
                System.out.println("3. Back");
                System.out.println("Enter choice: ");
                int choice = sc.nextInt();
                switch (choice) {
                    case 1: {
                        System.out.println("Enter question ID you want to add: ");
                        int qID = sc.nextInt();
                        if (qr.questionExist(qID)) {
                            if (e.checkExist(qID)) System.out.println("Question is already in the exam! Try again");
                            else e.addQuestion(qID);
                        }
                        else System.out.println("Question with ID " + qID + " is not exist! Try again");
                        break;
                    }
                    case 2: {
                        System.out.println("Enter question ID you want to remove: ");
                        int qID = sc.nextInt();
                        if (qr.questionExist(qID)) e.removeQuestion(qID);
                        else System.out.println("Question with ID " + qID + " is not exist! Try again");
                        break;
                    }
                    case 3: {
                        back = true;
                        break;
                    }
                    default:
                        System.out.println("Invalid choice! Try again");
                }
            }
        }
    }

    public void removeExam(int id) {
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
        if(!exams.isEmpty()) exams.clear();
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
