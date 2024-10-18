package workflow;

public class Offer {
    private final String productId;
    private final String productName;
    private final int productQuantity;
    private double productPrice;
    private OfferState state;
    private final String buyerUserId;
    private final String sellerUserId;

    private Offer(String productId, String productName, int productQuantity, double productPrice, String buyerUserId, String sellerUserId){
        this.productId = productId;
        this.productName = productName;
        this.productQuantity = productQuantity;
        this.productPrice = productPrice;
        this.buyerUserId = buyerUserId;
        this.sellerUserId = sellerUserId;
        this.state = OfferState.SUBMIT;
    }

    public static Offer of(String productId, String productName, int productQuantity, double productPrice, String buyerUserId, String sellerUserId){
        return new Offer(productId, productName, productQuantity, productPrice, buyerUserId, sellerUserId);
    }

    public String getProductId() {
        return this.productId;
    }

    public String getProductName() {
        return this.productName;
    }

    public int getProductQuantity() {
        return this.productQuantity;
    }

    public double getProductPrice() {
        return this.productPrice;
    }

    public OfferState getState() {
        return this.state;
    }

    public void updatePrice(double newPrice) {
        this.productPrice = newPrice;
    }

    public void transitionToThisState(OfferState state) {
        if (!this.state.canTransitionToThisState(state)) {
            throw new InvalidStateTransitionException("State transition from " + this.state + " to " + state + " is not allowed.");
        }
        this.state = state;
    }

    public String getBuyerId() {
        return this.buyerUserId;
    }

    public String getSellerId() {
        return this.sellerUserId;
    }
}
