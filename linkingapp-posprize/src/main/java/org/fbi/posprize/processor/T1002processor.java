package org.fbi.posprize.processor;

import org.apache.commons.lang.StringUtils;
import org.fbi.linking.api.PosRequest;
import org.fbi.linking.api.PosResponse;
import org.fbi.posprize.helper.MD5Helper;
import org.fbi.posprize.helper.jdbctemplate.JdbcTemplate;
import org.fbi.posprize.helper.jdbctemplate.StatementCallback;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 3.2	兑奖资格查询交易
 */
public class T1002processor extends TxnMainProcessor {

    @Override
    public void execute(PosRequest request, PosResponse response, Map<String, Object> model) throws TxnRunTimeException {
        String txnCode = request.getTxnCode();
        String posId = request.getRequestMessage().substring(0 + 6, 0 + 6 + 32);
        String errCode = "";

        String cardNo = request.getRequestMessage().substring(LEN_MSG_HEADER, LEN_MSG_HEADER + LEN_MSG_CARDNO).trim();
        String eventNo = request.getRequestMessage().substring(LEN_MSG_HEADER + LEN_MSG_CARDNO, LEN_MSG_HEADER + LEN_MSG_CARDNO + 2);
        String responseBody = "";
        String dataLength = "";
        try {
            responseBody = processTxn(cardNo, eventNo);
            dataLength = StringUtils.rightPad(("" + (LEN_MSG_HEADER + (responseBody.getBytes("GBK").length))), 6, " ");
            errCode = "0000";
        } catch (Exception e) {
            logger.error("交易处理错误.", e);
            errCode = "1000";
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

    private String processTxn(String cardNo, String eventNo) throws UnsupportedEncodingException, SQLException {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();

        final String sql = String.format("select award_flag from B_S_ACT_INFO where crd_no = '%s' and act_no = '%s'", cardNo, eventNo);
        String result = (String) jdbcTemplate.execute(new StatementCallback() {
            @Override
            public Object doInStatement(Statement stmt) throws SQLException {
                String result = "XX";
                int count = 0;

                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    //查询结果出现多条时
                    result = rs.getString("award_flag");
                    count++;
                }

                if (count != 1) {
                    logger.error("兑奖资格查询时出现重复记录。");
                    throw new RuntimeException("兑奖资格查询时出现重复记录");
                }

                return result;
            }
        });
        return  StringUtils.rightPad(cardNo, LEN_MSG_CARDNO, " ") + StringUtils.rightPad(eventNo, 2, " ") + StringUtils.rightPad(result, 2, " ");
    }
}
