package br.edu.ifsp.scl.inscrypted;

public class InvalidCardPlacementException extends RuntimeException {
    public InvalidCardPlacementException(String message) {
        super(message);
    }
}
