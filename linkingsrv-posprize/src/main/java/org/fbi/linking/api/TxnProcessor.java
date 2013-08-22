package org.fbi.linking.api;


import org.fbi.linking.server.posprize.processor.TxnRunTimeException;

public interface TxnProcessor {
    void execute(PosRequest request, PosResponse response) throws TxnRunTimeException;
}
