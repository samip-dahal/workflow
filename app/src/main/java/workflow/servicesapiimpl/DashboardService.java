package workflow.servicesapiimpl;

import java.util.List;
import java.util.ArrayList;

import workflow.servicesapi.IDashboardService;
import workflow.exceptions.InvalidOfferException;
import workflow.exceptions.InvalidUserException;
import workflow.Offer;
import workflow.DashboardRow;
import workflow.Offer.OfferHistory;

/**
 * Implementation of the IDashBoardService interface to access offer history and version comparisons for users.
 */
public class DashboardService implements IDashboardService {
    final OfferService offerService;

    /**
     * Constructs a DashboardService with the specified OfferService.
     *
     * @param offerService an instance of OfferService for retrieving offer data
     */
    public DashboardService(OfferService offerService){
        this.offerService = offerService;
    }

    /**
     * Retrieves a specific version of offer history for a given user.
     *
     * @param userId  the ID of the user requesting offer history
     * @param version the version number of the offer history to retrieve
     * @return a DashboardRow representing the specified offer history version
     * @throws InvalidOfferException if the offer is not found
     * @throws InvalidUserException if the user ID is invalid
     * @throws IllegalArgumentException if the requested version is out of range
     */
    @Override
    public DashboardRow getHistory(String userId, int version)
            throws InvalidOfferException, InvalidUserException, IllegalArgumentException {
        Offer offer = offerService.getOfferFromUserId(userId);
        ArrayList<OfferHistory> offerHistory = offer.getOfferHistory();
        if (version > offerHistory.size()) {
            throw new IllegalArgumentException("Invalid version");
        }
        OfferHistory history = offerHistory.get(version - 1);
        DashboardRow data = DashboardRow.of(version, history.getOfferAction(), history.getOfferUserId(),
                history.getOfferState(), history.getOfferDetails().getProductName(), offer.getBuyerUserId(),
                offer.getSellerUserId(), history.getOfferDetails().getProductQuantity(),
                history.getOfferDetails().getProductPrice());
        return data;
    }

    /**
     * Retrieves the full offer history for a given user.
     *
     * @param userId the ID of the user requesting offer history
     * @return a list of DashboardRow objects, each representing a version of offer history
     * @throws InvalidOfferException if the offer is not found
     * @throws InvalidUserException if the user ID is invalid
     */
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

    /**
     * Retrieves the differences between two versions of offer history for a given user.
     *
     * @param userId   the ID of the user requesting version comparison
     * @param version1 the first version number for comparison
     * @param version2 the second version number for comparison
     * @throws InvalidOfferException if the offer is not found
     * @throws InvalidUserException if the user ID is invalid
     * @throws IllegalArgumentException if either version is out of range
     */
    @Override
    public void getDifference(String userId, int version1, int version2)
            throws InvalidOfferException, InvalidUserException, IllegalArgumentException {
        // Method to be implemented: compares two versions and returns differences
    }
}
