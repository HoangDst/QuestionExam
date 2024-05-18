package Menu;
import User.*;
import java.util.*;
import java.util.Scanner;

public class Menu {
    Scanner scanner = new Scanner(System.in);
    public void loginMenu() {
        System.out.println("Login Menu");
        System.out.println("1. Login");
        System.out.println("2. Create Account");
        System.out.println("3. Exit program");
        System.out.println("Choose your option: ");
        int i = scanner.nextInt();
        switch (i) {
            case 1:
                login();
            case 2:
                createAccount();
            case 3:
                break;
        }
    }
    public void login() {
        System.out.println("Enter username: ");
        String username = scanner.nextLine();
        System.out.println("Enter password: ");
        String password = scanner.nextLine();
        while (!User.authentication(username, password)) {
            System.out.println("Incorrect username/password. Please try again");
            System.out.println("Enter username: ");
            username = scanner.nextLine();
            System.out.println("Enter password: ");
            password = scanner.nextLine();
        }
        int i = User.getID();
        System.out.println("Login successfully");
        mainMenu();
    }

    public void createAccount() {
        User.addUser();
    }

    public void manageUser(int id) {
        System.out.println("Your Account");
        System.out.println("1. Edit username");
        System.out.println("2. Edit password");
        System.out.println("3. Edit name");
        System.out.println("4. Edit email");
        System.out.println("5. Delete Account");
        System.out.println("6. Back");
        System.out.println("Choose your option: ");
        int i = scanner.nextInt();
        Scanner scanner = new Scanner(System.in);
        switch (i) {
            case 1:
                System.out.println("Enter new username: ");
                String newUsername = scanner.nextLine();
                User.setUsername(newUsername);
            case 2:
                System.out.println("Enter new password: ");
                String newPassword = scanner.nextLine();
                User.setPassword(newPassword);
            case 3:
                System.out.println("Enter new name: ");
                String newName = scanner.nextLine();
                User.setName(newName);
            case 4:
                System.out.println("Enter new email: ");
                String newEmail = scanner.nextLine();
                User.setEmail(newEmail);
            case 5:
                id = User.getID();
                User.deleteUser(id);
            case 6:
                loginMenu();
        }
        scanner.close();
    }

    public void mainMenu() {
        System.out.println("Main Menu");
        System.out.println("1. Your Account");
        System.out.println("2. Question");
        System.out.println("3. Exam");
        System.out.println("4. Logout");
        System.out.println("Choose your option: ");
        int i = scanner.nextInt();
        switch (i) {
            case 1:
                manageUser();
            case 2:
                break;
            case 3:
                break;
            case 4:
                loginMenu();
        }
    }


}
