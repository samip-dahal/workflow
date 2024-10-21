# Haggling Platform

## Overview
This is a design for a haggling platform where a user can submit an offer to another user to purchase their product. An offer consists of the name of the product, the quantity the buyer is looking to buy, and the price offered (these are the “offer details”). The process runs until either user accepts the details of the offer, or cancels it. It manages offers, state transitions, and actions that users can perform. This system also allows the user to see their overall offer history or for a specific version.

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
  - Offer maintains a list of offer history which is used for the dashboard view.
  - Private data for a user is associated with an offer. It is maintained inside the offer history for an offer.
  - All implementations for the workflow and dashboard are done in OfferService.java and DashboardService.java respectively.
