package workflow;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

import java.util.Map;
import java.util.HashMap;

public class Offer {
    private final String offerId;
    private OfferState currentState;
    private final String buyerUserId;
    private final String sellerUserId;
    private final List<OfferHistory> offerHistory;

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

    private Offer(final String buyerUserId, final String sellerUserId, final OfferDetails offerDetails,
            final OfferHistory offerHistory) {
        this.offerId = UUID.randomUUID().toString();
        this.buyerUserId = buyerUserId;
        this.sellerUserId = sellerUserId;
        this.currentState = OfferState.AWAITING_SELLER_ACCEPTANCE;
        this.offerHistory = new ArrayList<>();
        this.offerHistory.add(offerHistory);
    }

    public static Offer of(final String buyerUserId, final String sellerUserId, final OfferDetails offerDetails) {
        OfferHistory offerHistory = new OfferHistory(offerDetails, OfferState.AWAITING_SELLER_ACCEPTANCE, Action.SUBMIT,
                buyerUserId, new HashMap<>());
        return new Offer(buyerUserId, sellerUserId, offerDetails, offerHistory);
    }

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

    public void transition(OfferState newState, OfferState currentState, Action action, String userId,
            boolean isBuyer) {
        PermittedActionMap.checkAction(currentState, action, isBuyer);
        StateTransitionMap.checkTransition(currentState, newState, isBuyer);

        OfferHistory lastOfferHistory = getLastOfferHistory();
        OfferDetails offerDetails = OfferDetails.of(lastOfferHistory.getOfferDetails().getProductName(),
                lastOfferHistory.getOfferDetails().getProductQuantity(),
                lastOfferHistory.getOfferDetails().getProductPrice());

        transition(newState, action, userId, offerDetails);
    }

    public void transition(OfferState newState, OfferState currentState, Action action, String userId,
            OfferDetails offerDetails, boolean isBuyer) {
        PermittedActionMap.checkAction(currentState, action, isBuyer);
        StateTransitionMap.checkTransition(currentState, newState, isBuyer);

        transition(newState, action, userId, offerDetails);
    }

    private void transition(OfferState newState, Action action, String userId, OfferDetails offerDetails) {
        this.currentState = newState;
        addOfferHistory(offerDetails, newState, action, userId);
    }

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

    public boolean isBuyer(final String userId) {
        return this.buyerUserId.equals(userId);
    }

    public ArrayList<OfferHistory> getOfferHistory() {
        return new ArrayList<>(this.offerHistory);
    }

    public OfferHistory getLastOfferHistory() {
        return this.offerHistory.get(this.offerHistory.size() - 1);
    }

}
