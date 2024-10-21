package workflow.servicesapi;

import java.util.Map;

import workflow.OfferDetails;
import workflow.exceptions.InvalidNextActionException;
import workflow.exceptions.InvalidStateTransitionException;
import workflow.exceptions.InvalidUserException;

/**
 * Interface representing the operations related to managing an Offer.
 */
public interface IOfferService {

    /**
     * Submits a new offer.
     *
     * @param buyerUserId   the ID of the buyer submitting the offer
     * @param sellerUserId  the ID of the seller receiving the offer
     * @param offerDetails  the details of the offer being submitted (product name, product quantity, product price)
     * @return a unique ID of the submitted offer
     */
    String submit(String buyerUserId, String sellerUserId, OfferDetails offerDetails);

    /**
     * Accepts an offer for the specified user.
     *
     * @param userId the ID of the user accepting the offer
     * @throws InvalidStateTransitionException if the state transition is not allowed
     * @throws InvalidUserException if the user ID is invalid
     * @throws InvalidNextActionException if the next action is not allowed from the current state
     */
    void accept(String userId) 
            throws InvalidStateTransitionException, InvalidUserException, InvalidNextActionException;

    /**
     * Cancels an offer for the specified user.
     *
     * @param userId the ID of the user canceling the offer
     * @throws InvalidStateTransitionException if the state transition is not allowed
     * @throws InvalidUserException if the user ID is invalid
     * @throws InvalidNextActionException if the next action is not allowed from the current state
     */
    void cancel(String userId) 
            throws InvalidStateTransitionException, InvalidUserException, InvalidNextActionException;

    /**
     * Proposes an update to the offer for the specified user.
     *
     * @param userId the ID of the user proposing the update
     * @param offerDetails the updated details of the offer being proposed
     * @throws InvalidStateTransitionException if the state transition is not allowed
     * @throws InvalidUserException if the user ID is invalid
     * @throws InvalidNextActionException if the next action is not allowed from the current state
     */
    void proposeUpdate(String userId, OfferDetails offerDetails) 
            throws InvalidStateTransitionException, InvalidUserException, InvalidNextActionException;

    /**
     * Withdraws an offer for the specified user.
     *
     * @param userId the ID of the user withdrawing the offer
     * @throws InvalidStateTransitionException if the state transition is not allowed
     * @throws InvalidUserException if the user ID is invalid
     * @throws InvalidNextActionException if the next action is not allowed from the current state
     */
    void withdraw(String userId) 
            throws InvalidStateTransitionException, InvalidUserException, InvalidNextActionException;

    /**
     * Updates private data for the specified user in an offer.
     *
     * @param userId the ID of the user updating the private data
     * @param privateData a map containing key-value pairs representing the private data
     */
    void updatePrivateData(String userId, Map<String, String> privateData);
}

