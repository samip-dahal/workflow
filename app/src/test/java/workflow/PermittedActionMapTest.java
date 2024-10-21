package workflow;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import workflow.exceptions.InvalidNextActionException;

public class PermittedActionMapTest {
    @Test
    void validAction(){
        assertDoesNotThrow(() -> 
            PermittedActionMap.checkAction(OfferState.AWAITING_SELLER_ACCEPTANCE, Action.CANCEL, true)
        );

        assertDoesNotThrow(() -> 
            PermittedActionMap.checkAction(OfferState.AWAITING_SELLER_ACCEPTANCE, Action.ACCEPT, false)
        );
    }

    @Test
    void invalidAction(){
        assertThrows(InvalidNextActionException.class, () -> 
            PermittedActionMap.checkAction(OfferState.WITHDRAWN_BY_SELLER, Action.ACCEPT, true)
        );

        assertThrows(InvalidNextActionException.class, () -> 
            PermittedActionMap.checkAction(OfferState.ACCEPTED, Action.PROPOSE_UPDATE, false)
        );
    }

    @Test
    void updatePrivateDataFromEveryState() {
        assertDoesNotThrow(() -> 
            PermittedActionMap.checkAction(OfferState.AWAITING_BUYER_ACCEPTANCE, Action.UPDATE_PRIVATE_DATA, false)
        );

        assertDoesNotThrow(() -> 
            PermittedActionMap.checkAction(OfferState.AWAITING_SELLER_ACCEPTANCE, Action.UPDATE_PRIVATE_DATA, true)
        );

        assertDoesNotThrow(() -> 
            PermittedActionMap.checkAction(OfferState.ACCEPTED, Action.UPDATE_PRIVATE_DATA, true)
        );

        assertDoesNotThrow(() -> 
            PermittedActionMap.checkAction(OfferState.CANCELLED, Action.UPDATE_PRIVATE_DATA, true)
        );

        assertDoesNotThrow(() -> 
            PermittedActionMap.checkAction(OfferState.ACCEPTED, Action.UPDATE_PRIVATE_DATA, false)
        );

        assertDoesNotThrow(() -> 
            PermittedActionMap.checkAction(OfferState.CANCELLED, Action.UPDATE_PRIVATE_DATA, false)
        );

        assertDoesNotThrow(() -> 
            PermittedActionMap.checkAction(OfferState.WITHDRAWN_BY_BUYER, Action.UPDATE_PRIVATE_DATA, true)
        );

        assertDoesNotThrow(() -> 
            PermittedActionMap.checkAction(OfferState.WITHDRAWN_BY_SELLER, Action.UPDATE_PRIVATE_DATA, false)
        );
    }
}
