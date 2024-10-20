package workflow;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class OfferServiceTest {
    
    @Test
    void submit(){
        final OfferService offerService = new OfferService();
        final String buyerUserId = "123";
        final String sellerUserId = "456";
        final OfferDetails offerDetails = OfferDetails.of("Test", 5, 50); 
        
        final String offerId = offerService.submit(buyerUserId, sellerUserId, offerDetails);
        
        Offer offer = offerService.getOffer(offerId);
        assertEquals(offer.getCurrentState(), OfferState.AWAITING_SELLER_ACCEPTANCE);
    }

    @Test
    void submitThrowsExceptionOnInvalidArgument(){
        final OfferService offerService = new OfferService();
        final String buyerUserId = "123";
        final String sellerUserId = "456";
        OfferDetails offerDetails = OfferDetails.of("Test", -15, 150); 
        
        assertThrows(IllegalArgumentException.class, () -> {
            offerService.submit(buyerUserId, sellerUserId, offerDetails);
        });
    }

    @Test
    void sellerAccepts(){
        final OfferService offerService = new OfferService();
        final String buyerUserId = "123";
        final String sellerUserId = "456";
        final OfferDetails offerDetails = OfferDetails.of("Test", 5, 50); 
        
        final String offerId = offerService.submit(buyerUserId, sellerUserId, offerDetails);
        offerService.accept(sellerUserId);

        assertEquals(offerService.getOffer(offerId).getCurrentState(), OfferState.ACCEPTED);
    }

    @Test
    void buyerAccepts(){
        final OfferService offerService = new OfferService();
        final String buyerUserId = "123";
        final String sellerUserId = "456";
        final OfferDetails offerDetails = OfferDetails.of("Test", 5, 50);
        final OfferDetails updatedOffer = OfferDetails.of("Test", 5, 100);

        final String offerId = offerService.submit(buyerUserId, sellerUserId, offerDetails);
        offerService.proposeUpdate(sellerUserId, updatedOffer);
        offerService.accept(buyerUserId);

        assertEquals(offerService.getOffer(offerId).getCurrentState(), OfferState.ACCEPTED);
    }

    @Test
    void noOrInvalidActionTakenAfterAccept(){
        final OfferService offerService = new OfferService();
        final String buyerUserId = "123";
        final String sellerUserId = "456";
        final OfferDetails offerDetails = OfferDetails.of("Test", 5, 50);

        offerService.submit(buyerUserId, sellerUserId, offerDetails);
        offerService.accept(sellerUserId);
        assertThrows(InvalidNextActionException.class, () -> {
            offerService.cancel(sellerUserId);
        });
    }
    
    @Test
    void sellerCancels(){
        final OfferService offerService = new OfferService();
        final String buyerUserId = "123";
        final String sellerUserId = "456";
        final OfferDetails offerDetails = OfferDetails.of("Test", 5, 50); 
        
        final String offerId = offerService.submit(buyerUserId, sellerUserId, offerDetails);
        offerService.cancel(sellerUserId);

        assertEquals(offerService.getOffer(offerId).getCurrentState(), OfferState.CANCELLED);
    }

    @Test
    void buyerCancels(){
        final OfferService offerService = new OfferService();
        final String buyerUserId = "123";
        final String sellerUserId = "456";
        final OfferDetails offerDetails = OfferDetails.of("Test", 5, 50);
        final OfferDetails updatedOffer = OfferDetails.of("Test", 5, 100);

        final String offerId = offerService.submit(buyerUserId, sellerUserId, offerDetails);
        offerService.proposeUpdate(sellerUserId, updatedOffer);
        offerService.cancel(buyerUserId);

        assertEquals(offerService.getOffer(offerId).getCurrentState(), OfferState.CANCELLED);
    }

    @Test
    void noOrInvalidActionTakenAfterCancel(){
        final OfferService offerService = new OfferService();
        final String buyerUserId = "123";
        final String sellerUserId = "456";
        final OfferDetails offerDetails = OfferDetails.of("Test", 5, 50);

        offerService.submit(buyerUserId, sellerUserId, offerDetails);
        offerService.cancel(sellerUserId);
        assertThrows(InvalidNextActionException.class, () -> {
            offerService.withdraw(sellerUserId);
        });
    }

    @Test
    void sellerProposesUpdate(){
        final OfferService offerService = new OfferService();
        final String buyerUserId = "123";
        final String sellerUserId = "456";
        final OfferDetails offerDetails = OfferDetails.of("Test", 5, 50); 
        
        final String offerId = offerService.submit(buyerUserId, sellerUserId, offerDetails);
        final OfferDetails updatedOffer = OfferDetails.of("Test", 5, 100);
        offerService.proposeUpdate(sellerUserId, updatedOffer);

        assertEquals(offerService.getOffer(offerId).getCurrentState(), OfferState.AWAITING_BUYER_ACCEPTANCE);
    }

    @Test
    void buyerProposesUpdate(){
        final OfferService offerService = new OfferService();
        final String buyerUserId = "123";
        final String sellerUserId = "456";
        final OfferDetails offerDetails = OfferDetails.of("Test", 5, 50); 
        
        final String offerId = offerService.submit(buyerUserId, sellerUserId, offerDetails);
        final OfferDetails sellerUpdatedOffer = OfferDetails.of("Test", 5, 150);
        offerService.proposeUpdate(sellerUserId, sellerUpdatedOffer);

        final OfferDetails buyerUpdatedOffer = OfferDetails.of("Test", 5, 200);
        offerService.proposeUpdate(buyerUserId, buyerUpdatedOffer);
        assertEquals(offerService.getOffer(offerId).getCurrentState(), OfferState.AWAITING_SELLER_ACCEPTANCE);
    }

    @Test
    void invalidActionAfterProposeUpdate(){
        final OfferService offerService = new OfferService();
        final String buyerUserId = "123";
        final String sellerUserId = "456";
        final OfferDetails offerDetails = OfferDetails.of("Test", 5, 50); 
        
        offerService.submit(buyerUserId, sellerUserId, offerDetails);
        final OfferDetails updatedOffer = OfferDetails.of("Test", 5, 100);
        offerService.proposeUpdate(sellerUserId, updatedOffer);
        assertThrows(InvalidNextActionException.class, () -> {
            offerService.accept(sellerUserId);
        });
    }

    @Test
    void buyerWithdraws(){
        final OfferService offerService = new OfferService();
        final String buyerUserId = "123";
        final String sellerUserId = "456";
        final OfferDetails offerDetails = OfferDetails.of("Test", 5, 50); 
        
        final String offerId = offerService.submit(buyerUserId, sellerUserId, offerDetails);
        offerService.withdraw(buyerUserId);

        assertEquals(offerService.getOffer(offerId).getCurrentState(), OfferState.WITHDRAWN_BY_BUYER);
    }

    @Test
    void sellerWithdraws(){
        final OfferService offerService = new OfferService();
        final String buyerUserId = "123";
        final String sellerUserId = "456";
        final OfferDetails offerDetails = OfferDetails.of("Test", 5, 50); 
        
        final String offerId = offerService.submit(buyerUserId, sellerUserId, offerDetails);
        final OfferDetails sellerUpdatedOffer = OfferDetails.of("Test", 5, 150);
        offerService.proposeUpdate(sellerUserId, sellerUpdatedOffer);
        offerService.withdraw(sellerUserId);

        assertEquals(offerService.getOffer(offerId).getCurrentState(), OfferState.WITHDRAWN_BY_SELLER);
    }

    @Test
    void invalidActionAfterWithdraw(){
        final OfferService offerService = new OfferService();
        final String buyerUserId = "123";
        final String sellerUserId = "456";
        final OfferDetails offerDetails = OfferDetails.of("Test", 5, 50); 
        
        offerService.submit(buyerUserId, sellerUserId, offerDetails);
        offerService.withdraw(buyerUserId);
        
        assertThrows(InvalidNextActionException.class, () -> {
            offerService.accept(buyerUserId);
        });
    } 

    @Test
    void exampleOneSuccessfulPurchase(){
        final OfferService offerService = new OfferService();
        final String buyerUserId = "Superman";
        final String sellerUserId = "Batman";
        final OfferDetails offerDetails = OfferDetails.of("Batmobile", 5, 500); 
        
        final String offerId = offerService.submit(buyerUserId, sellerUserId, offerDetails);
        offerService.accept(sellerUserId);

        assertEquals(offerService.getOffer(offerId).getCurrentState(), OfferState.ACCEPTED);
    }

    @Test
    void exampleTwoCounterOffer(){
        final OfferService offerService = new OfferService();
        final String buyerUserId = "Superman";
        final String sellerUserId = "Batman";
        final OfferDetails offerDetails = OfferDetails.of("Batmobile", 5, 500);
        final OfferDetails sellerUpdatedOffer = OfferDetails.of("Batmobile", 5, 550);

        final String offerId = offerService.submit(buyerUserId, sellerUserId, offerDetails);
        offerService.proposeUpdate(sellerUserId, sellerUpdatedOffer);
        offerService.accept(buyerUserId);

        assertEquals(offerService.getOffer(offerId).getCurrentState(), OfferState.ACCEPTED);
    }

    @Test
    void exampleThreeWithdrawnOffer(){
        final OfferService offerService = new OfferService();
        final String buyerUserId = "Superman";
        final String sellerUserId = "Batman";
        final OfferDetails offerDetails = OfferDetails.of("Batmobile", 5, 500);
        final OfferDetails buyerUpdatedOffer = OfferDetails.of("Batmobile", 5, 450);

        final String offerId = offerService.submit(buyerUserId, sellerUserId, offerDetails);
        offerService.withdraw(buyerUserId);
        offerService.proposeUpdate(buyerUserId, buyerUpdatedOffer);
        offerService.accept(sellerUserId);

        assertEquals(offerService.getOffer(offerId).getCurrentState(), OfferState.ACCEPTED);
    }

    @Test
    void exampleFourInvalidAction(){
        final OfferService offerService = new OfferService();
        final String buyerUserId = "Superman";
        final String sellerUserId = "Batman";
        final OfferDetails offerDetails = OfferDetails.of("Batmobile", 5, 500);
        final OfferDetails buyerUpdatedOffer = OfferDetails.of("Batmobile", 5, 450);

        offerService.submit(buyerUserId, sellerUserId, offerDetails);

        assertThrows(InvalidNextActionException.class, () -> {
            offerService.proposeUpdate(buyerUserId, buyerUpdatedOffer);
        });
    }

    @Test
    void testMultipleHaggling(){
        final OfferService offerService = new OfferService();
        final String buyerUserId = "Superman";
        final String sellerUserId = "Batman";
        final OfferDetails offerDetails = OfferDetails.of("Batmobile", 5, 500);
        final OfferDetails buyerUpdatedOffer = OfferDetails.of("Batmobile", 5, 450);
        final OfferDetails sellerUpdatedOffer = OfferDetails.of("Batmobile", 5, 550);

        final String offerId = offerService.submit(buyerUserId, sellerUserId, offerDetails);
        assertEquals(offerService.getOffer(offerId).getCurrentState(), OfferState.AWAITING_SELLER_ACCEPTANCE);

        offerService.withdraw(buyerUserId);
        assertEquals(offerService.getOffer(offerId).getCurrentState(), OfferState.WITHDRAWN_BY_BUYER);

        offerService.proposeUpdate(buyerUserId, buyerUpdatedOffer);
        assertEquals(offerService.getOffer(offerId).getCurrentState(), OfferState.AWAITING_SELLER_ACCEPTANCE);

        offerService.proposeUpdate(sellerUserId, sellerUpdatedOffer);
        assertEquals(offerService.getOffer(offerId).getCurrentState(), OfferState.AWAITING_BUYER_ACCEPTANCE);

        offerService.accept(buyerUserId);
        assertEquals(offerService.getOffer(offerId).getCurrentState(), OfferState.ACCEPTED);
    }
}
