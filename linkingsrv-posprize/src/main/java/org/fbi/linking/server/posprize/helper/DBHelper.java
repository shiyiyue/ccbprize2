package org.fbi.linking.server.posprize.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-5-16
 */
public class DBHelper {
    public static Logger logger = LoggerFactory.getLogger(DBHelper.class);
    public static String dbDriver = (String) ProjectConfigManager.getInstance().getProperty("ConnectionManager.driver");
    public static String dbConnection = (String) ProjectConfigManager.getInstance().getProperty("ConnectionManager.connection");
    public static String dbUser = (String) ProjectConfigManager.getInstance().getProperty("ConnectionManager.user");
    public static String dbPasswd = (String) ProjectConfigManager.getInstance().getProperty("ConnectionManager.password");

    private DBHelper(){
    }

    public static Connection getConnection(){
        try {
            Class.forName(dbDriver);
            Connection con = DriverManager.getConnection(dbConnection, dbUser, dbPasswd);
            return con;
        } catch (ClassNotFoundException e) {
            logger.error("数据库链接初始化失败！驱动程序未找到。", e);
        } catch (SQLException e) {
            logger.error("数据库链接初始化失败！", e);
        }
        return null;
    }

}
