package de.scout24.util;

import com.google.common.collect.Lists;
import de.scout24.exception.ApiException;
import de.scout24.exception.ErrorCodes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Component
public class HtmlProcessorUtil {

    private final Logger logger = Logger.getLogger(HtmlProcessorUtil.class.getName());

    /**
     * Extract HTML Version
     *
     * @param document
     * @return
     */
    public String getHtmlVersion(Document document) {
        logger.log(Level.INFO, "Getting html version");
        List<Node> nodes = document.childNodes();
        return nodes.stream().filter(node -> node instanceof DocumentType).map(this::generateHtmlVersion).collect(Collectors.joining());
    }

    private String generateHtmlVersion(Node node) {
        DocumentType documentType = (DocumentType) node;
        String htmlVersion = documentType.attr(Constants.PUBLIC_ID);
        return "".equals(htmlVersion) ? Constants.HTML_5 : htmlVersion;
    }

    /**
     * Extract HTML title
     *
     * @param doc
     * @return
     */
    public String getTitle(Document doc) {
        logger.log(Level.INFO, "Getting document title");
        if (doc.title() != null) {
            return doc.title();
        } else {
            return "Title is not set";
        }
    }

    /**
     * Number of headings grouped by heading level
     *
     * @param doc
     * @return
     */
    public Map<String, Integer> getHeadingsCount(Document doc) {
        logger.log(Level.INFO, "Getting headings count");
        Map<String, Integer> hTagsMap = new HashMap<>();
        Elements hTags = doc.select(Constants.HEADING_H1 + ", " + Constants.HEADING_H2 + ", " + Constants.HEADING_H3 + ", " + Constants.HEADING_H4 + ", " + Constants.HEADING_H5 + ", " + Constants.HEADING_H6);
        hTagsMap.put(Constants.HEADING_H1, hTags.select(Constants.HEADING_H1).size());
        hTagsMap.put(Constants.HEADING_H2, hTags.select(Constants.HEADING_H2).size());
        hTagsMap.put(Constants.HEADING_H3, hTags.select(Constants.HEADING_H3).size());
        hTagsMap.put(Constants.HEADING_H4, hTags.select(Constants.HEADING_H4).size());
        hTagsMap.put(Constants.HEADING_H5, hTags.select(Constants.HEADING_H5).size());
        hTagsMap.put(Constants.HEADING_H6, hTags.select(Constants.HEADING_H6).size());
        return hTagsMap;
    }

    /**
     * 1. Get all the href tag
     * 2. Check if domain of given URL matches and count the number of external and internal links
     *
     * @param doc
     * @return
     */
    public Map<String, Integer> getNoOfHyperMediaLinks(Document doc) {
        logger.log(Level.INFO, "Getting internal and external links count");
        Map<String, Integer> hyperMediaLinksMap = new HashMap<>();
        List<String> hyperLinks = getHyperLinks(doc);

        int internalLinksCount = 0;
        int externalLinksCount = 0;

        try {
            URL url = new URL(doc.baseUri());
            String domain = url.getHost();
            for (String hyperLink : hyperLinks) {
                if (hyperLink.contains(domain))
                    internalLinksCount++;
                else
                    externalLinksCount++;
            }

            hyperMediaLinksMap.put(Constants.INTERNAL_LINKS, internalLinksCount);
            hyperMediaLinksMap.put(Constants.EXTERNAL_LINKS, externalLinksCount);
        } catch (MalformedURLException e) {
            logger.log(Level.SEVERE, "Malformed URL", e);
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCodes.MALFORMED_URL);
        }
        return hyperMediaLinksMap;
    }

    private List<String> getHyperLinks(Document doc) {
        List<String> hyperLinks = new ArrayList<>();

        Elements links = doc.select(Constants.A_HREF);
        for (Element link : links) {

            String href = link.attr(Constants.ABS_HREF);
            if (href != null)
                hyperLinks.add(href);
        }
        return hyperLinks;
    }

    /**
     * Check if the web document has a login form by checking the input type = password
     *
     * @param doc
     * @return
     */
    public boolean hasLoginForm(Document doc) {
        logger.log(Level.INFO, "Checking if document has login form");
        boolean hasLoginForm = false;

        Elements inputs = doc.getElementsByTag(Constants.INPUT);

        for (Element element : inputs) {
            String password = element.attr(Constants.TYPE);
            if (null != password && password.equalsIgnoreCase(Constants.PASSWORD)) {
                hasLoginForm = true;
                break;
            }
        }
        return hasLoginForm;
    }

    /**
     * With web document, validate if the resource is available
     *
     * @param doc
     * @return
     */
    public Map<String, Integer> validateResources(Document doc) {
        Map<String, Integer> resourceValidationMap = new HashMap<>();
        List<String> hyperLinks = getHyperLinks(doc);

        final int numOfThread = 9;
        ExecutorService threadPool = Executors.newFixedThreadPool(numOfThread);
        List<FutureTask<Map<String, Integer>>> futureTaskList = new ArrayList<>();
        List<List<String>> subSets = Lists.partition(hyperLinks, numOfThread);
        for (List<String> subList: subSets) {
            ResourceValidator task = new ResourceValidator(subList);
            FutureTask<Map<String, Integer>> futureTask = new FutureTask<>(task);
            threadPool.submit(futureTask);
            futureTaskList.add(futureTask);
        }
        for (FutureTask<Map<String, Integer>> completeFutureTask: futureTaskList) {
            try {
                resourceValidationMap.putAll(completeFutureTask.get());
            } catch (InterruptedException e) {
                logger.log(Level.SEVERE, "InterruptedException", e);
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCodes.INTERNAL_SERVER_ERROR);
            } catch (ExecutionException e) {
                logger.log(Level.SEVERE, "ExecutionException", e);
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCodes.INTERNAL_SERVER_ERROR);
            }
        }
        threadPool.shutdown();
        return  resourceValidationMap;
    }
}
