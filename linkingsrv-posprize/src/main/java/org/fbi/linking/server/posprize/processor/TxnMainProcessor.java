package org.fbi.linking.server.posprize.processor;

import org.apache.commons.lang.StringUtils;
import org.fbi.linking.api.MessageConfig;
import org.fbi.linking.api.PosRequest;
import org.fbi.linking.api.PosResponse;
import org.fbi.linking.api.TxnProcessor;
import org.fbi.linking.server.posprize.helper.MD5Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public abstract class TxnMainProcessor implements TxnProcessor, MessageConfig {
    public Logger logger = LoggerFactory.getLogger(this.getClass());

	public abstract void execute(PosRequest request, PosResponse response, Map<String, Object> model) throws TxnRunTimeException;

	public void execute(PosRequest request, PosResponse response) throws TxnRunTimeException {
		Map<String, Object> model = new HashMap<String, Object>();
		execute(request, response, model);
	}

    protected String getErrResponse(String txnCode, String posId, String errCode) {
        String dataLength = StringUtils.rightPad(("" + LEN_MSG_HEADER), 6, " ");
        String currDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String mac = MD5Helper.getMD5String("" + currDate + clientUserId);

        String message = dataLength
                + posId
                + txnCode
                + errCode
                + currDate
                + mac;
        return message;
    }


}
