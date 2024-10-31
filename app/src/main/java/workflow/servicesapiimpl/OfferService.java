package workflow.servicesapiimpl;

import java.util.HashMap;
import java.util.Map;
import workflow.exceptions.InvalidNextActionException;
import workflow.exceptions.InvalidOfferException;
import workflow.exceptions.InvalidStateTransitionException;
import workflow.exceptions.InvalidUserException;

import workflow.servicesapi.IOfferService;
import workflow.Offer;
import workflow.OfferDetails;
import workflow.OfferState;
import workflow.Action;

/**
 * Implementation of the IOfferService interface to handle offer operations 
 * such as submission, acceptance, cancellation, proposal for update, 
 * withdrawal, and private data update.
 */
public class OfferService implements IOfferService {

    // Maps to associate buyers and sellers with their offers
    private Map<String, String> buyerToOfferMap = new HashMap<>();
    private Map<String, String> sellerToOfferMap = new HashMap<>();
    private Map<String, Offer> offerMap = new HashMap<>();

    /**
     * Validates the given userId to ensure it's neither null nor empty.
     * 
     * @param userId the ID of the user to validate
     * @throws InvalidUserException if the userId is invalid
     */
    private void validateUser(String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new InvalidUserException("Invalid user ID: " + userId);
        }
    }

    /**
     * Retrieves an offer based on its unique offer ID.
     * 
     * @param offerId the unique ID of the offer
     * @return the Offer object if found; otherwise null
     */
    public Offer getOffer(String offerId) {
        return offerMap.get(offerId);
    }

    /**
     * Validates offer details to ensure product information is complete and 
     * contains no negative values.
     * 
     * @param offerDetails the OfferDetails object to validate
     * @throws IllegalArgumentException if any offer detail is invalid
     */
    private void validateOfferDetails(final OfferDetails offerDetails) {
        if (offerDetails.getProductName() == null) {
            throw new IllegalArgumentException("Product name cannot be null");
        }
        if (offerDetails.getProductQuantity() < 0) {
            throw new IllegalArgumentException("Product quantity cannot be a negative value");
        }
        if (offerDetails.getProductPrice() < 0) {
            throw new IllegalArgumentException("Product price cannot be a negative value");
        }
    }

    /**
     * Retrieves an offer associated with a specific userId.
     * 
     * @param userId the user ID for retrieving the offer
     * @return the Offer associated with the user ID
     * @throws InvalidOfferException if no offer is found for the user ID
     */
    Offer getOfferFromUserId(final String userId) {
        String offerId = buyerToOfferMap.get(userId);
        if (offerId == null) {
            offerId = sellerToOfferMap.get(userId);
            if (offerId == null) {
                throw new InvalidOfferException("Offer not found");
            }
        }
        return getOffer(offerId);
    }

    /**
     * Submits a new offer with buyer, seller, and offer details.
     * 
     * @param buyerUserId  the ID of the buyer
     * @param sellerUserId the ID of the seller
     * @param offerDetails the details of the offer
     * @return the unique ID of the submitted offer
     */
    @Override
    public String submit(String buyerUserId, String sellerUserId, OfferDetails offerDetails) {
        validateOfferDetails(offerDetails);
        validateUser(buyerUserId);
        validateUser(sellerUserId);

        Offer offer = Offer.of(buyerUserId, sellerUserId, offerDetails);
        offerMap.put(offer.getOfferId(), offer);
        buyerToOfferMap.put(buyerUserId, offer.getOfferId());
        sellerToOfferMap.put(sellerUserId, offer.getOfferId());

        return offer.getOfferId();
    }

    /**
     * Accepts an offer on behalf of a user and updates the offer state.
     * 
     * @param userId the ID of the user accepting the offer
     * @throws InvalidStateTransitionException, InvalidUserException, InvalidNextActionException if any state transitions or user validations fail
     */
    @Override
    public void accept(String userId)
            throws InvalidStateTransitionException, InvalidUserException, InvalidNextActionException {
        validateUser(userId);
        Offer offer = getOfferFromUserId(userId);
        boolean isBuyer = offer.isBuyer(userId);
        
        offer.transition(OfferState.ACCEPTED, offer.getCurrentState(), Action.ACCEPT, userId, isBuyer);
    }

    /**
     * Cancels an offer on behalf of a user and updates the offer state.
     * 
     * @param userId the ID of the user cancelling the offer
     * @throws InvalidStateTransitionException, InvalidUserException, InvalidNextActionException if any state transitions or user validations fail
     */
    @Override
    public void cancel(String userId)
            throws InvalidStateTransitionException, InvalidUserException, InvalidNextActionException {
        validateUser(userId);
        Offer offer = getOfferFromUserId(userId);
        boolean isBuyer = offer.isBuyer(userId);
        
        offer.transition(OfferState.CANCELLED, offer.getCurrentState(), Action.CANCEL, userId, isBuyer);
    }

    /**
     * Proposes an update to an offer on behalf of a user.
     * 
     * @param userId the ID of the user proposing the update
     * @param offerDetails the new offer details proposed
     * @throws InvalidStateTransitionException, InvalidUserException, InvalidNextActionException if any state transitions or user validations fail
     */
    @Override
    public void proposeUpdate(String userId, OfferDetails offerDetails)
            throws InvalidStateTransitionException, InvalidUserException, InvalidNextActionException {
        validateUser(userId);
        Offer offer = getOfferFromUserId(userId);
        boolean isBuyer = offer.isBuyer(userId);
        OfferState newState = isBuyer ? OfferState.AWAITING_SELLER_ACCEPTANCE : OfferState.AWAITING_BUYER_ACCEPTANCE;

        offer.transition(newState, offer.getCurrentState(), Action.PROPOSE_UPDATE, userId, offerDetails, isBuyer);
    }

    /**
     * Withdraws an offer on behalf of a user.
     * 
     * @param userId the ID of the user withdrawing the offer
     * @throws InvalidStateTransitionException, InvalidUserException, InvalidNextActionException if any state transitions or user validations fail
     */
    @Override
    public void withdraw(String userId)
            throws InvalidStateTransitionException, InvalidUserException, InvalidNextActionException {
        validateUser(userId);
        Offer offer = getOfferFromUserId(userId);
        boolean isBuyer = offer.isBuyer(userId);
        OfferState newState = isBuyer ? OfferState.WITHDRAWN_BY_BUYER : OfferState.WITHDRAWN_BY_SELLER;
        
        offer.transition(newState, offer.getCurrentState(), Action.WITHDRAW, userId, isBuyer);
    }

    /**
     * Updates private data associated with an offer.
     * 
     * @param userId the ID of the user requesting the update
     * @param privateData the private data to be updated
     */
    @Override 
    public void updatePrivateData(String userId, Map<String, String> privateData){
        Offer offer = getOfferFromUserId(userId);
        offer.updatePrivateData(userId, privateData);
    }
}
