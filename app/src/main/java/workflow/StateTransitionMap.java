package workflow;

import java.util.Map;

import workflow.exceptions.InvalidStateTransitionException;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

final class StateTransitionMap {
    private static Map<OfferState, List<OfferState>> buyerAllowedTransitions = new HashMap<>(); //set
    private static Map<OfferState, List<OfferState>> sellerAllowedTransitions = new HashMap<>();

    static void checkTransition(final OfferState currentState, final OfferState nextState, boolean isBuyer){
        List<OfferState> allowedTransitions = isBuyer
                ? buyerAllowedTransitions.get(currentState)
                : sellerAllowedTransitions.get(currentState);

        if (!allowedTransitions.contains(nextState)) {
            throw new InvalidStateTransitionException(
                    "Cannot transition from current " + currentState + " state to next " + nextState);
        }
    }

    static {
        initializeTransitions();
    }

    private static void initializeTransitions() {
        buyerAllowedTransitions.put(OfferState.AWAITING_SELLER_ACCEPTANCE,
                Arrays.asList(OfferState.WITHDRAWN_BY_BUYER, OfferState.CANCELLED));
        buyerAllowedTransitions.put(OfferState.AWAITING_BUYER_ACCEPTANCE,
                Arrays.asList(OfferState.ACCEPTED, OfferState.CANCELLED, OfferState.AWAITING_SELLER_ACCEPTANCE)); // last param propose update
        buyerAllowedTransitions.put(OfferState.ACCEPTED, Collections.emptyList());
        buyerAllowedTransitions.put(OfferState.CANCELLED, Collections.emptyList());
        buyerAllowedTransitions.put(OfferState.WITHDRAWN_BY_BUYER,
                Arrays.asList(OfferState.AWAITING_SELLER_ACCEPTANCE, OfferState.CANCELLED));
        buyerAllowedTransitions.put(OfferState.WITHDRAWN_BY_SELLER, Arrays.asList(OfferState.CANCELLED));

        sellerAllowedTransitions.put(OfferState.AWAITING_BUYER_ACCEPTANCE,
                Arrays.asList(OfferState.WITHDRAWN_BY_SELLER, OfferState.CANCELLED));
        sellerAllowedTransitions.put(OfferState.AWAITING_SELLER_ACCEPTANCE,
                Arrays.asList(OfferState.ACCEPTED, OfferState.CANCELLED, OfferState.AWAITING_BUYER_ACCEPTANCE)); // last param propose update
                                                                                                                 
        sellerAllowedTransitions.put(OfferState.ACCEPTED, Collections.emptyList());
        sellerAllowedTransitions.put(OfferState.CANCELLED, Collections.emptyList());
        sellerAllowedTransitions.put(OfferState.WITHDRAWN_BY_SELLER,
                Arrays.asList(OfferState.AWAITING_BUYER_ACCEPTANCE, OfferState.CANCELLED));
        sellerAllowedTransitions.put(OfferState.WITHDRAWN_BY_BUYER, Arrays.asList(OfferState.CANCELLED)); // I have to wait for the new offer

    }
}
