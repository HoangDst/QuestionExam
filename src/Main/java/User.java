package User;

public class User {
    private int ID = 1;
    private String username;
    private String password;
    private String email;
    private String name;

    public int getID() {
        return this.ID;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User(int ID, String username, String password, String email, String name) {
        this.ID = ID;
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
    }

    public void addUser(User[] User) {
        ID++;
        User[ID].setUsername(username);
        User[ID].setPassword(password);
        User[ID].setEmail(email);
        User[ID].setName(name);
        System.out.println("User " + username + " added successfully.");
    }

    public void changePassword(int ID, String newPassword) {
        this.password = newPassword;
        System.out.println("Password for user changed successfully.");
    }

    public void forgetPassword(int ID) {
        this.password = "123456";
        System.out.println("Password for user reset successfully.");
    } //forget password, change the password to 123456

    public void deleteUser(int ID) {
        if (this.ID == 0) {
            System.out.println("User does not exist.");
        }
        else {
            System.out.println("Deleting user with ID: " + this.ID);
            this.ID = 0;
            this.username = null;
            this.password = null;
            this.email = null;
            this.name = null;
            System.out.println("User deleted successfully.");
        }
    }

    public boolean authentication(String enteredUsername, String enteredPassword) {
        return (enteredUsername.equals(this.username) && enteredPassword.equals(this.password));
    }

    public String displayUser() {
        return "id = " + getID()
                + "\nusername = " + getUsername()
                + "\npassword = " + getPassword()
                + "\nname = " + getName()
                + "\nemail = " + getEmail();
    }
}