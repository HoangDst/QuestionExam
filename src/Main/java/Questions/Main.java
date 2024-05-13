package Questions;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        QuestionRepository questionRepository = new QuestionRepository();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n==================================");
            System.out.println("1. Add question");
            System.out.println("2. Update question");
            System.out.println("3. Delete question");
            System.out.println("4. Display all questions");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    questionRepository.addQuestion();
                    System.out.println("Add question successfully !");
                    break;
                case 2:
                    System.out.println("Enter id of question:");
                    int idToUpdate = scanner.nextInt();
                    scanner.nextLine();
                    questionRepository.updateQuestion(idToUpdate);
                    System.out.println("Updated successfully !");
                    break;
                case 3:
                    System.out.println("Enter id of question:");
                    int idToDelete = scanner.nextInt();
                    scanner.nextLine();
                    questionRepository.deleteQuestion(idToDelete);
                    System.out.println("Deleted successfully !");
                    break;
                case 4:
                    System.out.println("List of questions:");
                    List<Question> allQuestions = questionRepository.getAllQuestions();
                    for (Question question : allQuestions) {
                        System.out.println(question.toString());
                    }
                    break;
                case 5:
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again !");
            }
        }

    }
}