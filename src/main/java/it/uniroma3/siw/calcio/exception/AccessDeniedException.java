package it.uniroma3.siw.calcio.exception;

public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException() {
        super("User not authorized to modify this comment");
    }
}
