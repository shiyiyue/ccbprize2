package org.fbi.linking.api;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-8-22
 * Time: 上午7:58
 * To change this template use File | Settings | File Templates.
 */
public interface ProcessorManagerService {
    String getName();
    TxnProcessor getProcessor(String txnCode) throws ClassNotFoundException, IllegalAccessException, InstantiationException;
}
