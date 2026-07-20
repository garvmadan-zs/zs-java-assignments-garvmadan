
package com.zs.assignment5.exceptions;

public class GitLogFormatException extends Exception {
    public GitLogFormatException(String message) {
        super(message);
    }

    public GitLogFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
