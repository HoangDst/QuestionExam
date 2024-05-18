package Menu;

import java.util.Scanner;
import java.util.*;
public class Main {
    public static void main(String[] args) {
        Menu menu = new Menu();
        menu.loginMenu();
        Scanner scanner = new Scanner(System.in);
        int i = scanner.nextInt();
    }
}
