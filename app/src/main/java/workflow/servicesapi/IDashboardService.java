package workflow.servicesapi;

import java.util.List;

import workflow.exceptions.InvalidOfferException;
import workflow.exceptions.InvalidUserException;
import workflow.DashboardRow;

/**
 * Interface representing operations related to managing and retrieving offer history and differences.
 */
public interface IDashboardService {

    /**
     * Retrieves the offer history for a specified user and version.
     *
     * @param userId the ID of the user whose offer history is being retrieved
     * @param version the version number of the offer history to retrieve
     * @return a {@link DashboardRow} containing details of the specified version of the offer history
     * @throws InvalidOfferException if the offer is not found or is invalid
     * @throws InvalidUserException if the provided user ID is not valid or not associated to any offer
     * @throws IllegalArgumentException if the provided version number is not valid
     */
    DashboardRow getHistory(String userId, int version) throws InvalidOfferException, InvalidUserException, IllegalArgumentException;

    /**
     * Retrieves the complete offer history for a specified user.
     *
     * @param userId the ID of the user whose complete offer history is being retrieved
     * @return a list of {@link DashboardRow} objects representing the offer history for the specified user
     * @throws InvalidOfferException if the offer is not found or is invalid
     * @throws InvalidUserException if the provided user ID is not valid or not associated to any offer
     */
    List<DashboardRow> getHistory(String userId) throws InvalidOfferException, InvalidUserException;

    /**
     * Calculates and displays the differences between two versions of an offer.
     *
     * @param userId the ID of the user whose offer differences are being calculated
     * @param version1 the first version number to compare
     * @param version2 the second version number to compare
     * @throws InvalidOfferException if the offer is not found or is invalid
     * @throws InvalidUserException if the provided user ID is not valid or not associated to any offer
     * @throws IllegalArgumentException if the provided version number is not valid
     */
    void getDifference(String userId, int version1, int version2) throws InvalidOfferException, InvalidUserException, IllegalArgumentException;
}
