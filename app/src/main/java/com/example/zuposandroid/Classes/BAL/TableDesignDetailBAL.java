package com.example.zuposandroid.Classes.BAL;

import com.example.zuposandroid.Classes.DAL.DBConncetion;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TableDesignDetailBAL {
    public String table_design_detail_seq;
    public String table_design_def_seq;
    public String table_design_detail_table_name;
    public String table_design_detail_table_number;

    private ArrayList<TableDesignDetailBAL> LoadResuldSet(ResultSet rs) throws SQLException {
        ArrayList<TableDesignDetailBAL> modelList = new ArrayList<TableDesignDetailBAL>();
        while (rs.next()) {
            TableDesignDetailBAL model = new TableDesignDetailBAL();
            try {
                model.table_design_detail_seq = rs.getString("table_design_detail_seq");
            } catch (Exception ex) {
            }
            try {
                model.table_design_def_seq = rs.getString("table_design_def_seq");
            } catch (Exception ex) {
            }
            try {
                model.table_design_detail_table_name = rs.getString("table_design_detail_table_name");
            } catch (Exception ex) {
            }
            try {
                model.table_design_detail_table_number = rs.getString("table_design_detail_table_number");
            } catch (Exception ex) {
            }
            modelList.add(model);
        }
        return modelList;
    }

    public ArrayList<TableDesignDetailBAL> SelectByDesignID(String table_design_def_seq) {
        ArrayList<TableDesignDetailBAL> result = new ArrayList<TableDesignDetailBAL>();
        DBConncetion dbConncetion = new DBConncetion();
        try {
            ResultSet resultSet;
            String sql = "select [table_design_detail_seq], [table_design_def_seq], [table_design_detail_table_name], [table_design_detail_table_number] " +
                    "from TableDesignDetail where table_design_def_seq= " + table_design_def_seq + " and isTable=1   order by control_number ";
            resultSet = dbConncetion.connectionRun(sql);
            result = LoadResuldSet(resultSet);
        } catch (Exception ex) {
        }
        return result;
    }

    @Override
    public String toString() {
        return table_design_detail_table_name;
    }
}
