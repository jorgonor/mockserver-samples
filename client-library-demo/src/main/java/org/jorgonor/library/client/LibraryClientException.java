package org.jorgonor.library.client;

/**
 * Base library client exception class
 */
public abstract class LibraryClientException extends RuntimeException {

    public LibraryClientException(Throwable cause) {
        super(cause);
    }

    public LibraryClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
