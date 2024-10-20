package workflow;

public class OfferDetails {
    private final String productName;
    private final int productQuantity;
    private final double productPrice;

    private OfferDetails(final String productName, final int productQuantity, final double productPrice) {
        this.productName = productName;
        this.productQuantity = productQuantity;
        this.productPrice = productPrice;
    }

    public static OfferDetails of(final String productName, final int productQuantity, final double productPrice){
        return new OfferDetails(productName, productQuantity, productPrice);
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
}
