package User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import DatabaseConfiguration.DBConnector;
import Lib.Table;
public class UserRepository {
    private final String USERNAME = "admin";
    private final String PASSWORD = "123456";
    private static int nextID = 1;
    private List<User> users;
    private DBConnector connector = new DBConnector();

    public UserRepository() {
        users = new ArrayList<>();
        loadUser();
        if (users.isEmpty()) {
            users.add(new User(nextID, USERNAME, PASSWORD, "", ""));
            nextID++;
        }
    }

    public void addUser() {
        int id = nextID;
        nextID++;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter username: ");
        String username = scanner.nextLine();
        System.out.println("Enter password: ");
        String password = scanner.nextLine();
        System.out.println("Enter email: ");
        String email = scanner.nextLine();
        System.out.println("Enter Name: ");
        String name = scanner.nextLine();
        scanner.close();
        User user = new User(id, username, password, email, name);
        users.add(user);
    }
    public void editUser(int id) {
        for (User u : users) {
            if (u.getID() == id) {
                Scanner sc = new Scanner(System.in);
                boolean back = false;
                while (!back) {
                    try {
                        System.out.println("Options");
                        System.out.println("1. Change username");
                        System.out.println("2. Change password");
                        System.out.println("3. Change email");
                        System.out.println("4. Change name");
                        System.out.println("5. Back");
                        System.out.println("Enter choice: ");
                        int choice = sc.nextInt();
                        switch (choice) {
                            case 1: {
                                System.out.println("Enter new username: ");
                                String username = sc.nextLine();
                                u.setUsername(username);
                                break;
                            }
                            case 2: {
                                System.out.println("Enter old password: ");
                                String password = sc.nextLine();
                                if (u.getPassword() == password) {
                                    System.out.println("Enter new password: ");
                                    String newPassword = sc.nextLine();
                                    u.setPassword(newPassword);
                                }
                                else {
                                    System.out.println("Incorrect password!");
                                }
                                break;
                            }
                            case 3: {
                                System.out.println("Enter new email: ");
                                String email = sc.nextLine();
                                u.setEmail(email);
                                break;
                            }
                            case 4: {
                                System.out.println("Enter new name: ");
                                String name = sc.nextLine();
                                u.setName(name);
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
                break;
            }
        }
    }
    public void removeUser(int id) {
        for (User u : users) {
            if (u.getID() == id) {
                users.remove(u);
                break;
            }
        }
    }
    public void forgetPassword() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your username: ");
        String username = sc.nextLine();
        System.out.println("Enter your email: ");
        String email = sc.nextLine();
        for (User u : users) {
            if (u.getUsername() == username && u.getEmail() == email) {
                System.out.println("Enter new password: ");
                String password = sc.nextLine();
                u.setPassword(password);
                return;
            }
        }
        System.out.println("Username or email is not exist!");
    }
    private boolean isExist(String username, String email) {
        for (User u : users) {
            if (u.isExist(username, email)) return true;
        }
        return false;
    }
    public boolean authentication(String username, String password) {
        for (User u : users) {
            if (u.authentication(username, password)) return true;
        }
        return false;
    }

    public boolean IDExist(int id) {
        for (User u : users) {
            if (u.getID() == id) return true;
        }
        return false;
    }
    private void deleteDatabaseUser() {
        String query = "DELETE FROM answers";
        connector.execution(query);
    }
    public void saveUser() {
        deleteDatabaseUser();
        for (User u : users) {
            String query = "INSERT INTO users (ID, Username, Password, Email, Name) VALUES (?, ?, ?, ?, ?)";
            List<Object> parameters = Arrays.asList(
                    u.getID(),
                    u.getUsername(),
                    u.getPassword(),
                    u.getEmail(),
                    u.getName()
            );
            connector.execution(query, parameters);
        }
    }
    public void loadUser() {
        if (!users.isEmpty()) users.clear();
        String query = "SELECT * FROM users";

        try (PreparedStatement preparedStatement = connector.getConnection().prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String username = resultSet.getString("Username");
                String password = resultSet.getString("Password");
                String email = resultSet.getString("Email");
                String name = resultSet.getString("Name");
                User u = new User(id, username, password, email, name);
                users.add(u);
            }
            if (!users.isEmpty()) nextID = users.get(users.size() - 1).getID() + 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void display() {
        Table table = new Table("ID", "Username", "Password", "Email", "Name");
        for (User u : users) {
            String id = Integer.toString(u.getID());
            String username = u.getUsername();
            String password = u.getPassword();
            String email = u.getEmail();
            String name = u.getName();
            table.addRow(id, username, password, email, name);
        }
        System.out.println(table);
    }
}
