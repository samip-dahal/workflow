package workflow.exceptions;

/**
 * Exception thrown when an invalid action is attempted in the workflow.
 * This exception is typically used to signal that a user has attempted 
 * an action that is not allowed based on the current state of the workflow.
 */
public class InvalidNextActionException extends RuntimeException {
    
    /**
     * Constructs a new InvalidNextActionException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception.
     */
    public InvalidNextActionException(String message) {
        super(message);
    }
}
