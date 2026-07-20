
package com.zs.assignment5;

import com.zs.assignment5.controller.controller;
import java.util.Scanner;

public class GitLogParser {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        controller controller = new controller();

        String filePath = args.length > 0 ? args[0] : "";


        if (filePath.isBlank()) {
            System.out.print("Enter the git log file path: ");
            filePath = scanner.nextLine().trim();
        }
        String dateText = args.length > 1 ? args[1] : "";
        if (dateText.isBlank()) {
            System.out.print("Enter the date (yyyy-MM-dd): ");
            dateText = scanner.nextLine().trim();
        }

        controller.run(filePath, dateText);
        scanner.close();
    }
}
