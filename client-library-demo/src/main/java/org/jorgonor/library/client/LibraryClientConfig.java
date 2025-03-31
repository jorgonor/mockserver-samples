package org.jorgonor.library.client;

public class LibraryClientConfig {

    private String baseUrl;
    private int timeout;

    public String getBaseUrl() {
        return baseUrl;
    }

    public LibraryClientConfig setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }
    
    public int getTimeout() {
        return timeout;
    }

    public LibraryClientConfig setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }
}