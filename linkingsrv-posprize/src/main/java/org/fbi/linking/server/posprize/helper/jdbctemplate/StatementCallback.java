package org.fbi.linking.server.posprize.helper.jdbctemplate;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * User: zhanrui
 * Date: 13-5-16
 */
public interface StatementCallback {
    Object doInStatement(Statement stmt) throws SQLException;
}
