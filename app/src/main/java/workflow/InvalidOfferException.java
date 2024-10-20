package workflow;

public class InvalidOfferException extends RuntimeException{
    public InvalidOfferException(String message){
        super(message);
    }
}