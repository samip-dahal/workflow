package workflow;

import java.util.Map;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public final class PermittedActionMap {
    private static Map<OfferState, List<Action>> buyerPermittedActions = new HashMap<>();
    private static Map<OfferState, List<Action>> sellerPermittedActions = new HashMap<>();

    public static void checkAction(final OfferState currentState, final Action nextAction, boolean isBuyer){
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
            Arrays.asList(Action.WITHDRAW, Action.CANCEL));
        buyerPermittedActions.put(OfferState.AWAITING_BUYER_ACCEPTANCE, 
            Arrays.asList(Action.ACCEPT, Action.CANCEL, Action.PROPOSE_UPDATE));
        buyerPermittedActions.put(OfferState.WITHDRAWN_BY_SELLER, 
            Arrays.asList(Action.CANCEL));
        buyerPermittedActions.put(OfferState.WITHDRAWN_BY_BUYER, 
            Arrays.asList(Action.CANCEL, Action.PROPOSE_UPDATE));
        buyerPermittedActions.put(OfferState.ACCEPTED, 
            Collections.emptyList()); 
        buyerPermittedActions.put(OfferState.CANCELLED, 
            Collections.emptyList()); 

        sellerPermittedActions.put(OfferState.AWAITING_BUYER_ACCEPTANCE, 
            Arrays.asList(Action.WITHDRAW, Action.CANCEL));
        sellerPermittedActions.put(OfferState.AWAITING_SELLER_ACCEPTANCE, 
            Arrays.asList(Action.ACCEPT, Action.CANCEL, Action.PROPOSE_UPDATE));
        sellerPermittedActions.put(OfferState.WITHDRAWN_BY_BUYER, 
            Arrays.asList(Action.CANCEL));
        sellerPermittedActions.put(OfferState.WITHDRAWN_BY_SELLER, 
            Arrays.asList(Action.CANCEL, Action.PROPOSE_UPDATE));
        sellerPermittedActions.put(OfferState.ACCEPTED, 
            Collections.emptyList());
        sellerPermittedActions.put(OfferState.CANCELLED, 
            Collections.emptyList()); 
    }
}
