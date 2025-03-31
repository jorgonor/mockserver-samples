package org.jorgonor.library.client;

public class LibraryServerException extends LibraryClientException {

    public LibraryServerException(Throwable cause) {
        super(cause);
    }

    public LibraryServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
