package workflow;

public interface IOfferService {
    String submit(String buyerUserId, String sellerUserId, OfferDetails offerDetails);
    void accept(String userId) throws InvalidStateTransitionException, InvalidUserException, InvalidNextActionException;
    void cancel(String userId) throws InvalidStateTransitionException, InvalidUserException, InvalidNextActionException;
    void proposeUpdate(String userId, OfferDetails offerDetails) throws InvalidStateTransitionException, InvalidUserException, InvalidNextActionException;
    void withdraw(String userId) throws InvalidStateTransitionException, InvalidUserException, InvalidNextActionException;
}
