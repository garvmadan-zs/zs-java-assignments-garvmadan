
package com.zs.assignment5.services;

import com.zs.assignment5.exceptions.GitFileNotFoundException;
import com.zs.assignment5.exceptions.GitLogFormatException;
import com.zs.assignment5.exceptions.GitLogParseException;
import com.zs.assignment5.exceptions.IncompleteCommitMessageException;
import com.zs.assignment5.model.CommitAnalysisResult;
import com.zs.assignment5.model.CommitEntry;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GitLogAnalysisService {
    private static final List<DateTimeFormatter> GIT_DATE_FORMATTERS = Arrays.asList(
            DateTimeFormatter.ofPattern("EEE, d MMM yyyy HH:mm:ss XX", Locale.ENGLISH),
            DateTimeFormatter.ofPattern("EEE MMM d HH:mm:ss yyyy XX", Locale.ENGLISH),
            DateTimeFormatter.ofPattern("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH),
            DateTimeFormatter.ofPattern("EEE MMM d HH:mm:ss yyyy Z", Locale.ENGLISH)
    );
    private static final Pattern GIT_DATE_PATTERN = Pattern.compile(
            "^(Mon|Tue|Wed|Thu|Fri|Sat|Sun)\\s+(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)\\s+(\\d{1,2})\\s+(\\d{2}):(\\d{2}):(\\d{2})\\s+(\\d{4})\\s+([+-]\\d{4})$");
    private static final Pattern GIT_DATE_PATTERN_ALT = Pattern.compile(
            "^(Mon|Tue|Wed|Thu|Fri|Sat|Sun),\\s+(\\d{1,2})\\s+(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)\\s+(\\d{4})\\s+(\\d{2}):(\\d{2}):(\\d{2})\\s+([+-]\\d{4})$");

    public CommitAnalysisResult analyzeGitLog(String filePath, LocalDate thresholdDate) throws GitLogParseException, GitLogFormatException, IncompleteCommitMessageException, GitFileNotFoundException {
        List<CommitEntry> commits = parseGitLog(filePath, thresholdDate);
        return buildAnalysis(commits, thresholdDate);
    }

    public List<CommitEntry> parseGitLog(String filePath) throws GitLogParseException, GitLogFormatException, IncompleteCommitMessageException, GitFileNotFoundException {
        return parseGitLog(filePath, LocalDate.MIN);
    }

    public List<CommitEntry> parseGitLog(String filePath, LocalDate thresholdDate) throws GitLogParseException, GitLogFormatException, IncompleteCommitMessageException, GitFileNotFoundException {
        Path path = Paths.get(filePath);
        if (Files.notExists(path)) {
            throw new GitFileNotFoundException(filePath);
        }
        if (!Files.isRegularFile(path)) {
            throw new GitLogFormatException("The provided path is not a regular file: " + filePath);
        }

        try {
            List<String> lines = Files.readAllLines(path);
            if (lines.isEmpty()) {
                throw new GitLogFormatException("Git log file is empty.");
            }

            List<CommitEntry> commits = new ArrayList<>();
            List<String> currentBlock = new ArrayList<>();
            int currentBlockStartLineNumber = 0;

            for (int index = 0; index < lines.size(); index++) {
                String line = lines.get(index);
                String trimmed = line.trim();
                if (trimmed.startsWith("commit ")) {
                    if (!currentBlock.isEmpty()) {
                        CommitEntry parsedCommit = parseCommitBlock(currentBlock, thresholdDate, currentBlockStartLineNumber);
                        if (parsedCommit == null) {
                            break;
                        }
                        commits.add(parsedCommit);
                    }
                    currentBlock = new ArrayList<>();
                    currentBlockStartLineNumber = index + 1;
                    currentBlock.add(trimmed);
                } else {
                    currentBlock.add(trimmed);
                }
            }

            if (!currentBlock.isEmpty()) {
                CommitEntry parsedCommit = parseCommitBlock(currentBlock, thresholdDate, currentBlockStartLineNumber);
                if (parsedCommit != null) {
                    commits.add(parsedCommit);
                }
            }

            if (commits.isEmpty()) {
                throw new GitLogFormatException("The file format is not correct for git log data.");
            }
            return commits;
        } catch (IOException ex) {
            throw new GitLogFormatException("Unable to read the git log file: " + filePath, ex);
        }
    }

    private CommitEntry parseCommitBlock(List<String> block, LocalDate thresholdDate, int blockStartLineNumber) throws GitLogParseException, GitLogFormatException, IncompleteCommitMessageException {
        if (block.isEmpty()) {
            throw new GitLogFormatException("Encountered an empty commit block.");
        }

        String firstLine = block.get(0);
        if (!firstLine.startsWith("commit ")) {
            throw new GitLogFormatException("The file format is not correct for git log data.");
        }

        String hash = firstLine.substring("commit ".length()).trim();
        if (hash.isEmpty()) {
            throw new GitLogFormatException("Incomplete commit information: missing commit hash.");
        }

        String author = null;
        String dateLine = null;
        List<String> messageLines = new ArrayList<>();
        int problematicLineNumber = -1;
        String problematicLine = null;

        for (int index = 1; index < block.size(); index++) {
            String line = block.get(index);
            if (line.startsWith("Author:")) {
                author = extractAuthor(line);
            } else if (line.startsWith("Date:")) {
                dateLine = line.substring("Date:".length()).trim();
            } else if (line.startsWith("Merge:")) {
                continue;
            } else if (!line.isEmpty()) {
                messageLines.add(line);
                problematicLineNumber = blockStartLineNumber + index;
                problematicLine = line;
            }
        }

        if (author == null || author.isBlank()) {
            throw new GitLogFormatException("Incomplete commit information: missing author details.");
        }
        if (dateLine == null || dateLine.isBlank()) {
            throw new GitLogFormatException("Incomplete commit information: missing commit date.");
        }
        if (messageLines.isEmpty()) {
            throw new GitLogFormatException("Incomplete commit information: missing commit message.");
        }

        LocalDate commitDate;
        try {
            commitDate = parseGitDate(dateLine);
        } catch (DateTimeParseException ex) {
            throw new GitLogFormatException("Incomplete commit information: invalid commit date format.", ex);
        }

        if (commitDate.isBefore(thresholdDate)) {
            return null;
        }

        String messageText = String.join(" ", messageLines).trim();
        if (!isValidCommitMessage(messageText)) {
            throw new IncompleteCommitMessageException(
                    "Incomplete commit message: expected format 'Name | assignment no. | work done'",
                    problematicLineNumber + ": " + (problematicLine == null ? "<no message line>" : problematicLine));
        }

        return new CommitEntry(hash, author, commitDate, messageText);
    }

    private boolean isValidCommitMessage(String messageText) {
        if (messageText == null || messageText.isBlank()) {
            return false;
        }

        String trimmed = messageText.trim();
        if (trimmed.startsWith("Merge ")) {
            return true;
        }

        String[] parts = trimmed.split("\\|");
        if (parts.length != 3) {
            return false;
        }

        String[] normalizedParts = Arrays.stream(parts)
                .map(String::trim)
                .toArray(String[]::new);

        return !normalizedParts[0].isEmpty()
                && !normalizedParts[1].isEmpty()
                && !normalizedParts[2].isEmpty();
    }

    private String extractAuthor(String line) {
        String authorValue = line.substring("Author:".length()).trim();
        if (authorValue.contains("<") && authorValue.contains(">")) {
            int start = authorValue.indexOf('<');
            int end = authorValue.indexOf('>');
            return authorValue.substring(0, start).trim();
        }
        return authorValue;
    }

    private LocalDate parseGitDate(String dateLine) {
        String normalized = dateLine.replaceAll("\\s+", " ").trim();
        for (DateTimeFormatter formatter : GIT_DATE_FORMATTERS) {
            try {
                OffsetDateTime offsetDateTime = OffsetDateTime.parse(normalized, formatter);
                return offsetDateTime.toLocalDate();
            } catch (DateTimeParseException ignored) {
            }
        }

        Matcher matcher = GIT_DATE_PATTERN.matcher(normalized);
        if (matcher.matches()) {
            int day = Integer.parseInt(matcher.group(3));
            int month = monthNumber(matcher.group(2));
            int year = Integer.parseInt(matcher.group(7));
            return LocalDate.of(year, month, day);
        }

        Matcher altMatcher = GIT_DATE_PATTERN_ALT.matcher(normalized);
        if (altMatcher.matches()) {
            int day = Integer.parseInt(altMatcher.group(2));
            int month = monthNumber(altMatcher.group(3));
            int year = Integer.parseInt(altMatcher.group(4));
            return LocalDate.of(year, month, day);
        }

        throw new DateTimeParseException("Unsupported git log date format: " + dateLine, normalized, 0);
    }

    private int monthNumber(String monthName) {
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        for (int index = 0; index < months.length; index++) {
            if (months[index].equalsIgnoreCase(monthName)) {
                return index + 1;
            }
        }
        throw new IllegalArgumentException("Unsupported month name: " + monthName);
    }

    private boolean isWeekend(LocalDate date) {
        return date.getDayOfWeek() == java.time.DayOfWeek.SATURDAY || date.getDayOfWeek() == java.time.DayOfWeek.SUNDAY;
    }

    private CommitAnalysisResult buildAnalysis(List<CommitEntry> commits, LocalDate thresholdDate) {
        Map<String, Integer> totalCommitsByDeveloper = new TreeMap<>();
        Map<String, Map<LocalDate, Integer>> commitsByDeveloperByDay = new TreeMap<>();

        for (CommitEntry commit : commits) {
            if (commit.getCommitDate().isBefore(thresholdDate)) {
                continue;
            }

            totalCommitsByDeveloper.merge(commit.getAuthor(), 1, Integer::sum);
            commitsByDeveloperByDay.computeIfAbsent(commit.getAuthor(), key -> new LinkedHashMap<>())
                    .merge(commit.getCommitDate(), 1, Integer::sum);
        }

        List<String> inactiveDevelopers = findDevelopersInactiveForTwoDays(commitsByDeveloperByDay, thresholdDate);
        return new CommitAnalysisResult(totalCommitsByDeveloper, commitsByDeveloperByDay, inactiveDevelopers);
    }

    private List<String> findDevelopersInactiveForTwoDays(Map<String, Map<LocalDate, Integer>> commitsByDeveloperByDay, LocalDate thresholdDate) {
        if (commitsByDeveloperByDay.isEmpty()) {
            return Collections.emptyList();
        }

        List<String> inactiveDevelopers = new ArrayList<>();
        LocalDate latestDate = commitsByDeveloperByDay.values().stream()
                .flatMap(map -> map.keySet().stream())
                .max(LocalDate::compareTo)
                .orElse(thresholdDate);

        for (Map.Entry<String, Map<LocalDate, Integer>> entry : commitsByDeveloperByDay.entrySet()) {
            String developer = entry.getKey();
            Map<LocalDate, Integer> dailyCounts = entry.getValue();
            boolean inactiveForTwoDays = false;

            LocalDate currentDay = thresholdDate;
            while (!currentDay.isAfter(latestDate)) {
                LocalDate nextDay = currentDay.plusDays(1);
                if (isWeekend(currentDay) || isWeekend(nextDay)) {
                    currentDay = nextDay;
                    continue;
                }
                if (!currentDay.isAfter(latestDate) && !nextDay.isAfter(latestDate)) {
                    boolean noCommitToday = !dailyCounts.containsKey(currentDay);
                    boolean noCommitTomorrow = !dailyCounts.containsKey(nextDay);
                    if (noCommitToday && noCommitTomorrow) {
                        inactiveForTwoDays = true;
                        break;
                    }
                }
                currentDay = nextDay;
            }

            if (inactiveForTwoDays) {
                inactiveDevelopers.add(developer);
            }
        }

        return inactiveDevelopers;
    }
}
