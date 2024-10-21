package workflow.exceptions;

public class InvalidOfferException extends RuntimeException{
    public InvalidOfferException(String message){
        super(message);
    }
}