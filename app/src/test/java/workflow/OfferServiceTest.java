package workflow;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import workflow.Offer.OfferHistory;
import workflow.exceptions.InvalidNextActionException;
import workflow.servicesapiimpl.OfferService;

import java.util.Map;
import java.util.HashMap;
import java.util.List;


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
    void submitWithInvalidArgumentThrowsException(){
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
    void invalidActionAfterAcceptThrowsException(){
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
    void invalidActionAfterCancelThrowsException(){
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
    void invalidActionAfterProposeUpdateThrowsException(){
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
        final OfferDetails sellerUpdatedOffer = OfferDetails.of("Test", 5, 150);
        final OfferDetails buyerUpdatedOffer = OfferDetails.of("Test", 5, 200);

        final String offerId = offerService.submit(buyerUserId, sellerUserId, offerDetails);
        offerService.proposeUpdate(sellerUserId, sellerUpdatedOffer);
        offerService.proposeUpdate(buyerUserId, buyerUpdatedOffer);
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
    void invalidActionAfterWithdrawThrowsException(){
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
    void exampleFourInvalidActionThrowsException(){
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

    @Test
    void buyerUpdatesPrivateData() {
        final OfferService offerService = new OfferService();
        final String buyerUserId = "123";
        final String sellerUserId = "456";
        final OfferDetails offerDetails = OfferDetails.of("Test", 5, 50); 
        final Map<String, String> buyerPrivateData = new HashMap<>();
        buyerPrivateData.put("reference", "123");

        final String offerId = offerService.submit(buyerUserId, sellerUserId, offerDetails);
        offerService.updatePrivateData(buyerUserId, buyerPrivateData);
        final List<OfferHistory> history = offerService.getOffer(offerId).getOfferHistory();
        final OfferHistory offerHistory = history.get(history.size() - 1);
        final Map<String, String> expectedPrivateData = offerHistory.getPrivateData();

        assertEquals(expectedPrivateData, buyerPrivateData);
    }

    @Test
    void sellerUpdatesPrivateData() {
        final OfferService offerService = new OfferService();
        final String buyerUserId = "123";
        final String sellerUserId = "456";
        final OfferDetails offerDetails = OfferDetails.of("Test", 5, 50); 
        final Map<String, String> sellerPrivateData = new HashMap<>();
        sellerPrivateData.put("reference", "123");

        final String offerId = offerService.submit(buyerUserId, sellerUserId, offerDetails);
        final OfferDetails sellerUpdatedOffer = OfferDetails.of("Test", 5, 150);
        offerService.proposeUpdate(sellerUserId, sellerUpdatedOffer);
        offerService.updatePrivateData(sellerUserId, sellerPrivateData);

        final List<OfferHistory> history = offerService.getOffer(offerId).getOfferHistory();
        final OfferHistory offerHistory = history.get(history.size() - 1);
        final Map<String, String> expectedPrivateData = offerHistory.getPrivateData();

        assertEquals(expectedPrivateData, sellerPrivateData);
    }

    @Test 
    void updatePrivateDataAfterEndStates() {
        final OfferService offerService = new OfferService();
        final String buyerUserId = "123";
        final String sellerUserId = "456";
        final OfferDetails offerDetails = OfferDetails.of("Test", 5, 50);
        final Map<String, String> sellerPrivateData = new HashMap<>();
        sellerPrivateData.put("reference", "456");

        final String offerId = offerService.submit(buyerUserId, sellerUserId, offerDetails);
        offerService.accept(sellerUserId);
        offerService.updatePrivateData(sellerUserId, sellerPrivateData);

        final List<Offer.OfferHistory> history = offerService.getOffer(offerId).getOfferHistory();
        final Offer.OfferHistory offerHistory = history.get(history.size() - 1);
        final Map<String, String> expectedPrivateData = offerHistory.getPrivateData();

        assertEquals(sellerPrivateData, expectedPrivateData);
    }
}
