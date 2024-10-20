package workflow;

import java.util.ArrayDeque;
import java.util.Deque;

public class Offer {
    private final String offerId;
    private OfferState currentState;
    private final String buyerUserId;
    private final String sellerUserId;
    private final Deque<OfferDetails> listOfOffers;
    private final Deque<OfferState> offerStates;

    private Offer(final String buyerUserId, final String sellerUserId, final OfferDetails offerDetails){
        this.offerId = Utilities.generateUniqueId();
        this.buyerUserId = buyerUserId;
        this.sellerUserId = sellerUserId;
        this.currentState = OfferState.AWAITING_SELLER_ACCEPTANCE;
        this.listOfOffers = new ArrayDeque<>();
        addOfferDetails(offerDetails);
        this.offerStates = new ArrayDeque<>();
        addOfferState(currentState);
    }

    public static Offer of(final String buyerUserId, final String sellerUserId, final OfferDetails offerDetails){
        return new Offer(buyerUserId, sellerUserId, offerDetails);
    }

    public String getOfferId() {
        return this.offerId;
    }

    public OfferState getCurrentState() {
        return this.currentState;
    }

    public void transition(OfferState newState) {
        this.currentState = newState;
        addOfferState(newState);
    }

    public void addOfferState(final OfferState offerState){
        this.offerStates.addLast(offerState);
    }

    public void addOfferDetails(final OfferDetails offerDetails){
        this.listOfOffers.addLast(offerDetails);
    }

    public String getBuyerUserId() {
        return this.buyerUserId;
    }

    public String getSellerUserId() {
        return this.sellerUserId;
    }

    public boolean isBuyer(final String userId){
        return this.buyerUserId.equals(userId);
    }

    public Deque<OfferState> getOfferStates() {
        return this.offerStates;
    }

    public OfferDetails getLastOfferDetails() {
        return this.listOfOffers.getLast();
    }

}
