package workflow.servicesapiimpl;

import java.util.List;
import java.util.ArrayList;

import workflow.servicesapi.IDashboardService;
import workflow.exceptions.InvalidOfferException;
import workflow.exceptions.InvalidUserException;
import workflow.Offer;
import workflow.DashboardRow;
import workflow.Offer.OfferHistory;

public class DashboardService implements IDashboardService {
    final OfferService offerService;

    public DashboardService(OfferService offerService){
        this.offerService = offerService;
    }

    @Override
    public DashboardRow getHistory(String userId, int version)
            throws InvalidOfferException, InvalidUserException, IllegalArgumentException {
        Offer offer = offerService.getOfferFromUserId(userId);
        ArrayList<OfferHistory> offerHistory = offer.getOfferHistory();
        if (version > offerHistory.size()){
            throw new IllegalArgumentException("Invalid version");
        }
        OfferHistory history = offerHistory.get(version - 1);
        DashboardRow data = DashboardRow.of(version, history.getOfferAction(), history.getOfferUserId(),
                history.getOfferState(), history.getOfferDetails().getProductName(), offer.getBuyerUserId(),
                offer.getSellerUserId(), history.getOfferDetails().getProductQuantity(),
                history.getOfferDetails().getProductPrice());
        return data;
    }

    @Override
    public List<DashboardRow> getHistory(String userId)
            throws InvalidOfferException, InvalidUserException {
                Offer offer = offerService.getOfferFromUserId(userId);
                List<OfferHistory> offerHistory = offer.getOfferHistory();
                List<DashboardRow> dashboard = new ArrayList<>();
                for (int i = 0; i < offerHistory.size(); i++) {
                    OfferHistory history = offerHistory.get(i);
                    DashboardRow data = DashboardRow.of(i + 1, history.getOfferAction(), history.getOfferUserId(),
                        history.getOfferState(), history.getOfferDetails().getProductName(), offer.getBuyerUserId(),
                        offer.getSellerUserId(), history.getOfferDetails().getProductQuantity(),
                        history.getOfferDetails().getProductPrice());
                    dashboard.add(data);
                }
                return dashboard;
    }

    @Override
    public void getDifference(String userId, int version1, int version2)
            throws InvalidOfferException, InvalidUserException, IllegalArgumentException {

    }
}
