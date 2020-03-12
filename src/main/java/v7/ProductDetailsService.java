package v7;

import util.LogPrinter;

public class ProductDetailsService {

    private final ApiRequestHandler apiRequestHandler;

    public ProductDetailsService(ApiRequestHandler apiRequestHandler) {
        this.apiRequestHandler = apiRequestHandler;
    }

    public String getDetails(String details) throws InterruptedException {

        String[] orderDetails = details.split("\\|");
        if (orderDetails.length != 2) {
            return "Invalid request Parameter";
        }
        String productId = orderDetails[0];

        ProductClient productClient = new ProductClient(productId,
                "http://172.16.1.71:5080/products/id/%s",
                apiRequestHandler);
        Thread productThread = new Thread(productClient);
        productThread.start();

        String[] inventory = apiRequestHandler.get(String.format("http://172.16.1.71:5080/inventory/id/%s", productId)).split(",");


        productThread.join();
        String product = productClient.getValue();
        int availableQty;
        try {
            availableQty = Integer.parseInt(inventory[1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            availableQty = 0;
        }
        int orderQty = Integer.parseInt(orderDetails[1]);
        LogPrinter.logMsg(String.format("Available Qty: %d | Ordered Qty: %d", availableQty, orderQty));

        String productName;
        try {
            productName = product.split(",")[2];
        } catch (ArrayIndexOutOfBoundsException e) {
            productName = "No such Product";
        }
        if (availableQty >= orderQty) {
            return productName + " is Available";
        } else {
            return productName + " NOT Available";
        }

    }

    static class ProductClient implements Runnable {

        private String id;
        private String url;
        private String res;
        private ApiRequestHandler apiRequestHandler;

        public ProductClient(String id, String url, ApiRequestHandler apiRequestHandler) {
            this.id = id;
            this.url = url;
            this.apiRequestHandler = apiRequestHandler;
        }

        @Override
        public void run() {
            res = apiRequestHandler.get(String.format(url, id));
        }

        public String getValue() {
            return res;
        }
    }

}


