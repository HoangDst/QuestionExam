 package Exams;

import DatabaseConfiguration.DBConnector;
import Questions.*;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

 public class ExamRepository {
    private static int nextID = 1;
    List<Exam> exams;
    DBConnector connector = new DBConnector();

    public ExamRepository() {
        exams = new ArrayList<>();
        loadExam();
    }

    public List<Exam> getExams() {
        return exams;
    }

    public void addExam() {
        Scanner sc = new Scanner(System.in);
        int id = nextID;
        nextID++;
        int totalQuestions;
        QuestionRepository qr = new QuestionRepository();
        while (true) {
            try {
                System.out.println("Enter number of questions for exam " + id + ": ");
                totalQuestions = sc.nextInt();
                if (totalQuestions > 0 && totalQuestions <= 100 && totalQuestions <= qr.getQuestions().size()) break;
                else System.out.println("Number of questions must be between 1 and 100, " +
                        "and not greater than total questions in the repository");
            }
            catch (InputMismatchException e) {
                System.out.println("Must enter a integer value! Try again");
                sc.nextLine();
            }
        }
        Exam exam = new Exam(id, totalQuestions);
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
                try {
                    System.out.println("Options");
                    System.out.println("1. Add new question");
                    System.out.println("2. Remove question");
                    System.out.println("3. Back");
                    System.out.println("Enter choice: ");
                    int choice = sc.nextInt();
                    switch (choice) {
                        case 1: {
                            try {
                                System.out.println("Enter question ID you want to add: ");
                                int qID = sc.nextInt();
                                if (qr.questionExist(qID)) {
                                    if (e.checkExist(qID)) System.out.println("Question is already in the exam! Try again");
                                    else {
                                        e.addQuestion(qID);
                                        System.out.println("Question added successfully!");
                                    }
                                }
                                else System.out.println("Question with ID " + qID + " is not exist! Try again");
                            }
                            catch (InputMismatchException ex) {
                                System.out.println("Must enter a integer value! Try again");
                                sc.nextLine();
                            }
                            break;
                        }
                        case 2: {
                            try {
                                System.out.println("Enter question ID you want to remove: ");
                                int qID = sc.nextInt();
                                if (qr.questionExist(qID)){
                                    e.removeQuestion(qID);
                                    System.out.println("Question removed successfully!");
                                }
                                else System.out.println("Question with ID " + qID + " is not exist! Try again");
                            }
                            catch (InputMismatchException ex) {
                                System.out.println("Must enter a integer value! Try again");
                                sc.nextLine();
                            }
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
                catch (InputMismatchException ex) {
                    System.out.println("Must enter a integer value! Try again");
                    sc.nextLine();
                }

            }
        }
    }

    public void removeExam(int id) {
        for (Exam e : exams) {
            if (e.getId() == id) {
                exams.remove(e);
                System.out.println("Exam removed successfully!");
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
                size--;
                if (size > 0) str += ",";
            }
            String query = "INSERT INTO exams (ID, questionIDs) VALUES (?, ?)";
            List<Object> parameter = Arrays.asList(e.getId(), str);
            connector.execution(query,parameter);
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
            if (!exams.isEmpty()) nextID = exams.get(exams.size() - 1).getId() + 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void display() {
        for (Exam e : exams) {
            e.display();
            System.out.println("\n\n");
        }
    }
}
