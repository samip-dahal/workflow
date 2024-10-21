package workflow.exceptions;

/**
 * Exception thrown when an invalid state transition is attempted in the workflow.
 * This exception is typically used to indicate that a user has tried to transition 
 * an offer to a state that is not permitted based on the current workflow logic.
 */
public class InvalidStateTransitionException extends RuntimeException {

    /**
     * Constructs a new InvalidStateTransitionException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception.
     */
    public InvalidStateTransitionException(String message) {
        super(message);
    }
}
