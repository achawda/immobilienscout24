package de.scout24.util;

import de.scout24.exception.ApiException;
import de.scout24.exception.ErrorCodes;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ResourceValidator implements Callable<Map<String, Integer>> {

    private List<String> hyperLinks;
    private Map<String, Integer> resourceValidationMap = new HashMap<>();

    private final Logger logger = Logger.getLogger(ResourceValidator.class.getName());

    public ResourceValidator(List<String> hyperLinks) {
        this.hyperLinks = hyperLinks;
    }

    @Override
    public Map<String, Integer> call() {
        for (String resource : hyperLinks) {
            int respCode;
            try {
                respCode = getResponseCode(resource);
                resourceValidationMap.put(resource, respCode);
            } catch (MalformedURLException e) {
                logger.log(Level.SEVERE, "Malformed URL", e);
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCodes.MALFORMED_URL);
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Cannot read document", e);
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCodes.CANNOT_READ_DOCUMENT);
            }
        }
        return resourceValidationMap;
    }

    public static int getResponseCode(String resource) throws IOException {
        URL url = new URL(resource);
        if (url.openConnection() instanceof HttpURLConnection) {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(Constants.HEAD);
            connection.setInstanceFollowRedirects(false);
            HttpURLConnection.setFollowRedirects(false);
            connection.connect();
            return connection.getResponseCode();
        }
        return -1;
    }
}
