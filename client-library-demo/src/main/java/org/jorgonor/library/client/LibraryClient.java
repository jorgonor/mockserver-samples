package org.jorgonor.library.client;

import java.util.List;

public interface LibraryClient {

    List<Book> getBooks();
    CheckoutResult checkout(String isbn);
}