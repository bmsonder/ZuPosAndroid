package com.example.zuposandroid.Classes.BAL;

import com.example.zuposandroid.Classes.DAL.DBConncetion;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TableDesignBAL {
    public String table_design_def_seq;
    public String table_design_def_name;
    public ArrayList<TableDesignDetailBAL> tableDesignDetailBALList;

    private ArrayList<TableDesignBAL> LoadResuldSet(ResultSet rs) throws SQLException {
        ArrayList<TableDesignBAL> modelList = new ArrayList<TableDesignBAL>();
        while (rs.next()) {
            TableDesignBAL model = new TableDesignBAL();
            try {
                model.table_design_def_seq = rs.getString("table_design_def_seq");
            } catch (Exception ex) {
            }
            try {
                model.table_design_def_name = rs.getString("table_design_def_name");
            } catch (Exception ex) {
            }
            modelList.add(model);
        }
        return modelList;
    }

    public ArrayList<TableDesignBAL> SelectAll() {
        ArrayList<TableDesignBAL> result = new ArrayList<TableDesignBAL>();
        TableDesignDetailBAL tableDesignDetailBAL = new TableDesignDetailBAL();
        DBConncetion dbConncetion = new DBConncetion();
        try {
            ResultSet resultSet;
            String sql = "select table_design_def_seq, table_design_def_name from TableDesignDef where [status]=1 ";
            resultSet = dbConncetion.connectionRun(sql);
            result = LoadResuldSet(resultSet);
            if (result != null) {
                if (result.size() > 0) {
                    for (int i = 0; i < result.size(); i++) {
                        result.get(i).tableDesignDetailBALList = tableDesignDetailBAL.SelectByDesignID(result.get(i).table_design_def_seq);
                    }
                }
            }
        } catch (Exception ex) {
        }
        return result;
    }

    @Override
    public String toString() {
        return table_design_def_name;
    }

}
