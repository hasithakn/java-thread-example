package v7;

public interface SocketQueue<T> {

    void newRequestReceived(T value);
    T getRequest() throws InterruptedException;
}
