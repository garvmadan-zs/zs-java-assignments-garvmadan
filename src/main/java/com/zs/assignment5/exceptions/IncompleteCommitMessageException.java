
package com.zs.assignment5.exceptions;

public class IncompleteCommitMessageException extends Exception {
    public IncompleteCommitMessageException(String message, String line, Throwable cause) {
        super(message + "\nProblem line: " + line, cause);
    }

    public IncompleteCommitMessageException(String message, String line) {
        super(message + "\nProblem line: " + line);
    }
}
