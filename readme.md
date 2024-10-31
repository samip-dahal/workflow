# Haggling Platform

## Overview
This system is designed as a haggling platform where users can negotiate over product offers. An offer includes key details, such as the product name, desired quantity, and proposed price. Users engage in a back-and-forth exchange, with negotiations continuing until both users either reach an agreement on the offer details or one user decides to cancel the offer.

To support the haggling process, this system enables the following actions:

- `Submit`: A user can initiate a new offer by submitting the product details, including their user ID and the other user’s ID (the seller).

- `Accept`: The receiving user can choose to accept the offer by providing their user ID, indicating agreement with the offer.

- `Cancel`: Either user can cancel the offer at any point in the negotiation, permanently ending the offer by submitting their user ID.

- `ProposeUpdate`: Users have the option to modify the existing offer details by proposing updates, which requires their user ID along with the revised offer details.

- `Withdraw`: If a user wishes to make adjustments to the offer before it is accepted, they can withdraw it temporarily by submitting their user ID. Withdrawal indicates a pause for review or modification rather than a cancellation.

- `UpdatePrivateData`: Users can update their personal, private data—stored as key-value pairs—linked to the offer. This private data is accessible only to the individual user and remains hidden from the other party involved in the transaction. Private data can be modified at any time, as it does not influence the ongoing negotiation process.

The actions above transition the offer into various states, each representing a phase in the haggling process:

- `AWAITING_SELLER_ACCEPTANCE`
- `AWAITING_BUYER_ACCEPTANCE`
- `ACCEPTED`
- `CANCELLED`
- `WITHDRAWN_BY_BUYER`  
- `WITHDRAWN_BY_SELLER` 

The final states (`ACCEPTED` and `CANCELLED`) are symmetrical and do not permit any actions that impact the offer state itself, except for private data updates. At any point before reaching an end state, users have the option to cancel the offer.

When an offer is initially submitted, it enters the AWAITING_SELLER_ACCEPTANCE state from the perspective of both users involved in the transaction.

The system's public API is designed to facilitate these core actions, allowing users to drive state changes as they progress through the haggling workflow. Each action includes the user ID of the individual executing it, specifying their role in the offer. The buyer (submitter) and seller IDs are defined at the beginning and remain constant throughout the offer's lifecycle.

If an action is attempted by an unauthorized user (one whose ID does not match either the buyer or seller), the system will raise an exception or handle the invalid attempt appropriately. Additionally, if a user attempts an action that is not permitted in the current state, an exception will be triggered.

Beyond core actions, the system provides APIs for users to view the offer history. One API allows access to previous versions of the offer, displaying private data as it existed at that time. Another API enables users to print a history table showing the full price, calculated as the product of quantity and price for each offer version.


## Project Structure
**src/main/java/workflow/exceptions**: Contains exception classes to handle invalid actions and state transitions.
  - `InvalidNextActionException.java`: Exception for invalid next actions attempted by users.
  - `InvalidOfferException.java`: Exception for invalid offers.
  - `InvalidStateTransitionException.java`: Exception for invalid state transitions.
  - `InvalidUserException.java`: Exception for invalid user actions.

**src/main/java/workflow/serviceapi**: Contains service interface definitions.
  - `IDashboardService.java`: Defines the dashboard service interface.
  - `IOfferService.java`: Defines the offer service interface.

**src/main/java/workflow/services-api-impl**: Contains the implementations of the service interfaces.
  - `DashboardService.java`: Provides the implementation for managing the dashboard view using offer data.
  - `OfferService.java`: Core service for managing offers, their state transitions, actions, and validation.

**src/main/java/workflow**: Contains core domain classes and enums.
  - `Offer.java`: Represents an offer with its details, history, and current state.
  - `OfferDetails.java`: Encapsulates the details of an offer such as product name, quantity, and price.
  - `OfferState.java`: Enum to define the possible states of an offer.
  - `Action.java`: Enum to define the actions a user can perform.
  - `User.java`: Represents a user with a unique identifier.
  - `PermittedActionMap.java`: Contains logic to determine permissible actions for users based on their role and the current state.
  - `StateTransitionMap.java`: Contains logic to define and validate state transitions for offers based on their current state and user roles.
  - `DashboardRow.java`: Represents a row in a dashboard view.

## Assumptions
  - This system supports only a single buyer and seller at this time. 
  - Offer maintains a list of offer history which is used for the dashboard view.
  - Private data for a user is associated with an offer. It is associated with a user and is stored within the offer history. 
  - All implementations for the workflow and dashboard are done in OfferService.java and DashboardService.java respectively.
  - The system handles offers for both **buyers** and **sellers**, and the **state naming conventions** are aligned with the user roles in the offer negotiation. For example, states like `AWAITING_BUYER_ACCEPTANCE` and `AWAITING_SELLER_ACCEPTANCE` are used instead of generic names like `AWAITING_MY_ACCEPTANCE` or `AWAITING_THEIR_ACCEPTANCE`. This role-specific naming convention is applied consistently across all states to improve clarity and ensure that the next responsible party is clearly identified during the negotiation process.

## How to Run

- **Clone the Repository (if using Git)**:
    - Run the following commands to clone the repository and navigate into the project directory:
      ```bash
      git clone <repository-url>
      ```

- **Build the Project**:
    - Use Gradle to build the project, resolve dependencies, and compile the source code:
      ```bash
      .\gradlew clean build  # For Windows
      ```
