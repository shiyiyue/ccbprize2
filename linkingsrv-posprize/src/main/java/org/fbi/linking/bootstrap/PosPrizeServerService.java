package org.fbi.linking.bootstrap;

import org.fbi.linking.server.posprize.PosServer;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-8-18
 * Time: 下午7:03
 */
public final class PosPrizeServerService implements Runnable {
    private static final String PID = "netty.service.echoserver";

    private final BundleContext context;
    private boolean running;
    private Thread thread;
    private ServiceRegistration configServiceReg;
    private PosServer server;

    public PosPrizeServerService(BundleContext context) {
        this.context = context;
    }

    public void start() {
        this.thread = new Thread(this, "Pos Prize Service");
        this.thread.start();
    }

    public void stop() throws Exception {
        if (this.configServiceReg != null) {
            this.configServiceReg.unregister();
        }

        this.running = false;
        this.thread.interrupt();

        try {
            this.thread.join(3000);
        } catch (InterruptedException e) {
            //
        }
    }

    @Override
    public void run() {
        this.running = true;
        Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());

        while (this.running) {
            startNetty();

            synchronized (this) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    //
                }
            }
            stopNetty();
        }
    }


    private void startNetty() {
        try {
            this.server = new PosServer();
            this.server.run();
        } catch (Exception e) {
            //TODO SystemLogger.error
        }
    }

    private void stopNetty() {
        if (this.server != null) {
            try {
                this.server.stop();
                this.server = null;
            } catch (Exception e) {
                //TODO SystemLogger.error
            }
        }
    }

}
