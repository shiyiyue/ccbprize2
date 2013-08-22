package org.fbi.linking.server.posprize;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-8-21
 * Time: 下午1:14
 * To change this template use File | Settings | File Templates.
 */
public class PosServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast("decoder", new MessageDecoder());
        pipeline.addLast("encoder", new MessageEncoder());

        pipeline.addLast("handler", new MessageServerHandler());
    }


}
