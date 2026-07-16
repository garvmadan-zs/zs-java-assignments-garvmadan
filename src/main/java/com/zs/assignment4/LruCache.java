package zs.assignment4;

import zs.assignment4.controller.Controller;
import java.util.Scanner;

public class LruCache{
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Controller controller = new Controller(scanner);
        controller.showMenu();
        scanner.close();
    }
}
