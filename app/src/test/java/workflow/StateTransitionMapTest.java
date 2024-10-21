package workflow;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import workflow.exceptions.InvalidNextActionException;
import workflow.exceptions.InvalidStateTransitionException;


public class StateTransitionMapTest {

    @Test
    void validTransition(){
        assertDoesNotThrow(() -> 
            StateTransitionMap.checkTransition(OfferState.AWAITING_SELLER_ACCEPTANCE, OfferState.CANCELLED, true)
        );
        assertDoesNotThrow(() -> 
            StateTransitionMap.checkTransition(OfferState.AWAITING_BUYER_ACCEPTANCE, OfferState.WITHDRAWN_BY_SELLER, false)
        );
    }

    @Test
    void invalidTransition(){
        assertThrows(InvalidStateTransitionException.class, () -> 
            StateTransitionMap.checkTransition(OfferState.ACCEPTED, OfferState.AWAITING_BUYER_ACCEPTANCE, true)
        );

        assertThrows(InvalidStateTransitionException.class, () -> 
            StateTransitionMap.checkTransition(OfferState.CANCELLED, OfferState.AWAITING_BUYER_ACCEPTANCE, false)
        );
    }
}
