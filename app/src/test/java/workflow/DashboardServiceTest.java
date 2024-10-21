package workflow;

import java.util.List;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import workflow.servicesapiimpl.DashboardService;
import workflow.servicesapiimpl.OfferService;

public class DashboardServiceTest {
    @Test
    void getSellerDashboardByVersion() {
        final OfferService offerService = new OfferService();
        final DashboardService dashboardService = new DashboardService(offerService);
        final String buyerUserId = "123";
        final String sellerUserId = "456";
        final OfferDetails offerDetails = OfferDetails.of("Test", 5, 50);
        final DashboardRow expectedVersionHistory1 = DashboardRow.of(1, Action.SUBMIT, "123",
                OfferState.AWAITING_SELLER_ACCEPTANCE,
                "Test", "123", "456", 5, 50);
        final DashboardRow expectedVersionHistory2 = DashboardRow.of(2, Action.ACCEPT, "456", OfferState.ACCEPTED,
                "Test", "123", "456", 5, 50);

        offerService.submit(buyerUserId, sellerUserId, offerDetails);
        offerService.accept(sellerUserId);

        DashboardRow actualVersionHistory1 = dashboardService.getHistory(sellerUserId, 1);
        DashboardRow actualVersionHistory2 = dashboardService.getHistory(sellerUserId, 2);

        check(expectedVersionHistory1, actualVersionHistory1);
        check(expectedVersionHistory2, actualVersionHistory2);
    }

    @Test
    void getBuyerDashboardByVersion() {
        final OfferService offerService = new OfferService();
        final DashboardService dashboardService = new DashboardService(offerService);
        final String buyerUserId = "123";
        final String sellerUserId = "456";
        final OfferDetails offerDetails = OfferDetails.of("Test", 5, 50);
        final OfferDetails updatedOfferDetails = OfferDetails.of("Test", 5, 55);

        final DashboardRow expectedVersionHistory1 = DashboardRow.of(1, Action.SUBMIT, "123",
                OfferState.AWAITING_SELLER_ACCEPTANCE,
                "Test", "123", "456", 5, 50);
        final DashboardRow expectedVersionHistory2 = DashboardRow.of(2, Action.PROPOSE_UPDATE, "456",
                OfferState.AWAITING_BUYER_ACCEPTANCE,
                "Test", "123", "456", 5, 55);
        final DashboardRow expectedVersionHistory3 = DashboardRow.of(3, Action.ACCEPT, "123", OfferState.ACCEPTED,
                "Test", "123", "456", 5, 55);

        offerService.submit(buyerUserId, sellerUserId, offerDetails);
        offerService.proposeUpdate(sellerUserId, updatedOfferDetails);
        offerService.accept(buyerUserId);

        DashboardRow actualVersionHistory1 = dashboardService.getHistory(buyerUserId, 1);
        DashboardRow actualVersionHistory2 = dashboardService.getHistory(buyerUserId, 2);
        DashboardRow actualVersionHistory3 = dashboardService.getHistory(buyerUserId, 3);

        check(expectedVersionHistory1, actualVersionHistory1);
        check(expectedVersionHistory2, actualVersionHistory2);
        check(expectedVersionHistory3, actualVersionHistory3);
    }

    @Test
    void getEntireHistory(){
        final OfferService offerService = new OfferService();
        final DashboardService dashboardService = new DashboardService(offerService);
        final String buyerUserId = "123";
        final String sellerUserId = "456";
        final OfferDetails offerDetails = OfferDetails.of("Test", 5, 50);
        final OfferDetails updatedOfferDetails = OfferDetails.of("Test", 5, 55);

        final DashboardRow expectedVersionHistory1 = DashboardRow.of(1, Action.SUBMIT, "123",
                OfferState.AWAITING_SELLER_ACCEPTANCE,
                "Test", "123", "456", 5, 50);
        final DashboardRow expectedVersionHistory2 = DashboardRow.of(2, Action.PROPOSE_UPDATE, "456",
                OfferState.AWAITING_BUYER_ACCEPTANCE,
                "Test", "123", "456", 5, 55);
        final DashboardRow expectedVersionHistory3 = DashboardRow.of(3, Action.ACCEPT, "123", OfferState.ACCEPTED,
                "Test", "123", "456", 5, 55);
        final List<DashboardRow> expectedHistory = new ArrayList<>();
        expectedHistory.add(expectedVersionHistory1);
        expectedHistory.add(expectedVersionHistory2);
        expectedHistory.add(expectedVersionHistory3);


        offerService.submit(buyerUserId, sellerUserId, offerDetails);
        offerService.proposeUpdate(sellerUserId, updatedOfferDetails);
        offerService.accept(buyerUserId);

        List<DashboardRow> actualHistory = dashboardService.getHistory(sellerUserId);
        for (int i = 0; i < actualHistory.size(); i++){
            check(expectedHistory.get(i), actualHistory.get(i));
        }
    }

    private void check(final DashboardRow expectedVersionHistory, final DashboardRow actualVersionHistory) {
        assertEquals(expectedVersionHistory.getVersion(), actualVersionHistory.getVersion());
        assertEquals(expectedVersionHistory.getAction(), actualVersionHistory.getAction());
        assertEquals(expectedVersionHistory.getUserId(), actualVersionHistory.getUserId());
        assertEquals(expectedVersionHistory.getState(), actualVersionHistory.getState());
        assertEquals(expectedVersionHistory.getProductName(), actualVersionHistory.getProductName());
        assertEquals(expectedVersionHistory.getBuyerUserId(), actualVersionHistory.getBuyerUserId());
        assertEquals(expectedVersionHistory.getSellerUserId(), actualVersionHistory.getSellerUserId());
        assertEquals(expectedVersionHistory.getTotalPrice(), actualVersionHistory.getTotalPrice());
    }
}
