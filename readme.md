# Haggling Platform

## Overview
This system is designed as a haggling platform where a user can submit an offer to another user to purchase their product. An offer contains essential details such as the name of the product, the quantity the buyer wants to purchase, and the proposed price. The haggling process is a dynamic, back-and-forth exchange between users, where negotiations continue until either both users agree to the offer details or one user decides to cancel the offer.

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
