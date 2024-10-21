package workflow.exceptions;

/**
 * Exception thrown when an invalid user is involved in the workflow.
 * This exception is typically used to indicate that a user does not have the necessary
 * permissions or is not recognized as a valid participant in the workflow.
 */
public class InvalidUserException extends RuntimeException {

    /**
     * Constructs a new InvalidUserException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception.
     */
    public InvalidUserException(String message) {
        super(message);
    }
}
