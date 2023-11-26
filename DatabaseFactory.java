package com.MavenWebAPI.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.jline.utils.Log;

public class DatabaseFactory {
    // Method to get bank account information based on the request
    public static ResUserAccount getUserAccount(String userId) {
        ResUserAccount res = new ResUserAccount();
        List<ResUserAccount> list = new ArrayList<>();
        int statementIndex = 0;

        String strSQL =
                "SELECT tb_users.id, tb_users.username, tb_users.email, tb_users.first_name, tb_users.last_name "+
                "FROM tb_users " +
                "WHERE tb_users.id = ?";

        try (LoadDataSource ds = new LoadDataSource()) {
            ds.query(strSQL);
            if (!userId.isEmpty()) {
                ds.setCustomStatement(++statementIndex, userId);
            }

            ResultSet rs = ds.execute();

            while (rs.next()) {
                res.setId(rs.getString("id"));
                res.setUsername(rs.getString("username"));
                res.setUseremail(rs.getString("email"));
                res.setFirst_name(rs.getString("first_name"));
                res.setLast_name(rs.getString("last_name"));
                list.add(res);
            }

            // Check if data exists in the list
            if (list.isEmpty()) {
                res.setRc("50001");
                res.setRcDesc("Data does not exist");
            } else {
                res.setRc("20000");
                res.setRcDesc("SUCCESS");
            }

            ds.reset();
        } catch (Exception e) {
            res.setRc("50005");
            res.setRcDesc("General Error (Internal / External Server Error)");
        }

        return res;
    }

    public static boolean postUserAccount(ReqUserAccount req) {
        String strSQLCheck = "SELECT COUNT(*) FROM tb_users WHERE username = ? OR email = ?";
        String strSQL = "INSERT INTO tb_users " +
                "(username, email, first_name, last_name, password, created_on) " +
                "VALUES (?, ?, ?, ?, PASSWORD(?), NOW())";
        try (LoadDataSource ds = new LoadDataSource(); Connection con = ds.getConnection()) {

            try (PreparedStatement stmtCheck = con.prepareStatement(strSQLCheck)) {
                stmtCheck.setString(1, req.getUsername());
                stmtCheck.setString(2, req.getUseremail());
    
                ResultSet resultSet = stmtCheck.executeQuery();
                resultSet.next();
                int count = resultSet.getInt(1);
    
                if (count > 0) {
                    // A matching record already exists; return false or handle the error
                    return false;
                }
            }

            try (PreparedStatement stmt = con.prepareStatement(strSQL)) {
                stmt.setString(1, req.getUsername());
                stmt.setString(2, req.getUseremail());
                stmt.setString(3, req.getFirst_name());
                stmt.setString(4, req.getLast_name());
                stmt.setString(5, req.getPassword());

                int rowsUpdated = stmt.executeUpdate();
                return rowsUpdated > 0; // Return true if at least one row is updated
            }
        } catch (Exception e){
            Log.info("ERROR: ", e);
            e.printStackTrace();
            return false;
        }
    }

    // Method to delete the bank account from the database
    public static boolean deleteAccount(String username) {
        try (LoadDataSource ds = new LoadDataSource(); Connection con = ds.getConnection()) {
            String deleteSQL = "DELETE FROM tb_users WHERE username = ?";
            try (PreparedStatement stmt = con.prepareStatement(deleteSQL)) {
                stmt.setString(1, username);

                int rowsDeleted = stmt.executeUpdate();
                return rowsDeleted > 0;
            }
        } catch (Exception e) {
            Log.info("ERROR: ", e);
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateUserAccount(ReqUserAccount req) {
        try (LoadDataSource ds = new LoadDataSource(); Connection con = ds.getConnection()) {
            String updateSQL = "UPDATE tb_users SET first_name = ?, last_name = ?, email = ?, password = PASSWORD(?), " +
                            "updated_on = now(), updated_by = ? WHERE username = ?";
            Log.info("updateSQL: " + updateSQL);
            try (PreparedStatement stmt = con.prepareStatement(updateSQL)) {
                stmt.setString(1, req.getFirst_name());
                stmt.setString(2, req.getLast_name());
                stmt.setString(3, req.getUseremail());
                stmt.setString(4, req.getPassword());
                stmt.setString(5, req.getUpdated_by());
                stmt.setString(6, req.getUsername());

                int rowsUpdated = stmt.executeUpdate();
                return rowsUpdated > 0;
            }
        } catch (Exception e) {
            Log.info("ERROR: ", e);
            e.printStackTrace();
            return false;
        }
    }
}
