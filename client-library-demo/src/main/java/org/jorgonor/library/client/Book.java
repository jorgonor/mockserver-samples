package org.jorgonor.library.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.Builder;

@Value
@Builder
public class Book {
    String isbn;
    String title;
    String author;

    @JsonCreator
    public Book(@JsonProperty("isbn") String isbn, @JsonProperty("title") String title, @JsonProperty("author") String author) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
    }
}