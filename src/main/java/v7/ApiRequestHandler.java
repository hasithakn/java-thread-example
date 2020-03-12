package v7;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import util.LogPrinter;

import java.io.IOException;

public class ApiRequestHandler {

    private final CloseableHttpClient httpClient;

    public ApiRequestHandler(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public String get(String url) {
        try {
            return EntityUtils.toString(httpClient.execute(new HttpGet(url)).getEntity());
        } catch (IOException e) {
            LogPrinter.logMsg("Can not process request properly. " + e.getMessage());
        }
        return "";
    }
}
