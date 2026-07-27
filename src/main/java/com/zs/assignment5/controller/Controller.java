package com.zs.assignment5.controller;
import com.zs.assignment5.exceptions.GitFileNotFoundException;
import com.zs.assignment5.exceptions.GitLogFormatException;
import com.zs.assignment5.exceptions.IncompleteCommitMessageException;
import com.zs.assignment5.model.CommitAnalysisResult;
import com.zs.assignment5.services.GitLogAnalysisService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class Controller {

    private final GitLogAnalysisService gitLogAnalysisService;
    private static final DateTimeFormatter INPUT_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Controller() {

        this.gitLogAnalysisService = new GitLogAnalysisService();
    }

    public void run(String filePath, String dateText) {
        try {
            LocalDate thresholdDate = parseDate(dateText);
            CommitAnalysisResult result = gitLogAnalysisService.analyzeGitLog(filePath, thresholdDate);
            printReport(filePath, thresholdDate, result);
        } catch (GitFileNotFoundException | GitLogFormatException | IncompleteCommitMessageException ex) {
            System.out.println("Error: " + ex.getMessage());
        } catch (DateTimeParseException ex) {
            System.out.println("Error: Please enter the date in yyyy-MM-dd format.");
        }
    }



    private LocalDate parseDate(String dateText) {
        return LocalDate.parse(dateText, INPUT_DATE_FORMATTER);
    }

    private void printReport(String filePath, LocalDate thresholdDate, CommitAnalysisResult result) {
        System.out.println("\nGit log analysis for: " + filePath);
        System.out.println("Threshold date: " + thresholdDate);

        System.out.println("\n1) Total commits by each developer since " + thresholdDate + ":");
        if (result.getTotalCommitsByDeveloper().isEmpty()) {
            System.out.println("No commits found on or after the threshold date.");
        } else {
            for (Map.Entry<String, Integer> entry : result.getTotalCommitsByDeveloper().entrySet()) {
                System.out.println("- " + entry.getKey() + ": " + entry.getValue());
            }
        }

        System.out.println("\n2) Commits by each developer since " + thresholdDate + " for each day:");
        if (result.getCommitsByDeveloperByDay().isEmpty()) {
            System.out.println("No daily commit data found.");
        } else {
            for (Map.Entry<String, Map<LocalDate, Integer>> entry : result.getCommitsByDeveloperByDay().entrySet()) {
                System.out.println("- " + entry.getKey() + ":");
                Map<LocalDate, Integer> sortedDailyCounts = new TreeMap<>(entry.getValue());
                for (Map.Entry<LocalDate, Integer> dayEntry : sortedDailyCounts.entrySet()) {
                    System.out.println("  * " + dayEntry.getKey() + " -> " + dayEntry.getValue());
                }
            }
        }

        System.out.println("\n3) Developers inactive for two consecutive days:");
        if (result.getDevelopersInactiveForTwoDays().isEmpty()) {
            System.out.println("No developers met the two-day inactivity rule.");
        } else {
            for (String developer : result.getDevelopersInactiveForTwoDays()) {
                System.out.println("- " + developer);
            }
        }
    }
}
