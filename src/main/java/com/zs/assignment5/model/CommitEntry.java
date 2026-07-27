package com.zs.assignment5.model;

import java.time.LocalDate;

public class CommitEntry {
    private final String hash;
    private final String author;
    private final LocalDate commitDate;
    private final String message;

    public CommitEntry(String hash, String author, LocalDate commitDate, String message) {
        this.hash = hash;
        this.author = author;
        this.commitDate = commitDate;
        this.message = message;
    }

    public String getHash() {
        return hash;
    }

    public String getAuthor() {
        return author;
    }

    public LocalDate getCommitDate() {
        return commitDate;
    }

    public String getMessage() {
        return message;
    }
}
