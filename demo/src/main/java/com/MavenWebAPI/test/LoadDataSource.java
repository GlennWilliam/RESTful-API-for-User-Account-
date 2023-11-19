package com.MavenWebAPI.test;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Statement;

public class LoadDataSource implements AutoCloseable {
    static org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager.getLogger(LoadDataSource.class.getName());
    
    private static HikariDataSource hikariDataSource = new HikariDataSource();
    static {
        
        hikariDataSource.setJdbcUrl("jdbc:mysql://localhost:3306/accountdatabase?zeroDateTimeBehavior=convertToNull");
        hikariDataSource.setUsername("root");
        hikariDataSource.setPassword("");
        hikariDataSource.setIdleTimeout(30000);
        hikariDataSource.setMaximumPoolSize(200);
        hikariDataSource.setMaxLifetime(30000);
        hikariDataSource.setMinimumIdle(1);
        hikariDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
    }
    private Connection con = null;
    private PreparedStatement stmt = null;
    private ResultSet rs = null;

    public LoadDataSource() {
        //log.info("CREATE CONNECTION DATASOURCE!");
        if (con == null) {
            try {
                con = hikariDataSource.getConnection();
            } catch (SQLException e) {
                log.error("ERROR : ", e);
            }
        }
    }

    public void query(String sql) throws SQLException {
        stmt = (PreparedStatement) con.prepareStatement(sql);
    }

    public void customQuery(String sql) throws SQLException {
        stmt = (PreparedStatement) con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    }

    public void statement(Object... s) throws SQLException {
        for (int i = 1; i <= s.length; i++) {
            stmt.setObject(i, s[i - 1]);
        }
    }

    public void setCustomStatement(int index, Object data) throws SQLException {
        stmt.setObject(index, data);
    }

    public void timeout(int timeout) throws SQLException {
        stmt.setQueryTimeout(timeout);
    }

    public ResultSet execute() throws SQLException {
        return rs = stmt.executeQuery();
    }
    
    public void autoCommit(boolean status) throws SQLException {
        con.setAutoCommit(status);
    }
    
    public void commit() throws SQLException {
        con.commit();
    }

    public void rollback() throws SQLException {
        con.rollback();
    }

    public int insertupdate() throws SQLException {
        return stmt.executeUpdate();
    }
    
    public void reset() throws SQLException{
        stmt = null ;
    }

    public ResultSet getGeneratedKeys() throws SQLException {
        stmt.execute();
        return rs = stmt.getGeneratedKeys();
    }

    public Connection getConnection() throws SQLException {
        return hikariDataSource.getConnection();
    }

    @Override
    public void close() throws Exception {
        //;log.info("CONNECTION STMT CON CLOSED!");
        if (rs != null) {
            rs.close();
        }
        if (stmt != null) {
            stmt.close();
        }
        if (con != null) {
            con.close();
        }
    }
}


