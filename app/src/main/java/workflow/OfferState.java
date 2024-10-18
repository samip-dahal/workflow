package workflow;

import java.util.EnumSet;
import java.util.Set;

public enum OfferState {
    SUBMIT,
    ACCEPT,
    CANCEL,
    PROPOSE_UPDATE,
    WITHDRAW;

    private Set<OfferState> allowedTransitionsFromThisState;

    static {
        SUBMIT.allowedTransitionsFromThisState = EnumSet.of(ACCEPT, PROPOSE_UPDATE, CANCEL, WITHDRAW);
        ACCEPT.allowedTransitionsFromThisState = EnumSet.noneOf(OfferState.class); 
        CANCEL.allowedTransitionsFromThisState = EnumSet.noneOf(OfferState.class); 
        PROPOSE_UPDATE.allowedTransitionsFromThisState = EnumSet.of(ACCEPT, PROPOSE_UPDATE, CANCEL);
        WITHDRAW.allowedTransitionsFromThisState = EnumSet.of(PROPOSE_UPDATE, CANCEL);         
    }

    public boolean canTransitionToThisState(OfferState offerState){
        return allowedTransitionsFromThisState.contains(offerState);
    }
}
