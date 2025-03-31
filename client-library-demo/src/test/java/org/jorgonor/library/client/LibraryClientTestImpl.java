package org.jorgonor.library.client;

import java.util.List;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.RequestDefinition;

class LibraryClientTestImpl {

    private static final int DEFAULT_PORT = 8181;

    private final LibraryClient libraryClient = new LibraryClientImpl(new LibraryClientConfig()
        .setBaseUrl("http://localhost:" + DEFAULT_PORT)
        .setTimeout(1000)
    );

    private ClientAndServer clientAndServer;

    @BeforeEach
    void setUp() {
        clientAndServer = ClientAndServer.startClientAndServer(DEFAULT_PORT);
    }

    @AfterEach
    void tearDown() {
        clientAndServer.stop();
    }

    @Test
    void testGetBooks() {
        final String responseBody = "[{"
            + " \"isbn\": \"9781593279509\", "
            + " \"title\":\"Eloquent Javascript\", "
            + " \"author\":\"Marijin Haverbeke\""
            + "}, {"
            + " \"isbn\": \"9781801072977\", "
            + " \"title\":\"Microservices with Spring Boot and Spring Cloud\", "
            + " \"author\":\"Magnus Larsson\" "
            + "}, {"
            + " \"isbn\": \"9780596004651\", "
            + " \"title\":\"Head First Java\", "
            + " \"author\":\"Kathy Sierra\""
            + " }]";
        
        expectResponse("GET", "/v1/books", 200,
                responseBody);
        

        List<Book> books = libraryClient.getBooks();
        Book book;

        Assertions.assertEquals(3, books.size());

        book = books.get(0);

        Assertions.assertEquals("9781593279509", book.getIsbn());
        Assertions.assertEquals("Eloquent Javascript", book.getTitle());
        Assertions.assertEquals("Marijin Haverbeke", book.getAuthor());

        book = books.get(1);

        Assertions.assertEquals("9781801072977", book.getIsbn());
        Assertions.assertEquals("Microservices with Spring Boot and Spring Cloud", book.getTitle());
        Assertions.assertEquals("Magnus Larsson", book.getAuthor());

        book = books.get(2);

        Assertions.assertEquals("9780596004651", book.getIsbn());
        Assertions.assertEquals("Head First Java", book.getTitle());
        Assertions.assertEquals("Kathy Sierra", book.getAuthor());
        
        RequestDefinition[] requests = clientAndServer.retrieveRecordedRequests(HttpRequest.request()
                .withMethod("GET")
                .withPath("/v1/books"));

        Assertions.assertEquals(1, requests.length);
    }

    @Test
    void testGetBooksNotAvailableResponse() {
        expectResponse("GET", "/v1/books", 503, "{\"message\": \"Not available\"}");

        Assertions.assertThrows(LibraryServerException.class, () -> libraryClient.getBooks());
    }

    @Test
    void testSuccessfulCheckoutBook() {
        expectResponse("POST", "/v1/books/9781593279509/checkout", 200, "{\"status\": \"OK\", \"checkoutId\": \"1\", \"maxDays\": \"14\"}");

        CheckoutResult checkoutResult = libraryClient.checkout("9781593279509");

        Assertions.assertEquals("OK", checkoutResult.getStatus());
        Assertions.assertEquals(1, checkoutResult.getCheckoutId());
        Assertions.assertEquals(14, checkoutResult.getMaxDays());
    }

    @Test
    void testNotSuccessfulCheckoutBook() {
        expectResponse("POST", "/v1/books/9781801072977/checkout", 200, "{\"status\": \"NOT_AVAILABLE\"}");

        CheckoutResult checkoutResult = libraryClient.checkout("9781801072977");

        Assertions.assertEquals("NOT_AVAILABLE", checkoutResult.getStatus());
        Assertions.assertNull(checkoutResult.getCheckoutId());
        Assertions.assertNull(checkoutResult.getMaxDays());
    }

    private void expectResponse(String httpMethod, String path, int statusCode, String responseBody) {
        clientAndServer.when(HttpRequest.request()
            .withMethod(httpMethod)
            .withPath(path))
            .respond(
                HttpResponse.response()
                    .withStatusCode(statusCode)
                    .withHeader("Content-Type", "application/json")
                    .withBody(responseBody)
            );
    }

}
