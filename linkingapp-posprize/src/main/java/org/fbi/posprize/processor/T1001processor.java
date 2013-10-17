package org.fbi.posprize.processor;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.fbi.linking.api.PosRequest;
import org.fbi.linking.api.PosResponse;
import org.fbi.posprize.common.MybatisFactory;
import org.fbi.posprize.helper.MD5Helper;
import org.fbi.posprize.helper.jdbctemplate.JdbcTemplate;
import org.fbi.posprize.helper.jdbctemplate.StatementCallback;
import org.fbi.posprize.repository.dao.PrizeMapper;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 3.1	当前有效信用卡活动信息查询
 */
public class T1001processor extends TxnMainProcessor {
    private final static int LEN_EVENT_NAME = 20;
    @Override
    public void execute(PosRequest request, PosResponse response, Map<String, Object> model) throws TxnRunTimeException {
        String txnCode = request.getTxnCode();
        String posId = request.getRequestMessage().substring(0 + 6, 0 + 6 + 32);
        String errCode = "0000";

        String responseBody = "";
        try {
            Map<String, String> map = new HashMap<String, String>();
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
                logger.error("编码错误");
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

    private String processTxn_mybatis() throws UnsupportedEncodingException, SQLException {
        SqlSessionFactory sqlSessionFactory = MybatisFactory.ORACLE.getInstance();

        SqlSession session = sqlSessionFactory.openSession();
        int cnt;
        try {
            PrizeMapper prizeMapper = session.getMapper(PrizeMapper.class);
            cnt = prizeMapper.selectCount();
        } finally {
            session.close();
        }
        return "1111-----" + cnt;
    }
    private String processTxn() throws UnsupportedEncodingException, SQLException {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();

        String currDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        final String sql = String.format("select act_no,act_name from B_M_ACT_POS_AWARD where act_begin_date <= '%s' and act_end_date >= '%s'", currDate, currDate);

        return (String) jdbcTemplate.execute(new StatementCallback() {
            @Override
            public Object doInStatement(Statement stmt) throws SQLException {
                String result = "";

                ResultSet rs = stmt.executeQuery(sql);
                try {
                    while (rs.next()) {
                        //多笔情况
                        String act_name = StringUtils.rightPad(rs.getString("act_name"), LEN_EVENT_NAME, " ");
                        byte[] bActName = act_name.getBytes("GBK");
                        if (bActName.length > LEN_EVENT_NAME) {
                            act_name = new String(bActName, 0, LEN_EVENT_NAME);
                        }
                        result += rs.getString("act_no") + act_name;
                    }
                } catch (UnsupportedEncodingException e) {
                    logger.error("活动名称处理错误", e);
                    throw new RuntimeException("活动名称处理错误", e);
                }
                return result;
            }
        });
    }
}
