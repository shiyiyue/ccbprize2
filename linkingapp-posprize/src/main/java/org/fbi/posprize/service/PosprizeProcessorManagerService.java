package org.fbi.posprize.service;

import org.fbi.linking.api.ProcessorManagerService;
import org.fbi.linking.api.TxnProcessor;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-8-22
 * Time: 上午7:49
 * To change this template use File | Settings | File Templates.
 */
public class PosprizeProcessorManagerService implements ProcessorManagerService{
    @Override
    public String getName() {
        return this.getClass().getName();
    }

    public TxnProcessor getProcessor(String txnCode) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class clazz = Class.forName("org.fbi.posprize.processor.T" + txnCode + "processor");
        TxnProcessor processor = (TxnProcessor)clazz.newInstance();

        //TODO
        return processor;
    }
}
