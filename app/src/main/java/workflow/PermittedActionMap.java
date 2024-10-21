package workflow;

import java.util.Map;

import workflow.exceptions.InvalidNextActionException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

final class PermittedActionMap {
    private static Map<OfferState, List<Action>> buyerPermittedActions = new HashMap<>();
    private static Map<OfferState, List<Action>> sellerPermittedActions = new HashMap<>();

    static void checkAction(final OfferState currentState, final Action nextAction, boolean isBuyer){
        List<Action> permittedActions = isBuyer
                ? buyerPermittedActions.get(currentState)
                : sellerPermittedActions.get(currentState);

        if (!permittedActions.contains(nextAction)) {
            throw new InvalidNextActionException(
                    "Cannot perform " + nextAction + " from current state: " + currentState);
        }
    }

    static {
        initializePermittedActions();
    }

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
