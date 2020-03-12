package v7;

import util.LogPrinter;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class ProductDetailsService {

    private final ApiRequestHandler apiRequestHandler;
    private final ExecutorService executorService;

    public ProductDetailsService(ApiRequestHandler apiRequestHandler, ExecutorService executorService) {
        this.apiRequestHandler = apiRequestHandler;
        this.executorService = executorService;
    }

    public String getDetails(String details) throws InterruptedException, ExecutionException {

        String[] orderDetails = details.split("\\|");
        if (orderDetails.length != 2) {
            return "Invalid request Parameter";
        }
        String productId = orderDetails[0];

        ProductClient productClient = new ProductClient(productId,
                "http://172.16.1.71:5080/products/id/%s",
                apiRequestHandler);

        Future<String> submit = executorService.submit(productClient);
        String[] inventory = apiRequestHandler.get(String.format("http://172.16.1.71:5080/inventory/id/%s", productId)).split(",");

        String product = submit.get();
//        String product = productClient.getValue();
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

    static class ProductClient implements Callable<String> {

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
        public String call() throws Exception {
            LogPrinter.logMsg(String.format("Product client callable"));
            return apiRequestHandler.get(String.format(url, id));
        }
    }

}


