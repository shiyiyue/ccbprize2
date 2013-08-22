package org.fbi.posprize.internal;

import org.fbi.posprize.service.PosprizeProcessorManagerService;
import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceFactory;
import org.osgi.framework.ServiceRegistration;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-8-22
 * Time: 上午7:44
 * To change this template use File | Settings | File Templates.
 */
public class ProcessorFactory implements ServiceFactory<PosprizeProcessorManagerService> {
    @Override
    public PosprizeProcessorManagerService getService(Bundle bundle, ServiceRegistration<PosprizeProcessorManagerService> registration) {
        System.out.println(bundle.getSymbolicName() + "已获取服务。 ");
        return new PosprizeProcessorManagerService();
    }

    @Override
    public void ungetService(Bundle bundle, ServiceRegistration<PosprizeProcessorManagerService> registration, PosprizeProcessorManagerService service) {
        System.out.println(bundle.getSymbolicName() + "已释放服务。");
    }
}
