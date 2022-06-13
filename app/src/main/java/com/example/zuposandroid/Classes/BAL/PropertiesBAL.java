package com.example.zuposandroid.Classes.BAL;


import com.example.zuposandroid.Classes.DAL.DBConncetion;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PropertiesBAL {
    public String PropertiesSeqID;
    public String Name;
    public String Value;

    private ArrayList<PropertiesBAL> LoadResuldSet(ResultSet rs) throws SQLException {
        ArrayList<PropertiesBAL> modelList = new ArrayList<PropertiesBAL>();
        while (rs.next()) {
            PropertiesBAL model = new PropertiesBAL();
            try {
                model.PropertiesSeqID = rs.getString("PropertiesSeqID");
            } catch (Exception ex) {
            }
            try {
                model.Name = rs.getString("Name");
            } catch (Exception ex) {
            }
            try {
                model.Value = rs.getString("Value");
            } catch (Exception ex) {
            }
            modelList.add(model);
        }
        return modelList;
    }

    public ArrayList<PropertiesBAL> SelectAll() {
        ArrayList<PropertiesBAL> result = new ArrayList<PropertiesBAL>();
        DBConncetion dbConncetion = new DBConncetion();
        try {
            ResultSet resultSet;
            String sql = "select * from Properties ";
            resultSet = dbConncetion.connectionRun(sql);
            result = LoadResuldSet(resultSet);
        } catch (Exception ex) {
        }
        return result;
    }
}
