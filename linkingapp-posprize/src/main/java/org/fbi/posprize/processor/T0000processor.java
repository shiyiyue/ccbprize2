package org.fbi.posprize.processor;

import org.apache.commons.lang.StringUtils;
import org.fbi.linking.api.PosRequest;
import org.fbi.linking.api.PosResponse;
import org.fbi.posprize.helper.MD5Helper;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 测试交易
 */
public class T0000processor extends TxnMainProcessor {
    private final static int LEN_EVENT_NAME = 20;
    @Override
    public void execute(PosRequest request, PosResponse response, Map<String, Object> model) throws TxnRunTimeException {
        String txnCode = request.getTxnCode();
        String posId = request.getRequestMessage().substring(0 + 6, 0 + 6 + 32);
        String errCode = "0000";

        String responseBody = "";
        try {
            responseBody = processTxn();
        } catch (Exception e) {
            logger.error("交易处理错误.", e);
            errCode = "1000";
        }

        if ("1000".equals(errCode)) {
            response.setResponseMessage(getErrResponse(txnCode, posId, errCode));
        } else {
            String dataLength = null;
            try {
                dataLength = StringUtils.rightPad(("" + (LEN_MSG_HEADER + responseBody.getBytes("GBK").length)), 6, " ");
            } catch (UnsupportedEncodingException e) {
                logger.error("编码错误。");
            }
            String currDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String mac = MD5Helper.getMD5String(responseBody + currDate + clientUserId);
            String message = dataLength
                    + posId
                    + txnCode
                    + errCode
                    + currDate
                    + mac
                    + responseBody;
            response.setResponseMessage(message);
        }
    }
    private String processTxn() throws UnsupportedEncodingException, InterruptedException {
        logger.info("other client");
/*
        PosClient client = new PosClient();
        client.processTxn2001();
*/
        return "test";
    }
}
