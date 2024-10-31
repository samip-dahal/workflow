package workflow;

import java.util.Map;
import workflow.exceptions.InvalidStateTransitionException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Manages allowed state transitions based on the current offer state and the user's role (buyer or seller).
 * Provides a method to validate whether a transition from one state to another is permitted.
 */
final class StateTransitionMap {

    // Maps storing allowed state transitions for buyers and sellers based on current offer state
    private static Map<OfferState, List<OfferState>> buyerAllowedTransitions = new HashMap<>();
    private static Map<OfferState, List<OfferState>> sellerAllowedTransitions = new HashMap<>();

    /**
     * Validates whether the specified nextState transition is permitted from the currentState
     * based on the user's role (buyer or seller).
     * 
     * @param currentState the current state of the offer
     * @param nextState    the state to transition to
     * @param isBuyer      specifies if the user is the buyer
     * @throws InvalidStateTransitionException if the transition is not permitted
     */
    static void checkTransition(final OfferState currentState, final OfferState nextState, boolean isBuyer) {
        List<OfferState> allowedTransitions = isBuyer
                ? buyerAllowedTransitions.get(currentState)
                : sellerAllowedTransitions.get(currentState);

        if (!allowedTransitions.contains(nextState)) {
            throw new InvalidStateTransitionException(
                    "Cannot transition from current " + currentState + " state to next " + nextState);
        }
    }

    // Static block to initialize allowed state transitions when the class is loaded
    static {
        initializeTransitions();
    }

    /**
     * Populates the allowed transitions map for each user role based on offer states.
     */
    private static void initializeTransitions() {
        buyerAllowedTransitions.put(OfferState.AWAITING_SELLER_ACCEPTANCE,
                Arrays.asList(OfferState.WITHDRAWN_BY_BUYER, OfferState.CANCELLED));
        buyerAllowedTransitions.put(OfferState.AWAITING_BUYER_ACCEPTANCE,
                Arrays.asList(OfferState.ACCEPTED, OfferState.CANCELLED, OfferState.AWAITING_SELLER_ACCEPTANCE));
        buyerAllowedTransitions.put(OfferState.ACCEPTED, Collections.emptyList());
        buyerAllowedTransitions.put(OfferState.CANCELLED, Collections.emptyList());
        buyerAllowedTransitions.put(OfferState.WITHDRAWN_BY_BUYER,
                Arrays.asList(OfferState.AWAITING_SELLER_ACCEPTANCE, OfferState.CANCELLED));
        buyerAllowedTransitions.put(OfferState.WITHDRAWN_BY_SELLER, Arrays.asList(OfferState.CANCELLED));

        sellerAllowedTransitions.put(OfferState.AWAITING_BUYER_ACCEPTANCE,
                Arrays.asList(OfferState.WITHDRAWN_BY_SELLER, OfferState.CANCELLED));
        sellerAllowedTransitions.put(OfferState.AWAITING_SELLER_ACCEPTANCE,
                Arrays.asList(OfferState.ACCEPTED, OfferState.CANCELLED, OfferState.AWAITING_BUYER_ACCEPTANCE));
        sellerAllowedTransitions.put(OfferState.ACCEPTED, Collections.emptyList());
        sellerAllowedTransitions.put(OfferState.CANCELLED, Collections.emptyList());
        sellerAllowedTransitions.put(OfferState.WITHDRAWN_BY_SELLER,
                Arrays.asList(OfferState.AWAITING_BUYER_ACCEPTANCE, OfferState.CANCELLED));
        sellerAllowedTransitions.put(OfferState.WITHDRAWN_BY_BUYER, Arrays.asList(OfferState.CANCELLED));
    }
}
