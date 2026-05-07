package br.edu.ifsp.scl.inscrypted;

public class NoCardSelectedException extends RuntimeException {
    public NoCardSelectedException(String message) {
        super(message);
    }
}
