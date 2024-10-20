package workflow;

public class InvalidNextActionException extends RuntimeException {
    public InvalidNextActionException(String message) {
        super(message);
    }
}
