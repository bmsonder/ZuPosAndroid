package com.example.zuposandroid.Classes.BAL;

import com.example.zuposandroid.Classes.DAL.DBConncetion;

public class ErrorLogBAL {
    public void AddLog(String Message) {
        String sql = "Insert into deneme (ad, status, image) " +
                "values ((select case when cast(max(ad) as bigint) is null then 1 else cast(max(ad) as bigint)+1 end  from deneme where [image] like '%error%'), 1,'error " + Message + "')";
        try {
            DBConncetion dbConncetion = new DBConncetion();
            dbConncetion.connectionRun(sql);
            dbConncetion.close();
        } catch (Exception ex) {

        }
    }
}
