package org.fbi.linking.server.posprize;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.commons.lang.StringUtils;
import org.fbi.linking.api.MessageConfig;
import org.fbi.linking.api.PosRequest;
import org.fbi.linking.api.PosResponse;
import org.fbi.linking.api.ProcessorManagerService;
import org.fbi.linking.bootstrap.PosPrizeServerActivator;
import org.fbi.linking.server.posprize.helper.MD5Helper;
import org.fbi.linking.server.posprize.helper.ProjectConfigManager;
import org.fbi.linking.api.TxnProcessor;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-4-13
 */
public class MessageServerHandler extends SimpleChannelInboundHandler<String> implements MessageConfig {
    private static final Logger logger = LoggerFactory.getLogger(MessageServerHandler.class);

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String requestMessage) throws Exception {
        String responseMessage = "";
        logger.info("服务器收到报文：" + requestMessage);

        try {
            //1.MAC校验  实时获取是否校验标志，方便更新
            String macFlag = (String) ProjectConfigManager.getInstance().getProperty("posserver_mac_flag");
            if (macFlag != null && "1".equals(macFlag)) {//需校验
                //TODO
            }

            //2.获取交易码
            String txnCode = requestMessage.substring(0 + 6 + 32, 0 + 6 + 32 + 4);
            logger.info("服务器收到报文，交易号:" + txnCode);

            //3.调用业务逻辑处理程序
            //Class clazz = Class.forName("org.fbi.linking.server.posprize.processor.T" + txnCode + "processor");
            //TxnProcessor processor = (TxnProcessor)clazz.newInstance();


            TxnProcessor processor = getTxnprocessor(txnCode);


            PosRequest request = new PosRequest();
            request.setTxnCode(txnCode);
            request.setRequestMessage(requestMessage);

            PosResponse response = new PosResponse();

            processor.execute(request, response);
            responseMessage = response.getResponseMessage();
        } catch (Exception ex) {
            logger.error("Get  txn processor instance error.", ex);
            responseMessage = getErrResponse("1000");
        }

        ctx.writeAndFlush(responseMessage);
        logger.info("服务器返回报文：" + responseMessage);

        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("ChannelInactived.");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("Unexpected exception from downstream.", cause);
        ctx.close();
    }


    private TxnProcessor getTxnprocessor(String txnCode) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        BundleContext context = PosPrizeServerActivator.getBundleContext();
        ServiceReference reference = null;
        reference = context.getServiceReference(ProcessorManagerService.class.getName());
        if (reference == null) {
            System.out.println("服务名称未找到" + ProcessorManagerService.class.getName());
            throw new RuntimeException("此交易的应用处理程序未找到：" + txnCode);
        }
        ProcessorManagerService service = (ProcessorManagerService) context.getService(reference);
        //System.out.println("服务名称: " + service.getName());
        return service.getProcessor(txnCode);
    }

    private String getErrResponse(String errCode) {
        String dataLength = StringUtils.rightPad(("" + LEN_MSG_HEADER), 6, " ");
        String currDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String mac = MD5Helper.getMD5String("" + currDate + clientUserId);
        String message = dataLength
                + StringUtils.rightPad("", LEN_MSG_POSNO, " ")
                + "9999"
                + errCode
                + currDate
                + mac;
        return message;
    }
}
