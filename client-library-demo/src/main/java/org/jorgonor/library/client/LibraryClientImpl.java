package org.jorgonor.library.client;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

import java.util.List;

public class LibraryClientImpl implements LibraryClient {

    private final RestTemplate restTemplate;

    public LibraryClientImpl(LibraryClientConfig config) {
        restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(config.getBaseUrl()));
        restTemplate.setRequestFactory(new SimpleClientHttpRequestFactory() {
            @Override
            public void setConnectTimeout(int connectTimeout) {
                super.setConnectTimeout(config.getTimeout());
            }

            @Override
            public void setReadTimeout(int readTimeout) {
                super.setReadTimeout(config.getTimeout());
            }
        });
    }

    @Override
    public List<Book> getBooks() {
        try {
            ResponseEntity<List<Book>> responseEntity = restTemplate.exchange("/v1/books", HttpMethod.GET,
                    null, new ParameterizedTypeReference<>() {});

            return responseEntity.getBody();
        } catch (HttpServerErrorException e) {
            throw new LibraryServerException("Could not get books", e);
        }
    }

    @Override
    public CheckoutResult checkout(String isbn) {
        String path = "/v1/books/" + isbn + "/checkout";

        return restTemplate.exchange(path, HttpMethod.POST, null, CheckoutResult.class).getBody();
    }

}