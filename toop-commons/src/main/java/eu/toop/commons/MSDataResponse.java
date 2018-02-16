package eu.toop.commons;

/*
 * A Mockup MSDataResponse
 */

import java.io.Serializable;

public class MSDataResponse implements Serializable {
    public String identifier;

    public MSDataResponse(String identifier) {
        this.identifier = identifier;
    }
}
