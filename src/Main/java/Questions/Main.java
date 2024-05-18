package Questions;

import Exams.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

// Press Shift twice to open the Search Everywhere dialog and type show whitespaces,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        QuestionRepository qr = new QuestionRepository();
        //qr.addQuestion();
        //qr.deleteDatabaseQuestions();
        List<Question> questions = qr.getQuestions();
//        for (Question q : questions) {
//            System.out.println(q);
//        }

        //for (Question q : questions) System.out.println(q + "\n\n\n");
       // qr.updateQuestion(1);
        qr.filter();
        qr.saveQuestions();
        //qr.deleteQuestion(1);
        //List<Question> fq = qr.filter();
        //System.out.println("\n\n\n");
        //for (Question q : fq) System.out.println(q);

        ExamRepository er = new ExamRepository();
        System.out.println("\n\n\n");
        List<Exam> exams = er.getExams();
        for (Exam e : exams) System.out.println(e + "\n\n\n");
        er.removeExam(2);
        for (Exam e : exams) System.out.println(e + "\n\n\n");
        er.saveExam();


        //for (Question q : questions) System.out.println(q);
        //qr.saveQuestions();
    }
}
