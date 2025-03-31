package org.jorgonor.library.client;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;
import lombok.Builder;

@Value
@Builder
class CheckoutResult {
    /**
     * The result status of the checkout operation.
     */
    String status;
    /**
     * The ID of the checkout operation.
     */
    Integer checkoutId;
    /**
     * Optional maximum number of days to return the book to the library after the checkout
     */
    Integer maxDays;

    @JsonCreator
    public CheckoutResult(@JsonProperty("status") String status, @JsonProperty("checkoutId") Integer checkoutId, @JsonProperty("maxDays") Integer maxDays) {
        this.status = status;
        this.checkoutId = checkoutId;
        this.maxDays = maxDays;
    }
}