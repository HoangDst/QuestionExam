package Menu;
import User.*;
import Exams.*;
import Lib.Table;
import Questions.*;
import java.util.*;
import java.io.IOException;

public class Menu {
    private Scanner sc;
    private UserRepository ur = new UserRepository();
    private QuestionRepository qr = new QuestionRepository();
    private ExamRepository er = new ExamRepository();
    public Menu() {
        sc = new Scanner(System.in);
    }

    public void loginMenu() {
        boolean exit = false;
        while (!exit) {

            Table loginTable = new Table("", "Login Menu");
            loginTable.addRow("1", "Login");
            loginTable.addRow("2", "Forget password");
            loginTable.addRow("3", "Exit");
            System.out.println(loginTable);


            try {
                System.out.println("Enter choice: ");
                int lc = sc.nextInt();
                switch (lc) {
                    case 1: {
                        sc.nextLine();
                        System.out.println("Enter username: ");
                        String username = sc.nextLine();
                        System.out.println("Enter password: ");
                        String password = sc.nextLine();
                        if (ur.authentication(username, password)) {
                            System.out.println("Login successful! \n\n\n");
                            mainMenu();
                        }
                        else System.out.println("Login fail! Invalid username, password");
                        break;
                    }
                    case 2: {
                        ur.forgetPassword();
                        break;
                    }
                    case 3: {
                        exit = true;
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
    
    private void mainMenu() {
        boolean back = false;

        while (!back) {
            Table mainMenu = new Table("", "Main menu");
            mainMenu.addRow("1", "Account");
            mainMenu.addRow("2", "Question");
            mainMenu.addRow("3", "Exam");
            mainMenu.addRow("4", "Back");
            System.out.println(mainMenu);

            try {
                System.out.println("Enter choice");
                int mc = sc.nextInt();
                sc.nextLine();
                switch (mc) {
                    case 1: {
                        userMenu();
                        break;
                    }
                    case 2: {
                        questionMenu();
                        break;
                    }
                    case 3: {
                        examMenu();
                        break;
                    }
                    case 4: {
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
    private void userMenu() {
        boolean back = false;
        while (!back) {
            Table userMenu = new Table("", "User Menu");
            userMenu.addRow("1", "Display all users");
            userMenu.addRow("2", "Add user");
            userMenu.addRow("3", "Edit user");
            userMenu.addRow("4", "Delete user");
            userMenu.addRow("5", "Back");
            System.out.println(userMenu);

            try {
                System.out.println("Enter choice");
                int uc = sc.nextInt();
                sc.nextLine();
                switch (uc) {
                    case 1: {
                        ur.display();
                        break;
                    }
                    case 2: {
                        ur.addUser();
                        ur.saveUser();
                        break;
                    }
                    case 3: {
                        try {
                            System.out.println("Enter user id you want to edit: ");
                            int id = sc.nextInt();
                            sc.nextLine();
                            if (ur.IDExist(id)) {
                                ur.editUser(id);
                                ur.saveUser();
                            }
                            else System.out.println("Invalid user id!");
                        }
                        catch (InputMismatchException ex) {
                            System.out.println("Must enter a integer value! Try again");
                            sc.nextLine();
                        }
                        break;
                    }
                    case 4: {
                        try {
                            System.out.println("Enter user id you want to delete: ");
                            int id = sc.nextInt();
                            sc.nextLine();
                            if (ur.IDExist(id)) {
                                ur.removeUser(id);
                                ur.saveUser();
                            }
                            else System.out.println("Invalid user id!");
                        }
                        catch (InputMismatchException ex) {
                            System.out.println("Must enter a integer value! Try again");
                            sc.nextLine();
                        }
                        break;
                    }
                    case 5: {
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
    private void questionMenu() {
        boolean back = false;

        while (!back) {
            Table questionMenu = new Table("", "Question menu");
            questionMenu.addRow("1", "Display all question");
            questionMenu.addRow("2", "Add question");
            questionMenu.addRow("3", "Edit question");
            questionMenu.addRow("4", "Delete question");
            questionMenu.addRow("5", "Filter");
            questionMenu.addRow("6", "Back");
            System.out.println(questionMenu);

            try {
                System.out.println("Enter choice:");
                int qc = sc.nextInt();
                sc.nextLine();
                switch (qc) {
                    case 1: {
                        qr.display(qr.getQuestions());
                        break;
                    }
                    case 2: {
                        qr.addQuestion();
                        qr.saveQuestions();
                        break;
                    }
                    case 3: {
                        try {
                            System.out.println("Enter question id you want to edit: ");
                            int id = sc.nextInt();
                            sc.nextLine();
                            if (qr.questionExist(id)) {
                                qr.updateQuestion(id);
                                qr.saveQuestions();
                            }
                            else System.out.println("Invalid question id!");
                        }
                        catch (InputMismatchException ex) {
                            System.out.println("Must enter a integer value! Try again");
                            sc.nextLine();
                        }
                        break;
                    }
                    case 4: {
                        try {
                            System.out.println("Enter question id you want to delete: ");
                            int id = sc.nextInt();
                            sc.nextLine();
                            if (qr.questionExist(id)) {
                                qr.deleteQuestion(id);
                                qr.saveQuestions();
                            }
                            else System.out.println("Invalid question id!");
                        }
                        catch (InputMismatchException ex) {
                            System.out.println("Must enter a integer value! Try again");
                            sc.nextLine();
                        }
                        break;
                    }
                    case 5: {
                        List<Question> questions = qr.filter();
                        qr.display(questions);
                        break;
                    }
                    case 6: {
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
    private void examMenu() {
        boolean back = false;
        while (!back) {
            Table userMenu = new Table("", "Exam Menu");
            userMenu.addRow("1", "Display all exams");
            userMenu.addRow("2", "Add exam");
            userMenu.addRow("3", "Edit exam");
            userMenu.addRow("4", "Delete exam");
            userMenu.addRow("5", "Back");
            System.out.println(userMenu);

            try {
                System.out.println("Enter choice");
                int uc = sc.nextInt();
                sc.nextLine();
                switch (uc) {
                    case 1: {
                        er.display();
                        break;
                    }
                    case 2: {
                        er.addExam();
                        er.saveExam();
                        break;
                    }
                    case 3: {
                        try {
                            System.out.println("Enter exam id you want to edit: ");
                            int id = sc.nextInt();
                            sc.nextLine();
                            if (er.examExist(id)) {
                                er.editExam(id);
                                er.saveExam();
                            }
                            else System.out.println("Invalid exam id!");
                        }
                        catch (InputMismatchException ex) {
                            System.out.println("Must enter a integer value! Try again");
                            sc.nextLine();
                        }
                        break;
                    }
                    case 4: {
                        try {
                            System.out.println("Enter exam id you want to delete: ");
                            int id = sc.nextInt();
                            sc.nextLine();
                            if (er.examExist(id)) {
                                er.removeExam(id);
                                er.saveExam();
                            }
                            else System.out.println("Invalid exam id!");
                        }
                        catch (InputMismatchException ex) {
                            System.out.println("Must enter a integer value! Try again");
                            sc.nextLine();
                        }
                        break;
                    }
                    case 5: {
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
