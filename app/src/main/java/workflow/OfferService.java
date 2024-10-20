package workflow;

import java.util.HashMap;
import java.util.Map;

public class OfferService implements IOfferService {

    private Map<String, String> buyerToOfferMap = new HashMap<>();
    private Map<String, String> sellerToOfferMap = new HashMap<>();
    private Map<String, Offer> offerMap = new HashMap<>();

    private void validateUser(String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new InvalidUserException("Invalid user ID: " + userId);
        }
    }

    Offer getOffer(String offerId) {
        return offerMap.get(offerId);
    }

    private void validateOfferDetails(final OfferDetails offerDetails) {
        if (offerDetails.getProductName() == null) {
            throw new IllegalArgumentException("Invalid product name");
        }
        if (offerDetails.getProductQuantity() < 0) {
            throw new IllegalArgumentException("Product quantity cannot be a negative value");
        }
        if (offerDetails.getProductPrice() < 0) {
            throw new IllegalArgumentException("Product price cannot be a negative value");
        }
    }

    private Offer getOfferFromUserId(final String userId) {
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
        //do I need to check that an offer for a user is already in progress and then allow to submit another offer
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
    public void accept(String userId) throws InvalidStateTransitionException, InvalidUserException, InvalidNextActionException {
        validateUser(userId);

        Offer offer = getOfferFromUserId(userId);

        boolean isBuyer = offer.isBuyer(userId);

        PermittedActionMap.checkAction(offer.getCurrentState(), Action.ACCEPT, isBuyer);
        StateTransitionMap.checkTransition(offer.getCurrentState(), OfferState.ACCEPTED, isBuyer);
        offer.transition(OfferState.ACCEPTED);

        offer.addOfferDetails(offer.getLastOfferDetails());
    }

    @Override
    public void cancel(String userId) throws InvalidStateTransitionException, InvalidUserException, InvalidNextActionException {
        validateUser(userId);

        Offer offer = getOfferFromUserId(userId);

        boolean isBuyer = offer.isBuyer(userId);

        PermittedActionMap.checkAction(offer.getCurrentState(), Action.CANCEL, isBuyer);
        StateTransitionMap.checkTransition(offer.getCurrentState(), OfferState.CANCELLED, isBuyer);
        offer.transition(OfferState.CANCELLED);

        offer.addOfferDetails(offer.getLastOfferDetails());
    }

    @Override
    public void proposeUpdate(String userId, OfferDetails offerDetails)
            throws InvalidStateTransitionException, InvalidUserException, InvalidNextActionException {
        validateUser(userId);

        Offer offer = getOfferFromUserId(userId);

        boolean isBuyer = offer.isBuyer(userId);
        OfferState newState = isBuyer ? OfferState.AWAITING_SELLER_ACCEPTANCE : OfferState.AWAITING_BUYER_ACCEPTANCE;

        PermittedActionMap.checkAction(offer.getCurrentState(), Action.PROPOSE_UPDATE, isBuyer);
        StateTransitionMap.checkTransition(offer.getCurrentState(), newState, isBuyer);
        offer.transition(newState);

        offer.addOfferDetails(offerDetails);
    }

    @Override
    public void withdraw(String userId) throws InvalidStateTransitionException, InvalidUserException, InvalidNextActionException {
        validateUser(userId);

        Offer offer = getOfferFromUserId(userId);

        boolean isBuyer = offer.isBuyer(userId);
        OfferState newState = isBuyer ? OfferState.WITHDRAWN_BY_BUYER : OfferState.WITHDRAWN_BY_SELLER;

        PermittedActionMap.checkAction(offer.getCurrentState(), Action.WITHDRAW, isBuyer);
        StateTransitionMap.checkTransition(offer.getCurrentState(), newState, isBuyer);
        offer.transition(newState);

        offer.addOfferDetails(offer.getLastOfferDetails());
    }
}