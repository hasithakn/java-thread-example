//package v7;
//
//
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//public class RequestCoordinator {
////
////    private final SocketQueueImpl socketStore;
//    private final ExecutorService executors =
//            Executors.newFixedThreadPool(10, r -> new Thread(r, "server-threads"));
//
//
//    public RequestCoordinator(SocketQueueImpl socketStore) {
////        this.socketStore = socketStore;
//    }
//
//    public void start() {
//        for (int i = 0; i < 10; i++) {
//            RequestProcessor rp = new RequestProcessor(i, socketStore, executors);
//            executors.execute(rp);
//        }
//        System.out.println("Processor started");
//    }
//}
