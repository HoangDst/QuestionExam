package Questions;

import Exams.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import Lib.Table;
public class Main {
    public static void main(String[] args) {

        ExamRepository er = new ExamRepository();
        List<Exam> exams = er.getExams();
        for (Exam e : exams) e.display();

        QuestionRepository qr = new QuestionRepository();
        qr.updateQuestion(2);
        qr.saveQuestions();
/*
        Table table = new Table("Firstname", "Lastname", "Email", "Phone");
        table.setColumnAlignment(3, false);
        table.addRow("John", "Doe", "johndoe@nothing.com", "+2137999999");
        table.addRow("Jane", "Doe", "janedoe@nothin.com", "+21379aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\naaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa9999");
        System.out.println(table);*/
    }
}
