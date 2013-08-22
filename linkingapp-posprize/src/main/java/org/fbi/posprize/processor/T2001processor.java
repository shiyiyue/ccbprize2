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
 * 	3.3	�ҽ�������
 */
public class T2001processor extends TxnMainProcessor {

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
            logger.error("���״������.", e);
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

    private String processTxn(final String cardNo, final String eventNo) throws UnsupportedEncodingException, SQLException {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();

        String result = (String) jdbcTemplate.execute(new StatementCallback() {
            @Override
            public Object doInStatement(Statement stmt) throws SQLException {
                String result = "";
                int count = 0;

                String sql = String.format("select award_flag from B_S_ACT_INFO where crd_no = '%s' and act_no = '%s'", cardNo, eventNo);
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    //��ѯ������ֶ���ʱ
                    result = rs.getString("award_flag");
                    count++;
                }

                if (count != 1) {
                    logger.error("�ҽ��ʸ��ѯʱ�����ظ���¼��");
                    throw new RuntimeException("�ҽ��ʸ��ѯʱ�����ظ���¼");
                }

                /*
                00��δ��ҽ��ʸ�
                01���ѻ�ҽ��ʸ�δ�ҽ���
                02���ÿ��ڸû�Ѷҽ�
                99�������������˹�����
                 */

                if ("01".equals(result)) {
                    String award_date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                    sql = String.format("update B_S_ACT_INFO set award_flag = '02', award_date = '%s'  where crd_no = '%s' and act_no = '%s' and award_flag = '01'", award_date, cardNo, eventNo);
                    int i = stmt.executeUpdate(sql);
                    if (i != 0) {
                        result = "02";  //�����Ѷҽ�
                    }
                    logger.info(String.format("����T2001���ҽ��ɹ�����¼״̬������Ϊ'02'���Ѷҽ���������=%s ���=%s", cardNo, eventNo));
                }else{
                    logger.info(String.format("����T2001���ҽ����ɹ������ص�ǰ��¼״̬=%s ������=%s ���=%s", result, cardNo, eventNo));
                }
                return result;
            }
        });
        return  StringUtils.rightPad(cardNo, LEN_MSG_CARDNO, " ") + StringUtils.rightPad(eventNo, 2, " ") + StringUtils.rightPad(result, 2, " ");
    }
}
