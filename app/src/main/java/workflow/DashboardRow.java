package workflow;

public class DashboardRow {
    private final int version;
    private final Action action;
    private final String userId;
    private final String buyerUserId;
    private final String sellerUserId;
    private final OfferState state;
    private final String productName;
    private final double totalPrice;

    private DashboardRow(final int version, final Action action, final String userId, final OfferState state,
            final String productName, final String buyerUserId, final String sellerUserId, final double totalPrice) {
        this.version = version;
        this.action = action;
        this.userId = userId;
        this.state = state;
        this.productName = productName;
        this.buyerUserId = buyerUserId;
        this.sellerUserId = sellerUserId;
        this.totalPrice = totalPrice;
    }

    public static DashboardRow of(final int version, final Action action, final String userId, final OfferState state,
            final String productName, final String buyerUserId, final String sellerUserId, final int productQuantity,
            final double productPrice) {
        final double totalPrice = productQuantity * productPrice;
        return new DashboardRow(version, action, userId, state, productName, buyerUserId, sellerUserId, totalPrice);
    }

    public int getVersion() {
        return this.version;
    }

    public Action getAction() {
        return this.action;
    }

    public String getUserId() {
        return this.userId;
    }

    public OfferState getState() {
        return this.state;
    }

    public String getProductName() {
        return this.productName;
    }

    public String getBuyerUserId() {
        return this.buyerUserId;
    }

    public String getSellerUserId() {
        return this.sellerUserId;
    }

    public double getTotalPrice() {
        return this.totalPrice;
    }
}
