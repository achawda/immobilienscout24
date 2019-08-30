package de.scout24.service;

import de.scout24.exception.ApiException;
import de.scout24.exception.ErrorCodes;
import de.scout24.model.WebDocument;
import de.scout24.util.Constants;
import de.scout24.util.HtmlProcessorUtil;
import org.apache.commons.validator.routines.UrlValidator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class HtmlProcessorServiceImpl implements HtmlProcessorService {

    @Autowired
    HtmlProcessorUtil htmlProcessorUtil;

    private final Logger logger = Logger.getLogger(HtmlProcessorServiceImpl.class.getName());

    @Override
    public void processHtml(WebDocument webDocument) {
        boolean isValidUrl = validateUrl(webDocument.getUri());
        if (isValidUrl) {
            try {
                Document doc = Jsoup.connect(webDocument.getUri()).get();
                webDocument.setHtmlVersion(htmlProcessorUtil.getHtmlVersion(doc));
                webDocument.setPageTitle(htmlProcessorUtil.getTitle(doc));
                webDocument.setHeadingsMap(htmlProcessorUtil.getHeadingsCount(doc));
                Map<String, Integer> hyperMediaLinksMap = htmlProcessorUtil.getNoOfHyperMediaLinks(doc);
                webDocument.setNoOfInternalLinks(hyperMediaLinksMap.get(Constants.INTERNAL_LINKS));
                webDocument.setNoOfExternalLinks(hyperMediaLinksMap.get(Constants.EXTERNAL_LINKS));
                webDocument.setHasLoginForm(htmlProcessorUtil.hasLoginForm(doc));
                webDocument.setResourceValidationMap(htmlProcessorUtil.validateResources(doc));
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Cannot read document", e);
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCodes.CANNOT_READ_DOCUMENT);
            }
        } else {
            logger.log(Level.SEVERE, "Invalid URL");
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCodes.INVALID_URL);
        }
    }

    private boolean validateUrl(String url) {
        logger.log(Level.INFO, "Validating URL");
        String[] schemes = {Constants.HTTP, Constants.HTTPS};
        UrlValidator urlValidator = new UrlValidator(schemes);
        if (urlValidator.isValid(url)) {
            return true;
        } else {
            return false;
        }
    }
}
