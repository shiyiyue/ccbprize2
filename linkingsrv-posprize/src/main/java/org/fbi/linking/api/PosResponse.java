package org.fbi.linking.api;

import java.io.Serializable;

public class PosResponse implements Serializable {
    private String responseMessage;

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
}
