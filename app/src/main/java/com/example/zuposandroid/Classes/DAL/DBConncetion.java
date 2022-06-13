package com.example.zuposandroid.Classes.DAL;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.StrictMode;

import com.example.zuposandroid.Classes.Functions.FileFunctions;
import com.example.zuposandroid.Classes.Functions.GeneralleFunctions;
import com.example.zuposandroid.Classes.Parameters.DBParameters;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConncetion {
    public DBConncetion() {

    }

    public Connection conn;
    public ResultSet rs;
    public Statement stmt;

    @SuppressLint("NewApi")
    public Connection connectionclass() {
        boolean conn = false;
        String z = "";
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            ConnectionURL = "jdbc:jtds:sqlserver://" + DBParameters.IP + ";"
                    + "databaseName=" + DBParameters.DATABASE_NAME + ";user=" + DBParameters.DATABASE_USERNAME + ";password="
                    + DBParameters.DATABASE_PASSWORD + ";";
            connection = DriverManager.getConnection(ConnectionURL);
            conn = true;
        } catch (Exception ex) {
            z = ex.getMessage();
        }
        return connection;
    }

    public ResultSet connectionRun(String sql) {
        String z = "";
        try {
            conn = connectionclass();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
        } catch (Exception ex) {
            z = ex.getMessage();
        }
        return rs;
    }

    public void connectionRunSet(String sql) {
        String z = "";
        try {
            conn = connectionclass();
            conn.createStatement().executeUpdate(sql);
        } catch (Exception ex) {
            z = ex.getMessage();
        }
    }

    public void close() throws SQLException {
        if (conn != null)
            conn.close();
    }
}
