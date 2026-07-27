
package com.zs.assignment5.exceptions;

public class GitFileNotFoundException extends Exception {
    public GitFileNotFoundException(String filePath) {
        super("Git file not found: " + filePath);
    }
}
