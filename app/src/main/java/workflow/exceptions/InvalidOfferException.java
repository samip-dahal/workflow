package workflow.exceptions;

/**
 * Exception thrown when an offer is invalid in the workflow.
 * This exception is typically used to indicate that the provided offer 
 * details or offer status does not comply with the expected conditions.
 */
public class InvalidOfferException extends RuntimeException {

    /**
     * Constructs a new InvalidOfferException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception.
     */
    public InvalidOfferException(String message) {
        super(message);
    }
}
