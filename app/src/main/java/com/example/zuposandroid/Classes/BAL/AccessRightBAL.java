package com.example.zuposandroid.Classes.BAL;

import com.example.zuposandroid.Classes.DAL.DBConncetion;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AccessRightBAL {
    public String acc_right_def_code;
    public String role_seq;
    public String status;

    private ArrayList<AccessRightBAL> LoadResuldSet(ResultSet rs) throws SQLException {
        ArrayList<AccessRightBAL> modelList = new ArrayList<AccessRightBAL>();
        while (rs.next()) {
            AccessRightBAL model = new AccessRightBAL();
            try {
                model.acc_right_def_code = rs.getString("acc_right_def_code");
            } catch (Exception ex) {
            }
            try {
                model.role_seq = rs.getString("role_seq");
            } catch (Exception ex) {
            }
            try {
                model.status = rs.getString("status");
                if (model.status == null)
                    model.status = "0";
            } catch (Exception ex) {
            }
            modelList.add(model);
        }
        return modelList;
    }

    public ArrayList<AccessRightBAL> SelectAllActiveCheck(String acc_right_def_code, String role_seq) {
        ArrayList<AccessRightBAL> result = new ArrayList<AccessRightBAL>();
        AccessRightBAL itemsBAL = new AccessRightBAL();
        DBConncetion dbConncetion = new DBConncetion();
        try {
            ResultSet resultSet;
            String sql = "select * from AccessRight where acc_right_def_code='" + acc_right_def_code + "' and role_seq= " + role_seq;
            resultSet = dbConncetion.connectionRun(sql);
            result = LoadResuldSet(resultSet);
        } catch (Exception ex) {
        }
        return result;
    }
}
