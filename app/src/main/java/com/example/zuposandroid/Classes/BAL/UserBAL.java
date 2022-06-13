package com.example.zuposandroid.Classes.BAL;

import com.example.zuposandroid.Classes.DAL.DBConncetion;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserBAL {
    public String user_seq;
    public String userName;
    public String def_rvc_seq;
    public String role_seq;
    public String role_name;
    public String rvc_def_seq;
    public String rvc_def_name;

    private ArrayList<UserBAL> LoadResuldSet(ResultSet rs) throws SQLException {
        ArrayList<UserBAL> modelList = new ArrayList<UserBAL>();
        while (rs.next()) {
            UserBAL model = new UserBAL();
            try {
                model.user_seq = rs.getString("user_seq");
            } catch (Exception ex) {
            }
            try {
                model.userName = rs.getString("userName");
            } catch (Exception ex) {
            }
            try {
                model.def_rvc_seq = rs.getString("def_rvc_seq");
            } catch (Exception ex) {
            }
            try {
                model.role_name = rs.getString("role_name");
            } catch (Exception ex) {
            }
            try {
                model.role_seq = rs.getString("role_seq");
            } catch (Exception ex) {
            }
            try {
                model.rvc_def_seq = rs.getString("rvc_def_seq");
            } catch (Exception ex) {
            }
            try {
                model.rvc_def_name = rs.getString("rvc_def_name");
            } catch (Exception ex) {
            }
            modelList.add(model);
        }
        return modelList;
    }

    public ArrayList<UserBAL> SearchUser(String password) {
        ArrayList<UserBAL> result = new ArrayList<>();
        DBConncetion dbConncetion = new DBConncetion();
        try {
            ResultSet resultSet;
            String sql = "select   u.user_seq" +
                    ", u.first_name+' '+u.last_name as 'userName'" +
                    ", u.def_rvc_seq" +
                    ", r.role_seq" +
                    ", r.role_name" +
                    ", rvc.rvc_def_seq" +
                    ", (select rvc_def_name from RvcDef rr where rr.rvc_def_seq=u.def_rvc_seq) as 'defRevcName'" +
                    ", rvc.rvc_def_name " +
                    "from [User] u  " +
                    "inner join UserRelation ur on ur.user_seq=u.user_seq " +
                    "inner join UserPosition up on up.user_relation_seq=ur.user_relation_seq " +
                    "inner join [Role] r on r.role_seq=ur.role_def_seq " +
                    "inner join RvcDef rvc on rvc.rvc_def_seq=ur.rvc_def_seq " +
                    "where up.user_pass='" + password + "'";
            resultSet = dbConncetion.connectionRun(sql);
            result = LoadResuldSet(resultSet);
        } catch (Exception ex) {
        }
        return result;
    }
}