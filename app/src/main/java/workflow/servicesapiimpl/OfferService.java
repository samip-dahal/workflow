package workflow.servicesapiimpl;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.errorprone.annotations.OverridingMethodsMustInvokeSuper;

import workflow.exceptions.InvalidNextActionException;
import workflow.exceptions.InvalidOfferException;
import workflow.exceptions.InvalidStateTransitionException;
import workflow.exceptions.InvalidUserException;

import workflow.servicesapi.IOfferService;
import workflow.Offer;
import workflow.OfferDetails;
import workflow.OfferState;
import workflow.Action;

public class OfferService implements IOfferService {

    private Map<String, String> buyerToOfferMap = new HashMap<>();
    private Map<String, String> sellerToOfferMap = new HashMap<>();
    private Map<String, Offer> offerMap = new HashMap<>();

    private void validateUser(String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new InvalidUserException("Invalid user ID: " + userId);
        }
    }

    public Offer getOffer(String offerId) {
        return offerMap.get(offerId);
    }

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

    @Override
    public String submit(String buyerUserId, String sellerUserId, OfferDetails offerDetails) {
        // check that an offer for a user is already in progress and then allow to
        // submit another offer
        validateOfferDetails(offerDetails);

        validateUser(buyerUserId);
        validateUser(sellerUserId);

        Offer offer = Offer.of(
                buyerUserId,
                sellerUserId,
                offerDetails);

        offerMap.put(offer.getOfferId(), offer);
        buyerToOfferMap.put(buyerUserId, offer.getOfferId());
        sellerToOfferMap.put(sellerUserId, offer.getOfferId());

        return offer.getOfferId();
    }

    @Override
    public void accept(String userId)
            throws InvalidStateTransitionException, InvalidUserException, InvalidNextActionException {
        validateUser(userId);

        Offer offer = getOfferFromUserId(userId);

        boolean isBuyer = offer.isBuyer(userId);
        
        offer.transition(OfferState.ACCEPTED, offer.getCurrentState(), Action.ACCEPT, userId, isBuyer);
    }

    @Override
    public void cancel(String userId)
            throws InvalidStateTransitionException, InvalidUserException, InvalidNextActionException {
        validateUser(userId);

        Offer offer = getOfferFromUserId(userId);

        boolean isBuyer = offer.isBuyer(userId);
        
        offer.transition(OfferState.CANCELLED, offer.getCurrentState(), Action.CANCEL, userId, isBuyer);
    }

    @Override
    public void proposeUpdate(String userId, OfferDetails offerDetails)
            throws InvalidStateTransitionException, InvalidUserException, InvalidNextActionException {
        validateUser(userId);

        Offer offer = getOfferFromUserId(userId);

        boolean isBuyer = offer.isBuyer(userId);
        OfferState newState = isBuyer ? OfferState.AWAITING_SELLER_ACCEPTANCE : OfferState.AWAITING_BUYER_ACCEPTANCE;

        offer.transition(newState, offer.getCurrentState(), Action.PROPOSE_UPDATE, userId, offerDetails, isBuyer);
    }

    @Override
    public void withdraw(String userId)
            throws InvalidStateTransitionException, InvalidUserException, InvalidNextActionException {
        validateUser(userId);

        Offer offer = getOfferFromUserId(userId);

        boolean isBuyer = offer.isBuyer(userId);
        OfferState newState = isBuyer ? OfferState.WITHDRAWN_BY_BUYER : OfferState.WITHDRAWN_BY_SELLER;
        
        offer.transition(newState, offer.getCurrentState(), Action.WITHDRAW, userId, isBuyer);
    }

    @Override 
    public void updatePrivateData(String userId, Map<String, String> privateData){
        Offer offer = getOfferFromUserId(userId);
        offer.updatePrivateData(userId, privateData);
    }
}