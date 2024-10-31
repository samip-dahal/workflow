package workflow;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

/**
 * Represents an Offer with buyer and seller information, current state, and historical data.
 * Tracks state transitions and actions taken on the offer.
 */
public class Offer {
    private final String offerId;
    private OfferState currentState;
    private final String buyerUserId;
    private final String sellerUserId;
    private final List<OfferHistory> offerHistory;

    /**
     * Inner class to store historical actions, state changes, and private data for an offer.
     */
    public static class OfferHistory {
        private OfferDetails offerDetails;
        private OfferState offerState;
        private Action offerAction;
        private String userId;
        private Map<String, String> privateData;

        OfferHistory(OfferDetails offerDetails, OfferState offerState, Action offerAction, String userId, Map<String, String> privateData) {
            this.offerDetails = offerDetails;
            this.offerState = offerState;
            this.offerAction = offerAction;
            this.userId = userId;
            this.privateData = new HashMap<>(privateData);
        }

        public OfferDetails getOfferDetails() {
            return this.offerDetails;
        }
        
        public OfferState getOfferState(){
            return this.offerState;
        }

        public Action getOfferAction(){
            return this.offerAction;
        }

        public String getOfferUserId(){
            return this.userId;
        }

        public Map<String, String> getPrivateData(){
            return this.privateData;
        }
    }

    /**
     * Private constructor to initialize an Offer instance.
     */
    private Offer(final String buyerUserId, final String sellerUserId, final OfferDetails offerDetails,
            final OfferHistory offerHistory) {
        this.offerId = UUID.randomUUID().toString();
        this.buyerUserId = buyerUserId;
        this.sellerUserId = sellerUserId;
        this.currentState = OfferState.AWAITING_SELLER_ACCEPTANCE;
        this.offerHistory = new ArrayList<>();
        this.offerHistory.add(offerHistory);
    }

    /**
     * Factory method to create a new Offer with initial state and history.
     * 
     * @param buyerUserId  the ID of the buyer
     * @param sellerUserId the ID of the seller
     * @param offerDetails the details of the offer
     * @return a new Offer instance
     */
    public static Offer of(final String buyerUserId, final String sellerUserId, final OfferDetails offerDetails) {
        OfferHistory offerHistory = new OfferHistory(offerDetails, OfferState.AWAITING_SELLER_ACCEPTANCE, Action.SUBMIT,
                buyerUserId, new HashMap<>());
        return new Offer(buyerUserId, sellerUserId, offerDetails, offerHistory);
    }

    /**
     * Updates private data for the offer, appending this action to the history.
     *
     * @param userId     the ID of the user making the update
     * @param privateData the private data to update
     */
    public void updatePrivateData(String userId, Map<String, String> privateData){
        OfferHistory offerHistory = new OfferHistory(this.getLastOfferHistory().getOfferDetails(), this.getLastOfferHistory().getOfferState(), Action.UPDATE_PRIVATE_DATA, userId, privateData);
        this.offerHistory.add(offerHistory);
    }

    public String getOfferId() {
        return this.offerId;
    }

    public OfferState getCurrentState() {
        return this.currentState;
    }

    /**
     * Executes a transition to a new state for the offer based on the action and user role.
     * Validates the transition through PermittedActionMap and StateTransitionMap.
     * 
     * @param newState     the new state to transition to
     * @param currentState the current state of the offer
     * @param action       the action being performed
     * @param userId       the ID of the user performing the action
     * @param isBuyer      true if the user is the buyer, false if the seller
     */
    public void transition(OfferState newState, OfferState currentState, Action action, String userId, boolean isBuyer) {
        PermittedActionMap.checkAction(currentState, action, isBuyer);
        StateTransitionMap.checkTransition(currentState, newState, isBuyer);

        OfferHistory lastOfferHistory = getLastOfferHistory();
        OfferDetails offerDetails = OfferDetails.of(lastOfferHistory.getOfferDetails().getProductName(),
                lastOfferHistory.getOfferDetails().getProductQuantity(),
                lastOfferHistory.getOfferDetails().getProductPrice());

        transition(newState, action, userId, offerDetails);
    }

    /**
     * Executes a transition to a new state for the offer with updated offer details.
     * 
     * @param newState     the new state to transition to
     * @param currentState the current state of the offer
     * @param action       the action being performed
     * @param userId       the ID of the user performing the action
     * @param offerDetails the new offer details
     * @param isBuyer      true if the user is the buyer, false if the seller
     */
    public void transition(OfferState newState, OfferState currentState, Action action, String userId,
            OfferDetails offerDetails, boolean isBuyer) {
        PermittedActionMap.checkAction(currentState, action, isBuyer);
        StateTransitionMap.checkTransition(currentState, newState, isBuyer);

        transition(newState, action, userId, offerDetails);
    }

    /**
     * Internal transition method that updates the state and logs history.
     * 
     * @param newState     the new state to transition to
     * @param action       the action being performed
     * @param userId       the ID of the user performing the action
     * @param offerDetails the offer details for the new state
     */
    private void transition(OfferState newState, Action action, String userId, OfferDetails offerDetails) {
        this.currentState = newState;
        addOfferHistory(offerDetails, newState, action, userId);
    }

    /**
     * Adds a new entry to the offer history.
     * 
     * @param offerDetails the details of the offer
     * @param offerState   the state of the offer
     * @param offerAction  the action performed on the offer
     * @param userId       the ID of the user performing the action
     */
    public void addOfferHistory(OfferDetails offerDetails, OfferState offerState, Action offerAction, String userId) {
        OfferHistory offerHistory = new OfferHistory(offerDetails, offerState, offerAction, userId, this.getLastOfferHistory().getPrivateData());
        this.offerHistory.add(offerHistory);
    }

    public String getBuyerUserId() {
        return this.buyerUserId;
    }

    public String getSellerUserId() {
        return this.sellerUserId;
    }

    /**
     * Determines if the specified user is the buyer of the offer.
     * 
     * @param userId the ID of the user to check
     * @return true if the user is the buyer, otherwise false
     */
    public boolean isBuyer(final String userId) {
        return this.buyerUserId.equals(userId);
    }

    /**
     * Returns a copy of the offer history list.
     * 
     * @return an ArrayList containing offer history
     */
    public ArrayList<OfferHistory> getOfferHistory() {
        return new ArrayList<>(this.offerHistory);
    }

    /**
     * Retrieves the last entry in the offer history.
     * 
     * @return the last OfferHistory entry
     */
    public OfferHistory getLastOfferHistory() {
        return this.offerHistory.get(this.offerHistory.size() - 1);
    }
}
