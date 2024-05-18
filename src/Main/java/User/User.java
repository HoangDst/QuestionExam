package User;

import java.util.Scanner;
import java.util.*;

public class User {
    private int ID;
    private String username;
    private String password;
    private String email;
    private String name;

    public User() {
    }

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

    public boolean isExist(String username, String email) {
        return this.username.equals(username) || this.email.equals(email);
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
