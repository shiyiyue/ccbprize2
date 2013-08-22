package org.fbi.linking.server.posprize;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-4-13
 */
public class MessageDecoder  extends ByteToMessageDecoder {
    private static final Logger logger = LoggerFactory.getLogger(MessageDecoder.class);
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if (in.readableBytes() < 6) {
            return;
        }

        byte[] lengthBuffer = new byte[6];
        in.getBytes(0,lengthBuffer);

        int dataLength = Integer.parseInt(new String(lengthBuffer).trim());
        if (in.readableBytes() < dataLength) {
            return;
        }

        byte[] decoded = new byte[dataLength];
        in.readBytes(decoded);
        String msg = new String(decoded);
        //logger.info(msg);
        out.add(msg);
    }
}
