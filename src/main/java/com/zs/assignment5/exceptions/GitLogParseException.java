
package com.zs.assignment5.exceptions;

public class GitLogParseException extends Exception {
    public GitLogParseException(String message) {
        super(message);
    }

    public GitLogParseException(String message, Throwable cause) {
        super(message, cause);
    }
}

