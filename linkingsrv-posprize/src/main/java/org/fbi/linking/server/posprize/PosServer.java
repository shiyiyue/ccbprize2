package org.fbi.linking.server.posprize;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.fbi.linking.server.posprize.helper.ProjectConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: zr
 * Date: 13-4-13
 */
public class PosServer {
    private static final Logger logger = LoggerFactory.getLogger(PosServer.class);

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private final int port;

    public PosServer() {
        this.port = Integer.valueOf((String) ProjectConfigManager.getInstance().getProperty("posserver_listener_port"));
    }

    public PosServer(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new PosServerInitializer());

            b.bind(port).sync().channel().closeFuture().sync();
            logger.info("POS 兑奖服务器启动完成......");
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public void stop(){
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    public static void main(String[] args) throws Exception {
        int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 8080;
        }
        new PosServer(port).run();
    }
}
