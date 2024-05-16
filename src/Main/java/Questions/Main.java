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
        qr.addQuestion();



        List<Question> questions = qr.getQuestions();
        for (Question q : questions) System.out.println(q);
    }
}
