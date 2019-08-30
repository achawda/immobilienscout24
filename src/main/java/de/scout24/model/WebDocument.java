package de.scout24.model;

import java.util.Map;

public class WebDocument {

    private String uri;
    private String htmlVersion;
    private String pageTitle;
    private Map<String, Integer> headingsMap;
    private int noOfInternalLinks;
    private int noOfExternalLinks;
    private boolean hasLoginForm;
    private Map<String, Integer> resourceValidationMap;


    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getHtmlVersion() {
        return htmlVersion;
    }

    public void setHtmlVersion(String htmlVersion) {
        this.htmlVersion = htmlVersion;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public Map<String, Integer> getHeadingsMap() {
        return headingsMap;
    }

    public void setHeadingsMap(Map<String, Integer> headingsMap) {
        this.headingsMap = headingsMap;
    }

    public int getNoOfInternalLinks() {
        return noOfInternalLinks;
    }

    public void setNoOfInternalLinks(int noOfInternalLinks) {
        this.noOfInternalLinks = noOfInternalLinks;
    }

    public int getNoOfExternalLinks() {
        return noOfExternalLinks;
    }

    public void setNoOfExternalLinks(int noOfExternalLinks) {
        this.noOfExternalLinks = noOfExternalLinks;
    }

    public boolean isHasLoginForm() {
        return hasLoginForm;
    }

    public void setHasLoginForm(boolean hasLoginForm) {
        this.hasLoginForm = hasLoginForm;
    }

    public Map<String, Integer> getResourceValidationMap() {
        return resourceValidationMap;
    }

    public void setResourceValidationMap(Map<String, Integer> resourceValidationMap) {
        this.resourceValidationMap = resourceValidationMap;
    }
}
