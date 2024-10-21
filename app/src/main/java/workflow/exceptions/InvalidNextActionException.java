package workflow.exceptions;

public class InvalidNextActionException extends RuntimeException {
    public InvalidNextActionException(String message) {
        super(message);
    }
}
