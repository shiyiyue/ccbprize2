package org.fbi.linking.api;

import java.io.Serializable;

public class PosRequest implements Serializable {
    private String txnCode;
    private String requestMessage;

    public String getTxnCode() {
        return txnCode;
    }

    public void setTxnCode(String txnCode) {
        this.txnCode = txnCode;
    }

    public String getRequestMessage() {
        return requestMessage;
    }

    public void setRequestMessage(String requestMessage) {
        this.requestMessage = requestMessage;
    }
}
