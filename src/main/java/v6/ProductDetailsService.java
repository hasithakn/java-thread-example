package v6;

import util.LogPrinter;

public class ProductDetailsService {

    private final ApiRequestHandler apiRequestHandler;

    public ProductDetailsService(ApiRequestHandler apiRequestHandler) {
        this.apiRequestHandler = apiRequestHandler;
    }

    public String getDetails(String details) {

        String[] orderDetails = details.split("\\|");
        if (orderDetails.length != 2) {
            return "Invalid request Parameter";
        }
        String productId = orderDetails[0];
        String product = apiRequestHandler.get(String.format("http://localhost:5080/products/id/%s", productId));

        if (product.equals("null")) {
            return "Product not available.";
        }
        String[] inventory = apiRequestHandler.get(String.format("http://localhost:5080/inventory/id/%s", productId)).split(",");

        int availableQty = Integer.parseInt(inventory[1]);
        int orderQty = Integer.parseInt(orderDetails[1]);
        LogPrinter.logMsg(String.format("Available Qty: %d | Ordered Qty: %d", availableQty, orderQty));
        String productName = product.split(",")[2];
        if (availableQty >= orderQty) {
            return productName +" is Available";
        } else {
            return productName+ " NOT Available";
        }
    }
}
