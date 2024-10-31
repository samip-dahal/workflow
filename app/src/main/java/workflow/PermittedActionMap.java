package workflow;

import java.util.Map;
import workflow.exceptions.InvalidNextActionException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Manages permitted actions based on offer states and user roles (buyer or seller).
 * Provides a method to validate if a specific action is allowed from the current state.
 */
final class PermittedActionMap {

    // Maps storing permitted actions for buyers and sellers based on current offer state
    private static Map<OfferState, List<Action>> buyerPermittedActions = new HashMap<>();
    private static Map<OfferState, List<Action>> sellerPermittedActions = new HashMap<>();

    /**
     * Validates whether the specified nextAction is permitted from the currentState
     * based on the user's role (buyer or seller).
     * 
     * @param currentState the current state of the offer
     * @param nextAction   the action user wants to perform
     * @param isBuyer      specifies if the user is the buyer
     * @throws InvalidNextActionException if the next action is not permitted
     */
    static void checkAction(final OfferState currentState, final Action nextAction, boolean isBuyer) {
        List<Action> permittedActions = isBuyer
                ? buyerPermittedActions.get(currentState)
                : sellerPermittedActions.get(currentState);

        if (!permittedActions.contains(nextAction)) {
            throw new InvalidNextActionException(
                    "Cannot perform " + nextAction + " from current state: " + currentState);
        }
    }

    // Static block to initialize permitted actions when the class is loaded
    static {
        initializePermittedActions();
    }

    /**
     * Populates the permitted actions map for each user role based on offer states.
     */
    private static void initializePermittedActions() {
        buyerPermittedActions.put(OfferState.AWAITING_SELLER_ACCEPTANCE,
                Arrays.asList(Action.WITHDRAW, Action.CANCEL, Action.UPDATE_PRIVATE_DATA));
        buyerPermittedActions.put(OfferState.AWAITING_BUYER_ACCEPTANCE,
                Arrays.asList(Action.ACCEPT, Action.CANCEL, Action.PROPOSE_UPDATE, Action.UPDATE_PRIVATE_DATA));
        buyerPermittedActions.put(OfferState.WITHDRAWN_BY_SELLER,
                Arrays.asList(Action.CANCEL, Action.UPDATE_PRIVATE_DATA));
        buyerPermittedActions.put(OfferState.WITHDRAWN_BY_BUYER,
                Arrays.asList(Action.CANCEL, Action.PROPOSE_UPDATE, Action.UPDATE_PRIVATE_DATA));
        buyerPermittedActions.put(OfferState.ACCEPTED,
                Arrays.asList(Action.UPDATE_PRIVATE_DATA));
        buyerPermittedActions.put(OfferState.CANCELLED,
                Arrays.asList(Action.UPDATE_PRIVATE_DATA));

        sellerPermittedActions.put(OfferState.AWAITING_BUYER_ACCEPTANCE,
                Arrays.asList(Action.WITHDRAW, Action.CANCEL, Action.UPDATE_PRIVATE_DATA));
        sellerPermittedActions.put(OfferState.AWAITING_SELLER_ACCEPTANCE,
                Arrays.asList(Action.ACCEPT, Action.CANCEL, Action.PROPOSE_UPDATE, Action.UPDATE_PRIVATE_DATA));
        sellerPermittedActions.put(OfferState.WITHDRAWN_BY_BUYER,
                Arrays.asList(Action.CANCEL, Action.UPDATE_PRIVATE_DATA));
        sellerPermittedActions.put(OfferState.WITHDRAWN_BY_SELLER,
                Arrays.asList(Action.CANCEL, Action.PROPOSE_UPDATE, Action.UPDATE_PRIVATE_DATA));
        sellerPermittedActions.put(OfferState.ACCEPTED,
                Arrays.asList(Action.UPDATE_PRIVATE_DATA));
        sellerPermittedActions.put(OfferState.CANCELLED,
                Arrays.asList(Action.UPDATE_PRIVATE_DATA));
    }
}
