package org.fbi.posprize.internal;

import org.fbi.linking.api.ProcessorManagerService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;

public class AppActivator implements BundleActivator {

    private static BundleContext context;

    public static BundleContext getBundleContext() {
        return context;
    }

    public void start(BundleContext context) {
        AppActivator.context = context;

        ProcessorFactory factory = new ProcessorFactory();
        Dictionary<String, Object> properties = new Hashtable<String, Object>();
        //properties.put(Constants.SERVICE_RANKING, 1);
        properties.put(Constants.SERVICE_PID, ProcessorManagerService.class.getName());

        context.registerService(ProcessorManagerService.class.getName(), factory, properties);

        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
                + " - Starting the Pos app bundle...." );
    }

    public void stop(BundleContext context) throws Exception {
        AppActivator.context = null;

        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
                + " - Stopping the Pos app bundle...");
    }

}
